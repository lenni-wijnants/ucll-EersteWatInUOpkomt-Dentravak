package be.ucll.da.dentravak.controllers;

import be.ucll.da.dentravak.model.Sandwich;
import be.ucll.da.dentravak.model.SandwichPreferences;
import be.ucll.da.dentravak.repositories.SandwichRepository;
import com.google.common.collect.Lists;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.naming.ServiceUnavailableException;
import java.net.URI;
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

            if(preferences.size() > 1) {
                float max = -9999;
                List<Sandwich> sortedList = new ArrayList<>();
                List<Sandwich> sortResidue = new ArrayList<>();
                Sandwich fill = new Sandwich();

                for(int i = 0; i < allSandwiches.size(); i++){
                    for(int j = 0; j < allSandwiches.size(); j++){
                        if(preferences.getRatingForSandwich(allSandwiches.get(i).getId()) == null) {
                            sortResidue.add(allSandwiches.get(i));
                            allSandwiches.remove(i);
                        }else if (preferences.getRatingForSandwich(allSandwiches.get(j).getId()) == null) {
                            sortResidue.add(allSandwiches.get(j));
                            allSandwiches.remove(j);
                        }else if(preferences.getRatingForSandwich(allSandwiches.get(i).getId()) < preferences.getRatingForSandwich(allSandwiches.get(j).getId())){
                            Sandwich temp = allSandwiches.get(i);
                            allSandwiches.set(i, allSandwiches.get(j));
                            allSandwiches.set(j, temp);
                        }
                    }
                }

                for(Sandwich s : allSandwiches){
                    if(preferences.getRatingForSandwich(s.getId()) == null){
                        sortResidue.add(s);
                    }else {
                        if(preferences.getRatingForSandwich(s.getId()) > max){
                            sortedList = insertAtStartOfList(sortedList, s);
                        }else {
                            sortedList.add(s);
                        }
                    }
                }
                sortedList.addAll(sortResidue);

                return sortedList;
            }else return allSandwiches;
        } catch (ServiceUnavailableException e) {
            return repository.findAll();
        }
    }

    List<Sandwich> insertAtStartOfList(List<Sandwich> sandwiches, Sandwich s){
        List<Sandwich> newList = new ArrayList<>(sandwiches.size() + 1);
        newList.set(0, s);
        for(int i = 0; i < sandwiches.size(); i++){
            newList.set(i + 1, sandwiches.get(i));
        }

        return newList;
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