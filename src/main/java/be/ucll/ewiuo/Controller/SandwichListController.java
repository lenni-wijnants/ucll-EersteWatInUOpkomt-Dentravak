package be.ucll.ewiuo.Controller;

import be.ucll.ewiuo.Model.Sandwich;
import be.ucll.ewiuo.repository.SandwichRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class SandwichListController {

    @Autowired SandwichRepository repository;

    @RequestMapping(value = "/sandwiches", method = RequestMethod.GET)
    public Iterable<Sandwich> sandwiches() {
        return repository.findAll();
    }

    @RequestMapping(value = "/sandwiches", method = RequestMethod.POST)
    public Sandwich addSandwich(@RequestBody Sandwich sandwich){
        return repository.save(sandwich);
    }

    @RequestMapping(value = "/sandwiches/{id}", method = RequestMethod.GET)
    public Sandwich getSandwich(@PathVariable UUID id){
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException(id + "not found"));
    }

    @RequestMapping(value = "/sandwiches/{id}", method = RequestMethod.PUT)
    public Sandwich updateSandwich(@PathVariable UUID id, @RequestBody Sandwich newsandwich){
        return repository.findById(id).map(sandwich -> {
            sandwich.setName(newsandwich.getName());
            sandwich.setIngredients(newsandwich.getIngredients());
            sandwich.setPrice(newsandwich.getPrice());
            return repository.save(sandwich);
        }).orElseGet(() -> {
            newsandwich.setId(id);
            return repository.save(newsandwich);
        });
    }
}