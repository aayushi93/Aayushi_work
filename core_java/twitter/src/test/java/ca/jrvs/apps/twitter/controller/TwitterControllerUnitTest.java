package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TwitterControllerUnitTest {

    @Mock
    TwitterService twitterService;

    @InjectMocks
    TwitterController twitterController;

    @Test
    public void postTweet() {
        when(twitterService.postTweet(any())).thenReturn(new Tweet());
        String[] postText = {"Post text", "Controller Unit testing", "43.7:59.3"};
        Tweet testTweet = twitterController.postTweet(postText);
        assertEquals(testTweet.getText(), null);
        assertEquals(testTweet.getCoordinates(), null);
        assertEquals(testTweet.getIdStr(), null);
    }

    @Test
    public void showTweet() {
        when(twitterService.showTweet(any(), any())).thenReturn(new Tweet());
        String[] showText = {"Show text", "1209606892781264897", "created_at, id_str"};
        Tweet showTweet = twitterController.showTweet(showText);
        assertEquals(showTweet.getText(), null);
        assertEquals(showTweet.getIdStr(), null);
        assertEquals(showTweet.getCoordinates(), null);
    }

    @Test
    public void deleteText() {
        when(twitterService.deleteTweets(any())).thenReturn(new ArrayList<>());
        String[] deleteId = {"delete tweet", "1209606892781264897, 1209606892160528385"};
        List<Tweet> tweetDelete = twitterController.deleteTweet(deleteId);
        for (Tweet obj : tweetDelete) {
            assertEquals(obj.getText(), null);
            assertEquals(obj.getIdStr(), null);
            assertEquals(obj.getCoordinates(), null);
        }
    }
}
