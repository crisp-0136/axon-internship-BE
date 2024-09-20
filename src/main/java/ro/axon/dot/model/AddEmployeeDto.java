package ro.axon.dot.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AddEmployeeDto {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private Long teamId;
    private LocalDate contractStartDate;
    private int noDaysOff;
}
