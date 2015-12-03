package com.tb2g.qboinventory.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

/**
 * Created by Cuong on 11/17/2015.
 */
public class RestClient {

    private static QBOService QBOService;
    private static UPCDatabaseService upcService;
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

    public static QBOService getQBOService(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        if (QBOService == null) {

            OkHttpOAuthConsumer oAuthConsumer = new OkHttpOAuthConsumer(consumerKey, consumerSecret);

            oAuthConsumer.setTokenWithSecret(accessToken, accessTokenSecret);

            OkHttpClient okHttpClient = new OkHttpClient();

            okHttpClient.interceptors().add(new SigningInterceptor(oAuthConsumer));

            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

            RestAdapter.Builder builder = new RestAdapter.Builder()
                    .setEndpoint(QBOService.BASE_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient(okHttpClient))
                    .setConverter(new GsonConverter(gson));

            RestAdapter restAdapter = builder.build();

            QBOService = restAdapter.create(QBOService.class);
        }

        return QBOService;
    }

    public static UPCDatabaseService getUPCService(){

        if (upcService == null) {

            RestAdapter.Builder builder = new RestAdapter.Builder()
                    .setEndpoint(upcService.BASE_URL)
                    .setLogLevel(RestAdapter.LogLevel.BASIC)
                    .setConverter(new GsonConverter(gson));

            RestAdapter restAdapter = builder.build();

            upcService = restAdapter.create(UPCDatabaseService.class);
        }

        return upcService;
    }

}
