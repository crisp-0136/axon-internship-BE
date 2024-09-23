package ro.axon.dot.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorCode {

    EMPLOYEE_NOT_FOUND(
            formatErrorCode("0001", "400"),
            "Employee was not found",
            HttpStatus.NOT_FOUND),

    TEAM_NOT_FOUND(
            formatErrorCode("0002", "400"),
            "Team was not found",
            HttpStatus.NOT_FOUND),

    LEAVE_REQUEST_NOT_FOUND(
            formatErrorCode("0003", "400"),
            "Leave request was not found.",
            HttpStatus.BAD_REQUEST),

    INVALID_DB_VERSION(
            formatErrorCode("0004", "409"),
            "User cannot be updated. Db version conflict.",
            HttpStatus.CONFLICT),

    LEAVE_REQUEST_REJECTED (
            formatErrorCode("0005", "400"),
            "The action cannot be applied on leave request with REJECTED status.",
    HttpStatus.BAD_REQUEST),

    LEAVE_REQUEST_APPROVED_IN_PAST(
            formatErrorCode("0006", "400"),
            "The action cannot be applied on leave request with days approved in the past.",
    HttpStatus.BAD_REQUEST),

    COMBINATION_NOT_FOUND(
            formatErrorCode("0007", "400"),
            "Data associated to the given combination of identifiers does not exist.",
            HttpStatus.BAD_REQUEST),

    INVALID_LEAVE_REQUEST_V(
            formatErrorCode("0008", "409"),
            "Current leave request version is higher than the specified version.",
            HttpStatus.CONFLICT),

    INVALID_DATE_RANGE(
            formatErrorCode("0009", "400"),
            "Start date cannot be greater than end date.",
            HttpStatus.BAD_REQUEST),

    INSUFFICIENT_YEARLY_DAYS_OFF(
            formatErrorCode("0010", "400"),
            "Employee does not have sufficient yearly days off",
            HttpStatus.BAD_REQUEST),

    LEAVE_REQ_PERIOD_NOT_IN_SAME_YEAR(
            formatErrorCode("0010", "400"),
            "Start date and end date must be in same year.",
            HttpStatus.BAD_REQUEST),

    LEAVE_REQUEST_PERIOD_IN_PAST(
            formatErrorCode("0010", "400"),
            "The start date must not be earlier than the first day of the current month.",
    HttpStatus.BAD_REQUEST);



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
