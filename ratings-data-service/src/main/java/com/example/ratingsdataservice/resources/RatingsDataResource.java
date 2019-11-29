package com.example.ratingsdataservice.resources;

import com.example.ratingsdataservice.Model.Rating;
import com.example.ratingsdataservice.Model.UserRatings;
import com.example.ratingsdataservice.test.Holder;
import com.example.ratingsdataservice.test.Ractangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ratingsdata")
public class RatingsDataResource {

    @Autowired
    MongoTemplate mongoTemplate;


    //    @RequestMapping("/movies/{movieId}")
//    public Rating getMoviesRatings(@PathVariable("movieId") String movieId) {
//
//        return new Rating(movieId, 4);
//    }
    int count = 0;

    @RequestMapping("/user/{userId}")
    public UserRatings getUserRatings(@PathVariable("userId") String userId) {
        count++;
        if (count % 3 == 0) {
            throw new RuntimeException("i should fail each 3rd request");
        }
        return new UserRatings(userId, Arrays.asList(
                new Rating("100", 5),
                new Rating("200", 4))
        );
    }

    @RequestMapping("/mongo")
    public Rating mongo() {
        List<Holder> holders = mongoTemplate.find(Query.query(Criteria.where("hid").is("1")), Holder.class);
        Ractangle r = new Ractangle();
        r.setName("r1");
        r.setRectField("rect1");

        Holder holder = new Holder();
        holder.setHid("1");
        holder.setShape(r);
        holder.setShapes(Arrays.asList(r));
        mongoTemplate.save(holder);

        return null;
    }


}
