package com.example.votech;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


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

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;


    public profileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        queryBuilder = DataQueryBuilder.create();
        dialogBuilder= new AlertDialog.Builder(getActivity() , R.style.MyDialogTheme);

        sharedpreferences= getActivity().getSharedPreferences("LoginPrefs",Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

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
           changeName.setTextColor(Color.WHITE);
           changeName.setHint("New Name");
           changeName.setHintTextColor(Color.parseColor("#646873"));
           TextView title = new TextView(getActivity());
           title.setPadding(20, 30, 20, 30);
           title.setTextSize(20F);
           title.setText("Change Your Name");
           title.setTextColor(Color.WHITE);
           dialogBuilder.setView(changeName)
                   .setCustomTitle(title)
                   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                                user.setProperty("name",changeName.getText().toString().trim());
                                Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                                    public void handleResponse(BackendlessUser user) {
                                        name.setText(user.getProperty("name").toString());
                                    }

                                    public void handleFault(BackendlessFault fault) {
                                        Toast.makeText(getActivity(), fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                       }
                   })
                   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {}
       });
           dialog = dialogBuilder.create();
           dialog.show();
           dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

           changeName.addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               }

               @Override
               public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               }

               @Override
               public void afterTextChanged(Editable editable) {
                   if (TextUtils.isEmpty(editable))
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                   else
                       dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
               }
           });

       }
       else if (view.getId()==R.id.changePassword)
       {
           LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                                                                             ,LinearLayout.LayoutParams.WRAP_CONTENT);
           final EditText oldPass = new EditText(getActivity());

           oldPass.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
           oldPass.setTextColor(Color.WHITE);
           oldPass.setHint("Old Password");
           oldPass.setHintTextColor(Color.parseColor("#646873"));
           oldPass.setLayoutParams(params);

           final EditText newPass = new EditText(getActivity());
           newPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
           newPass.setTextColor(Color.WHITE);
           newPass.setHint("New Password");
           newPass.setHintTextColor(Color.parseColor("#646873"));
           newPass.setLayoutParams(params);

           final EditText ConfirmnewPass = new EditText(getActivity());
           ConfirmnewPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
           ConfirmnewPass.setTextColor(Color.WHITE);
           ConfirmnewPass.setHint("Confirm New Password");
           ConfirmnewPass.setHintTextColor(Color.parseColor("#646873"));
           ConfirmnewPass.setLayoutParams(params);


           LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                   ,LinearLayout.LayoutParams.MATCH_PARENT);
           LinearLayout linearLayout= new LinearLayout(getActivity());
           linearLayout.setLayoutParams(params2);
           linearLayout.addView(oldPass);
           linearLayout.addView(newPass);
           linearLayout.addView(ConfirmnewPass);
           linearLayout.setOrientation(LinearLayout.VERTICAL);

           TextView title = new TextView(getActivity());
           title.setPadding(20, 30, 20, 30);
           title.setTextSize(20F);
           title.setText("Change Your Password");
           title.setTextColor(Color.WHITE);

           dialogBuilder.setView(linearLayout)
            .setCustomTitle(title)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       if(!(MainActivity.UserPassword.equals(oldPass.getText().toString())))
                       {
                            Log.i("pass",oldPass.getText().toString());
                           Toast.makeText(getActivity(), "Entered old password doesn't match your password ", Toast.LENGTH_LONG).show();
                       }
                       else if (!(newPass.getText().toString().equals(ConfirmnewPass.getText().toString())))
                       {
                           Toast.makeText(getActivity(), "New Passwords don't match", Toast.LENGTH_LONG).show();
                       }
                       else if(oldPass.getText().toString().equals("") ||
                               newPass.getText().toString().equals("") || ConfirmnewPass.getText().toString().equals("")) {
                           Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_LONG).show();

                       }
                       else {
                           user.setPassword(newPass.getText().toString().trim());
                           Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                               public void handleResponse(BackendlessUser user) {
                                   MainActivity.UserPassword=newPass.getText().toString().trim();
                                   Toast.makeText(getActivity(), "Password Changed Successfully", Toast.LENGTH_LONG).show();
                               }

                               public void handleFault(BackendlessFault fault) {
                                   Toast.makeText(getActivity(), fault.getMessage(), Toast.LENGTH_LONG).show();
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
       else if (view.getId()==R.id.Logout)
       {
           final CheckBox saveData = new CheckBox(getActivity());
           saveData.setTextColor(Color.WHITE);
           saveData.setText("Save Login Info for next time? ");
           saveData.setGravity(Gravity.CENTER_VERTICAL);

           saveData.setTextSize(15F);
           saveData.setChecked(true);

           TextView title = new TextView(getActivity());
           title.setPadding(20, 30, 20, 30);
           title.setTextSize(20F);
           title.setText("Logout");
           title.setTextColor(Color.WHITE);
           dialogBuilder.setView(saveData)
                   .setCustomTitle(title)
                   .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           if(saveData.isChecked()){
                               editor.putString("email",user.getEmail());
                               editor.putString("password",MainActivity.UserPassword);
                           }
                           else if (!saveData.isChecked())
                           {
                               editor.putString("email","");
                               editor.putString("password","");
                           }
                           editor.commit();
                           Backendless.UserService.logout( new AsyncCallback<Void>()
                           {
                               public void handleResponse( Void response )
                               {
                                   Intent in = new Intent(getActivity(),MainActivity.class);
                                   startActivity(in);
                                   getActivity().finish();
                               }

                               public void handleFault( BackendlessFault fault )
                               {
                                   Toast.makeText(getActivity(), fault.getMessage(), Toast.LENGTH_LONG).show();
                               }
                           });
                       }
                   })
                   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {

                       }
                   });
           dialog = dialogBuilder.create();
           dialog.show();
       }
       else if (view.getId()==R.id.changePic)
       {

       }
    }



}



