package com.tb2g.qboinventory.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tb2g.qboinventory.R;
import com.tb2g.qboinventory.model.UPCProduct;
import com.tb2g.qboinventory.service.BaseResultReceiver;
import com.tb2g.qboinventory.service.IntentService;
import com.tb2g.qboinventory.util.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ScanActivity extends BaseAppCompatActivity {

    @Bind(R.id.scan)
    Button scanButton;
    @Bind(R.id.productname)
    TextView productName;
    @Bind(R.id.description)
    TextView productDesc;
    @Bind(R.id.upc)
    TextView upc;
    @Bind(R.id.productimg)
    ImageView productImage;

    private UPCProduct mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        ButterKnife.bind(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.EXTRA_UPC_PRODUCT)){
            mProduct = savedInstanceState.getParcelable(Constants.EXTRA_UPC_PRODUCT);
            populateViews();
        }



        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetViews();
                IntentIntegrator integrator = new IntentIntegrator(ScanActivity.this);
                integrator.addExtra("SCAN_WIDTH", 800);
                integrator.addExtra("SCAN_HEIGHT", 200);
                integrator.addExtra("RESULT_DISPLAY_DURATION_MS", 3000L);
                integrator.addExtra("PROMPT_MESSAGE", "Scan a barcode");
                integrator.initiateScan(IntentIntegrator.PRODUCT_CODE_TYPES);//scan product code type

            }
        });



    }

    private void showDiaglog(int numberOfItems){

        String msg = "";
        if (numberOfItems == 0) {
            msg = "There is no item in your QuickBooks Online with this SKU: " + mProduct.getNumber() + "\n";
            msg += "Would you like to add this item to the inventory?";
        } else if (numberOfItems == 1){

            msg = "There is no item in your QuickBooks Online with this SKU: " + mProduct.getNumber() + "\n";
            msg += "Would you like to add this item to the inventory?";
        }
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        logd("onActivityResult");
        if (mReceiver.isNull()){
            mReceiver = new BaseResultReceiver(new Handler());
            mReceiver.setReceiver(this);
        }
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (result != null && "UPC_A".equals(result.getFormatName())) {
            String unpaddedUPC = result.getContents();
            upc.setText("UPC: " + unpaddedUPC);
            //go to upcdatabase.org to get product info
            String paddedUPC = "0000000000000".substring(unpaddedUPC.length()) + unpaddedUPC; //padding 0 to make 13 long
            IntentService.getProductInfoByUPC(mReceiver, this, paddedUPC);

        }

    }

    private void populateViews(){
        if (mProduct != null && mProduct.isValid()) {
            productName.setText(mProduct.getItemname());
            productDesc.setText(mProduct.getDescription());
            upc.setText("UPC: " + mProduct.getNumber());
        }
    }


    private void resetViews(){
        productName.setText("");
        productDesc.setText("");
        upc.setText("");

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case Constants.RESULT_CODE_PRODUCT:

                mProduct = resultData.getParcelable(Constants.EXTRA_UPC_PRODUCT);

                if (mProduct.isValid())
                    populateViews();
                else
                    Toast.makeText(this, "Product not found in the database", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mProduct != null)
            outState.putParcelable(Constants.EXTRA_UPC_PRODUCT, mProduct);
    }
}
