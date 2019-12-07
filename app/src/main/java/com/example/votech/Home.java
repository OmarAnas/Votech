package com.example.votech;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.backendless.Backendless;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        int userType= Integer.parseInt(Backendless.UserService.CurrentUser().getProperty("UserTypeID").toString());

        if (userType==1) // Instructor
            navView.inflateMenu(R .menu.bottom_nav_menu);
        else if (userType==2) // Student
            navView.inflateMenu(R.menu.bottom_nav_menu_student);

        navView.setOnNavigationItemSelectedListener(this);

        //default fragment for both users
        FirstFragment fragment = new FirstFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId()==R.id.navigation_home)
        {
            FirstFragment fragment = new FirstFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
        }
        else if (menuItem.getItemId()==R.id.navigation_dashboard)
        {
            SecondFragment fragment = new SecondFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
        }
//        else if (menuItem.getItemId()==R.id.navigation_notifications)
//        {
//            ThirdFragment fragment = new ThirdFragment();
//            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
//        }
        else if (menuItem.getItemId()==R.id.navigation_profile)
        {
            profileFragment fragment = new profileFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
        }
        return true;
    }
}
