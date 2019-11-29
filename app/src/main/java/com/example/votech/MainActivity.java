package com.example.votech;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class MainActivity extends AppCompatActivity implements AsyncCallback<BackendlessUser> {
     EditText email,password;
     TextView signup;
     String text = "First time here? Sign Up.";
     SpannableString spannableString = new SpannableString(text);
     boolean isNewActivity=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=findViewById(R.id.name);
        password=findViewById(R.id.password2);
        signup=findViewById(R.id.signUp);

        spannableString.setSpan(clickableSpan, 17,24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signup.setText(spannableString);
        signup.setMovementMethod(LinkMovementMethod.getInstance());
        signup.setHighlightColor(Color.TRANSPARENT);

        Backendless.initApp(this,"B808DB02-5530-3A99-FFE3-39A739A3D500","BB1EC234-5D1B-4738-8B61-4471397180A7");

       if(Backendless.UserService.CurrentUser() != null )
       {
           Intent in=new Intent(this,HomePage.class);
           startActivity(in);
           this.finish();
       }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(Backendless.UserService.CurrentUser() != null )
        {
            Intent in=new Intent(this,HomePage.class);
            startActivity(in);
            this.finish();
        }
    }

    ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            Intent in=new Intent(MainActivity.this,Register.class);
            startActivity(in);
            MainActivity.this.finish();
        }
    };


    public void Login(View view) {
        String mail=email.getText().toString();
        String pass=password.getText().toString();
        Backendless.UserService.login(mail,pass,this,true);
    }

    @Override
    public void handleResponse(BackendlessUser response) {
        if(isNewActivity)
        {
            isNewActivity=false;
            Intent in=new Intent(this,HomePage.class);
//        Toast.makeText(this, response.getProperty("id").toString(), Toast.LENGTH_LONG).show();
            startActivity(in);
            this.finish();
        }

    }

    @Override
    public void handleFault(BackendlessFault fault) {
        if (fault.getCode().equals("3003"))
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show();
        else if(fault.getCode().equals("3006"))
            Toast.makeText(this, "Email or Password is empty", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,fault.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
