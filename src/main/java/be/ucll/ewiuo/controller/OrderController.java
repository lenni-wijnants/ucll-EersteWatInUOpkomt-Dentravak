package be.ucll.ewiuo.controller;

import be.ucll.ewiuo.model.LunchOrder;
import be.ucll.ewiuo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@RestController
public class OrderController {

    @Autowired
    OrderRepository repository;

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public Iterable<LunchOrder> orders() {
        return repository.findAll();
    }

    @RequestMapping(value = "/orders?date={dateText}", method = RequestMethod.GET)
    public Iterable getOrdersByDate(@PathVariable String dateText){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        LocalDateTime date;
        date = LocalDateTime.parse(dateText);
        return repository.findAllByDate(date);
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public LunchOrder addOrder(@RequestBody LunchOrder lunchOrder){
        return repository.save(lunchOrder);
    }
}