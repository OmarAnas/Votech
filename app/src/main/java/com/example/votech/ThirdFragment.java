package com.example.votech;


import android.app.AlertDialog;
import android.content.Context;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThirdFragment extends Fragment implements View.OnClickListener{
    Button addgb;
    ListView list;
    DataQueryBuilder queryBuilder;
    BackendlessUser user;
    EditText groupname;
    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder ;
    Context mContext;
    int instructorFacultyID ;

    public ThirdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
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
        instructorFacultyID=Integer.parseInt(user.getProperty("FacultyID").toString());
        dialogBuilder= new AlertDialog.Builder(mContext , R.style.MyDialogTheme);

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
        if (groupname.getText().toString().trim().equals(""))
            Toast.makeText(mContext, "Group Name is missing", Toast.LENGTH_LONG).show();
        else if (groupname.getText().toString().replaceAll("\\s{2,}", " ").trim().length() < 3
                || groupname.getText().toString().replaceAll("\\s{2,}", " ").trim().length() > 16)
            Toast.makeText(mContext, "Group Name should be more than 3 characters and less than 16 characters", Toast.LENGTH_LONG).show();
        else if (isSpecialCharacter(groupname.getText().toString().replaceAll("\\s{2,}", " ").trim()))
            Toast.makeText(mContext, "Please only enter A-z 0-9 _.- characters", Toast.LENGTH_LONG).show();
        else {
            TextView question = new TextView(mContext);
            question.setText("Are you sure you want to add group '"+groupname.getText().toString().replaceAll("\\s{2,}", " ").trim()+"' ?");
            question.setTextColor(Color.WHITE);
            question.setPadding(50, 30, 20, 30);

            TextView title = new TextView(mContext);
            title.setPadding(20, 30, 20, 30);
            title.setTextSize(20F);
            title.setText("Confirm group");
            title.setTextColor(Color.WHITE);
            dialogBuilder.setView(question)
                    .setCustomTitle(title)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setPositiveButton("Yes" , new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                              addgb.setClickable(false);
                            Backendless.Data.of(groups.class).find(queryBuilder.setProperties("Max(id) as id"),new AsyncCallback<List<groups>>() {
                                @Override
                                public void handleResponse(List<groups> response) {
                                    groups g = new groups();
                                    g.setName(groupname.getText().toString().replaceAll("\\s{2,}", " ").trim());
                                    g.setFacultyID(instructorFacultyID);
                                    Log.i("object",response.get(0).toString());
                                    g.setId(response.get(0).getId()+1);
                                    Backendless.Persistence.save( g, new AsyncCallback<groups>() {
                                        @Override
                                        public void handleResponse(groups response) {
                                            Toast.makeText(mContext,"Group Added",Toast.LENGTH_LONG).show();
                                            groupname.setText("");
                                            addgb.setClickable(true);
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            if(fault.getCode().equals("1155"))
                                                Toast.makeText(mContext, "Group Already exists", Toast.LENGTH_LONG).show();
                                            else
                                             Toast.makeText(mContext,fault.getMessage(),Toast.LENGTH_LONG).show();
                                            addgb.setClickable(true);
                                        }
                                    });

                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Toast.makeText(mContext,fault.getMessage(),Toast.LENGTH_LONG).show();

                                }

                            });

                        }


                    });
            dialog = dialogBuilder.create();
            dialog.show();
        }

    }
    public boolean isSpecialCharacter(String s) {

        Pattern p = Pattern.compile("[^A-Za-z0-9 _.\\-]");
        Matcher m = p.matcher(s);

        boolean b = m.find();
        if (b)
            return true;
        else
            return false;

    }


}