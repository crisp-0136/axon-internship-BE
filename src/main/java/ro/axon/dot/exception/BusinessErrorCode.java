package ro.axon.dot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BusinessErrorCode {

    EMPLOYEE_NOT_FOUND(
            formatErrorCode("0001", "404"),
            "Employee was not found",
            HttpStatus.NOT_FOUND),

    INVALID_EMPLOYEE(
            formatErrorCode("0002", "404"),
            "Employee data does not correspond to the rules",
            HttpStatus.BAD_REQUEST),

    REQUEST_NOT_FOUND(
            formatErrorCode("0003", "404"),
            "Request was not found",
            HttpStatus.NOT_FOUND),

    INVALID_REQUEST(
            formatErrorCode("0004", "400"),
            "Request data does not correspond to the rules",
            HttpStatus.BAD_REQUEST),

    TEAM_NOT_FOUND(
            formatErrorCode("0005", "404"),
            "Team was not found",
            HttpStatus.NOT_FOUND);

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
