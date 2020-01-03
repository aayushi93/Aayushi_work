package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.Util.TweetUtil;
import ca.jrvs.apps.twitter.dao.TwitterDAO;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TwitterControllerIntTest {

    private Controller controller;
    private TwitterService twitterService;
    private TwitterDAO dao;
    private Tweet tweet1;
    private Tweet tweet2;

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
        this.controller = new TwitterController(twitterService);

        String hashTag = "#holidayseason";
        String text1 = "Happy holidays 2019!!" + " " + hashTag + " " + System.currentTimeMillis();
        Double latitude1 = 43.7;
        Double longitude1 = 79.3;
        String text2 = "Merry Christmas 2019!!" + " " + hashTag + " " + System.currentTimeMillis();
        Double latitude2 = 43.7;
        Double longitude2 = 79.3;
        Tweet postTweet1 = TweetUtil.buildTweet(text1, longitude1, latitude1);
        Tweet postTweet2 = TweetUtil.buildTweet(text2, longitude2, latitude2);
        tweet1 = twitterService.postTweet(postTweet1);
        tweet2 = twitterService.postTweet(postTweet2);
        String[] input1 = {"Text1", postTweet1.getText(), "43.7:79.3"};
        tweet1 = controller.postTweet(input1);
        String[] input2 = {"Text2", postTweet2.getText(), "43.7:79.3"};
        tweet2 = controller.postTweet(input2);


        assertEquals(text1, tweet1.getText());
        assertNotNull(tweet1.getCoordinates());
        assertEquals(2, tweet1.getCoordinates().getCoordinatesTweet().size());
        assertEquals(longitude1, tweet1.getCoordinates().getCoordinatesTweet().get(0));
        assertEquals(latitude1, tweet1.getCoordinates().getCoordinatesTweet().get(1));


    }

    @Test
    public void showTweet() {
        String[] input_show = {"Show String", tweet1.getIdStr(), "created_at"};
        Tweet showText = controller.showTweet(input_show);
        assertEquals(tweet1.getText(), showText.getText());
        assertEquals(tweet1.getIdStr(), showText.getIdStr());
    }

    @After
    public void deleteTweet() {
        String deleteTweetId = tweet1.getIdStr() + "," + tweet2.getIdStr();
        String[] deleteText = {"delete", deleteTweetId};
        List<Tweet> tweetDelete = controller.deleteTweet(deleteText);
        assertEquals(tweetDelete.get(0).getText(), tweet1.getText());
        assertEquals(tweetDelete.get(1).getText(), tweet2.getText());

    }
}