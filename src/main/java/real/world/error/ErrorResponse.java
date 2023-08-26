package real.world.error;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import real.world.error.exception.BusinessException;

@JsonRootName(value = "errors")
public class ErrorResponse {

    private final Map<String, String> errors;

    private ErrorResponse(Map<String, String> errors) {
        this.errors = errors;
    }

    public static ErrorResponse of() {
        return new ErrorResponse(Collections.emptyMap());
    }

    public static ErrorResponse of(Map<String, String> errors) {
        return new ErrorResponse(errors);
    }

    public static ErrorResponse of(BusinessException e) {
        final Map<String, String> errors = new HashMap<>();
        errors.put(e.getBody(), e.getMessage());
        return new ErrorResponse(errors);
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        final Map<String, String> errors = new HashMap<>();
        errors.put(errorCode.getBody(), errorCode.getMessage());
        return new ErrorResponse(errors);
    }

    @JsonAnyGetter
    public Map<String, String> getErrors() {
        return errors;
    }

}
