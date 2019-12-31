package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.Util.JsonUtil;
import ca.jrvs.apps.twitter.Util.TweetUtil;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TwitterDAOIntTest {
    private TwitterDAO dao;
    private Tweet post;

    @Before
    public void setup() throws JsonProcessingException {
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");
        System.out.println(consumerKey + " | " + consumerSecret + " | " + accessToken + " | " + tokenSecret);
        //setup dependency
        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
        //pass dependency
        this.dao = new TwitterDAO(httpHelper);

        String hashTag = "#holidayseason";
        String text = "Happy holidays 2019!!" + " " + hashTag + " " + System.currentTimeMillis();
        Double latitude = 43.7;
        Double longitude = 79.3;
        Tweet postTweet = TweetUtil.buildTweet(text, longitude, latitude);
        System.out.println(JsonUtil.toPrettyJson(postTweet));

        Tweet tweet = dao.create(postTweet);

        assertEquals(text, tweet.getText());
        assertNotNull(tweet.getCoordinates());
        assertEquals(2, tweet.getCoordinates().getCoordinatesTweet().size());
        assertEquals(latitude, tweet.getCoordinates().getCoordinatesTweet().get(0));
        assertEquals(longitude, tweet.getCoordinates().getCoordinatesTweet().get(1));

        assertTrue(hashTag.contains(tweet.getEntities().getHashtags().get(0).getText()));
    }

    @Test
    public void findById() throws Exception {
        long id = post.getId();
        Tweet tweet = dao.findById(Long.toString(id));

        assertEquals(post.getText(), tweet.getText());
        assertNotNull(tweet.getCoordinates());
        assertEquals(2, tweet.getCoordinates().getCoordinatesTweet().size());
        assertEquals(post.getCoordinates().getCoordinatesTweet().get(0), tweet.getCoordinates().getCoordinatesTweet().get(0));
        assertEquals(post.getCoordinates().getCoordinatesTweet().get(1), tweet.getCoordinates().getCoordinatesTweet().get(1));
    }

    @After
    public void deleteById() throws Exception {
        long id = post.getId();
        Tweet tweet = dao.deleteById(Long.toString(id));

        assertEquals(post.getText(), tweet.getText());
        assertNotNull(tweet.getCoordinates());
        assertEquals(2, tweet.getCoordinates().getCoordinatesTweet().size());
        assertEquals(post.getCoordinates().getCoordinatesTweet().get(0), tweet.getCoordinates().getCoordinatesTweet().get(0));
        assertEquals(post.getCoordinates().getCoordinatesTweet().get(1), tweet.getCoordinates().getCoordinatesTweet().get(1));
    }


}