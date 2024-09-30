package linkfit.adminController;

import jakarta.validation.Valid;
import linkfit.dto.GymRegisterRequest;
import linkfit.service.GymService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/gyms")
public class AdminGymController {

    private final GymService gymService;

    public AdminGymController(GymService gymService) {
        this.gymService = gymService;
    }

    @GetMapping
    public String findAllGym(Model model) {
        model.addAttribute("gymList", gymService.findAllGym());
        return "gym";
    }

    @GetMapping("/register")
    public String getGymRegisterForm(Model model) {
        model.addAttribute("gym", new GymRegisterRequest());
        return "gym-form";
    }

    @PostMapping("/register")
    public String registerGym(@Valid @ModelAttribute GymRegisterRequest gymRegisterRequest,
        BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "gym-form";
        }
        gymService.registerGym(gymRegisterRequest);
        return "redirect:/admin/gyms";
    }
}