package be.ucll.da.dentravak.controllers;

import be.ucll.da.dentravak.model.SandwichOrder;
import be.ucll.da.dentravak.repositories.SandwichOrderRepository;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
public class SandwichOrderController {

    private SandwichOrderRepository repository;

    public SandwichOrderController(SandwichOrderRepository repository) {
        this.repository = repository;
    }

    @RequestMapping("/orders")
    public Iterable<SandwichOrder> orders() {
        return repository.findAll();
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public SandwichOrder createSandwichOrder(@RequestBody SandwichOrder sandwichOrder) {
        sandwichOrder.setCreationDate(LocalDateTime.now());
        return repository.save(sandwichOrder);
    }

    @RequestMapping(value = "/orders/{id}", method = RequestMethod.GET)
    public SandwichOrder getOrder(@PathVariable("id") UUID id) {
        return repository.findById(id).get();
    }

    @RequestMapping(value = "/orders/{date}", method = RequestMethod.GET)
    public Iterable<SandwichOrder> getOrder(@PathVariable("date") LocalDateTime date) {
        return repository.findAllByDate(date);
    }

}

