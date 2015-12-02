package com.tb2g.qboinventory.service;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.widget.Toast;

import com.intuit.ipp.data.Item;
import com.tb2g.qboinventory.R;
import com.tb2g.qboinventory.model.QueryItemResponse;
import com.tb2g.qboinventory.model.UPCProduct;
import com.tb2g.qboinventory.util.Constants;

import java.math.BigDecimal;

public class IntentService extends android.app.IntentService {


    public IntentService() {
        super("IntentService");
    }

    public static void loginToQBO(ResultReceiver receiver, Context context) {
        Intent intent = new Intent(context, IntentService.class);
        intent.putExtra(Constants.EXTRA_RESULT_RECEIVER, receiver);
        intent.setAction(Constants.ACTION_LOGIN);
        context.startService(intent);
    }

    public static void receiveItem(ResultReceiver receiver, Context context, UPCProduct scanItem){
        Intent intent = new Intent(context, IntentService.class);
        intent.putExtra(Constants.EXTRA_RESULT_RECEIVER, receiver);
        intent.setAction(Constants.ACTION_RECEIVE_ITEM);
        intent.putExtra(Constants.EXTRA_UPC_PRODUCT, scanItem);
        context.startService(intent);

    }

    public static void getProductInfoByUPC(ResultReceiver receiver, Context context, String upc){

        Intent intent = new Intent(context, IntentService.class);
        intent.putExtra(Constants.EXTRA_RESULT_RECEIVER, receiver);
        intent.setAction(Constants.ACTION_UPC_LOOKUP);
        intent.putExtra(Constants.EXTRA_UPC, upc);
        context.startService(intent);


    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final ResultReceiver receiver = intent.getParcelableExtra(Constants.EXTRA_RESULT_RECEIVER);
            if (Constants.ACTION_LOGIN.equals(action)) {
                handleLogin(receiver);
            }else if (Constants.ACTION_RECEIVE_ITEM.equals(action)) {

                UPCProduct scanItem = intent.getParcelableExtra(Constants.EXTRA_UPC_PRODUCT);
                handleReceivingItem(receiver, scanItem);
            }else if (Constants.ACTION_UPC_LOOKUP.equals(action)) {

                String upc = intent.getStringExtra(Constants.EXTRA_UPC);
                handleUPCLookup(receiver, upc);
            }
        }
    }

    private void handleLogin(ResultReceiver receiver) {

    }

    private void handleUPCLookup(ResultReceiver receiver, String upc){

        String mUPCDBAPIKEY = getString(R.string.upcdbkey);

        UPCProduct product = RestClient.getUPCService().lookupProduct(mUPCDBAPIKEY, upc);
        Bundle b = new Bundle();
        b.putParcelable(Constants.EXTRA_UPC_PRODUCT, product);
        receiver.send(Constants.RESULT_CODE_PRODUCT, b);
    }


    private void handleReceivingItem(ResultReceiver receiver, UPCProduct scanItem) {
        //check if item exist
        String query = "Select * FROM Item WHERE sku='" + scanItem.getNumber() + "'";

        String mCompanyId = getString(R.string.companyId);

        QueryItemResponse response = RestClient.getQBOService().findItemsBySKU(mCompanyId, query);

        int count = response.getTotalCount().intValue();
        //if not found then create new
        if (count == 0) {

            Item item = new Item();

            RestClient.getQBOService().createItm(mCompanyId, item);

        }

        //if found and count = 1 then increase quantity
        else if (count == 1) {

            Item item = response.getItem().get(0);

            item.setQtyOnHand(item.getQtyOnHand().add(new BigDecimal(1)));

            RestClient.getQBOService().updateItem(mCompanyId, item);

        }
        //if found and count > 1 then raise error
        else
            Toast.makeText(IntentService.this, "There are more than one inventory item with same SKU " + scanItem.getNumber(), Toast.LENGTH_SHORT).show();

    }

    private void handleIssuingItem(ResultReceiver receiver, String invoiceId, String sku){
        //check if orders for that sku exist


    }
}
