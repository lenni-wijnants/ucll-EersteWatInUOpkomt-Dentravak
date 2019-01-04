package be.ucll.da.dentravak.controllers;

import be.ucll.da.dentravak.model.Sandwich;
import be.ucll.da.dentravak.model.SandwichPreferences;
import be.ucll.da.dentravak.repositories.SandwichRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.naming.ServiceUnavailableException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@RestController
public class SandwichController {

    @Inject
    private DiscoveryClient discoveryClient;

    @Inject
    private SandwichRepository repository;

    @Inject
    private RestTemplate restTemplate;

    @RequestMapping("/sandwiches")
    public Iterable<Sandwich> sandwiches() {
        try {
            SandwichPreferences preferences = getPreferences("ronald.dehuysser@ucll.be");
            //TODO: sort allSandwiches by float in preferences
            Iterable<Sandwich> allSandwiches = repository.findAll();
            List<Sandwich> sandwiches = (List<Sandwich>) allSandwiches;
            List<Sandwich> sortedSandwiches = new ArrayList<>();
            sandwiches.sort((Sandwich s1, Sandwich s2) -> preferences.getRatingForSandwich(s2.getId()).compareTo(preferences.getRatingForSandwich(s1.getId())));

            return sortedSandwiches;
        } catch (ServiceUnavailableException e) {
            return repository.findAll();
        }
    }

    void sortSandwichList(SandwichPreferences prefs, ArrayList<UUID> sortedKeys){
        if(prefs.size() > 1) {
            float smallestRating = -999;
            for (UUID key : prefs.keySet()) {
                float currentRating = prefs.getRatingForSandwich(key);
                if (smallestRating < currentRating) {
                    smallestRating = currentRating;
                    sortedKeys.add(key);
                    prefs.remove(key);
                    sortSandwichList(prefs, sortedKeys);
                    break;
                }
            }
        } else {
            sortedKeys.add((UUID) prefs.keySet().toArray()[0]);
        }
        //allSandwiches.sort((Sandwich s1, Sandwich s2) -> prefs.getRatingForSandwich(s2.getId()).compareTo(prefs.getRatingForSandwich(s1.getId())));
        //return allSandwiches;
    }

    @RequestMapping(value = "/sandwiches", method = RequestMethod.POST)
    public Sandwich createSandwich(@RequestBody Sandwich sandwich) {
        return repository.save(sandwich);
    }

    @RequestMapping(value = "/sandwiches/{id}", method = RequestMethod.PUT)
    public Sandwich updateSandwich(@PathVariable UUID id, @RequestBody Sandwich sandwich) {
        if(!id.equals(sandwich.getId())) throw new IllegalArgumentException("Nownow, are you trying to hack us.");
        return repository.save(sandwich);
    }

    // why comment: for testing
    @GetMapping("/getpreferences/{emailAddress}")
    public SandwichPreferences getPreferences(@PathVariable String emailAddress) throws RestClientException, ServiceUnavailableException {
        URI service = recommendationServiceUrl()
                .map(s -> s.resolve("/recommendation/recommend/" + emailAddress))
                .orElseThrow(ServiceUnavailableException::new);
        return restTemplate
                .getForEntity(service, SandwichPreferences.class)
                .getBody();
    }

//    public Optional<URI> recommendationServiceUrl() {
//        try {
//            return Optional.of(new URI("http://localhost:8081"));
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public Optional<URI> recommendationServiceUrl() {
        return discoveryClient.getInstances("recommendation")
                .stream()
                .map(si -> si.getUri())
                .findFirst();
    }
}