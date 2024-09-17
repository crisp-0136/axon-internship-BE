package ro.axon.dot.domain;

import lombok.Getter;
import lombok.Setter;
import ro.axon.dot.domain.Enums.LeaveRequestType;
import ro.axon.dot.domain.Enums.LeaveRequestStatus;
import ro.axon.dot.model.EmployeeListItem;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@SequenceGenerator(name="LEAVE_REQUEST_ID_SQ", sequenceName = "LEAVE_REQUEST_ID_SQ", allocationSize = 1)
@Entity
@Setter
@Getter
@Table(name = "LEAVE_REQUEST")
public class LeaveReqEty extends SrgKeyEntityTml<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LEAVE_REQUEST_ID_SQ")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "EMPLOYEE_ID", foreignKey = @ForeignKey(name = "LEAVE_REQUEST_EMPLOYEE_FK"))
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
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

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

//    @Version
//    @Column(name = "V", nullable = false)
//    private int version;


    @Override
    protected Class<? extends SrgKeyEntityTml<Long>> entityRefClass() {
        return null;
    }
}
