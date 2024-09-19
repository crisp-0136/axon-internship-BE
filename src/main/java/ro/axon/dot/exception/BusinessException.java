package ro.axon.dot.exception;

import lombok.Getter;

import java.io.Serial;
import java.util.Map;

@Getter
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final BusinessErrorCode businessErrorCode;
    private Map<String, Object> contextVariables;

    public BusinessException(BusinessErrorCode businessErrorCode) {
        this.businessErrorCode = businessErrorCode;
    }

    public BusinessException(BusinessErrorCode businessErrorCode, Map<String, Object> contextVariables) {
        this.businessErrorCode = businessErrorCode;
        this.contextVariables = contextVariables;
    }
}
