package ro.axon.dot.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import ro.axon.dot.domain.EmployeeEty;

import java.rmi.server.UID;
import java.util.List;


public interface EmployeeRepository extends
    JpaRepository<EmployeeEty, String>,
    QuerydslPredicateExecutor<EmployeeEty>{
    @Query("SELECT e FROM EmployeeEty e WHERE (:name IS NULL OR e.firstName LIKE %:name% OR e.lastName LIKE %:name%)")
    List<EmployeeEty> findByName(@Param("name") String name);
}
