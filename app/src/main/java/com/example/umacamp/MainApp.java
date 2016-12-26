package com.example.umacamp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Main App
 */
class MainApp extends Activity {
    // Debug identifier
    public static final String TAG = "Estimote";

    // BeaconManager and region variables
    private BeaconManager beaconManager;
    private Region region;

    // Stores beacon information
    private static Map<String, BeaconData> mBeacon = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        // Start beacons ranging modality
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    // Verifies all beacons detected in the zone
                    for (Beacon b : list) {
                        // Stores information only with IMMEDIATE beacons
                        if (Utils.computeProximity(b) == Utils.Proximity.IMMEDIATE) {
                            new BeaconAPIRequest(getApplicationContext()).execute(b);
                        } else {
                            String key;
                            key = b.getMajor() + ":" + b.getMinor();
                            if (mBeacon.containsKey(key)){
                                mBeacon.remove(key);
                            }
                        }
                    }
                }
            }
        });

        // Defines the region for beacons scanning
        region = new Region("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

    }

    // Adds new beacon to the dictionary of the beacons detected
    public static void addBeacon(BeaconData bd) {
        mBeacon.put(bd.getKey(), bd);
        Log.e(TAG, "key# " + bd.getKey() + " Items:" + mBeacon.size());
        // ToDo User Interface Update
    }

    @Override
    protected void onResume() {
        super.onResume();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
