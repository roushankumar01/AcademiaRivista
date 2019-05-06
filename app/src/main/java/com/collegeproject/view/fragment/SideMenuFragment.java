package com.collegeproject.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.collegeproject.R;
import com.collegeproject.view.activity.AddStoryActivity;
import com.collegeproject.view.activity.LoginActivity;
import com.collegeproject.view.activity.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;


/**
 * Created by vaio on 5/25/2016.
 */

public class SideMenuFragment extends BaseFragment implements View.OnClickListener {

    private View rootView;
    private RelativeLayout addStorySideLay, accountSideLay, logoutbtn;
    private Button btnLogout;

    @Override
    protected int getFragmentLayout() {
        return R.layout.ly_sidebar;
    }

    public SideMenuFragment() {
    }

    public static SideMenuFragment newInstance() {
        return new SideMenuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.ly_sidebar, container, false);
        addStorySideLay = rootView.findViewById(R.id.addStorySideLay);
        accountSideLay = rootView.findViewById(R.id.accountSideLay);
        logoutbtn = rootView.findViewById(R.id.logout);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","Test");
                logout();
            }
        });


        addStorySideLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddStoryActivity.class));
            }
        });

        accountSideLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){


        }
    }
    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getContext(), LoginActivity.class);
        getActivity().finish();
        startActivity(i);
    }
}





