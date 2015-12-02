package com.tb2g.qboinventory.service;

import com.intuit.ipp.core.IEntity;
import com.intuit.ipp.data.Item;
import com.tb2g.qboinventory.model.QueryItemResponse;

import java.util.Map;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Cuong on 12/1/2015.
 */
public interface QBOService {

    public static final String BASE_URL = "https://sandbox-quickbooks.api.intuit.com";

    @GET("/v3/company/{company}/query")
    public QueryItemResponse findItemsBySKU(@Path("company") String company, @Query("query") String query);

    @POST("/v3/company/{company}/item?operation=update")
    public Map<String, IEntity> updateItem(@Path("company") String company, @Body Item item);

    @POST("/v3/company/{company}/item")
    public Map<String, IEntity> createItm(@Path("company") String company, @Body Item item);


}
