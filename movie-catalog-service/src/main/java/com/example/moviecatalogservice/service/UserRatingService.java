package com.example.moviecatalogservice.service;

import com.example.moviecatalogservice.model.external.Rating;
import com.example.moviecatalogservice.model.external.UserRatings;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class UserRatingService {
    @Autowired
    private RestTemplate restTemplate;


    @HystrixCommand(fallbackMethod = "getUserRatingsFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "30000"),//https://github.com/Netflix/Hystrix/wiki/Configuration#executionisolationthreadtimeoutinmilliseconds
//                    @HystrixProperty(name = "hystrix.command.default.execution.isolation.strategy", value = "SEMAPHORE"),
                    @HystrixProperty(name = "execution.timeout.enabled", value = "false"),// default is true
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000"),// default is 10000
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "20"), // default is 50
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"), // circuit will open if in rolling time window fail % is reached and requestVolumeThreshold is crossed
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "30000") // default is 5000
            }
    )
    public UserRatings getUserRatings(String userId) {
        return restTemplate.getForObject("http://rating-data-service/ratingsdata/user/" + userId, UserRatings.class);
    }

    private UserRatings getUserRatingsFallback(String userId) {
        return new UserRatings(userId, Arrays.asList(new Rating("100", 0)));
    }
}
