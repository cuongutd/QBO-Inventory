package com.tb2g.qboinventory.model;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.os.ResultReceiver;

import com.tb2g.qboinventory.service.IntentService;
import com.tb2g.qboinventory.util.Constants;

/**
 * Created by Cuong on 12/2/2015.
 */
public class IntentServiceTask {

    private Context context;
    private Intent intent;

    public static IntentServiceTask with(Context context){
        IntentServiceTask task = new IntentServiceTask();
        task.context = context;
        task.intent = new Intent(context, IntentService.class);
        return task;
    }

    public IntentServiceTask goingToDo(String intentAction){
        this.intent.setAction(intentAction);
        return this;
    }

    public IntentServiceTask putExtra(String extra, String value){

        this.intent.putExtra(extra, value);
        return this;

    }

    public IntentServiceTask setResultReceiver(ResultReceiver rec){
        this.intent.putExtra(Constants.EXTRA_RESULT_RECEIVER, rec);
        return this;
    }

    public IntentServiceTask putExtra(String extra, Parcelable value){

        this.intent.putExtra(extra, value);
        return this;

    }

    public void start(){

        this.context.startService(this.intent);
    }

}