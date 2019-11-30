package com.example.moviecatalogservice.service;

import com.example.moviecatalogservice.model.external.Movie;
import com.netflix.discovery.converters.Auto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieInfoService {
    @Autowired
    private RestTemplate restTemplate;


//    @HystrixCommand(fallbackMethod = "getMovieFallback",
//            commandProperties = {
//                    @HystrixProperty(name = "hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", value = "10000"), //https://github.com/Netflix/Hystrix/wiki/Configuration#executionisolationthreadtimeoutinmilliseconds
//                    @HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false"),// default is true
//                    @HystrixProperty(name = "hystrix.command.default.metrics.rollingStats.timeInMilliseconds", value = "10000"),// default is 10000
//                    @HystrixProperty(name = "hystrix.command.default.circuitBreaker.errorThresholdPercentage", value = "50"), // default is 50
//                    @HystrixProperty(name = "hystrix.command.default.circuitBreaker.requestVolumeThreshold", value = "3"), // circuit will open if in rolling time window fail % is reached and requestVolumeThreshold is crossed
//                    @HystrixProperty(name = "hystrix.command.default.circuitBreaker.sleepWindowInMilliseconds", value = "30000") // default is 5000
//            }
//    )
    @HystrixCommand(fallbackMethod = "getMovieFallback")
    public Movie getMovie(String movieId){
        return restTemplate.getForObject("http://movie-info-service/movies/" + movieId, Movie.class);
    }
    private Movie getMovieFallback(String movieId){
        Movie movie = new Movie();
        movie.setMovieId(movieId);
        movie.setName("movie info is down..no name");
        movie.setDescription("movie info is down..no desc");
        return movie;
    }
}
