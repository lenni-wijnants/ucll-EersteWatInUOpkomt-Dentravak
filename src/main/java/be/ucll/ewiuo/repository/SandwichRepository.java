package be.ucll.ewiuo.repository;

import java.util.List;
import java.util.UUID;

import be.ucll.ewiuo.model.Sandwich;
import org.springframework.data.repository.CrudRepository;

public interface SandwichRepository extends CrudRepository<Sandwich, UUID> {
    List<Sandwich> findByName(String name);
}