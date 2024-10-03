package linkfit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/postcode")
    public String getPage(){
        return "postcode";
    }
}
