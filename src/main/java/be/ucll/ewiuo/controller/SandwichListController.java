package be.ucll.ewiuo.controller;

import be.ucll.ewiuo.model.Sandwich;
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
    public Sandwich updateSandwich(@PathVariable UUID id, @RequestBody Sandwich newSandwich){
        return repository.findById(id).map(sandwich -> {
            sandwich.setName(newSandwich.getName());
            sandwich.setIngredients(newSandwich.getIngredients());
            sandwich.setPrice(newSandwich.getPrice());
            return repository.save(sandwich);
        }).orElseGet(() -> {
            newSandwich.setId(id);
            return repository.save(newSandwich);
        });
    }
}