package ro.axon.dot.domain;


import lombok.Getter;
import lombok.Setter;
import ro.axon.dot.domain.enums.EmpYearlyDaysOffHistType;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name= "EMP_YEARLY_DAYS_OFF_Hist")
@SequenceGenerator(name = "EMP_YEARLY_DAYS_OFF_HIST_ID_SQ", sequenceName = "EMP_YEARLY_DAYS_OFF_HIST_ID_SQ", allocationSize = 1)
public class EmpYearlyDaysOffHistEty {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMP_YEARLY_DAYS_OFF_HIST_ID_SQ")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NO_DAYS")
    private Integer noDays;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private EmpYearlyDaysOffHistType type;

    @Column(name = "CRT_USR", nullable = false)
    private String crtUsr;

    @Column(name = "CRT_TMS", nullable = false)
    private Instant crtTms;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMP_YEARLY_DAYS_OFF_ID", nullable = false)
    private EmpYearlyDaysOffEty empYearlyDaysOffEty;

}
