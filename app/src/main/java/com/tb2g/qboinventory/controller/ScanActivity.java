package com.tb2g.qboinventory.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tb2g.qboinventory.R;
import com.tb2g.qboinventory.model.QBOCompanyInfo;
import com.tb2g.qboinventory.model.QBOItem;
import com.tb2g.qboinventory.model.QBOResponse;
import com.tb2g.qboinventory.model.UPCSearchProduct;
import com.tb2g.qboinventory.service.BaseResultReceiver;
import com.tb2g.qboinventory.service.IntentService;
import com.tb2g.qboinventory.util.Constants;
import com.tb2g.qboinventory.util.DateUtil;

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
    //@Bind(R.id.qboqty)
    //TextView qboQuantity;
    @Bind(R.id.qboqty)
    TextSwitcher mQtySwitcher;
    @Bind(R.id.qboprice)
    TextView qboPrice;
    @Bind(R.id.qbocost)
    TextView qboCost;
    @Bind(R.id.qbocreated)
    TextView qboCreatedDate;
    @Bind(R.id.qboupdated)
    TextSwitcher mUpdateSwitcher;
    //TextView qboUpdatedDate;
    @Bind(R.id.productimg)
    ImageView productImg;

    //private UPCProduct mProduct;
    private UPCSearchProduct mProduct;
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

        //mQtySwitcher = (TextSwitcher) findViewById(R.id.qboqty);
        //mUpdateSwitcher = (TextSwitcher) findViewById(R.id.qboupdated);
        initQtySwitcher();
        initUpdateSwitcher();

        setSupportActionBar(toolbar);

        handleSavedInstance(savedInstanceState);

        configFabButton();

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadQBOCompanyInfo();//try to load it if needed
    }

    private void initQtySwitcher(){

        mQtySwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                TextView myText = new TextView(ScanActivity.this);
                myText.setTypeface(null, Typeface.BOLD);
                return myText;
            }
        });

        // Declare the in and out animations and initialize them
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);

        // set the animation type of textSwitcher
        mQtySwitcher.setInAnimation(in);
        mQtySwitcher.setOutAnimation(out);

    }

    private void initUpdateSwitcher(){

        mUpdateSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                TextView myText = new TextView(ScanActivity.this);
                myText.setTypeface(null, Typeface.BOLD);
                return myText;
            }
        });

        // Declare the in and out animations and initialize them
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.fade_out);

        // set the animation type of textSwitcher
        mUpdateSwitcher.setInAnimation(in);
        mUpdateSwitcher.setOutAnimation(out);

    }


    private void loadQBOCompanyInfo(){

        if (!isKeysSetup()){
            startActivity(new Intent(this, SettingsActivity.class));
        }else if (mSampleQBOItem == null || mCompanyInfo == null)
            IntentService.getCompanyInfo(mReceiver, this);

    }


    private boolean isKeysSetup(){

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String consumerKey = sharedPref.getString(Constants.SHARED_PREF_CONSUMMER_KEY, "");
        String consumerSecret = sharedPref.getString(Constants.SHARED_PREF_CONSUMMER_SECRET, "");
        String accessToken = sharedPref.getString(Constants.SHARED_PREF_ACCESS_TOKEN, "");
        String accessTokenSecret = sharedPref.getString(Constants.SHARED_PREF_ACCESS_TOKEN_SECRET, "");
        String mCompanyId = sharedPref.getString(Constants.SHARED_PREF_QBO_COMPANY_ID, "");
        String mUPCDatabaseKey = sharedPref.getString(Constants.SHARED_PREF_UPCDB_KEY, "");
        String mSampleItemName = sharedPref.getString(Constants.SHARED_PREF_SAMPLE_ITEM_NAME, "");

        if (TextUtils.isEmpty(consumerKey)||
                TextUtils.isEmpty(consumerSecret)||
                TextUtils.isEmpty(accessToken)||
                TextUtils.isEmpty(accessTokenSecret)||
                TextUtils.isEmpty(mCompanyId)||
                TextUtils.isEmpty(mUPCDatabaseKey)||
                TextUtils.isEmpty(mSampleItemName))
            return false;
        else
            return true;

    }

    private boolean companyNotLoaded(){
        return (mCompanyInfo == null || mSampleQBOItem == null);
    }


    private void configFabButton(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isKeysSetup()){
                    showSnackbarNoAction(getString(R.string.sbar_no_keys_setting),true);
                    return;
                }

                if (companyNotLoaded()){
                    showSnackbarNoAction(getString(R.string.sbar_no_company),true);
                    return;
                }



                resetViews();
                IntentIntegrator integrator = new IntentIntegrator(ScanActivity.this);
                integrator.addExtra("SCAN_WIDTH", 800);
                integrator.addExtra("SCAN_HEIGHT", 300);
                integrator.addExtra("RESULT_DISPLAY_DURATION_MS", 3000L);
                integrator.addExtra("PROMPT_MESSAGE", getString(R.string.scan_prompt_msg));
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
            showSnackbarNoAction(getString(R.string.sbar_looking_web), false);
            IntentService.getWebProductInfo(mReceiver, this, unpaddedUPC);

        } else {
            showSnackbarNoAction(getString(R.string.sbar_cannot_read), true);
        }

    }

    private void populateViews() {
        if (mProduct != null && !TextUtils.isEmpty(mProduct.getProductname())) {
            productName.setText(mProduct.getProductname());
            productDesc.setText(mProduct.getProducturl());
            upc.setText(mProduct.getUpc());
            Glide.with(this).load(mProduct.getImageurl()).into(productImg);

        }

        if (mQBOItem != null) {
            qboId.setText(mQBOItem.getId());
            qboName.setText(mQBOItem.getName());
            qboDesc.setText(mQBOItem.getDescription());
            if (mQBOItem.getQtyOnHand() != null)
                mQtySwitcher.setText(mQBOItem.getQtyOnHand().toString());
            else
                mQtySwitcher.setText("0");
            if (mQBOItem.getUnitPrice() != null)
                qboPrice.setText(mQBOItem.getUnitPrice().toString());

            if (mQBOItem.getPurchaseCost() != null)
                qboCost.setText(mQBOItem.getPurchaseCost().toString());

            qboCreatedDate.setText(DateUtil.getReadableDate(DateUtil.formatStringToTimestamp(mQBOItem.getMetaData().getCreateTime())));
            mUpdateSwitcher.setText(DateUtil.getReadableDate(DateUtil.formatStringToTimestamp(mQBOItem.getMetaData().getLastUpdatedTime())));
        }
    }


    private void resetViews() {
        productName.setText("");
        productDesc.setText("");
        upc.setText("");

        qboId.setText("");
        qboName.setText("");
        qboDesc.setText("");
        mQtySwitcher.setText("");
        qboPrice.setText("");
        qboCost.setText("");
        qboCreatedDate.setText("");
        mUpdateSwitcher.setText("");


        //qboMessage.setText("");

    }

    private void showSnackbarUpdate() {
        snackbarStatus = 2;
        mSnackbar = Snackbar.make(fab, R.string.sbar_update_qty, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.sbar_action_update, new View.OnClickListener() {
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
        mSnackbar = Snackbar.make(fab, R.string.sbar_create_qbo, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.sbar_button_create, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mSampleQBOItem == null){
                            showSnackbarNoAction(getString(R.string.sbar_sample_notfound), true);
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

                if (!TextUtils.isEmpty(mProduct.getProductname())) {
                    showSnackbarNoAction(getString(R.string.sbar_product_found_web), false);
                    populateViews();
                } else {
                    showSnackbarNoAction(getString(R.string.sbar_product_not_found), false);
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
                    if (mProduct != null && !TextUtils.isEmpty(mProduct.getProductname()))
                        showSnackbarCreate();
                    else
                        showSnackbarNoAction(getString(R.string.sbar_product_notfound_qbo), true);
                } else if (count == 1) {//if found and count = 1 then increase quantity
                    mQBOItem = result.getQueryResponse().getItem().get(0);
                    populateViews();

                    showSnackbarUpdate();

                } else {//if found and count > 1 then raise error
                    String msg = "";
                    for (QBOItem item : result.getQueryResponse().getItem())
                        msg += item.getName() + ",\n";
                    showSnackbarNoAction(getString(R.string.sbar_sku_dup_error) + msg, true);
                }
                break;
            case Constants.RESULT_CODE_QBO_ITEM: //returned when you add/update qbo item

                result = resultData.getParcelable(Constants.EXTRA_QBO_ITEM);

                if (result.getItem() != null && result.getItem().getId() != null) {
                    mQBOItem = result.getItem();
                    populateViews();
                    showSnackbarNoAction(getString(R.string.sbar_item_saved), false);
                } else {
                    showSnackbarNoAction(getString(R.string.sbar_item_notsaved), false);
                }
                break;
            case Constants.RESULT_CODE_QBO_COMPANY:

                result = resultData.getParcelable(Constants.EXTRA_QBO_COMPANY);

                if (!TextUtils.isEmpty(result.getErrorCode()) ||!TextUtils.isEmpty(result.getErrorMsg())) //something wrong

                    showSnackbarNoAction(getString(R.string.sbar_generic_qbo_error) +
                            result.getErrorCode() +
                            "-" +
                            result.getErrorMsg(), true);

                else if (result.getCompanyInfo() != null
                        && result.getQueryResponse() != null
                        && result.getQueryResponse().getItem() != null
                        && result.getQueryResponse().getItem().size() == 1) {
                    mSampleQBOItem = result.getQueryResponse().getItem().get(0);
                    mCompanyInfo = result.getCompanyInfo();
                    getSupportActionBar().setSubtitle(mCompanyInfo.getCompanyName());
                }
                else
                    showSnackbarNoAction(getString(R.string.sbar_invalid_companyid), true);
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
