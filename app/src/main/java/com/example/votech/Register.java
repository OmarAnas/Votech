package com.example.votech;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.Persistence;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity implements AsyncCallback<BackendlessUser>  {
    EditText name, email, password, confirmpassword;
    Spinner facultySpinner;
    DataQueryBuilder dataQuery = DataQueryBuilder.create();
    boolean isRegistered=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.password2);
        facultySpinner = findViewById(R.id.faculty);

        getFaculties();

        facultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view;
                if(i==0)
                    tv.setTextColor(Color.parseColor("#646873"));
                else
                tv.setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void register(View view) {
        if (name.getText().toString().equals(""))
            Toast.makeText(this, "Name is required", Toast.LENGTH_LONG).show();
        else if (password.getText().toString().equals("") || confirmpassword.getText().toString().equals("") )
            Toast.makeText(Register.this, "Passwords cannot be empty", Toast.LENGTH_LONG).show();
        else if (!(password.getText().toString().equals(confirmpassword.getText().toString())))
            Toast.makeText(Register.this, "Passwords don't match", Toast.LENGTH_LONG).show();
        else if (facultySpinner.getSelectedItemPosition()==0)
            Toast.makeText(Register.this, "Please Select your Faculty", Toast.LENGTH_LONG).show();
        else
        Backendless.Data.of(BackendlessUser.class).getObjectCount(new AsyncCallback<Integer>()
        {
            @Override
            public void handleResponse(Integer count) {
                BackendlessUser user = new BackendlessUser();

                user.setPassword(password.getText().toString());
                user.setEmail(email.getText().toString());
                user.setProperty("name", name.getText().toString());
                user.setProperty("FacultyID", facultySpinner.getSelectedItemPosition());
                user.setProperty("id",count+1);
                Backendless.UserService.register(user, Register.this);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });


    }

    @Override
    public void handleResponse(BackendlessUser response) {
        isRegistered=true;
        Toast.makeText(this, "Registered Successfully", Toast.LENGTH_LONG).show();
        Intent in = new Intent(this, MainActivity.class);
        startActivity(in);
        this.finish();
    }

    @Override
    public void handleFault(BackendlessFault fault) {
        if(!isRegistered) {
            if (fault.getCode().equals("3011"))
                Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_LONG).show();
            else if (fault.getCode().equals("3013"))
                Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_LONG).show();
            else if (fault.getCode().equals("3033"))
                Toast.makeText(this, "User already exists", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, fault.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent in = new Intent(this, MainActivity.class);
                startActivity(in);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        Intent in = new Intent(this, MainActivity.class);
        startActivity(in);
        this.finish();
    }
    public void getFaculties() {

        Backendless.Data.of(Faculty.class).find(dataQuery.setSortBy("id"), new AsyncCallback<List<Faculty>>() {
            ArrayList<String> names = new ArrayList<>();
            @Override
            public void handleResponse(List<Faculty> faculty) {
                 names.add("Select Your Faculty...");
                for (int i = 0; i < faculty.size(); i++)
                    names.add(faculty.get(i).getName());

                ArrayAdapter adapter = new ArrayAdapter(Register.this, R.layout.spinner_item, names){
                    @Override
                    public boolean isEnabled(int position){
                        if(position == 0)
                            return false;
                        else
                            return true;
                    }
                    @Override
                    public View getDropDownView(int position, View convertView,ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if(position == 0){
                            // Set the hint text color gray
                            tv.setTextColor(Color.parseColor("#646873"));
                        }
                        else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                facultySpinner.setAdapter(adapter);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(Register.this, "" + fault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    }
