package com.example.android.sunshine.app;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;

public class SendWeatherToWearableTask extends AsyncTask<Node, Void, Void> {
    private final String[] mWeatherContents;
    private GoogleApiClient mGoogleApiClient;
    private Context mAppContext;

    public SendWeatherToWearableTask(Context appContext,
                                     GoogleApiClient googleApiClients, String[] mWeatherContents) {
        this.mWeatherContents = mWeatherContents;
        mGoogleApiClient = googleApiClients;
        mAppContext = appContext;
    }

    @Override
    protected Void doInBackground(Node... nodes) {

        PutDataMapRequest dataMap = PutDataMapRequest.create("myapp/myweatherdata");
        dataMap.getDataMap().putStringArray("weather_contents", mWeatherContents);

        Bitmap bitmap = getBitmapFromDrawable(Integer.parseInt(mWeatherContents[2]));
        Asset asset = createAssetFromBitmap(bitmap);
        dataMap.getDataMap().putAsset("weather_icon", asset);

        PutDataRequest request = dataMap.asPutDataRequest();

        //Sends data to wearable
        DataApi.DataItemResult dataItemResult = Wearable.DataApi
            .putDataItem(mGoogleApiClient, request).await();

        return null;
    }

    private Bitmap getBitmapFromDrawable(int weatherId){
        int drawableWeatherIconId = Utility.getIconResourceForWeatherCondition(weatherId);

        return BitmapFactory.decodeResource(mAppContext.getResources(),
            drawableWeatherIconId);
    }

    private static Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }
}