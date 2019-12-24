package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwitterService implements Service {
    private CrdDao dao;
    private String[] validFields = {"created_at",
            "id",
            "id_str",
            "text",
            "source",
            "coordinates",
            "entities",
            "retweet_count",
            "favorite_count",
            "favorited",
            "retweeted"};

    public TwitterService(CrdDao dao) {
        this.dao = dao;
    }
    @Override
    public Tweet postTweet(Tweet tweet) {
       validatePostTweetText(tweet);
       validateCoordinates(tweet);
        return(Tweet) dao.create(tweet);
    }

    @Override
    public Tweet showTweet(String id, String[] fields) {
        validateTweetId(id);
        for(String f : fields) {
            validateFields(f);
        }
        return (Tweet) dao.findById(id);
    }

    @Override
    public List<Tweet> deleteTweets(String[] ids) {
        List<Tweet> deletedTweets = new ArrayList<>();
        for(String id : ids) {
            validateTweetId(id);
            deletedTweets.add((Tweet) dao.deleteById(id));
        }
        return deletedTweets;
    }


    //Validating length of tweet text
    private void validatePostTweetText(Tweet tweet) {
        if (tweet.getText().length() > 140) {
            throw new RuntimeException("Tweet length exceeded max number of allowed characters. Please use shorter text");
        }

    }
    //Validating coordinates
    private void validateCoordinates(Tweet tweet) {
        if (tweet.getCoordinates() != null &&
        tweet.getCoordinates().getCoordinatesTweet().get(0) > 180 ||
        tweet.getCoordinates().getCoordinatesTweet().get(0) < -180 ||
        tweet.getCoordinates().getCoordinatesTweet().get(1) > 90 ||
        tweet.getCoordinates().getCoordinatesTweet().get(1) < -90) {
            throw new RuntimeException("Longitude and Latitude out of range. Valid longitude range - -180 to +180 " +
                    "and latitude range - -90 to 90");
        }
    }

    //Validating fields
    private void validateFields(String fields) {
        if (!Arrays.asList(validFields).contains(fields)) {
            throw new RuntimeException("Invalid field parameters. Valid fields: " + validFields);
        }
    }

    //Validating tweet id
    private void validateTweetId(String id) {
        if(!id.matches("[0-9]+")) {
            throw new RuntimeException("Invalid tweet id");
        }
    }
}
