package ro.axon.dot.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ro.axon.dot.domain.EmployeeEty;
import ro.axon.dot.domain.LegallyDaysOffEty;

import java.rmi.server.UID;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface LegallyDaysOffRepository extends
        JpaRepository<LegallyDaysOffEty, LocalDate>,
        QuerydslPredicateExecutor<LegallyDaysOffEty>{
}
