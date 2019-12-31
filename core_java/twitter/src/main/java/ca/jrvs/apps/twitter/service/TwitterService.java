package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@org.springframework.stereotype.Service
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

    @Autowired
    public TwitterService(CrdDao dao) {
        this.dao = dao;
    }

    /**
     * Verify tweet text and coordinates and post tweet
     *
     * @param tweet tweet to be created
     * @return tweet
     * @throws IllegalArgumentException if tweet text exceeds allowed characters and/or if Longitude and latitude are out of range
     */
    @Override
    public Tweet postTweet(Tweet tweet) {
        validatePostTweetText(tweet);
        validateCoordinates(tweet);
        return (Tweet) dao.create(tweet);
    }

    /**
     * @param id     tweet id
     * @param fields set fields not in the list to null
     * @return Tweet object
     * @throws IllegalArgumentException in case of invalid id or fields parameters.
     */

    @Override
    public Tweet showTweet(String id, String[] fields) {
        validateTweetId(id);
        for (String f : fields) {
            validateFields(f);
        }
        return (Tweet) dao.findById(id);
    }

    /**
     * Delete tweets by id
     *
     * @param ids tweet IDs which will be deleted
     * @return list od deleted tweets
     * @throws IllegalArgumentException in case of invalid id
     */
    @Override
    public List<Tweet> deleteTweets(String[] ids) {
        List<Tweet> deletedTweets = new ArrayList<>();
        for (String id : ids) {
            validateTweetId(id);
            deletedTweets.add((Tweet) dao.deleteById(id));
        }
        return deletedTweets;
    }


    private void validatePostTweetText(Tweet tweet) {
        if (tweet.getText().length() > 140) {
            throw new IllegalArgumentException("Tweet length exceeded max number of allowed characters. Please use shorter text");
        }

    }

    private void validateCoordinates(Tweet tweet) {
        if (tweet.getCoordinates() != null &&
                tweet.getCoordinates().getCoordinatesTweet().get(0) > 180 ||
                tweet.getCoordinates().getCoordinatesTweet().get(0) < -180 ||
                tweet.getCoordinates().getCoordinatesTweet().get(1) > 90 ||
                tweet.getCoordinates().getCoordinatesTweet().get(1) < -90) {
            throw new IllegalArgumentException("Longitude and Latitude out of range. Valid longitude range - -180 to +180 " +
                    "and latitude range - -90 to 90");
        }
    }

    private void validateFields(String fields) {
        if (!Arrays.asList(validFields).contains(fields)) {
            throw new IllegalArgumentException("Invalid field parameters. Valid fields: " + validFields);
        }
    }

    private void validateTweetId(String id) {
        if (!id.matches("[0-9]+")) {
            throw new IllegalArgumentException("Invalid tweet id");
        }
    }
}
