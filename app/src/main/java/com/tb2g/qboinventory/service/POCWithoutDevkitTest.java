package com.tb2g.qboinventory.service;

import android.util.Log;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;


public class POCWithoutDevkitTest {

    private OkHttpOAuthConsumer oAuthConsumer;
    private static String realmID = null;

    public POCWithoutDevkitTest() {
        realmID = "122294642099";
        String consumerKey = "qyprdrXItMdZB4cIEZRwxCWx9aCZao";
        String consumerSecret = "RJOJ3MTCL2A3Ao2xvkO9bBO9jEyKlbyRRuKb0Tuv";
        String accessToken = "qyprdUWHBsZk4Ehpa4b3yhOhNEoqJzZftItobLyltwvs8qCy";
        String accessTokenSecret = "knxHcgqZmlVB3RCPpMqMECksBrK7YLtDM5XWl5in";

        setupContext(consumerKey, consumerSecret, accessToken, accessTokenSecret);
    }

    public void setupContext(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        this.oAuthConsumer = new OkHttpOAuthConsumer(consumerKey, consumerSecret);
        oAuthConsumer.setTokenWithSecret(accessToken, accessTokenSecret);
    }

    public void executeGetRequest(String customURIString){

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(new SigningInterceptor(oAuthConsumer));

        Request request = new Request.Builder()
                .url(customURIString)
                .addHeader("Accept", "application/json")
                .addHeader("content-type", "application/json")
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();

            if (!response.isSuccessful()) Log.d("TAG", "Unexpected code " + response);

            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }

            System.out.println(response.body().string());

        } catch (IOException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

}