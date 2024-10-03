package linkfit.adminController;

import jakarta.validation.Valid;
import java.util.List;
import linkfit.dto.GymRegisterRequest;
import linkfit.dto.GymRegisterWaitingResponse;
import linkfit.dto.GymSearchResponse;
import linkfit.dto.GymTrainersResponse;
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
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/register-waiting")
    public String getGymRegisterForm(Model model) {
        List<GymRegisterWaitingResponse> gymRegisterWaitingList = gymService.getGymRegisterWaitingList();
        model.addAttribute("gymList", gymRegisterWaitingList);
        return "gym-register-request-list";
    }

    @PutMapping("/{gymId}")
    public String approvalGym(@PathVariable Long gymId) {
        gymService.approvalGym(gymId);
        return "redirect:/admin/gyms";
    }

    @DeleteMapping("/{gymId}")
    public String deleteGym(@PathVariable Long gymId) {
        gymService.deleteGym(gymId);
        return "redirect:/admin/gyms";
    }

    @GetMapping("/{gymId}")
    public String getGymDetails(@PathVariable Long gymId, Model model, Pageable pageable) {
        Gym gym = gymService.getGymById(gymId);
        model.addAttribute("gym", gym);
        GymTrainersResponse trainers = gymService.getGymTrainers(gymId, pageable);
        model.addAttribute("trainerList", trainers);
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