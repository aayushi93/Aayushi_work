package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import com.google.gdata.util.common.base.PercentEscaper;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class TwitterDAO implements  CrdDao<Tweet, String> {
    //URI constants
    private static final String API_BASE_URI = "https://api.twitter.com";
    private static final String POST_PATH = "/1.1/statuses/update.json";
    private static final String SHOW_PATH = "/1.1/statuses/show.json";
    private static final String DELETE_PATH = "/1.1/statuses/destroy";
    //URI Symbols
    private static final String QUERY_SYM = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUAL = "=";
    //Response code
    private static final int HTTP_OK = 200;
    private HttpHelper httpHelper;


    public TwitterDAO(HttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    @Override
    public Tweet create(Tweet tweet) {
        URI uri;
        try{
            uri = getPostUri(tweet);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Illegal tweet input", e);
        }
        //Execute Http request
        HttpResponse response = httpHelper.httpPost(uri);

        //Validate response and deserialize response to Tweet object
        return parseResponseBody(response, HTTP_OK);
    }

    //Helper method to postURI
    private URI getPostUri(Tweet tweet) throws URISyntaxException {
        String status = tweet.getText();
        Double longitude = tweet.getCoordinates().getCoordinatesTweet().get(0);
        Double latitude = tweet.getCoordinates().getCoordinatesTweet().get(1);
        PercentEscaper percentEscaper = new PercentEscaper("", false);
        return new URI(API_BASE_URI + POST_PATH + QUERY_SYM + "status" + EQUAL + percentEscaper.escape(status) +
                AMPERSAND + "long" + EQUAL + longitude + AMPERSAND + "lat" + EQUAL + latitude);
    }


    @Override
    public Tweet findById(String id) {
        URI uri;
        try{
            uri = getShowById(id);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid tweet ID", e);
        }

        HttpResponse response = httpHelper.httpGet(uri);
        return parseResponseBody(response, HTTP_OK);
    }

    //Helper method to showURI
    private URI getShowById(String id) throws URISyntaxException {
        return new URI(API_BASE_URI + SHOW_PATH + QUERY_SYM + "id" + EQUAL + id);
    }


    @Override
    public Tweet deleteById(String id) {
        URI uri;
        try{
            uri = getDeleteById(id);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid tweet ID", e);
        }

        HttpResponse response = httpHelper.httpPost(uri);
        return parseResponseBody(response, HTTP_OK);
    }

    //Helper method to deleteURI
    private URI getDeleteById(String id) throws URISyntaxException{
        return new URI(API_BASE_URI + DELETE_PATH + "/" + id + ".json");
    }


    Tweet parseResponseBody(HttpResponse response, Integer expectedStatusCode) {
        Tweet tweet = null;

        int status = response.getStatusLine().getStatusCode();
        if(status != expectedStatusCode){
            try {
                System.out.println(EntityUtils.toString(response.getEntity()));
            }catch (IOException e){
                System.out.println("Response has no entity");
            }
            throw new RuntimeException("Unexpected HTTP status: " + status);
        }


        if (response.getEntity() == null){
            throw new RuntimeException("Empty response body.");
        }


        //Converting reponse entity to string
        String jsonString;
        try{
            jsonString = EntityUtils.toString(response.getEntity());
        }catch (IOException e){
            throw new RuntimeException("Failed to convert from entity to String", e);
        }

        return tweet;
    }

    }



