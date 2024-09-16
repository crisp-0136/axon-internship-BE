package ro.axon.dot.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name= "EMP_YEARLY_DAYS_OFF_Hist")
public class EmpYearlyDaysOffHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NO_DAYS", nullable = false)
    private Integer noDays;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(name = "CRT_USR", nullable = false)
    private String createdBy;

    @Column(name = "CRT_TMS", nullable = false)
    private LocalDateTime createdTimestamp;

    // Many-to-One relationship with EmpYearlyDaysOff
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMP_YEARLY_DAYS_OFF_ID", nullable = false)
    private EmpYearlyDaysOff empYearlyDaysOff;


}
