package com.example.playconnect;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class matchList extends ArrayAdapter<Match> {
    private Activity context;
    private List<Match> matchList;

    final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");

    public matchList(Activity context, List<Match> matchList){
        super(context,R.layout.list_view_item, matchList);
        this.context= context;
        this.matchList= matchList;
    }


    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_view_item,null,true);

        TextView dateTimeView = (TextView) listViewItem.findViewById(R.id.dateAndTimeView);
        TextView locationView = (TextView) listViewItem.findViewById(R.id.locationView);
        TextView idView= (TextView) listViewItem.findViewById(R.id.matchid);
        final ImageView imageView=(ImageView) listViewItem.findViewById(R.id.imageView);


        Match match = matchList.get(position);
        dateTimeView.setText(match.getDate().toString()+"     "+match.getTime().toString()+"        "+match.admin.toString());
        locationView.setText(match.getLocation());
        idView.setText(match.getMatchID());

        databaseRef.child(match.admin).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    String url = user.getImageURL();
                    if(url!=""){
                        ImageDownloader task = new ImageDownloader();
                        Bitmap adminImage;
                        try{
                            adminImage=  task.execute(url).get();
                            imageView.setImageBitmap(adminImage);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
});
        return listViewItem;
}}
