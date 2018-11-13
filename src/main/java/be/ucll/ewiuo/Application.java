package be.ucll.ewiuo;

import be.ucll.ewiuo.Model.Sandwich;
import be.ucll.ewiuo.repository.SandwichRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(SandwichRepository repository) {
        return (args) -> {

            repository.save(Sandwich.SandwichBuilder.aSandwich()
                    .WithName("smos")
                    .WithIngedients("kaas, hesp")
                    .WithPrice(new BigDecimal(2.30))
                    .build());
            repository.save(Sandwich.SandwichBuilder.aSandwich()
                    .WithName("boulet")
                    .WithIngedients("boulet, andalouse, sla, ei, augurk")
                    .WithPrice(new BigDecimal(2.45)).build());
            repository.save(Sandwich.SandwichBuilder.aSandwich()
                    .WithName("americain").WithIngedients("americain, augurk")
                    .WithPrice(new BigDecimal(2.20))
                    .build());

            /*
            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            for (Customer customer : repository.findAll()) {
                log.info(customer.toString());
            }
            log.info("");

            // fetch an individual customer by ID
            repository.findById(1L)
                    .ifPresent(customer -> {
                        log.info("Customer found with findById(1L):");
                        log.info("--------------------------------");
                        log.info(customer.toString());
                        log.info("");
                    });

            // fetch customers by last name
            log.info("Customer found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            repository.findByLastName("Bauer").forEach(bauer -> {
                log.info(bauer.toString());
            });
            // for (Customer bauer : repository.findByLastName("Bauer")) {
            // 	log.info(bauer.toString());
            // }
            log.info("");
            */
        };
    }
}