package com.tb2g.qboinventory.controller;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tb2g.qboinventory.R;
import com.tb2g.qboinventory.service.BaseResultReceiver;

public abstract class BaseAppCompatActivity extends AppCompatActivity implements BaseResultReceiver.Receiver{

    private String LOG_TAG = this.getClass().getSimpleName();

    protected BaseResultReceiver mReceiver;

    protected ProgressDialog mProgressDialog;

    protected void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logd("onCreate");

        mReceiver = new BaseResultReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        logd("onConfigurationChanged");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        logd("onPostCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logd("onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logd("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logd("onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        logd("onStart");
        if (mReceiver.isNull()) {
            mReceiver = new BaseResultReceiver(new Handler());
            mReceiver.setReceiver(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logd("onSaveInstanceState");
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        logd("onResumeFragments");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mReceiver.setReceiver(null); // clear receiver so no leaks.
        logd("onStop");
    }

    protected void logd(String msg) {
        Log.d(LOG_TAG, msg);
    }

}
