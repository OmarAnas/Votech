package com.example.votech;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class profileFragment extends Fragment implements View.OnClickListener {

    CircleImageView profilePic;
    BackendlessUser user;
    TextView name,email,faculty;
    Button changePass,logout;
    DataQueryBuilder queryBuilder;
    ImageView changeName;
    FloatingActionButton changePic;
    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder ;

    public profileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        queryBuilder = DataQueryBuilder.create();
        dialogBuilder= new AlertDialog.Builder(getActivity());

        user=Backendless.UserService.CurrentUser();
        profilePic = view.findViewById(R.id.profilePic);
        name=view.findViewById(R.id.nameText);
        email=view.findViewById(R.id.emailText);
        faculty=view.findViewById(R.id.facultyText);
        changePass=view.findViewById(R.id.changePassword);
        logout=view.findViewById(R.id.Logout);
        changeName=view.findViewById(R.id.changeName);
        changePic=view.findViewById(R.id.changePic);

        changeName.setOnClickListener(this);
        changePic.setOnClickListener(this);
        changePass.setOnClickListener(this);
        logout.setOnClickListener(this);

        Picasso.get().load(user.getProperty("picture").toString()).into(profilePic);
        name.setText(user.getProperty("name").toString());
        email.setText(user.getEmail());
        getUserFaculty();


        return view;
    }





    public void getUserFaculty()
    {
        queryBuilder.setWhereClause("id="+user.getProperty("FacultyID"));
        Backendless.Data.of( Faculty.class ).find( queryBuilder,new AsyncCallback<List<Faculty>>(){

        @Override
        public void handleResponse( List<Faculty> facName )
        {
            faculty.setText(facName.get(0).getName());
        }
        @Override
        public void handleFault( BackendlessFault fault )
        {
            Toast.makeText(getActivity(), fault.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
    }

    @Override
    public void onClick(View view) {

       if (view.getId()==R.id.changeName)
       {
           final EditText changeName = new EditText(getActivity());
           dialogBuilder.setView(changeName)
                   .setTitle("Change Your Name")
                   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                            if(changeName.getText().toString().equals(""))
                                Toast.makeText(getActivity(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                            else {
                                user.setProperty("name",changeName.getText().toString());
                                Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                                    public void handleResponse(BackendlessUser user) {
                                        name.setText(user.getProperty("name").toString());
                                    }

                                    public void handleFault(BackendlessFault fault) {
                                        Toast.makeText(getActivity(), fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                       }
                   })
                   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {}
       });
         dialog = dialogBuilder.create();

       dialog.show();
       }
       else if (view.getId()==R.id.changePassword)
           Toast.makeText(getActivity(), "pass", Toast.LENGTH_SHORT).show();
       else if (view.getId()==R.id.changePic)
           Toast.makeText(getActivity(), "pic", Toast.LENGTH_SHORT).show();
       else if (view.getId()==R.id.Logout)
           Toast.makeText(getActivity(), "logout", Toast.LENGTH_SHORT).show();
    }
}
