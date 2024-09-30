package linkfit.adminController;

import linkfit.service.GymService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
}
