package be.ucll.ewiuo.controller;

import be.ucll.ewiuo.model.Sandwich;
import be.ucll.ewiuo.model.SandwichPreferences;
import be.ucll.ewiuo.repository.SandwichRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.naming.ServiceUnavailableException;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
public class SandwichListController {

    @Inject
    private DiscoveryClient discoveryClient;

    @Inject
    private SandwichRepository repository;

    @Inject
    private RestTemplate restTemplate;

    @RequestMapping(value = "/sandwiches", method = RequestMethod.GET)
    public Iterable<Sandwich> sandwiches() {
        try
        {
            SandwichPreferences preferences = getPreferences("ronald.dehuysser@ucll.be");
            Iterable<Sandwich> allSandwiches = repository.findAll();
            return allSandwiches;
        }catch (ServiceUnavailableException e)
        {
            return repository.findAll();
        }
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

    @GetMapping("/getpreferences/{emailAddress}")
    public SandwichPreferences getPreferences(@PathVariable String emailAddress) throws RestClientException, ServiceUnavailableException {
        URI service = recommendationServiceUrl()
                .map(s -> s.resolve("/recommend/" + emailAddress))
                .orElseThrow(ServiceUnavailableException::new);
        return restTemplate
                .getForEntity(service, SandwichPreferences.class)
                .getBody();
    }



    public Optional<URI> recommendationServiceUrl() {
        return discoveryClient.getInstances("recommendation")
                .stream()
                .map(si -> si.getUri())
                .findFirst();
    }
}