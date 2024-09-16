package ro.axon.dot.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name= "EMP_YEARLY_DAYS_OFF")
public class EmpYearlyDaysOff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TOTAL_NO_DAYS", nullable = false)
    private Integer totalNoDays;

    @Column(name = "YEAR", nullable = false)
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false)
    //private Employee employee;

    @OneToMany(mappedBy = "empYearlyDaysOff", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmpYearlyDaysOffHist> historyRecords;

}
