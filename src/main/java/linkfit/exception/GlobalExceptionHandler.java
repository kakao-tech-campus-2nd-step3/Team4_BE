package linkfit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String DUPLICATE_NAME = "The same name already exists";
    public static final String DUPLICATE_EMAIL = "The same email already exists";

    public static final String NOT_MATCH_PASSWORD = "Password does not match.";

    public static final String NOT_FOUND_CAREER = "Not found career";
    public static final String NOT_FOUND_TRAINER = "Not found trainer";
    public static final String NOT_FOUND_REVIEW = "Not found review";
    public static final String NOT_FOUND_USER = "Not found user";
    public static final String NOT_FOUND_PT = "Not found pt";
    public static final String NOT_FOUND_SPORTS = "Not found sports";
    public static final String NOT_FOUND_BODYINFO = "Not found body_info";
    public static final String NOT_FOUND_SCHEDULE = "Not found schedule";
    public static final String NOT_FOUND_EMAIL = "Not found email";
    public static final String NOT_FOUND_GYM = "Not found gym";
    public static final String NOT_FOUND_RELATION = "Not found relation";
    public static final String NOT_FOUND_PREFERENCE = "Not found preference";
    public static final String NOT_FOUND_IMAGE = "Not found image";

    public static final String FAILED_UPLOAD_IMAGE = "Failed to upload image";

    public static final String INVALID_TOKEN = "Invalid or Expired token";
    public static final String NOT_OWNER = "You can only control your own data";

    public static final String REVIEW_PERMISSION_DENIED = "You can only create a review after completing the pt.";
    public static final String GYM_ADMIN_PERMISSION_DENIED = "You do not have permission to handle information GYM.";
    public static final String CARRER_PERMISSION_DENIED = "You can only handle your own career.";
    public static final String UNREGISTERED_TRAINER = "This is an unregistered trainer.";

    public static final String ALREADY_COMPLETED_SCHEDULE = "Cannot delete an already completed schedule";
    public static final String UNRELATED_SCHEDULE = "Not a schedule related to your request";

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<String> handleImageUploadException(ImageUploadException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<String> handlePasswordMismatchException(PasswordMismatchException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    //잘못된 파라미터 handling
    @ExceptionHandler
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<String> handlePermissionException(PermissionException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<String> handleDuplicateException(DuplicateException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(DuplicateException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}