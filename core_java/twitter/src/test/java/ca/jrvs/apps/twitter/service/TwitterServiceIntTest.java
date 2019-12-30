package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.Util.TweetUtil;
import ca.jrvs.apps.twitter.dao.TwitterDAO;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TwitterServiceIntTest {
    private TwitterService twitterService;
    private TwitterDAO dao;
    private Tweet tweet1;
    private Tweet tweet2;
    private Tweet post;

    @Before
    public void setup() {
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");
        System.out.println(consumerKey + " | " + consumerSecret + " | " + accessToken + " | " + tokenSecret);
        //setup dependency
        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
        //pass dependency
        this.dao = new TwitterDAO(httpHelper);
        this.twitterService = new TwitterService(dao);

            String hashTag = "#holidayseason";
            String text1 = "Happy holidays 2019!!" + " " + hashTag + " " + System.currentTimeMillis();
            String text2 = "Merry Christmas 2019!!" + " " + hashTag + " " + System.currentTimeMillis();
            Double latitude = 43.7;
            Double longitude = 79.3;
            Tweet postTweet1 = TweetUtil.buildTweet(text1, longitude, latitude);
            Tweet postTweet2 = TweetUtil.buildTweet(text2, longitude, latitude);
            tweet1 = twitterService.postTweet(postTweet1);
            tweet2 = twitterService.postTweet(postTweet2);

            assertEquals(text1, tweet1.getText());
            assertNotNull(tweet1.getCoordinates());
            assertEquals(2, tweet1.getCoordinates().getCoordinatesTweet().size());
            assertEquals(longitude, tweet1.getCoordinates().getCoordinatesTweet().get(0));
            assertEquals(latitude, tweet1.getCoordinates().getCoordinatesTweet().get(1));
        }


        @Test
        public void showTweet() throws Exception{
            String[] fields = {"created_at", "id_str"};
            Tweet tweetShow = twitterService.showTweet(tweet1.getIdStr(),fields);
            assertEquals(tweet1.getText(), tweetShow.getText());
        }
//
        @After
        public void deleteTweets () {
            String[] tweetList = {tweet1.getIdStr(), tweet2.getIdStr()};
            List<Tweet> deletedTweets = twitterService.deleteTweets(tweetList);
            assertEquals(deletedTweets.get(0).getText(), tweet1.getText());
            assertEquals(deletedTweets.get(1).getText(), tweet2.getText());
        }
    }

