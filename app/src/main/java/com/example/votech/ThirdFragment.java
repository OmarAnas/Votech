package com.example.votech;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class ThirdFragment extends Fragment {
    Button addgroup;
    ListView list;
    DataQueryBuilder queryBuilder;
    BackendlessUser user;

    public ThirdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        queryBuilder = DataQueryBuilder.create();

        View view= inflater.inflate(R.layout.fragment_third, container, false);
        addgroup = view.findViewById(R.id.addg);
        list = view.findViewById(R.id.Studentlist);
        user=Backendless.UserService.CurrentUser();

        getstudents();
        return view;
    }

    public void getstudents() {
        queryBuilder.setWhereClause("FacultyID=" + user.getProperty("FacultyID"));
        ArrayList<String> names = new ArrayList<>();
        Backendless.Data.of(BackendlessUser.class).find(queryBuilder,new AsyncCallback<List<BackendlessUser>>(){
            @Override
            public void handleResponse(List<BackendlessUser> users) {
                for (int i = 0; i < users.size(); i++) {
                    Log.i("emails", "Email - " +users.get(i).getEmail());
                    Log.i("IDS", "User ID - " + users.get(i).getUserId());
                    Log.i("Phones", "Phone Number - " + users.get(i).getProperty("phoneNumber"));
                    Log.i("Border", "============================");
             //       names.add()
                }
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("ERROR", fault.getMessage());
            }
        });
    }
}