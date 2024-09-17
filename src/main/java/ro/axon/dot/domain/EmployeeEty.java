package ro.axon.dot.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "EMPLOYEE")
public class EmployeeEty extends SrgKeyEntityTml<String> {

    @Id
    @GeneratedValue(generator = "employee-uuid")
    @GenericGenerator(name = "employee-uuid", strategy = "uuid2")
    @Column(name = "EMPLOYEE_ID", nullable = false, length = 255)
    private String employeeId;

    @Column(name = "USERNAME", nullable = false, length = 255)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 255)
    private String password;

    @Column(name = "FIRST_NAME", nullable = false, length = 255)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 255)
    private String lastName;

    @Column(name = "EMAIL", nullable = false, length = 255)
    private String email;

    @Column(name = "CRT_USR", nullable = false, length = 255)
    private String crtUsr;

    @Column(name = "CRT_TMS", nullable = false)
    private Instant crtTms;

    @Column(name = "MDF_USR", nullable = false, length = 255)
    private String mdfUsr;

    @Column(name = "MDF_TMS", nullable = false)
    private Instant mdfTms;

    @Column(name = "ROLE", nullable = false, length = 255)
    private String role = "USER";

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 255)
    private String status;

    @Column(name = "CONTRACT_START_DATE", nullable = false)
    private Date contractStartDate;

    @Column(name = "CONTRACT_END_DATE")
    private Date contractEndDate;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID", nullable = false, foreignKey = @ForeignKey(name = "EMPLOYEE_TEAM_FK"))
    private TeamEty team;

    @Override
    public String getId() {
        return this.employeeId;
    }

    @Override
    protected Class<? extends SrgKeyEntityTml<String>> entityRefClass() {
        return EmployeeEty.class;
    }
}
