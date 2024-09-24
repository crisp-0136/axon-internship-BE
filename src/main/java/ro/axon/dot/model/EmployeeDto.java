package ro.axon.dot.model;

import lombok.Data;

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
    private String status;
    private LocalDate contractStartDate;
    private Integer totalVacationDays;
    private TeamDto teamDetails;
    private String username;
}
