package ca.jrvs.apps.twitter.dao.helper;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.net.URI;

public class TwitterHttpHelperTest {

    @Test
    public void httpPost() throws Exception {
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");

        System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);

        //create components
        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
        HttpResponse response = httpHelper.httpPost(new URI("https://api.twitter.com/1.1/statuses/update.json?status=HappyHolidays"));
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void httpGet() throws Exception {
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");

        System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);

        //create components
        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
        HttpResponse response = httpHelper.httpGet(new URI("https://api.twitter.com/1.1/statuses/update.json?status=1208608028909547520"));
        System.out.println(EntityUtils.toString(response.getEntity()));

    }

}
