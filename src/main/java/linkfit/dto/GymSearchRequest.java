package linkfit.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GymSearchRequest (@NotNull @Size(min=2) String keyword){

}
