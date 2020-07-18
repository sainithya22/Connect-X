package com.example.playconnect;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class matchList extends ArrayAdapter<Match> {
    private Activity context;
    private List<Match> matchList;

    public matchList(Activity context, List<Match> matchList){
        super(context,R.layout.list_view_item, matchList);
        this.context= context;
        this.matchList= matchList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_view_item,null,true);

        TextView dateTimeView = (TextView) listViewItem.findViewById(R.id.dateAndTimeView);
        TextView locationView = (TextView) listViewItem.findViewById(R.id.locationView);

        Match match = matchList.get(position);
        dateTimeView.setText(match.getDate().toString()+"     "+match.getTime().toString());
        locationView.setText(match.getLocation());

        return listViewItem;
    }
}
