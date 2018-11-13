package be.ucll.ewiuo.Controller;

        import java.math.BigDecimal;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;
        import java.util.concurrent.atomic.AtomicLong;

        import be.ucll.ewiuo.Model.Sandwich;
        import be.ucll.ewiuo.repository.SandwichRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestMethod;
        import org.springframework.web.bind.annotation.RequestParam;
        import org.springframework.web.bind.annotation.RestController;

@RestController
public class SandwichListController {

    @Autowired SandwichRepository repository;

    @RequestMapping(value = "/sandwiches", method = RequestMethod.GET)
    public List<Sandwich> sandwiches() {
        return (List<Sandwich>) repository.findAll();
    }
}
