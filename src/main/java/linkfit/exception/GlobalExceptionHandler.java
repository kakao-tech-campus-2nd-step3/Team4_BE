package linkfit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String NOT_EXIST_ID = "존재하지 않는 ID에 대한 접근입니다.";

    @ExceptionHandler(InvalidIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleInvalidIdException(InvalidIdException ex, Model model) {
        model.addAttribute("errorMessage", "Response error: " + ex.getMessage());
    }
}
