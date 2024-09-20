package ro.axon.dot.model;

import lombok.Data;

@Data
public class UpdateEmployeeDto {

    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private Long teamId;
    private int v;
}
