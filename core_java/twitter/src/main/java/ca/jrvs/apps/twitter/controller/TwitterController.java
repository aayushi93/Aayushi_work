package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.Util.TweetUtil;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

@org.springframework.stereotype.Controller
public class TwitterController implements Controller {
    private static final String COORD_SEP = ":";
    private static final String SEP = ",";

    private Service service;

    @Autowired
    public TwitterController(Service service) {
        this.service = service;
    }

    /**
     * Calling service classes to post a tweet by parsing user arguments
     *
     * @param args
     * @return tweet
     * @throws IllegalArgumentException
     */

    @Override
    public Tweet postTweet(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
        }
        String tweetText = args[1];
        String coordinates = args[2];
        String[] coordArray = coordinates.split(COORD_SEP);

        if (coordArray.length != 2 || StringUtils.isEmpty(tweetText)) {
            throw new IllegalArgumentException("Invalid location format\nUSAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
        }
        Double longitude = null;
        Double latitude = null;
        try {
            longitude = Double.parseDouble(coordArray[0]);
            latitude = Double.parseDouble(coordArray[1]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid location format\nUSAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"", e);
        }
        Tweet postTweetText = TweetUtil.buildTweet(tweetText, longitude, latitude);
        return service.postTweet(postTweetText);
    }

    /**
     * Calling service classes to search tweet by id
     *
     * @param args
     * @return searched tweet
     * @throws IllegalArgumentException for invalid args
     */

    @Override
    public Tweet showTweet(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
        }
        String id = args[1];
        String validFields = args[2];
        String[] validFieldsArr = validFields.split(SEP);
        return service.showTweet(id, validFieldsArr);
    }

    /**
     * Calling service classes to delete tweet by ID
     *
     * @param args
     * @return list of tweets deleted
     * @throws IllegalArgumentException
     */

    @Override
    public List<Tweet> deleteTweet(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
        }
        String id = args[1];
        String[] idArr = id.split(SEP);
        return service.deleteTweets(idArr);
    }
}
