package ro.axon.dot.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.rmi.server.UID;

public interface LegallyDaysOffRepository extends
        JpaRepository<EmployeeEty, UID>,
        QuerydslPredicateExecutor<EmployeeEty>{
}
