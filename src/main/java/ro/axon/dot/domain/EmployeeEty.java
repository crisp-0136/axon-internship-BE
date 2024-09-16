package ro.axon.dot.domain;

import liquibase.pro.packaged.C;
import lombok.Getter;
import lombok.Setter;
import ro.axon.dot.domain.Enums.Status;

import javax.persistence.*;
import java.rmi.server.UID;
import java.time.Instant;
import java.util.Date;

@Entity
@SequenceGenerator(name = "EMPLOYEE_ID_SQ", sequenceName = "EMPLOYEE_ID_SQ", allocationSize = 1)
@Getter
@Setter
@Table(name = "EMPLOYEE")
public class EmployeeEty extends SrgKeyEntityTml<UID>{

    @Id
    @Column(name = "EMPLOYEE_ID", nullable = false)
    private UID id;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "CRT_USR", nullable = false)
    private String crtUsr;

    @Column(name = "CRT_TMS", nullable = false)
    private Instant crtTms;

    @Column(name = "MDF_USR", nullable = false)
    private String mdfUsr;

    @Column(name = "MDF_TMS", nullable = false)
    private Instant mdfTms;

    @Column(name = "ROLE", nullable = false)
    private String role = "USER";

    @Column(name = "STATUS", nullable = false)
    private Status status;

    @Column(name = "CONTRACT_START_DATE", nullable = false)
    private Date contractStartDate;

    @Column(name = "CONTRACT_END_DATE", nullable = true)
    private Date contractEndDate;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID", nullable = false)
    private TeamEty team;

//    @Version
//    @Column(name = "V", nullable = false)
//    private int version;

    @PrePersist
    protected void onCreate() {
        if(this.id == null){
            this.id = new UID();
        }
    }

    @Override
    protected Class<? extends SrgKeyEntityTml<UID>> entityRefClass(){
        return EmployeeEty.class;
    }

}
