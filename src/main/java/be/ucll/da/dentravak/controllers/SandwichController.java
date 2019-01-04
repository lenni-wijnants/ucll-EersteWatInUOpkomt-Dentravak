package be.ucll.da.dentravak.controllers;

import be.ucll.da.dentravak.model.Sandwich;
import be.ucll.da.dentravak.model.SandwichPreferences;
import be.ucll.da.dentravak.repositories.SandwichRepository;
import com.google.common.collect.Lists;
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
            ArrayList<Sandwich> allSandwiches = Lists.newArrayList(repository.findAll());

            ArrayList<UUID> keys = new ArrayList<>();

            addIdsInOrder(preferences, keys);

            for(int i = 0; i < keys.size(); i++) {
                for(int j = 0; j < allSandwiches.size(); j++) {
                    if(keys.get(i).equals(allSandwiches.get(j).getId())) {
                        Collections.swap(allSandwiches, i, j);
                    }
                }
            }

            return allSandwiches;
        } catch (ServiceUnavailableException e) {
            return repository.findAll();
        }
    }

    private void addIdsInOrder(SandwichPreferences preferences, ArrayList<UUID> keys) {
        if(preferences.size() > 1) {
            float smallestRating = -1;
            for (UUID key : preferences.keySet()) {
                float currentRating = preferences.getRatingForSandwich(key);
                if (smallestRating < currentRating) {
                    smallestRating = currentRating;
                    keys.add(key);
                    preferences.remove(key);
                    addIdsInOrder(preferences, keys);
                    break;
                }
            }
        } else {
            keys.add((UUID) preferences.keySet().toArray()[0]);
        }
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