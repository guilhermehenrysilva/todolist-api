package todo.list.api.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import todo.list.api.domain.exceptions.AlreadyRegisteredUserException;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handlerError404() {
        log.info("[404] Not Found.");
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handlerValidationsError400(MethodArgumentNotValidException ex) {
        log.info("[400] Bad Request. " + ex.getMessage());
        var errorList = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(errorList.stream().map(DataValidationError::new).toList());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, AlreadyRegisteredUserException.class})
    public ResponseEntity handlerError400(Exception ex) {
        log.info("[400] Bad Request. " + ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity handlerErrorBadCredentials() {
        log.info("[401] Unauthorized.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity handlerErrorAuthentication() {
        log.info("[401] Unauthorized.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handlerErrorForbidden() {
        log.info("[403] Forbidden.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handlerError500(Exception ex) {
        log.info("[500] Internal Server Error. " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " +ex.getLocalizedMessage());
    }

     private record DataValidationError(String field, String message) {
        public DataValidationError(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

}
