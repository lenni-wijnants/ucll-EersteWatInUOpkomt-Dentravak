package be.ucll.ewiuo.repository;

import be.ucll.ewiuo.Model.LunchOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends CrudRepository<LunchOrder, UUID> {
    @Query("select a from LunchOrder a where SUBSTRING(a.creationDate, 1, 8) = SUBSTRING(:creationDate, 1, 8)")
    List<LunchOrder> findAllByDate(@Param("creationDate") LocalDateTime creationDate );
}
