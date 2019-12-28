package com.example.votech;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import com.ablanco.imageprovider.ImageProvider;
import com.ablanco.imageprovider.ImageSource;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.persistence.DataQueryBuilder;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileFragment extends Fragment implements View.OnClickListener {

    CircleImageView profilePic;
    BackendlessUser backendlessUser;
    TextView name,email,faculty;
    Button changePassBtn,logout;
    DataQueryBuilder queryBuilder;
    ImageView changeName;
    FloatingActionButton changePic;
    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder ;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    Context mContext;

    public profileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        queryBuilder = DataQueryBuilder.create();
        dialogBuilder= new AlertDialog.Builder(mContext , R.style.MyDialogTheme);

        sharedpreferences= mContext.getSharedPreferences("LoginPrefs",Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        backendlessUser =Backendless.UserService.CurrentUser();
        profilePic = view.findViewById(R.id.profilePic);
        name=view.findViewById(R.id.nameText);
        email=view.findViewById(R.id.emailText);
        faculty=view.findViewById(R.id.facultyText);
        changePassBtn=view.findViewById(R.id.changePassword);
        logout=view.findViewById(R.id.Logout);
        changeName=view.findViewById(R.id.changeName);
        changePic=view.findViewById(R.id.changePic);

        changeName.setOnClickListener(this);
        changePic.setOnClickListener(this);
        changePassBtn.setOnClickListener(this);
        logout.setOnClickListener(this);

        Picasso.get().load(backendlessUser.getProperty("picture").toString()).into(profilePic);
        name.setText(backendlessUser.getProperty("name").toString());
        email.setText(backendlessUser.getEmail());
        getUserFaculty();



        return view;
    }





    public void getUserFaculty()
    {
        queryBuilder.setWhereClause("id="+ backendlessUser.getProperty("FacultyID"));
        Backendless.Data.of( Faculty.class ).find( queryBuilder,new AsyncCallback<List<Faculty>>(){

        @Override
        public void handleResponse( List<Faculty> facName )
        {
            faculty.setText(facName.get(0).getName());
        }
        @Override
        public void handleFault( BackendlessFault fault )
        {
            Toast.makeText(mContext, fault.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
    }

    @Override
    public void onClick(View view) {

       if (view.getId()==R.id.changeName)
       {
           final EditText changeName = new EditText(mContext);
           changeName.setTextColor(Color.WHITE);
           changeName.setHint("New Name");
           changeName.setHintTextColor(Color.parseColor("#646873"));
           TextView title = new TextView(mContext);
           title.setPadding(20, 30, 20, 30);
           title.setTextSize(20F);
           title.setText("Change Your Name");
           title.setTextColor(Color.WHITE);
           dialogBuilder.setView(changeName)
                   .setCustomTitle(title)
                   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           String nameoo=changeName.getText().toString().replaceAll("\\s{2,}", " ").trim();
                               if (nameoo.length() < 3 || nameoo.length() > 16)
                               Toast.makeText(mContext, "Name should be more than 3 characters and less than 16 characters", Toast.LENGTH_LONG).show();
                               else if (isSpecialCharacter(nameoo))
                                   Toast.makeText(mContext, "Please only enter A-z 0-9 characters", Toast.LENGTH_LONG).show();
                               else {
                                   backendlessUser.setProperty("name", nameoo);
                                   Backendless.UserService.update(backendlessUser, new AsyncCallback<BackendlessUser>() {
                                       public void handleResponse(BackendlessUser user) {
                                           name.setText(user.getProperty("name").toString());
                                       }


                                       public void handleFault(BackendlessFault fault) {
                                           Toast.makeText(mContext, fault.getMessage(), Toast.LENGTH_SHORT).show();
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
           final EditText oldPass = new EditText(mContext);

           oldPass.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
           oldPass.setTextColor(Color.WHITE);
           oldPass.setHint("Old Password");
           oldPass.setHintTextColor(Color.parseColor("#646873"));
           oldPass.setLayoutParams(params);

           final EditText newPass = new EditText(mContext);
           newPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
           newPass.setTextColor(Color.WHITE);
           newPass.setHint("New Password");
           newPass.setHintTextColor(Color.parseColor("#646873"));
           newPass.setLayoutParams(params);

           final EditText ConfirmnewPass = new EditText(mContext);
           ConfirmnewPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
           ConfirmnewPass.setTextColor(Color.WHITE);
           ConfirmnewPass.setHint("Confirm New Password");
           ConfirmnewPass.setHintTextColor(Color.parseColor("#646873"));
           ConfirmnewPass.setLayoutParams(params);


           LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                   ,LinearLayout.LayoutParams.MATCH_PARENT);
           LinearLayout linearLayout= new LinearLayout(mContext);
           linearLayout.setLayoutParams(params2);
           linearLayout.addView(oldPass);
           linearLayout.addView(newPass);
           linearLayout.addView(ConfirmnewPass);
           linearLayout.setOrientation(LinearLayout.VERTICAL);

           TextView title = new TextView(mContext);
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
                           Toast.makeText(mContext, "Entered old password doesn't match your password ", Toast.LENGTH_LONG).show();
                       }
                       else if (!(newPass.getText().toString().equals(ConfirmnewPass.getText().toString())))
                       {
                           Toast.makeText(mContext, "New Passwords don't match", Toast.LENGTH_LONG).show();
                       }
                       else if(oldPass.getText().toString().equals("") ||
                               newPass.getText().toString().equals("") || ConfirmnewPass.getText().toString().equals("")) {
                           Toast.makeText(mContext, "Fill all fields", Toast.LENGTH_LONG).show();

                       }
                       else if (newPass.getText().toString().length() < 3 || newPass.getText().toString().length() > 16)
                           Toast.makeText(mContext, "Passwords should be more than 3 characters and less than 16 characters", Toast.LENGTH_LONG).show();
                       else if (isSpecialCharacter(newPass.getText().toString()))
                           Toast.makeText(mContext, "Please only enter A-z 0-9 characters", Toast.LENGTH_LONG).show();
                       else {
                           backendlessUser.setPassword(newPass.getText().toString().trim());
                           Backendless.UserService.update(backendlessUser, new AsyncCallback<BackendlessUser>() {
                               public void handleResponse(BackendlessUser user) {
                                   MainActivity.UserPassword=newPass.getText().toString().trim();
                                   Toast.makeText(mContext, "Password Changed Successfully", Toast.LENGTH_LONG).show();
                                   Backendless.UserService.login(user.getEmail(),MainActivity.UserPassword, new AsyncCallback<BackendlessUser>() {
                                       @Override
                                       public void handleResponse(BackendlessUser response) { }

                                       @Override
                                       public void handleFault(BackendlessFault fault) { }
                                   });
                               }

                               public void handleFault(BackendlessFault fault) {
                                   Toast.makeText(mContext, fault.getMessage(), Toast.LENGTH_LONG).show();
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
           final CheckBox saveData = new CheckBox(mContext);
           saveData.setTextColor(Color.WHITE);
           saveData.setText("Save Login Info for next time? ");
           saveData.setGravity(Gravity.CENTER_VERTICAL);

           saveData.setTextSize(15F);
           saveData.setChecked(true);

           TextView title = new TextView(mContext);
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
                               editor.putString("email", backendlessUser.getEmail());
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
                                   Intent in = new Intent(mContext,MainActivity.class);
                                   startActivity(in);
                                   getActivity().finish();
                               }

                               public void handleFault( BackendlessFault fault )
                               {
                                   Toast.makeText(mContext, fault.getMessage(), Toast.LENGTH_LONG).show();
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
           LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                   ,LinearLayout.LayoutParams.WRAP_CONTENT);


           final TextView gallery = new TextView(mContext);
           gallery.setTextColor(Color.WHITE);
           gallery.setText("Choose From Gallery");
           gallery.setGravity(Gravity.CENTER_VERTICAL);
           gallery.setTextSize(15F);
           gallery.setPadding(20, 50, 20, 50);
           gallery.setLayoutParams(params);
           gallery.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                   photoPickerIntent.setType("image/*");
                   startActivityForResult(photoPickerIntent, 0);
                   dialog.dismiss();
               }
           });

           final TextView camera = new TextView(mContext);
           camera.setTextColor(Color.WHITE);
           camera.setText("Take a Picture");
           camera.setGravity(Gravity.CENTER_VERTICAL);
           camera.setTextSize(15F);
           camera.setPadding(20, 50, 20, 50);
           camera.setLayoutParams(params);
           camera.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view)
               {
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   if (mContext.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                           requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
               else
               {
                   try {
                       Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                       startActivityForResult(cameraIntent, 1);

                   }catch (ActivityNotFoundException ae)
                   {
                       Toast.makeText(mContext, "Your SmartPhone seems not", Toast.LENGTH_SHORT).show();
                       Toast.makeText(mContext, "Camera is not supported", Toast.LENGTH_LONG).show();

                   }
               }
           }
           else
           {
           try {
               Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               startActivityForResult(cameraIntent, 1);

           }catch (ActivityNotFoundException ae)
           {
               Toast.makeText(mContext, "Your SmartPhone seems not", Toast.LENGTH_SHORT).show();
               Toast.makeText(mContext, "Camera is not supported", Toast.LENGTH_LONG).show();

           }

           }

           dialog.dismiss();
               }
           });

           final TextView remove = new TextView(mContext);
           remove.setTextColor(Color.WHITE);
           remove.setText("Remove Current Picture");
           remove.setGravity(Gravity.CENTER_VERTICAL);
           remove.setTextSize(15F);
           remove.setPadding(20, 50, 20, 50);
           remove.setLayoutParams(params);
           remove.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   backendlessUser.setProperty("picture", "https://backendlessappcontent.com/B808DB02-5530-3A99-FFE3-39A739A3D500/2E1C56A2-CA5C-4252-968D-DBE4A881AEEF/files/pictures/default-profile-picture1.jpg");
                   Backendless.UserService.update(backendlessUser, new AsyncCallback<BackendlessUser>() {
                       public void handleResponse(BackendlessUser user) {
                           dialog.dismiss();
                           Picasso.get().load(backendlessUser.getProperty("picture").toString()).into(profilePic);
                       }

                       public void handleFault(BackendlessFault fault) {
                           Toast.makeText(mContext, fault.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   });
               }
           });

           LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                   ,LinearLayout.LayoutParams.MATCH_PARENT);
           LinearLayout linearLayout= new LinearLayout(mContext);
           linearLayout.setLayoutParams(params2);
           linearLayout.addView(gallery);
           linearLayout.addView(camera);
           linearLayout.addView(remove);
           linearLayout.setOrientation(LinearLayout.VERTICAL);

           TextView title = new TextView(mContext);
           title.setPadding(20, 30, 20, 30);
           title.setTextSize(20F);
           title.setText("Upload a new Picture");
           title.setTextColor(Color.WHITE);
           dialogBuilder.setView(linearLayout).setCustomTitle(title);
           dialog = dialogBuilder.create();
           dialog.show();



       }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
            }
            else
            {
                Toast.makeText(mContext, "Camera Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         if (requestCode==0 && resultCode==getActivity().RESULT_OK)
        {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = mContext.getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    Backendless.Files.Android.upload(selectedImage, Bitmap.CompressFormat.JPEG, 30
                            , backendlessUser.getUserId()+ Calendar.getInstance().getTimeInMillis(), "pictures", new AsyncCallback<BackendlessFile>() {
                                @Override
                                public void handleResponse(BackendlessFile response) {
                                    profilePic.setImageBitmap(selectedImage);
                                    backendlessUser.setProperty("picture", response.getFileURL());
                                    Backendless.UserService.update(backendlessUser, new AsyncCallback<BackendlessUser>() {
                                        public void handleResponse(BackendlessUser user) {

                                        }

                                        public void handleFault(BackendlessFault fault) {
                                            Toast.makeText(mContext, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Toast.makeText(mContext, fault.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
//
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_LONG).show();
                }
        }
         else if (requestCode==1 && resultCode==getActivity().RESULT_OK)
         {
             final Bitmap takenPic= (Bitmap) data.getExtras().get("data");

             Backendless.Files.Android.upload(takenPic, Bitmap.CompressFormat.JPEG, 30
                     , backendlessUser.getUserId()+ Calendar.getInstance().getTimeInMillis(), "pictures", new AsyncCallback<BackendlessFile>() {
                         @Override
                         public void handleResponse(BackendlessFile response) {
                             profilePic.setImageBitmap(takenPic);
                             backendlessUser.setProperty("picture", response.getFileURL());
                             Backendless.UserService.update(backendlessUser, new AsyncCallback<BackendlessUser>() {
                                 public void handleResponse(BackendlessUser user) {

                                 }

                                 public void handleFault(BackendlessFault fault) {
                                     Toast.makeText(mContext, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                 }
                             });
                         }

                         @Override
                         public void handleFault(BackendlessFault fault) {
                             Toast.makeText(mContext, fault.getMessage(), Toast.LENGTH_LONG).show();
                         }
                     });

         }
//        else
//            Toast.makeText(getActivity(), "You haven't picked Image",Toast.LENGTH_LONG).show();

    }
    public boolean isSpecialCharacter(String s) {

        Pattern p = Pattern.compile("[^A-Za-z0-9. ]");
        Matcher m = p.matcher(s);

        boolean b = m.find();
        if (b)
            return true;
        else
            return false;

    }
}



