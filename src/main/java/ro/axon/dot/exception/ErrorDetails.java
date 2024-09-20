package ro.axon.dot.exception;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ErrorDetails {

    private List<ErrorDetailsItem> errors;

    @Builder
    @Data
    public static class ErrorDetailsItem {

        private String errorCode;
        private String message;
        private Map<String, Object> contextVariables;
    }
}