package com.tb2g.qboinventory.service;

import com.tb2g.qboinventory.model.QBOItem;
import com.tb2g.qboinventory.model.QBOResponse;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Cuong on 12/1/2015.
 */
public interface QBOService {

    public static final String BASE_URL = "https://sandbox-quickbooks.api.intuit.com";
    @Headers({
            "Accept: application/json",
            "content-type: application/json"
    })
    @GET("/v3/company/{company}/query?minorversion=4")
    public QBOResponse findItemsBySKU(@Path("company") String company, @Query("query") String query);

    @Headers({
            "Accept: application/json",
            "content-type: application/json"
    })
    @POST("/v3/company/{company}/item?operation=update&minorversion=4")
    public QBOResponse updateItem(@Path("company") String company, @Body QBOItem item);

    @Headers({
            "Accept: application/json",
            "content-type: application/json"
    })
    @POST("/v3/company/{company}/item?minorversion=4")
    public QBOResponse createItm(@Path("company") String company, @Body QBOItem item);

    @Headers({
            "Accept: application/json",
            "content-type: application/json"
    })
    @GET("/v3/company/{company}/companyinfo/{company}?minorversion=4")
    public QBOResponse getCompanyInfo(@Path("company") String company);

}
