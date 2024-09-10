package ro.axon.dot.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TeamRepository extends
    JpaRepository<TeamEty, Long>,
    QuerydslPredicateExecutor<TeamEty> {

}
