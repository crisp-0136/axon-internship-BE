package ro.axon.dot.domain;

import lombok.Getter;
import lombok.Setter;
import ro.axon.dot.domain.enums.LeaveRequestType;
import ro.axon.dot.domain.enums.LeaveRequestStatus;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@SequenceGenerator(name="LEAVE_REQUEST_ID_SQ", sequenceName = "LEAVE_REQUEST_ID_SQ", allocationSize = 1)
@Entity
@Setter
@Getter
@Table(name = "LEAVE_REQUEST")
public class LeaveReqEty extends SrgKeyEntityTml<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LEAVE_REQUEST_ID_SQ")
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false, foreignKey = @ForeignKey(name = "LEAVE_REQUEST_EMPLOYEE_FK"))
    private EmployeeEty employee;

    @Column(name = "CRT_USR")
    private String crtUsr;

    @Column(name = "CRT_TMS")
    private Instant crtTms;

    @Column(name = "MDF_USR")
    private String mdfUsr;

    @Column(name = "MDF_TMS")
    private Instant mdfTms;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "NO_DAYS")
    private int noDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private LeaveRequestType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private LeaveRequestStatus status;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "REJECT_REASON")
    private String rejectReason;

    @Override
    protected Class<? extends SrgKeyEntityTml<Long>> entityRefClass() {
        return LeaveReqEty.class;
    }
}
