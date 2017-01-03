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
    private String typeId;

    int icons[] = {
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

    BeaconData(String jsonData, int major, int minor) {
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

    public int getImage() {
        return image;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public String getTypeId() {
        return typeId;
    }
}
