package ro.axon.dot.model;

import lombok.Data;
import ro.axon.dot.domain.Enums.EmpYearlyDaysOffHistType;

import java.time.LocalDateTime;

@Data
public class EmpYearlyDaysOffHistItem {

    private Long id;
    private Integer noDays;
    private String description;
    private EmpYearlyDaysOffHistType type;
    private String createdBy;
    private LocalDateTime createdTimestamp;
}
