package ro.axon.dot.domain;


import lombok.Getter;
import lombok.Setter;
import ro.axon.dot.domain.Enums.EmpYearlyDaysOffHistType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name= "EMP_YEARLY_DAYS_OFF_Hist")
public class EmpYearlyDaysOffHist {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMP_YEARLY_DAYS_OFF_HIST_ID_SQ")
    @SequenceGenerator(name = "EMP_YEARLY_DAYS_OFF_HIST_ID_SQ", sequenceName = "EMP_YEARLY_DAYS_OFF_HIST_ID_SQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emp_yearly_days_off_id", insertable = false, updatable = false)
    private EmployeeEty employee;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private EmpYearlyDaysOffHistType type;

    @Column(name = "CRT_USR", nullable = false)
    private String createdBy;

    @Column(name = "CRT_TMS", nullable = false)
    private LocalDateTime createdTimestamp;

    // Many-to-One relationship with EmpYearlyDaysOff
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMP_YEARLY_DAYS_OFF_ID", nullable = false)
    private EmpYearlyDaysOff empYearlyDaysOff;


}
