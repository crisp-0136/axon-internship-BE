package ro.axon.dot.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name= "EMP_YEARLY_DAYS_OFF")
@SequenceGenerator(name = "EMP_YEARLY_DAYS_OFF_ID_SQ", sequenceName = "EMP_YEARLY_DAYS_OFF_ID_SQ", allocationSize = 1)
public class EmpYearlyDaysOffEty {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMP_YEARLY_DAYS_OFF_ID_SQ")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false, foreignKey = @ForeignKey(name = "EMP_YEARLY_DAYS_OFF_EMPLOYEE_FK"))
    private EmployeeEty employee;

    @Column(name = "TOTAL_NO_DAYS", nullable = false)
    private Integer totalNoDays;

    @Column(name = "YEAR", nullable = false)
    private Integer year;

    @OneToMany(mappedBy = "empYearlyDaysOffEty", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmpYearlyDaysOffHistEty> historyRecords = new HashSet<>();
}
