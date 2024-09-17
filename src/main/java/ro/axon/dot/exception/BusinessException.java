package ro.axon.dot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.util.Map;

@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final BusinessErrorCode businessErrorCode;
    private final Map<String, Object> contextVariables;
}
