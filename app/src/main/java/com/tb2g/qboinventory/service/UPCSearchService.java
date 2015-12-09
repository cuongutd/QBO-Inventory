package com.tb2g.qboinventory.service;

import com.tb2g.qboinventory.model.UPCProduct;
import com.tb2g.qboinventory.model.UPCSearchProduct;

import java.util.HashMap;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Cuong on 12/9/2015.
 */
public interface UPCSearchService {
    public static final String BASE_URL = "http://www.searchupc.com";
    /*http://www.searchupc.com/handlers/upcsearch.ashx?request_type=3&access_token=73C8EA26-5AFE-44AC-B193-ED15B574B377&upc=691464004804*/
    @GET("/handlers/upcsearch.ashx?request_type=3")
    public HashMap<Integer, UPCSearchProduct> lookupProduct(@Query("access_token") String accessToken, @Query("upc") String code);
}
