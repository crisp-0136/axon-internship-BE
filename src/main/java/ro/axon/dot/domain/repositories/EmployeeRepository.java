package ro.axon.dot.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ro.axon.dot.domain.EmployeeEty;

import java.rmi.server.UID;


public interface EmployeeRepository extends
    JpaRepository<EmployeeEty, String>,
    QuerydslPredicateExecutor<EmployeeEty>{

}
