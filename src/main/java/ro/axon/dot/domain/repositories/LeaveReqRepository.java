package ro.axon.dot.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import ro.axon.dot.domain.EmployeeEty;
import ro.axon.dot.domain.LeaveReqEty;
import ro.axon.dot.domain.enums.LeaveRequestType;

import java.time.LocalDate;
import java.util.Optional;

public interface LeaveReqRepository extends
        JpaRepository<LeaveReqEty, Long>,
        QuerydslPredicateExecutor<LeaveReqEty> {

    @Query("SELECT lr FROM LeaveReqEty lr WHERE lr.employeeEty = :employee AND lr.startDate = :startDate " +
            "AND lr.endDate = :endDate AND lr.type = :type")
    Optional<LeaveReqEty> findDuplicateRequests(@Param("employee") EmployeeEty employee,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate,
                                                @Param("type") LeaveRequestType type);
}
