package linkfit.exception;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.View;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final View error;
    private MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource, View error) {
        this.messageSource = messageSource;
        this.error = error;
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<String> handleImageUploadException(ImageUploadException e) {
        String responseMessage = messageSource.getMessage(e.getMessage(), null,
            Locale.getDefault());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<String> handlePasswordMismatchException(PasswordMismatchException e) {
        String responseMessage = messageSource.getMessage(e.getMessage(), null,
            Locale.getDefault());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    //잘못된 파라미터 handling
    @ExceptionHandler
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e) {
        String responseMessage = messageSource.getMessage(e.getMessage(), null,
            Locale.getDefault());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        String responseMessage = messageSource.getMessage(e.getMessage(), null,
            Locale.getDefault());
        return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<String> handlePermissionException(PermissionException e) {
        String responseMessage = messageSource.getMessage(e.getMessage(), null,
            Locale.getDefault());
        return new ResponseEntity<>(responseMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<String> handleDuplicateException(DuplicateException e) {
        String responseMessage = messageSource.getMessage(e.getMessage(), null,
            Locale.getDefault());
        return new ResponseEntity<>(responseMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(InvalidTokenException e) {
        String responseMessage = messageSource.getMessage(e.getMessage(), null,
            Locale.getDefault());
        return new ResponseEntity<>(responseMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException e) {
        StringBuilder errors = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(error ->
            errors.append(error.getField())
                .append(":\t")
                .append(error.getDefaultMessage())
                .append("\n")
        );
        return new ResponseEntity<>(errors.toString(), HttpStatus.BAD_REQUEST);
    }
}