package ro.axon.dot.model;

import lombok.Data;
import ro.axon.dot.domain.enums.Status;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class EmployeeDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String crtUsr;
    private Instant crtTms;
    private String mdfUsr;
    private Instant mdfTms;
    private String role;
    private Status status;
    private LocalDate contractStartDate;
    private Long v;
    private Integer totalVacationDays;
    private TeamDto teamDetails;
    private String username;
}
