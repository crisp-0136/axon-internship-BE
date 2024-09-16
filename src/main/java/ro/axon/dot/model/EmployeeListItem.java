package ro.axon.dot.model;

import lombok.Data;

import java.rmi.server.UID;
import java.time.Instant;

@Data
public class EmployeeListItem {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Instant contractStartDate;
    private Instant contractEndDate;
    private String teamName;
    private String status;
    private String crtUsr;
    private Instant crtTms;
    private String mdfUsr;
    private Instant mdfTms;
}
