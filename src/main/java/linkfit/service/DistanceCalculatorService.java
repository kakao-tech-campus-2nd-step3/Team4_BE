package linkfit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import linkfit.dto.Coordinate;

@Service
public class DistanceCalculatorService {
	
	@Value("${kakao.api-key}")
	private String KAKAO_API_KEY;
	@Value("${kakao.address-search-url}")
	private String ADDRESS_SEARCH_URL;

	public Coordinate getCoordinates(String address) {
        String response = requestAddressCoordinates(address);
        return parseCoordinatesFromResponse(response);
    }
	
	private String requestAddressCoordinates(String address) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ADDRESS_SEARCH_URL)
                .queryParam("query", address);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(), HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
	
	private Coordinate parseCoordinatesFromResponse(String response) {
        double latitude = Double.parseDouble(response.split("\"y\":\"")[1].split("\"")[0]);
        double longitude = Double.parseDouble(response.split("\"x\":\"")[1].split("\"")[0]);
        return new Coordinate(latitude, longitude);
    }

	public double calculateHaversineDistance(Coordinate coord1, Coordinate coord2) {
        double latDiff = Math.toRadians(coord2.getLatitude() - coord1.getLatitude());
        double lonDiff = Math.toRadians(coord2.getLongitude() - coord1.getLongitude());

        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                + Math.cos(Math.toRadians(coord1.getLatitude())) * Math.cos(Math.toRadians(coord2.getLatitude()))
                * Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        final double EARTH_RADIUS = 6371; // 지구 반경 (단위: km)
        return EARTH_RADIUS * c;
    }
}
