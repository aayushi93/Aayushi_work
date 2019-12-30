package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.Util.TweetUtil;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {
    @Mock
    CrdDao dao;

    @InjectMocks
    TwitterService twitterService;

    @Test
    public void postTweet() {
        when(dao.create(any())).thenReturn(new Tweet());
        Tweet testTweet = twitterService.postTweet(TweetUtil.buildTweet("test", 43.7, 59.3));
        assertEquals(testTweet.getText(), null);
        assertEquals(testTweet.getCoordinates(), null);
        assertEquals(testTweet.getIdStr(), null);
    }

    @Test
    public void showTweet() {
        when(dao.create(any())).thenReturn(new Tweet());
        String id = "0123456789101112131";
        String[] idFields = {"created_at", "id_str"};
        Tweet tweetShow = twitterService.showTweet(id, idFields);
        assertEquals(tweetShow.getText(), null);
        assertEquals(tweetShow.getIdStr(), null);
        assertEquals(tweetShow.getCoordinates(), null);
    }

    @Test
    public void deleteTweets() {
        when(dao.create(any())).thenReturn(new Tweet());
        String[] idArray= {"9876543211012131", "0123456789101112131"};
        List<Tweet> tweetDelete = twitterService.deleteTweets(idArray);
        for(Tweet obj: tweetDelete) {
            assertEquals(obj.getText(), null);
            assertEquals(obj.getIdStr(), null);
            assertEquals(obj.getCoordinates(), null);

        }



    }





}
