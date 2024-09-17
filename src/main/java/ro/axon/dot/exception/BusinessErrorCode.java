package ro.axon.dot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BusinessErrorCode {

    BAD_REQUEST(
            formatErrorCode("0003", "400"),
            "Bad request",
            HttpStatus.BAD_REQUEST),

    RESOURCE_NOT_FOUND(
            formatErrorCode("0001", "404"),
            "Resource not found",
            HttpStatus.NOT_FOUND),

    INTERNAL_SERVER_ERROR(
            formatErrorCode("0002", "500"),
            "Internal server error",
            HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorCode;
    private final String devMsg;
    private final HttpStatus status;

    private static final String ERROR_CODE_PATTERN = "EDOT%s%s";

    public static String formatErrorCode(String id, String status) {
        if(id.length() != 4 && status.length() != 3) {
            throw new IllegalArgumentException("Id must be 4 digits and status must be 3 digits.");
        }
        return String.format(ERROR_CODE_PATTERN, id, status);
    }
}
