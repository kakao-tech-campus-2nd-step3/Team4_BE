package linkfit.repository;

import java.util.List;
import linkfit.entity.Pt;
import linkfit.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByPt(Pt pt);
}
