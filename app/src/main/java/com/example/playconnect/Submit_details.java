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

import java.sql.Time;
import java.util.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.stream.IntStream;

public class Submit_details extends Fragment  {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.submit_details, container, false);
        Spinner dropdown = view.findViewById(R.id.spinner);
        Integer items[] = new Integer[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        /*FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add();*/

        final Address address;

        Bundle bundle = this.getArguments();

        if(bundle!=null) {
            String strtext = this.getArguments().getString("Sport");
            TextView SportName = (TextView) view.findViewById(R.id.textView3);
            SportName.setText(strtext);

        }

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("matches");
       /* Intent intent = getActivity().getIntent();
        if (intent.getExtras() != null) {
            String name =intent.getStringExtra("Sport");
        }*/
        ImageButton button = (ImageButton) view.findViewById(R.id.imageButton2);
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                TextView DateView = (TextView) getView().findViewById(R.id.choose_date);
                String dateString = dayOfMonth + "/" + month + "/" + year;
                DateView.setText(dateString);

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
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });

        /*if(getArguments().getString("Address")!=null){
            TextView addressView = (TextView) view.findViewById(R.id.address);
            addressView.setText(getArguments().getString("Address"));
        }*/

        Button submitButton = (Button) view.findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView DateView = (TextView) getView().findViewById(R.id.choose_date);
                TextView timeView = (TextView) view.findViewById(R.id.timeView);
                TextView SportName = (TextView) view.findViewById(R.id.textView3);
                Spinner dropdown = view.findViewById(R.id.spinner);
                //TextView addressView = (TextView) view.findViewById(R.id.address);
                String time = timeView.getText().toString();
                String date=  DateView.getText().toString();
                String sport = SportName.getText().toString();
                Integer players= (Integer)dropdown.getSelectedItem();
                String getArgument = getArguments().getString("Address");
                //String address = addressView.getText().toString();
                Log.i("ADDRESSSSSSSSSSSSSS",getArgument);

                String id = database.push().getKey();
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                Match match= new Match(sport,date,players,time,getArgument, currentFirebaseUser.getUid());
                database.child(id).setValue(match);
            }
        });
        return view;
    }


}
