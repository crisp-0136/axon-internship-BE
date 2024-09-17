package ro.axon.dot.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.axon.dot.exception.BusinessErrorCode;
import ro.axon.dot.exception.BusinessException;
import ro.axon.dot.exception.ErrorDetails;

import java.util.ArrayList;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDetails> handleBusinessException(BusinessException exception) {

        BusinessErrorCode businessErrorCode = exception.getBusinessErrorCode();

        HttpStatus status = businessErrorCode.getStatus();
        ErrorDetails errorDetails = new ErrorDetails();

        errorDetails.setErrors(new ArrayList<ErrorDetails.ErrorDetailsItem>());
        errorDetails.getErrors().add(
                ErrorDetails.ErrorDetailsItem.builder()
                        .errorCode(businessErrorCode.getErrorCode())
                        .message(businessErrorCode.getDevMsg())
                        .contextVariables(exception.getContextVariables())
                        .build()
        );

        return ResponseEntity.status(status)
                .body(errorDetails);
    }
}
