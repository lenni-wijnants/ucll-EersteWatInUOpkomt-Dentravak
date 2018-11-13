package be.ucll.ewiuo.Controller;

        import java.math.BigDecimal;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;
        import java.util.concurrent.atomic.AtomicLong;

        import be.ucll.ewiuo.Model.Sandwich;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestMethod;
        import org.springframework.web.bind.annotation.RequestParam;
        import org.springframework.web.bind.annotation.RestController;

@RestController
public class SandwichListController {

    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/sandwiches", method = RequestMethod.GET)
    public List<Sandwich> sandwiches() {
            Sandwich s1 = Sandwich.SandwichBuilder.aSandwich().WithName("smos").WithIngedients("kaas, hesp").WithPrice(new BigDecimal(2.30)).build();
            Sandwich s2 = Sandwich.SandwichBuilder.aSandwich().WithName("boulet").WithIngedients("boulet, andalouse, sla, ei, augurk").WithPrice(new BigDecimal(2.45)).build();
            Sandwich s3 = Sandwich.SandwichBuilder.aSandwich().WithName("americain").WithIngedients("americain, augurk").WithPrice(new BigDecimal(2.20)).build();
        return Arrays.asList(s1, s2, s3);
        //list.add(new Sandwich(counter.incrementAndGet(),"Smos","kaas, hesp, mayonnaise, ei, sla", new BigDecimal(2.45)));
        //list.add(new Sandwich(counter.incrementAndGet(),"Boulet","sla, ei, boulet, andalouse, augurk", new BigDecimal(2.45)));
        //list.add(new Sandwich(counter.incrementAndGet(),"Americain","americain, augurk", new BigDecimal(2.20)));
        //return list;
        //return new Sandwich(counter.incrementAndGet(), String.format("sandwich: %s", sandwich), String.format("contains: %s", ingredients));
    }
}
