package com.example.playconnect;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.stream.IntStream;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class Submit_details extends Fragment   {
    private TextView addressView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addressView = (TextView) view.findViewById(R.id.addressView);
    }
        void displayReceivedData (String message){
            addressView.setText(message);

    }
    String addressLine;
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode==5){
                addressLine=data.getStringExtra("addressLine");
                TextView addressView = (TextView) getView().findViewById(R.id.addressView);
                addressView.setText(addressLine);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.submit_details, container, false);
        Spinner dropdown = view.findViewById(R.id.spinner);
        Integer items[] = new Integer[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        Bundle bundle = this.getArguments();
        if(bundle!=null) {
            String strtext = bundle.getString("Sport");
            TextView SportName = (TextView) view.findViewById(R.id.textView3);
            SportName.setText(strtext);
        }

        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        ImageButton button = (ImageButton) view.findViewById(R.id.imageButton2);
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                boolean CurrentDateSet;
                Calendar c = Calendar.getInstance();
                int y = c.get(Calendar.YEAR);
                int m = c.get(Calendar.MONTH);
                int d = c.get(Calendar.DAY_OF_MONTH);
                if(year==y && month==m && dayOfMonth==d) CurrentDateSet=true;
                if(year<y || (year==y && month<m) || (year==y && month==m && dayOfMonth<d)) Toast.makeText(getContext(),"Choose a valid date",Toast.LENGTH_LONG).show();
                else{
                    TextView DateView = (TextView) getView().findViewById(R.id.choose_date);
                    month+=1;
                    String dateString = dayOfMonth + "/" + month+ "/" + year;
                    DateView.setText(dateString);
                }
            }
        };
         final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), listener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog.show();
            }
        });

        ImageButton setTime = (ImageButton) view.findViewById(R.id.setTime);
       final TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                TextView timeView = (TextView) view.findViewById(R.id.timeView);
                timeView.setText( selectedHour + ":" + selectedMinute);
            }
        },Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE), true);//Yes 24 hour time
        timePickerDialog.setTitle("Select Time");

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

        Button button1 = (Button) view.findViewById(R.id.LocationButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Fragment fragment = new MapsActivity();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragment.setTargetFragment(Submit_details.this,5);
                fragmentTransaction.add(R.id.submit_details,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        Button submitButton = (Button) view.findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView DateView = (TextView) getView().findViewById(R.id.choose_date);
                TextView timeView = (TextView) view.findViewById(R.id.timeView);
                TextView SportName = (TextView) view.findViewById(R.id.textView3);
                Spinner dropdown = view.findViewById(R.id.spinner);
                TextView addressView = (TextView) view.findViewById(R.id.addressView);

                String time = timeView.getText().toString();
                String date=  DateView.getText().toString();
                String sport = SportName.getText().toString();
                Integer players= (Integer)dropdown.getSelectedItem();
                String address= addressView.getText().toString();

                if(time.isEmpty() || date.isEmpty()|| address.isEmpty())
                    Toast.makeText(getContext(), "Enter all the required fields", Toast.LENGTH_SHORT).show();
                else {

                    String id= database.collection("matches").getId();
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    final Match match = new Match(sport, date, players, time, addressLine, currentFirebaseUser.getUid(),"a");
                    database.collection("matches")
                            .add(match)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    database.collection("matches").document(documentReference.getId()).update("id", documentReference.getId());
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    Toast.makeText(getContext(),"Match Scheduled successfully",Toast.LENGTH_LONG).show();
                                    getFragmentManager().popBackStack();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });

                }
            }
        });
        return view;
    }


}
