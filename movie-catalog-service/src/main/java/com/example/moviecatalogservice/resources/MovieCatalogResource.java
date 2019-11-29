package com.example.moviecatalogservice.resources;

import com.example.moviecatalogservice.model.CatalogItem;
import com.example.moviecatalogservice.model.external.Movie;
import com.example.moviecatalogservice.model.external.Rating;
import com.example.moviecatalogservice.model.external.UserRatings;
import com.google.common.collect.Lists;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.xml.ws.ResponseWrapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
@EnableEurekaClient // not mandatoruy
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    //@HystrixCommand(fallbackMethod = "getUserCatalogFallBack")
    public List<CatalogItem> getUserCatalog(@PathVariable("userId") String userId) {
        // get all rated movie ids
        // UserRatings userRatings = restTemplate.getForObject("http://localhost:8083/ratingsdata/user/" + userId, UserRatings.class);
        // the below aproach is with Eureka
        // first solution: without hystrix >> UserRatings userRatings = restTemplate.getForObject("http://rating-data-service/ratingsdata/user/" + userId, UserRatings.class);

        UserRatings userRatings = getUserRatings(userId);
        /* to deal with List Response from services
        ResponseEntity<List<Rating>> exchange = restTemplate.exchange("http://localhost:8083/ratingsdata/user/" + userId, HttpMethod.POST, HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<Rating>>() {
                });
        */


        // for each movie id, get movie info
        List<CatalogItem> catalogItems = userRatings.getRatings().stream()
                .map(rating -> {
                    Movie movie = getMovie(rating.getMovieId());
                    /* 2nd aproach..
                    Movie movie = webClientBuilder.build()
                            .get()
                            .uri("http://localhost:8082/movies/" + rating.getMovieId())
                            .retrieve()
                            .bodyToMono(Movie.class)
                            .block();*/
                    return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
                })
                .collect(Collectors.toList());

        // put them together
        // 1. Collections.singletonList(new CatalogItem("3 idots","a movie by amir khan", 5));
        return catalogItems;
    }

    // this fallback works here in this class (no need to create another service.) Because whoever is calling
    // getUserCatalog method (in our case spring is calling getUserCatalog) doesn't has actual instance of MovieCatalogResource
    // but it has a proxy instance of MovieCatalogResource and this proxy has logic of circuit breaking
    private List<CatalogItem> getUserCatalogFallBack(@PathVariable("userId") String userId) {
        return Collections.singletonList(new CatalogItem("no name", "no description", 0));
    }

    /*commandProperties = {
            @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE")
    }*/
    @HystrixCommand(fallbackMethod= "getUserRatingsFallback")
    public UserRatings getUserRatings(String userId) {
        return restTemplate.getForObject("http://rating-data-service/ratingsdata/user/" + userId, UserRatings.class);
    }

    @HystrixCommand(fallbackMethod= "getMovieFallback")
    private Movie getMovie(String movieId){
        return restTemplate.getForObject("http://movie-info-service/movies/" + movieId, Movie.class);
    }

    private Movie getMovieFallback(String movieId){
        Movie movie = new Movie();
        movie.setMovieId(movieId);
        movie.setName("no name");
        movie.setDescription("no desc");
        return movie;
    }

    public UserRatings getUserRatingsFallback(String userId) {
        return new UserRatings(userId, Arrays.asList(new Rating("100",0)));
    }
}
