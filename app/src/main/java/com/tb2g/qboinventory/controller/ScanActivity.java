package com.tb2g.qboinventory.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tb2g.qboinventory.R;
import com.tb2g.qboinventory.model.QBOCompanyInfo;
import com.tb2g.qboinventory.model.QBOItem;
import com.tb2g.qboinventory.model.QBOResponse;
import com.tb2g.qboinventory.model.UPCProduct;
import com.tb2g.qboinventory.service.BaseResultReceiver;
import com.tb2g.qboinventory.service.IntentService;
import com.tb2g.qboinventory.util.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ScanActivity extends BaseAppCompatActivity {

    @Bind(R.id.productname)
    TextView productName;
    @Bind(R.id.description)
    TextView productDesc;
    @Bind(R.id.upc)
    TextView upc;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.qboid)
    TextView qboId;
    @Bind(R.id.qboname)
    TextView qboName;
    @Bind(R.id.qbodesc)
    TextView qboDesc;
    @Bind(R.id.qboqty)
    TextView qboQuantity;
    @Bind(R.id.qboprice)
    TextView qboPrice;
    @Bind(R.id.qbocost)
    TextView qboCost;
    @Bind(R.id.qbocreated)
    TextView qboCreatedDate;
    @Bind(R.id.qboupdated)
    TextView qboUpdatedDate;

    private UPCProduct mProduct;
    private QBOItem mQBOItem;
    private QBOItem mSampleQBOItem;
    private QBOCompanyInfo mCompanyInfo;

    private Snackbar mSnackbar;
    private int snackbarStatus; //0--no show, 1- show infinit no action, 2- show update, 3 - show create
    private String snackbarMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        handleSavedInstance(savedInstanceState);

        configFabButton();

        if (mSampleQBOItem == null)
            //load a sample item for used when creating new items
            IntentService.getSampleQBOItem(mReceiver, this);

        if (mCompanyInfo == null)
            IntentService.getCompanyInfo(mReceiver, this);
    }

    private void configFabButton(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


    private void handleSavedInstance(Bundle savedInstanceState){

        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(Constants.EXTRA_UPC_PRODUCT))
                mProduct = savedInstanceState.getParcelable(Constants.EXTRA_UPC_PRODUCT);

            if (savedInstanceState.containsKey(Constants.EXTRA_QBO_ITEM))
                mQBOItem = savedInstanceState.getParcelable(Constants.EXTRA_QBO_ITEM);

            populateViews();

            if (savedInstanceState.containsKey(Constants.EXTRA_QBO_SAMPLE))
                mSampleQBOItem = savedInstanceState.getParcelable(Constants.EXTRA_QBO_SAMPLE);

            if (savedInstanceState.containsKey(Constants.EXTRA_QBO_COMPANY)) {
                mCompanyInfo = savedInstanceState.getParcelable(Constants.EXTRA_QBO_COMPANY);
                getSupportActionBar().setSubtitle(mCompanyInfo.getCompanyName());
            }

            if (savedInstanceState.containsKey(Constants.EXTRA_SNACKBAR_STATUS)) {
                snackbarStatus = savedInstanceState.getInt(Constants.EXTRA_SNACKBAR_STATUS);
                snackbarMsg = savedInstanceState.getString(Constants.EXTRA_SNACKBAR_MSG);
                if (1 == snackbarStatus)
                    showSnackbarNoAction(snackbarMsg, true);
                else if (2 == snackbarStatus)
                    showSnackbarUpdate();
                else if (3 == snackbarStatus)
                    showSnackbarCreate();
            }

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        logd("onActivityResult");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (result != null && "UPC_A".equals(result.getFormatName())) {
            String unpaddedUPC = result.getContents();
            upc.setText(unpaddedUPC);
            //go to upcdatabase.org to get product info
            String paddedUPC = "0000000000000".substring(unpaddedUPC.length()) + unpaddedUPC; //padding 0 to make 13 long

            if (mReceiver.isNull()) {
                mReceiver = new BaseResultReceiver(new Handler());
                mReceiver.setReceiver(this);
            }
            showSnackbarNoAction("Looking for product information on the web....", false);
            IntentService.getWebProductInfo(mReceiver, this, paddedUPC);

        } else {
            showSnackbarNoAction("Cannot read barcode!", true);
        }

    }

    private void populateViews() {
        if (mProduct != null && mProduct.isValid()) {
            productName.setText(mProduct.getItemname());
            productDesc.setText(mProduct.getDescription());
            upc.setText(mProduct.getNumber());
        }

        if (mQBOItem != null) {
            qboId.setText(mQBOItem.getId());
            qboName.setText(mQBOItem.getName());
            qboDesc.setText(mQBOItem.getDescription());
            if (mQBOItem.getQtyOnHand() != null)
                qboQuantity.setText(mQBOItem.getQtyOnHand().toString());
            if (mQBOItem.getUnitPrice() != null)
                qboPrice.setText(mQBOItem.getUnitPrice().toString());
            if (mQBOItem.getPurchaseCost() != null)
                qboCost.setText(mQBOItem.getPurchaseCost().toString());
            qboCreatedDate.setText(mQBOItem.getMetaData().getCreateTime());
            qboUpdatedDate.setText(mQBOItem.getMetaData().getLastUpdatedTime());
        }
    }


    private void resetViews() {
        productName.setText("");
        productDesc.setText("");
        upc.setText("");

        qboId.setText("");
        qboName.setText("");
        qboDesc.setText("");
        qboQuantity.setText("");
        qboPrice.setText("");
        qboCost.setText("");
        qboCreatedDate.setText("");
        qboUpdatedDate.setText("");


        //qboMessage.setText("");

    }

    private void showSnackbarUpdate() {
        snackbarStatus = 2;
        mSnackbar = Snackbar.make(fab, "Item found in QBO. Update quantity?", Snackbar.LENGTH_INDEFINITE)
                .setAction("UPDATE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentService.increaseQBOItemQuantity(mReceiver, ScanActivity.this, mQBOItem);
                    }
                });

        // Changing message text color
        mSnackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = mSnackbar.getView();
        TextView textView = (TextView)sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        mSnackbar.show();
    }

    private void showSnackbarCreate() {
        snackbarStatus = 3;
        mSnackbar = Snackbar.make(fab, "Item not found in QBO. Create new?", Snackbar.LENGTH_INDEFINITE)
                .setAction("CREATE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mSampleQBOItem == null){
                            showSnackbarNoAction("Item sample not found. Please check sample name setting.", true);
                            return;
                        }
                        QBOItem item = QBOItem.fromUPCProduct(mProduct);
                        item.setAssetAccountRef(mSampleQBOItem.getAssetAccountRef());
                        item.setIncomeAccountRef(mSampleQBOItem.getIncomeAccountRef());
                        item.setExpenseAccountRef(mSampleQBOItem.getExpenseAccountRef());

                        IntentService.createQBOItem(mReceiver, ScanActivity.this, item);
                    }
                });

        // Changing message text color
        mSnackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = mSnackbar.getView();
        TextView textView = (TextView)sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        mSnackbar.show();
    }

    private void showSnackbarNoAction(String msg, boolean indefinite) {
        int len = 0;
        if (indefinite) {
            snackbarStatus = 1;
            snackbarMsg = msg;
            len = Snackbar.LENGTH_INDEFINITE;
        } else
            len = Snackbar.LENGTH_LONG;
        mSnackbar = Snackbar.make(fab, msg, len);
        mSnackbar.show();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        QBOResponse result = null;
        int count = 0;
        switch (resultCode) {
            case Constants.RESULT_CODE_WEB_PRODUCT:

                mProduct = resultData.getParcelable(Constants.EXTRA_UPC_PRODUCT);

                if (mProduct.isValid()) {
                    showSnackbarNoAction("Product found on the internet", false);
                    populateViews();
                } else {
                    showSnackbarNoAction("Product not found in the web database!", false);
                }
                //lookup in QBO for item info
                IntentService.getQBOItemInfo(mReceiver, this, upc.getText().toString());
                break;
            case Constants.RESULT_CODE_QBO_QUERY:

                result = resultData.getParcelable(Constants.EXTRA_QBO_QUERY);

                if (result.getQueryResponse() != null && result.getQueryResponse().getItem() != null)
                    count = result.getQueryResponse().getItem().size();

                //if not found then create new
                if (count == 0) {
                    if (mProduct != null && mProduct.isValid()) {
                        showSnackbarCreate();
                    } else {

                        showSnackbarNoAction("Unknown product and it's not found in QBO!", true);

                    }

                } else if (count == 1) {//if found and count = 1 then increase quantity
                    mQBOItem = result.getQueryResponse().getItem().get(0);
                    populateViews();

                    showSnackbarUpdate();

                } else//if found and count > 1 then raise error
                    showSnackbarNoAction("Not good! " + count + " items having the same SKU " + upc.getText().toString() + " in QuickBooks Online!!!", true);

                break;
            case Constants.RESULT_CODE_QBO_ITEM: //returned when you add/update qbo item

                result = resultData.getParcelable(Constants.EXTRA_QBO_ITEM);

                if (result.getItem() != null && result.getItem().getId() != null) {
                    mQBOItem = result.getItem();
                    populateViews();
                    showSnackbarNoAction("Item saved to QBO!", false);
                } else {
                    showSnackbarNoAction("Failed to update QBO!!!", false);
                }
                break;
            case Constants.RESULT_CODE_QBO_SAMPLE:

                result = resultData.getParcelable(Constants.EXTRA_QBO_QUERY);

                if (result.getQueryResponse() != null && result.getQueryResponse().getItem() != null)
                    count = result.getQueryResponse().getItem().size();

                //if not found then create new
                if (count == 0) {
                    showSnackbarNoAction("Item sample not found! Please check sample name setting", true);
                } else//if found and count = 1 then increase quantity
                    mSampleQBOItem = result.getQueryResponse().getItem().get(0);
                break;
            case Constants.RESULT_CODE_QBO_COMPANY:

                result = resultData.getParcelable(Constants.EXTRA_QBO_COMPANY);

                if (result.getCompanyInfo() != null) {
                    mCompanyInfo = result.getCompanyInfo();
                    getSupportActionBar().setSubtitle(mCompanyInfo.getCompanyName());
                }
                else
                    showSnackbarNoAction("Invalid Company ID. Please check in setting!!!", true);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        if (mProduct != null)
            outState.putParcelable(Constants.EXTRA_UPC_PRODUCT, mProduct);

        if (mQBOItem != null)
            outState.putParcelable(Constants.EXTRA_QBO_ITEM, mQBOItem);

        if (mSampleQBOItem != null)
            outState.putParcelable(Constants.EXTRA_QBO_SAMPLE, mSampleQBOItem);

        if (mCompanyInfo != null)
            outState.putParcelable(Constants.EXTRA_QBO_COMPANY, mCompanyInfo);

        if (mSnackbar != null && mSnackbar.isShown() && (mSnackbar.getDuration() == Snackbar.LENGTH_INDEFINITE)) {

            outState.putInt(Constants.EXTRA_SNACKBAR_STATUS, snackbarStatus);
            outState.putString(Constants.EXTRA_SNACKBAR_MSG, snackbarMsg);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent settingIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingIntent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
