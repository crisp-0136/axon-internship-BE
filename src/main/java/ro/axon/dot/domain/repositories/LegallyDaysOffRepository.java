package ro.axon.dot.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import ro.axon.dot.domain.LegallyDaysOffEty;

import java.time.LocalDate;
import java.util.List;

public interface LegallyDaysOffRepository extends
        JpaRepository<LegallyDaysOffEty, LocalDate>,
        QuerydslPredicateExecutor<LegallyDaysOffEty>{

    // Custom query to filter by year (years should be in string format like "YYYY")
    @Query("SELECT l FROM LegallyDaysOffEty l WHERE TO_CHAR(l.date, 'YYYY') IN :years")
    List<LegallyDaysOffEty> findByYearIn(@Param("years") List<String> years);

    // Custom query to filter by periods (periods should be in "YYYY-MM" format)
    @Query("SELECT l FROM LegallyDaysOffEty l WHERE TO_CHAR(l.date, 'YYYY-MM') IN :periods")
    List<LegallyDaysOffEty> findByPeriodIn(@Param("periods") List<String> periods);
}
