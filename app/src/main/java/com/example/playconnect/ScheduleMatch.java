package com.example.playconnect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ScheduleMatch extends Fragment implements View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.schedule_match, container, false);
        Button button6 =(Button) view.findViewById(R.id.button1);
        button6.setOnClickListener(this);
        Button button7 =(Button) view.findViewById(R.id.button2);
        button7.setOnClickListener(this);
        Button button8 =(Button) view.findViewById(R.id.button3);
        button8.setOnClickListener(this);
        Button button9 =(Button) view.findViewById(R.id.button4);
        button9.setOnClickListener(this);
        Button button10 =(Button) view.findViewById(R.id.button5);
        button10.setOnClickListener(this);
       return view;
    }

    @Override
    public void onClick(View v) {
        final Button button = (Button) getActivity().findViewById(v.getId());
        final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 20);
        myAnim.setInterpolator(interpolator);
        button.startAnimation(myAnim);
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Bundle bundle=new Bundle();
                bundle.putString("Sport", button.getText().toString());
                Fragment fragment = new Submit_details();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frag_container2,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
}
