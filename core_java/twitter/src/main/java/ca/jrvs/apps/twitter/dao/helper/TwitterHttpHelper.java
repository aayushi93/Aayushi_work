package ca.jrvs.apps.twitter.dao.helper;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@Component
public class TwitterHttpHelper implements HttpHelper {

    /**
     *  Dependencies are set using private member variables
     */
    private OAuthConsumer consumer;
    private HttpClient httpClient;


    /**
     * Constructor
     * Setup dependecies using Twitter secrets
     * @param consumerKey
     * @param consumerSecret
     * @param accessToken
     * @param tokenSecret
     */
    public TwitterHttpHelper(String consumerKey, String consumerSecret, String accessToken, String tokenSecret) {
        consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(accessToken, tokenSecret);

        httpClient = HttpClientBuilder.create().build();
    }

    //Default Constructor
    public TwitterHttpHelper() {
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");
        consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(accessToken, tokenSecret);

        /**Default = single connection
         *
         */
        httpClient = HttpClientBuilder.create().build();
    }
    /** Helper function to execute http request
     *
     * @param method
     * @param uri
     * @param strEnt
     * @return HttpResponse
     * @throws IOException
     * @throws OAuthException
     */
    private HttpResponse executeHttpRequest(HttpMethod method, URI uri, StringEntity strEnt) throws IOException, OAuthException{
        if(method == HttpMethod.GET) {
            HttpGet request = new HttpGet(uri);
            consumer.sign(request);
            return httpClient.execute(request);
        } else if (method == HttpMethod.POST) {
            HttpPost request = new HttpPost(uri);
            if(strEnt != null) {
                request.setEntity(strEnt);
            }
            consumer.sign(request);
            return httpClient.execute(request);
        } else {
            throw  new RuntimeException("Invalid HTTP method: " + method.name());
        }
    }

    @Override
    public HttpResponse httpPost(URI uri) {
        try{
            return executeHttpRequest(HttpMethod.POST, uri, null);
        }catch (OAuthException | IOException e) {
            throw new RuntimeException("Execution failed", e);
        }

    }

    @Override
    public HttpResponse httpGet(URI uri) {
        try{
            return executeHttpRequest(HttpMethod.GET, uri, null);
        }catch (OAuthException |IOException e) {
            throw new RuntimeException("Execution failed", e);
        }

    }
    }

