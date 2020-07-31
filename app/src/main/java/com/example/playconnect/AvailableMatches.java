package com.example.playconnect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AvailableMatches extends Fragment {

    ListView listView;
    List<Match> MatchList = new ArrayList<>();
    List<String> MatchId = new ArrayList<>();
    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("matches");
    String SportName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.available_matches, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            SportName = this.getArguments().getString("Sport");
        }
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
               new AlertDialog.Builder(getContext())
                       .setMessage("Are you willing to play this match?")
                       .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which){
                                    Match match = MatchList.get(position);
                                    match.availablePlayers+=1;
                                    String matchID = match.getMatchID();
                                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    String userID= currentUser.getUid();
                                    match.player_ids.add(currentUser.getUid());

                           }
                       })
                       .setNegativeButton("NO", null)
                       .show();
           }

       });


        return view;
    }


    @Override
    public void onStart() {

        super.onStart();
        databaseReference.orderByChild("sport").equalTo(SportName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MatchList.clear();

                for(DataSnapshot ds :snapshot.getChildren()) {
                    Match match = ds.getValue(Match.class);

                    if(match.availablePlayers< match.requiredPlayers) {
                        String pattern = "dd-MM-yyyy";
                        String dateInString = new SimpleDateFormat(pattern).format(new Date());
                        MatchList.add(match);

                        //Log.i("Match name", matchId);
                    }
                }
                matchList adapter = new matchList(getActivity(), MatchList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
