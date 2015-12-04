package com.tb2g.qboinventory.service;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.os.ResultReceiver;

import com.tb2g.qboinventory.R;
import com.tb2g.qboinventory.model.IntentServiceTask;
import com.tb2g.qboinventory.model.QBOItem;
import com.tb2g.qboinventory.model.QBOResponse;
import com.tb2g.qboinventory.model.UPCProduct;
import com.tb2g.qboinventory.util.Constants;

import java.math.BigDecimal;

import retrofit.RetrofitError;

public class IntentService extends android.app.IntentService {
    String consumerKey;
    String consumerSecret;
    String accessToken;
    String accessTokenSecret;
    String mCompanyId;
    String mUPCDatabaseKey;
    String mSampleItemName;

    public IntentService() {
        super("IntentService");
    }

    private void loadKeys(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        consumerKey = sharedPref.getString(Constants.SHARED_PREF_CONSUMMER_KEY, "");
        consumerSecret = sharedPref.getString(Constants.SHARED_PREF_CONSUMMER_SECRET, "");
        accessToken = sharedPref.getString(Constants.SHARED_PREF_ACCESS_TOKEN, "");
        accessTokenSecret = sharedPref.getString(Constants.SHARED_PREF_ACCESS_TOKEN_SECRET, "");
        mCompanyId = sharedPref.getString(Constants.SHARED_PREF_QBO_COMPANY_ID, "");
        mUPCDatabaseKey = sharedPref.getString(Constants.SHARED_PREF_UPCDB_KEY, "");
        mSampleItemName = sharedPref.getString(Constants.SHARED_PREF_SAMPLE_ITEM_NAME, "");
    }


    public static void loginToQBO(ResultReceiver receiver, Context context) {
        Intent intent = new Intent(context, IntentService.class);
        intent.putExtra(Constants.EXTRA_RESULT_RECEIVER, receiver);
        intent.setAction(Constants.ACTION_LOGIN);
        context.startService(intent);
    }

    public static void getWebProductInfo(ResultReceiver receiver, Context context, String upc){

        IntentServiceTask.with(context)
                .setResultReceiver(receiver)
                .goingToDo(Constants.ACTION_UPC_LOOKUP)
                .putExtra(Constants.EXTRA_UPC, upc)
                .start();

    }

    public static void getQBOItemInfo(ResultReceiver receiver, Context context, String upc){
        IntentServiceTask.with(context)
                .setResultReceiver(receiver)
                .goingToDo(Constants.ACTION_QBO_LOOKUP)
                .putExtra(Constants.EXTRA_UPC, upc)
                .start();
    }

    public static void increaseQBOItemQuantity(ResultReceiver receiver, Context context, QBOItem qboItem){

        IntentServiceTask.with(context)
                .setResultReceiver(receiver)
                .goingToDo(Constants.ACTION_QBO_UPDATE)
                .putExtra(Constants.EXTRA_QBO_ITEM, qboItem)
                .start();

    }

    public static void createQBOItem(ResultReceiver receiver, Context context, QBOItem qboItem){

        IntentServiceTask.with(context)
                .setResultReceiver(receiver)
                .goingToDo(Constants.ACTION_QBO_CREATE)
                .putExtra(Constants.EXTRA_QBO_ITEM, qboItem)
                .start();

    }

    public static void getCompanyInfo(ResultReceiver receiver, Context context){

        IntentServiceTask.with(context)
                .setResultReceiver(receiver)
                .goingToDo(Constants.ACTION_QBO_COMPANY)
                .start();

    }



    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            loadKeys();
            final String action = intent.getAction();
            final ResultReceiver receiver = intent.getParcelableExtra(Constants.EXTRA_RESULT_RECEIVER);
            if (Constants.ACTION_LOGIN.equals(action)) {
                handleLogin(receiver);
            }else if (Constants.ACTION_UPC_LOOKUP.equals(action)) {

                String upc = intent.getStringExtra(Constants.EXTRA_UPC);
                handleUPCLookup(receiver, upc);
            }else if (Constants.ACTION_QBO_LOOKUP.equals(action)) {

                String upc = intent.getStringExtra(Constants.EXTRA_UPC);
                handleLookupQBOItem(receiver, upc);
            }else if (Constants.ACTION_QBO_UPDATE.equals(action)) {

                QBOItem item = intent.getParcelableExtra(Constants.EXTRA_QBO_ITEM);
                handleUpdateQBOItem(receiver, item);
            }else if (Constants.ACTION_QBO_CREATE.equals(action)) {

                QBOItem item = intent.getParcelableExtra(Constants.EXTRA_QBO_ITEM);
                handleCreateQBOItem(receiver, item);
            }else if (Constants.ACTION_QBO_COMPANY.equals(action)) {
                handleGetQBOCompanyInfo(receiver);
            }
        }
    }

    private void handleLogin(ResultReceiver receiver) {

    }

    private void handleUPCLookup(ResultReceiver receiver, String upc){

        UPCProduct product = RestClient.getUPCService().lookupProduct(mUPCDatabaseKey, upc);
        Bundle b = new Bundle();
        b.putParcelable(Constants.EXTRA_UPC_PRODUCT, product);
        receiver.send(Constants.RESULT_CODE_WEB_PRODUCT, b);
    }


    private QBOService getQBOService(){

        String baseURL = getString(R.string.qbo_base_url);
        return RestClient.getQBOService(consumerKey, consumerSecret, accessToken, accessTokenSecret, baseURL);

    }


    private void handleGetQBOCompanyInfo(ResultReceiver receiver){

        QBOResponse response = new QBOResponse();
        try {
            response = getQBOService().getCompanyInfo(mCompanyId);

            String query = "Select * FROM Item WHERE name='" + mSampleItemName + "'";
            QBOResponse response2 = getQBOService().queryItem(mCompanyId, query);

            response.setQueryResponse(response2.getQueryResponse());

        }catch (RetrofitError e){

            response.setErrorCode("" + e.getResponse().getStatus());
            response.setErrorMsg(e.getResponse().getReason());

        }catch (Exception e){
            response.setErrorMsg(e.getMessage());
        }

        Bundle b = new Bundle();
        b.putParcelable(Constants.EXTRA_QBO_COMPANY, response);
        receiver.send(Constants.RESULT_CODE_QBO_COMPANY, b);

    }
    private void handleLookupQBOItem(ResultReceiver receiver, String sku){

        String query = "Select * FROM Item WHERE sku='" + sku + "'";

        QBOResponse response = getQBOService().queryItem(mCompanyId, query);

        Bundle b = new Bundle();
        b.putParcelable(Constants.EXTRA_QBO_QUERY, response);
        receiver.send(Constants.RESULT_CODE_QBO_QUERY, b);

    }


    private void handleUpdateQBOItem(ResultReceiver receiver, QBOItem item){

        item.setQtyOnHand(item.getQtyOnHand().add(new BigDecimal(1))); //increase qty by 1

        QBOResponse result = getQBOService().updateItem(mCompanyId, item);

        Bundle b = new Bundle();
        b.putParcelable(Constants.EXTRA_QBO_ITEM, result);
        receiver.send(Constants.RESULT_CODE_QBO_ITEM, b);

    }

    private void handleCreateQBOItem(ResultReceiver receiver, QBOItem item){

        QBOResponse result = getQBOService().createItm(mCompanyId, item);

        Bundle b = new Bundle();
        b.putParcelable(Constants.EXTRA_QBO_ITEM, result);
        receiver.send(Constants.RESULT_CODE_QBO_ITEM, b);

    }

    private void handleIssuingItem(ResultReceiver receiver, String invoiceId, String sku){
        //check if orders for that sku exist


    }
}
