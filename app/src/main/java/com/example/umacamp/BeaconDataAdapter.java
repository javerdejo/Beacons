package com.example.umacamp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class BeaconDataAdapter  extends ArrayAdapter {
    private Context context;
    private ArrayList<BeaconData> data;

    public BeaconDataAdapter(Context context, ArrayList data) {
        super(context, R.layout.list_view_item, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.list_view_item, null);

        ImageView image = (ImageView) item.findViewById(R.id.image);
        image.setImageResource(data.get(position).getImage());

        TextView title = (TextView)item.findViewById(R.id.title);
        title.setText(data.get(position).getTitle());

        TextView subtitle = (TextView)item.findViewById(R.id.info);
        subtitle.setText(data.get(position).getInfo());

        return item;
    }

}
