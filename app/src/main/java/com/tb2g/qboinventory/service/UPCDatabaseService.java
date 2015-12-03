package com.tb2g.qboinventory.service;

import com.tb2g.qboinventory.model.UPCProduct;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Cuong on 12/1/2015.
 */
public interface UPCDatabaseService {

    public static final String BASE_URL = "http://api.upcdatabase.org";

    @GET("/json/{apikey}/{code}")
    public UPCProduct lookupProduct(@Path("apikey") String apikey, @Path("code") String code);
}
