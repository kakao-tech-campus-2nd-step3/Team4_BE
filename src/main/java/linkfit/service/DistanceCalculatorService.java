package linkfit.service;

import linkfit.config.properties.KakaoProperties;
import linkfit.dto.Coordinate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class DistanceCalculatorService {

    private final KakaoProperties kakaoProperties;

    public DistanceCalculatorService(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public Coordinate getCoordinates(String address) {
        String response = requestAddressCoordinates(address);
        return parseCoordinatesFromResponse(response);
    }

    private String requestAddressCoordinates(String address) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoProperties.apiKey());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                kakaoProperties.addressSearchUrl())
            .queryParam("query", address);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(builder.build(false).toUriString(),
            HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    private Coordinate parseCoordinatesFromResponse(String response) {
        double latitude = Double.parseDouble(response.split("\"y\":\"")[1].split("\"")[0]);
        double longitude = Double.parseDouble(response.split("\"x\":\"")[1].split("\"")[0]);
        return new Coordinate(latitude, longitude);
    }

    public double calculateHaversineDistance(Coordinate coord1, Coordinate coord2) {
        double latDiff = Math.toRadians(coord2.latitude() - coord1.latitude());
        double lonDiff = Math.toRadians(coord2.longitude() - coord1.longitude());
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
            + Math.cos(Math.toRadians(coord1.latitude())) * Math.cos(
            Math.toRadians(coord2.latitude()))
            * Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        final double EARTH_RADIUS = 6371; // 지구 반경 (단위: km)
        return EARTH_RADIUS * c;
    }
}
