package ro.axon.dot.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ro.axon.dot.domain.TeamEty;

public interface TeamRepository extends
    JpaRepository<TeamEty, Long>,
    QuerydslPredicateExecutor<TeamEty> {

}
