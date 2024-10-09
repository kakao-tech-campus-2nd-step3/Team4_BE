package linkfit.controller;

import jakarta.validation.Valid;
import linkfit.annotation.LoginTrainer;
import linkfit.annotation.LoginUser;
import linkfit.dto.ScheduleRequest;
import linkfit.dto.ScheduleResponse;
import linkfit.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pt/{ptId}/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<ScheduleResponse> getSchedules(@PathVariable Long ptId) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getSchedules(ptId));
    }

    @PostMapping
    public ResponseEntity<Void> registerSchedule(
        @LoginTrainer Long trainerId,
        @PathVariable Long ptId, @Valid @RequestBody ScheduleRequest scheduleRequest) {
        scheduleService.registerSchedule(trainerId, ptId, scheduleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<Void> completeSchedule(
        @LoginUser Long userId,
        @PathVariable Long ptId, @PathVariable Long scheduleId) {
        scheduleService.completeSchedule(userId, ptId, scheduleId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@LoginTrainer Long trainerId,
        @PathVariable Long ptId, @PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(trainerId, ptId, scheduleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
