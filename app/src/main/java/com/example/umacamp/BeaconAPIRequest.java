package com.example.umacamp;

import android.content.Context;
import android.os.AsyncTask;

import com.estimote.sdk.Beacon;

class BeaconAPIRequest extends AsyncTask<Beacon, Void, String> {
    private String strURL;
    private String strUUID;
    private int minor;
    private int major;

    BeaconAPIRequest(Context context){
        super();

        strURL = context.getResources().getString(R.string.url_meta);
        strUUID = context.getResources().getString(R.string.uuid);
    }

    @Override
    protected String doInBackground(Beacon... params) {
        Beacon beacon = params[0];
        major = beacon.getMajor();
        minor = beacon.getMinor();

        return HttpUrlConnectionJson.request(strURL + "/"
                + strUUID + "/"
                + major + "/"
                + minor);
    }

    protected void onPostExecute(String data) {
        MainApp.addBeacon(new BeaconData(data, major, minor));
    }

}
