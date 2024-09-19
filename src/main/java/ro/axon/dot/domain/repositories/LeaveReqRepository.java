package ro.axon.dot.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ro.axon.dot.domain.LeaveReqEty;

public interface LeaveReqRepository extends
        JpaRepository<LeaveReqEty, Long>,
        QuerydslPredicateExecutor<LeaveReqEty> {
}
