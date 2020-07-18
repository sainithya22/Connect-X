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


public class FindMatch extends Fragment implements View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.find_match, container, false);
        Button button1 =(Button) view.findViewById(R.id.button1);
        button1.setOnClickListener(this);
        Button button2 =(Button) view.findViewById(R.id.button2);
        button2.setOnClickListener(this);
        Button button3 =(Button) view.findViewById(R.id.button3);
        button3.setOnClickListener(this);
        Button button4 =(Button) view.findViewById(R.id.button4);
        button4.setOnClickListener(this);
        Button button5 =(Button) view.findViewById(R.id.button5);
        button5.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        final Button button = (Button) getView().findViewById(v.getId());
        final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 15);
        myAnim.setInterpolator(interpolator);
        button.startAnimation(myAnim);

        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {



            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        Bundle bundle=new Bundle();
        bundle.putString("Sport", button.getText().toString());
        Fragment fragment = new AvailableMatches();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frag_container1,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    }

