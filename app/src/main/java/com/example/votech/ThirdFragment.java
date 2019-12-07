package com.example.votech;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class ThirdFragment extends Fragment implements View.OnClickListener{
    Button addgb;
    ListView list;
    DataQueryBuilder queryBuilder;
    BackendlessUser user;
    EditText groupname;
    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder ;
    public ThirdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        queryBuilder = DataQueryBuilder.create();

        View view= inflater.inflate(R.layout.fragment_third, container, false);
        addgb = view.findViewById(R.id.addg);
        addgb.setOnClickListener((View.OnClickListener) this);
        groupname = view.findViewById(R.id.group);
        //list = view.findViewById(R.id.Studentlist);
        user=Backendless.UserService.CurrentUser();
        dialogBuilder= new AlertDialog.Builder(getActivity() , R.style.MyDialogTheme);

       //getstudents();
        return view;
    }

/*
    public void getstudents() {
        queryBuilder.setWhereClause("FacultyID=" + user.getProperty("FacultyID"));
        ArrayList<String> names = new ArrayList<>();
        Backendless.Data.of(BackendlessUser.class).find(queryBuilder, new AsyncCallback<List<BackendlessUser>>() {
            @Override
            public void handleResponse(List<BackendlessUser> users) {
                for (int i = 0; i < users.size(); i++) {
                    Log.i("emails", "Email - " + users.get(i).getEmail());
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
*/
    @Override
    public void onClick(View view) {
        if (groupname.getText().toString().equals(""))
            Toast.makeText(getActivity(), "group name is missing", Toast.LENGTH_LONG).show();
        else {
            TextView question = new TextView(getActivity());
            question.setText("Are you sure you ?");
            question.setTextColor(Color.WHITE);
            question.setPadding(50, 30, 20, 30);

            TextView title = new TextView(getActivity());
            title.setPadding(20, 30, 20, 30);
            title.setTextSize(20F);
            title.setText("Confirm group");
            title.setTextColor(Color.WHITE);
            dialogBuilder.setView(question)
                    .setCustomTitle(title)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setPositiveButton("Yes" , new DialogInterface.OnClickListener(){


                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Backendless.Data.of(groups.class).getObjectCount(new AsyncCallback<Integer>() {
                                @Override
                                public void handleResponse(Integer response) {
                                    groups g = new groups();
                                    g.setName(groupname.getText().toString());
                                    g.setFacultyID(3);
                                    g.setId(response+1);
                                    Backendless.Persistence.save( g, new AsyncCallback<groups>() {
                                        @Override
                                        public void handleResponse(groups response) {

                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                         Toast.makeText(getActivity(),fault.getMessage(),Toast.LENGTH_LONG);
                                        }
                                    });

                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Toast.makeText(getActivity(),fault.getMessage(),Toast.LENGTH_LONG);

                                }

                            });

                        }


                    });
        }
        dialog = dialogBuilder.create();
        dialog.show();
    }
}