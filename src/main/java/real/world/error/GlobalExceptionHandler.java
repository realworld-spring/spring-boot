package real.world.error;

import static real.world.error.ErrorCode.INTERNAL_SERVER;
import static real.world.error.ErrorCode.REQUEST_INVALID;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import real.world.error.exception.BusinessException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
        MissingServletRequestPartException.class,
        MissingRequestValueException.class,
        MethodArgumentTypeMismatchException.class,
        HttpMessageNotReadableException.class,
        HttpRequestMethodNotSupportedException.class
    })
    public ResponseEntity<ErrorResponse> handleRequestException(Exception e) {
        final ErrorResponse errorResponse = ErrorResponse.of(REQUEST_INVALID);
        return new ResponseEntity<>(errorResponse, REQUEST_INVALID.getCode());
    }


    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(
        ConstraintViolationException e) {
        final ErrorResponse response = ErrorResponse.of(
            convertConstraintViolations(e.getConstraintViolations()));
        return new ResponseEntity<>(response, REQUEST_INVALID.getCode());
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        final ErrorResponse response = ErrorResponse.of(convertBindingResult(e.getBindingResult()));
        return new ResponseEntity<>(response, REQUEST_INVALID.getCode());
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        final ErrorResponse response = ErrorResponse.of(convertBindingResult(e.getBindingResult()));
        return new ResponseEntity<>(response, REQUEST_INVALID.getCode());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        final ErrorResponse errorResponse = ErrorResponse.of(e);
        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        final ErrorResponse errorResponse = ErrorResponse.of(INTERNAL_SERVER);
        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER.getCode());
    }

    private Map<String, String> convertBindingResult(BindingResult bindingResult) {
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                error -> error.getDefaultMessage() == null ? error.getDefaultMessage() : ""
            ));
    }

    private Map<String, String> convertConstraintViolations(
        Set<ConstraintViolation<?>> constraintViolations) {
        return constraintViolations.stream()
            .collect(Collectors.toMap(
                error -> {
                    final int index = error.getPropertyPath().toString().indexOf(".");
                    return error.getPropertyPath().toString().substring(index + 1);
                },
                ConstraintViolation::getMessage
            ));
    }

}
