package ro.axon.dot.model;

import lombok.Data;
import ro.axon.dot.domain.enums.LeaveRequestType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class UpdateLeaveReqDTO {

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private LeaveRequestType type;

    @Size(max = 255)
    private String description;

    private int v;
}
