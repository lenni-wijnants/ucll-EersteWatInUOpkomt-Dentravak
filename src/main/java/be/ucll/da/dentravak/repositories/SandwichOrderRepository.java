package be.ucll.da.dentravak.repositories;

import be.ucll.da.dentravak.model.SandwichOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SandwichOrderRepository extends CrudRepository<SandwichOrder, UUID> {
    @Query("select a from LunchOrder a where SUBSTRING(a.creationDate, 1, 8) = SUBSTRING(:creationDate, 1, 8)")
    List<SandwichOrder> findAllByDate(@Param("creationDate") LocalDateTime creationDate );
}
