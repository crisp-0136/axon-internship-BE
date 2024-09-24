package ro.axon.dot.model;

import lombok.Data;
import ro.axon.dot.domain.enums.LeaveRequestType;

import java.time.LocalDate;

@Data
public class LeaveReqDto {

    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveRequestType type;
    private String description;

}
