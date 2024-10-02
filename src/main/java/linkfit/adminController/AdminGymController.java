package linkfit.adminController;

import jakarta.validation.Valid;
import java.util.List;
import linkfit.dto.AdminGymDetailResponse;
import linkfit.dto.GymRegisterRequest;
import linkfit.dto.GymSearchResponse;
import linkfit.entity.Gym;
import linkfit.service.GymService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/gyms")
public class AdminGymController {

    private final GymService gymService;

    public AdminGymController(GymService gymService) {
        this.gymService = gymService;
    }

    @GetMapping
    public String findAllGym(Model model, Pageable pageable) {
        List<Gym> gymList = gymService.findAllGym(pageable);
        model.addAttribute("gymList", gymList);
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

    @DeleteMapping("/{gymId}")
    public String deleteGym(@PathVariable Long gymId) {
        gymService.deleteGym(gymId);
        return "redirect:/admin/gyms";
    }

    @GetMapping("/{gymId}")
    public String getGymDetails(@PathVariable Long gymId, Model model, Pageable pageable) {
        AdminGymDetailResponse adminGymDetailResponse = gymService.getGymDetailsForAdmin(gymId, pageable);
        model.addAttribute("gym", adminGymDetailResponse.gym());
        model.addAttribute("trainerList", adminGymDetailResponse.trainerList());
        return "gym-details";
    }

    @GetMapping("/search")
    public String searchGymByName(@RequestParam String keyword, Model model, Pageable pageable) {
        GymSearchResponse gymList = gymService.findAllByKeyword(keyword, pageable);
        model.addAttribute("keyword", keyword);
        model.addAttribute("gymList", gymList);
        return "gym-search-list";
    }
}