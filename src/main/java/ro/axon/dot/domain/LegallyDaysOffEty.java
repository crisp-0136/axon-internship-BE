package ro.axon.dot.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;


@Entity
@Data
@Getter
@Setter
@Table(name = "LEGALLY_DAYS_OFF")
public class LegallyDaysOffEty {

    @Id
    @Column(name = "DATE", nullable = false)
    private LocalDate date;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;
}
