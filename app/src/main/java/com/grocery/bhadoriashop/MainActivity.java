package com.grocery.bhadoriashop;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.grocery.bhadoriashop.Fragments.HomeFragment;
import com.grocery.bhadoriashop.Fragments.LoginFragment;
import com.grocery.bhadoriashop.Fragments.RefundPolicyFragment;
import com.grocery.bhadoriashop.Fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    MenuItem logOutMenuItem, loginMenuItem;
    private boolean ShowLoginMenuItem=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.drawer_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        //load default fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, new HomeFragment());
        fragmentTransaction.commit();

        //Show/hide the login/logout menu item from navigation drawer
        logOutMenuItem = navigationView.getMenu().findItem(R.id.logout_menu);
        loginMenuItem = navigationView.getMenu().findItem(R.id.login_menu);
        if(FirebaseAuth.getInstance().getCurrentUser()!= null) {
            ShowLoginMenuItem = false;
        }
        else {
            ShowLoginMenuItem = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        navigationView.getMenu().findItem(R.id.login_menu).setVisible(ShowLoginMenuItem);
        navigationView.getMenu().findItem(R.id.logout_menu).setVisible(!ShowLoginMenuItem);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.home_menu :
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new HomeFragment());
                fragmentTransaction.commit();
                break;

            case R.id.another_menu :
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new RefundPolicyFragment());
                fragmentTransaction.commit();
                break;

            case R.id.login_menu :
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new LoginFragment());
                fragmentTransaction.commit();
                break;

            case R.id.profile_menu :
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new ProfileFragment());
                fragmentTransaction.commit();
                break;

            case R.id.logout_menu :
                FirebaseAuth.getInstance().signOut();
                ShowLoginMenuItem = true;
                invalidateOptionsMenu();
                break;

            default:


        }
        return true;
    }
}
