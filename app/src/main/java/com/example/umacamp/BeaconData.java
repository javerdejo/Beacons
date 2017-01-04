package com.example.umacamp;

import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Integer.parseInt;

class BeaconData {
    private int image;
    private int major;
    private int minor;
    private String title;
    private String info;

    BeaconData(String jsonData, int major, int minor) {
        String typeId;
        final int icons[] = {
                R.drawable.ic_museum,
                R.drawable.ic_church,
                R.drawable.ic_monument,
                R.drawable.ic_pantheon,
                R.drawable.ic_statue,
                R.drawable.ic_restaurant,
                R.drawable.ic_coffee,
                R.drawable.ic_pizza,
                R.drawable.ic_bar
        };

        try {
            JSONObject root = new JSONObject(jsonData);
            title = root.getString("title");
            info = root.getString("info");
            typeId = root.getString("typeId");
            image = icons[parseInt(typeId) - 1];
            this.minor = minor;
            this.major = major;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String getKey() { return major + ":" + minor; }

    int getImage() {
        return image;
    }

    int getMajor() {
        return major;
    }

    int getMinor() {
        return minor;
    }

    String getTitle() {
        return title;
    }

    String getInfo() {
        return info;
    }

}
