package com.example.umacamp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
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
    //public static final String TAG = "UMACamp";

    // BeaconManager and region variables
    private BeaconManager beaconManager;
    private Region region;

    // ListView for detected beacons
    public ListView lv;

    public static BeaconDataAdapter adapter;
    static NotificationManager nm;
    static Context context;
    static HashMap<String, String> beaconKeys = new HashMap<String,String>();

    // Stores detected beacons
    public static ArrayList<BeaconData> places = new ArrayList<>();

    // Stores beacon information
    private static Map<String, BeaconData> mBeacon = new HashMap<>();

    //
    protected static void displayNotification() {
        int notificationID = 1;
        Intent i = new Intent(context, MainApp.class);
        i.putExtra("notificationID", notificationID);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

        CharSequence ticker = "Hemos encontrado algo para ti";
        CharSequence contentTitle = "Málaga Interactiva";
        CharSequence contentText = "Hemos encontrado algo para ti";
        Notification noti = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setTicker(ticker)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_bar)
                .setVibrate(new long[]{100, 250, 100, 500})
                .build();
        nm.notify(notificationID, noti);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        context = getApplicationContext();

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
                        String key;
                        key = b.getMajor() + ":" + b.getMinor();
                        // Stores information only with IMMEDIATE beacons
                        if (Utils.computeProximity(b) == Utils.Proximity.IMMEDIATE) {
                                new BeaconAPIRequest(getApplicationContext()).execute(b);
                        } else {
                            if (mBeacon.containsKey(key)) {
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
        if (!mBeacon.containsKey(bd.getKey())) {
            mBeacon.put(bd.getKey(), bd);

            places.clear();
            for (BeaconData tmp : mBeacon.values()) {
                places.add(tmp);
            }
            adapter.notifyDataSetChanged();

            if (!beaconKeys.containsKey(bd.getKey())) {
                beaconKeys.put(bd.getKey(), bd.getTitle());
                displayNotification();
            }
        }
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
