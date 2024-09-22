package linkfit.controller;

import jakarta.validation.Valid;
import linkfit.dto.SportsRequest;
import linkfit.service.SportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/sports")
public class AdminSportsController {

    private final SportsService sportsService;

    @Autowired
    public AdminSportsController(SportsService sportsService) {
        this.sportsService = sportsService;
    }

    @GetMapping("/register")
    public String registerSport(Model model) {
        model.addAttribute("sports", new SportsRequest());
        return "sports-form";
    }

    @PostMapping
    public String registerSports(
        @Valid @RequestBody SportsRequest sportsRequest,
        BindingResult bindingResult,
        Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "sports-form";
        }
        sportsService.registerSport(sportsRequest);
        return "redirect:/admin/sports";
    }

}
