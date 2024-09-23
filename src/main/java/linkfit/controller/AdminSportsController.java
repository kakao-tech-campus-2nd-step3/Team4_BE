package linkfit.controller;

import jakarta.validation.Valid;
import java.util.List;
import linkfit.dto.SportsRequest;
import linkfit.dto.SportsResponse;
import linkfit.entity.Sports;
import linkfit.service.SportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/sports")
public class AdminSportsController {

    private final SportsService sportsService;

    @Autowired
    public AdminSportsController(SportsService sportsService) {
        this.sportsService = sportsService;
    }

    @GetMapping
    public String findAllSports(Model model) {
        List<SportsResponse> sportsList = sportsService.getAllSports(Pageable.unpaged());
        model.addAttribute("sportsList", sportsList);
        return "sports";
    }

    @GetMapping("/register")
    public String getSportsRegisterForm(Model model) {
        model.addAttribute("sports", new SportsRequest());
        return "sports-form";
    }

    @PostMapping
    public String registerSports(
        @Valid @ModelAttribute SportsRequest sportsRequest,
        BindingResult bindingResult,
        Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "sports-form";
        }
        sportsService.registerSport(sportsRequest);
        return "redirect:/admin/sports";
    }

    @GetMapping("/{sportsId}")
    public String getSportsUpdateForm(Model model, @PathVariable Long sportsId) {
        Sports sports = sportsService.findSportsById(sportsId);
        model.addAttribute("origin", sports.getName());
        model.addAttribute("sports", new SportsRequest());
        return "sports-update";
    }

    @PutMapping("/{sportsId}")
    public String updateSports(
        @PathVariable Long sportsId,
        @Valid @ModelAttribute SportsRequest sportsRequest,
        BindingResult bindingResult,
        Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "sports-update";
        }
        sportsService.updateSports(sportsId, sportsRequest);
        return "redirect:/admin/sports";
    }

}
