package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.Util.JsonUtil;
import ca.jrvs.apps.twitter.Util.TweetUtil;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {
    @Mock
    HttpHelper httpHelperMock;


    @InjectMocks
    TwitterDAO dao;

    @Test
    public void create() throws Exception {
        String hashTag = "#holidayseason";
        String text = "Happy holidays 2019!!" + " " + hashTag + " " + System.currentTimeMillis();
        Double latitude = 43.7;
        Double longitude = 79.3;
        when(httpHelperMock.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
        try{
            dao.create(TweetUtil.buildTweet(text, latitude, longitude));
        }catch (RuntimeException e) {
            assertTrue(true);
        }

        String tweetJsonString = "{\n"
                + "   \"created_at\":\"Mon Feb 21:24:39 +1111 2019\", \n"
                + "   \"id\":123456789101112,\n"
                + "   \"id_str\":\"1205582607922139137\",\n"
                + "   \"text\":\"some text here\",\n"
                + "   \"entities\":{\n"
                + "       \"hashtag\":[],\n"
                + "       \"user_mentions\":[]\n"
                + "   },\n"
                + "   \"coordinated\":null,\n"
                + "   \"retweet_count\":0,\n"
                + "   \"favorited_count\":0,\n"
                + "   \"favorited\":false,\n"
                + "   \"retweeted\":false\n"
                + "}";


        when(httpHelperMock.httpPost(isNotNull())).thenReturn(null);
        TwitterDAO spyDao = Mockito.spy(dao);
        Tweet expectedTweet = JsonUtil.toObjectFromJson(tweetJsonString, Tweet.class);

        doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
        Tweet tweet = spyDao.create(TweetUtil.buildTweet(text, longitude, latitude));
        assertNotNull(tweet);
        assertNotNull(tweet.getText());
    }

    @Test
    public void findTweet() throws Exception{
        String hashTag = "#holidayseason";
        String text = "Happy holidays 2019!!" + " " + hashTag + " " + System.currentTimeMillis();
        Double latitude = 43.7;
        Double longitude = 79.3;
        when(httpHelperMock.httpGet(isNotNull())).thenThrow(new RuntimeException("mock"));
        try{
            dao.findById("123456789101112");
        }catch (RuntimeException e) {
            assertTrue(true);
        }

        String tweetJsonString = "{\n"
                + "   \"created_at\":\"Mon Feb 21:24:39 +1111 2019\", \n"
                + "   \"id\":123456789101112,\n"
                + "   \"id_str\":\"1205582607922139137\",\n"
                + "   \"text\":\"some text here\",\n"
                + "   \"entities\":{\n"
                + "       \"hashtag\":[],\n"
                + "       \"user_mentions\":[]\n"
                + "   },\n"
                + "   \"coordinated\":null,\n"
                + "   \"retweet_count\":0,\n"
                + "   \"favorited_count\":0,\n"
                + "   \"favorited\":false,\n"
                + "   \"retweeted\":false\n"
                + "}";

        when(httpHelperMock.httpGet(isNotNull())).thenReturn(null);
        TwitterDAO spyDao = Mockito.spy(dao);
        Tweet expectedTweet = JsonUtil.toObjectFromJson(tweetJsonString, Tweet.class);

        doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
        Tweet tweet = spyDao.findById("123456789101112");
        assertNotNull(tweet);
        assertNotNull(tweet.getText());
    }

    @Test
    public void deleteTweet() throws Exception{
        String hashTag = "#holidayseason";
        String text = "Happy holidays 2019!!" + " " + hashTag + " " + System.currentTimeMillis();
        Double latitude = 43.7;
        Double longitude = 79.3;
        when(httpHelperMock.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
        try{
            dao.deleteById("123456789101112");
        }catch (RuntimeException e) {
            assertTrue(true);
        }

        String tweetJsonString = "{\n"
                + "   \"created_at\":\"Mon Feb 21:24:39 +1111 2019\", \n"
                + "   \"id\":123456789101112,\n"
                + "   \"id_str\":\"1205582607922139137\",\n"
                + "   \"text\":\"some text here\",\n"
                + "   \"entities\":{\n"
                + "       \"hashtag\":[],\n"
                + "       \"user_mentions\":[]\n"
                + "   },\n"
                + "   \"coordinated\":null,\n"
                + "   \"retweet_count\":0,\n"
                + "   \"favorited_count\":0,\n"
                + "   \"favorited\":false,\n"
                + "   \"retweeted\":false\n"
                + "}";

        when(httpHelperMock.httpPost(isNotNull())).thenReturn(null);
        TwitterDAO spyDao = Mockito.spy(dao);
        Tweet expectedTweet = JsonUtil.toObjectFromJson(tweetJsonString, Tweet.class);

        doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
        Tweet tweet = spyDao.deleteById("123456789101112");
        assertNotNull(tweet);
        assertNotNull(tweet.getText());

    }





    }





