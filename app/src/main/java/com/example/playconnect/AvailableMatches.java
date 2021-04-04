package com.example.playconnect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class AvailableMatches extends Fragment {

    ListView listView;
    List<Match> MatchList = new ArrayList<>();
    List<String> MatchId = new ArrayList<>();
    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("matches");
    final FirebaseFirestore database = FirebaseFirestore.getInstance();
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
        final DatabaseReference databaseRefUser = FirebaseDatabase.getInstance().getReference("users");
        final DatabaseReference databaseRefMatch= FirebaseDatabase.getInstance().getReference("matches");

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
               new AlertDialog.Builder(getContext())
                       .setMessage("Are you willing to play this match?")
                       .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which){
                                    TextView textView= view.findViewById(R.id.matchid);
                                    String ID= textView.getText().toString();
                                    Log.d("IDDDDDDDDDDDD",ID);
                                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                    //if((String) database.collection("matches").document(ID).get("admin")!= currentFirebaseUser.getUid())
                                    database.collection("matches").document(ID).update("player_ids",FieldValue.arrayUnion(currentFirebaseUser.getUid()));
                                    database.collection("matches").document(ID).update("availablePlayers",FieldValue.increment(1));

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
        Log.d("SPORTNAMEEE", SportName);
        database.collection("matches").whereEqualTo("sport",SportName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    MatchList.clear();
                    Date date = new Date();
                    Timestamp ts=new Timestamp(date.getTime());
                    for (QueryDocumentSnapshot document : task.getResult()) {
                         try {
                             Date matchDate = new SimpleDateFormat("dd/MM/yyyy").parse(document.getData().get("date").toString());
                             DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                             Date d = dateFormat.parse(document.getData().get("time").toString());
                             if(date.compareTo(matchDate)<=0 && date.compareTo(d)>0){
                                 Match match = document.toObject(Match.class);
                                 MatchList.add(match);
                                 Log.d("MATCH IDDDDDDDD",match.toString());
                                 matchList adapter = new matchList(getActivity(), MatchList);
                                 listView.setAdapter(adapter);
                                 adapter.notifyDataSetChanged();
                                 Log.d(TAG, "onSuccessssss: " + match.availablePlayers);
                             }
                             else{
                                 Log.d("NOT FOUND", "MATCH DATE "+d+"CURRENT DATE "+date);
                             }

                         }catch (Exception e){
                             Log.d("Error ","in retreiving date and time: YESSSS");
                             e.printStackTrace();
                         }
                        //Date matchDate= timestamp.toDate();

                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }}})
            .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });

            }
};

