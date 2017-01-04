package com.example.umacamp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Main App
 */
class MainApp extends Activity {
    // Debug identifier
    public static final String TAG = "UMACamp";

    // BeaconManager and region variables
    private BeaconManager beaconManager;
    private Region region;

    // ListView for detected beacons
    public ListView lv;

    public static BeaconDataAdapter adapter;

    // Stores detected beacons
    public static ArrayList<BeaconData> places = new ArrayList<>();

    // Stores beacon information
    private static Map<String, BeaconData> mBeacon = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        // Displays information about detected beacons
        lv = (ListView) findViewById(R.id.lvItems);

        // Creates new adapter to fill 'lv' list view and display the beacon info
        adapter = new BeaconDataAdapter(this, places);

        // Connects lv with their adapter
        lv.setAdapter(adapter);

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
                UUID.fromString(getResources().getString(R.string.uuid)),
                        null, null);

        // List view click event
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ShowInfoActivity.class);
                intent.putExtra("TITLE_ID", places.get(position).getTitle());
                intent.putExtra("INFO_ID", places.get(position).getInfo());
                intent.putExtra("MAJOR_ID", Integer.toString(places.get(position).getMajor()));
                intent.putExtra("MINOR_ID", Integer.toString(places.get(position).getMinor()));
                startActivity(intent);
            }
        });
    }

    // Adds new beacon to the dictionary of the beacons detected
    public static void addBeacon(BeaconData bd) {
        mBeacon.put(bd.getKey(), bd);
        //Log.e(TAG, "key# " + bd.getKey() + " Info: " + bd.getInfo() + ": " + " Items:" + mBeacon.size());

        // ToDo User Interface Update
        places.clear();
        for (BeaconData tmp: mBeacon.values()){
            places.add(tmp);
        }
        adapter.notifyDataSetChanged();

        //places.add(bd);
        Log.e(TAG, "Size: " +places.size());
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
