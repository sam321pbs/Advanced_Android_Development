package com.example.android.sunshine.app;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import android.os.AsyncTask;

/**
 * Created by SamMengistu on 4/17/16.
 */
public class SendWeatherToWearableTask extends AsyncTask<Node, Void, Void> {
    private final String[] contents;
    private GoogleApiClient mGoogleApiClient;

    public SendWeatherToWearableTask(GoogleApiClient googleApiClients, String[] contents) {
        this.contents = contents;
        mGoogleApiClient = googleApiClients;
    }

    @Override
    protected Void doInBackground(Node... nodes) {

        PutDataMapRequest dataMap = PutDataMapRequest.create("myapp/myweatherdata");
        dataMap.getDataMap().putStringArray("contents", contents);

        PutDataRequest request = dataMap.asPutDataRequest();

        //Sends data to wearable
        DataApi.DataItemResult dataItemResult = Wearable.DataApi
            .putDataItem(mGoogleApiClient, request).await();

        return null;
    }
}