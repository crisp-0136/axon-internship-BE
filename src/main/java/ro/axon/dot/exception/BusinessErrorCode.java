package ro.axon.dot.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorCode {

    EMPLOYEE_NOT_FOUND(
            formatErrorCode("0001", "404"),
            "Employee was not found",
            HttpStatus.NOT_FOUND),

    TEAM_NOT_FOUND(
            formatErrorCode("0002", "404"),
            "Team was not found",
            HttpStatus.NOT_FOUND),

    EMPLOYEE_INACTIVATION_FAILURE(
            formatErrorCode("0003", "400"),
            "Inactivation cannot be performed. Employee was not found.",
            HttpStatus.BAD_REQUEST),

    INVALID_DB_VERSION(
            formatErrorCode("0004", "409"),
            "User cannot be updated. Db version conflict.",
            HttpStatus.CONFLICT);


    private final String errorCode;
    private final String devMsg;
    private final HttpStatus status;

    private static final String ERROR_CODE_PATTERN = "EDOT%s%s";

    public static String formatErrorCode(String id, String status) {
        if(id.length() != 4 || status.length() != 3) {
            throw new IllegalArgumentException("Id must be 4 digits and status must be 3 digits.");
        }
        return String.format(ERROR_CODE_PATTERN, id, status);
    }
}
