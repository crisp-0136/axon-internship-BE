package ro.axon.dot.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LegallyDaysOffDto {
    private LocalDate date;
    private String description;

}
