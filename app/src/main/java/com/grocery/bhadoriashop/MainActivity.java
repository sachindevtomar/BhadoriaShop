package com.grocery.bhadoriashop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grocery.bhadoriashop.Fragments.HomeFragment;
import com.grocery.bhadoriashop.Fragments.LoginFragment;
import com.grocery.bhadoriashop.Fragments.RefundPolicyFragment;
import com.grocery.bhadoriashop.Fragments.ProfileFragment;
import com.grocery.bhadoriashop.Helper.SelectCategoryDialog;
import com.grocery.bhadoriashop.Models.User;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    MenuItem logOutMenuItem, loginMenuItem, updateProfileMenuItem;
    TextView menuCartCountTextView;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRefCart, mRefUser;
    public final int FRAGMENT_HOME = 1, FRAGMENT_B = 2, FRAGMENT_LOGIN = 3, FRAGMENT_UPDATE = 4;
    User currentUserFromDB;
    Button adminGateBtn;

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

        // Open default fragment based on intent extra value
        if(getIntent().getExtras() != null){
            int intentFragment = getIntent().getExtras().getInt("StartWithFragment");
            switch (intentFragment){
                case FRAGMENT_HOME:{
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.container_fragment, new HomeFragment());
                    fragmentTransaction.commit();
                    break;
                }
                case FRAGMENT_B:{
                    break;
                }
                case FRAGMENT_UPDATE:{
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.container_fragment, new ProfileFragment());
                    fragmentTransaction.commit();
                    break;
                }
                case FRAGMENT_LOGIN:{
                    break;
                }
            }
        }
        else{
            //load default fragment
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_fragment, new HomeFragment());
            fragmentTransaction.commit();
        }

        //Show/hide the login/logout menu item from navigation drawer
        logOutMenuItem = navigationView.getMenu().findItem(R.id.logout_menu);
        loginMenuItem = navigationView.getMenu().findItem(R.id.login_menu);
        updateProfileMenuItem = navigationView.getMenu().findItem(R.id.update_profile_menu);
        adminGateBtn = navigationView.findViewById(R.id.admin_gate_navigation_drawer_btn);
        //set FirebaseDatabase references
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRefCart = mFirebaseDatabase.getReference("ProductCart");
        mRefUser = mFirebaseDatabase.getReference("Users");

        addListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(FirebaseAuth.getInstance().getCurrentUser()!= null) {
            //User is logged in
            loginMenuItem.setVisible(false);
            updateProfileMenuItem.setVisible(true);
            logOutMenuItem.setVisible(true);
        }
        else {
            //user is not logged in
            loginMenuItem.setVisible(true);
            updateProfileMenuItem.setVisible(false);
            logOutMenuItem.setVisible(false);
        }

        //get Admin gate button only if any admin logs in
        if(firebaseAuth.getCurrentUser()!=null) {
            mRefUser.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        //Add code to show admin button in navigation drawer
                        currentUserFromDB = snapshot.getValue(User.class);
                        if(currentUserFromDB.isAdmin())
                            adminGateBtn.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        getMenuInflater().inflate(R.menu.user_main_menu, menu);
        //get cart icon with number of items in the cart
        RelativeLayout customCartWithCountLayout = (RelativeLayout) menu.findItem(R.id.product_cart_menu_item).getActionView();
        menuCartCountTextView = (TextView) customCartWithCountLayout.findViewById(R.id.menu_item_cart_count_textview);
        if(firebaseAuth.getCurrentUser()!=null) {
            mRefCart.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        menuCartCountTextView.setText(String.valueOf(snapshot.getChildrenCount()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //add listener on the cart icon
        customCartWithCountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseAuth.getCurrentUser() != null && !menuCartCountTextView.getText().toString().isEmpty() && !menuCartCountTextView.getText().toString().equals("0")) {
                    Intent i = new Intent(getApplicationContext(), UserCartActivity.class);
                    startActivity(i);
                }
                else{
                    Toasty.error(getApplicationContext(), R.string.login_required, Toast.LENGTH_LONG, true).show();
                }
            }
        });
        return true;
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

            case R.id.update_profile_menu :
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new ProfileFragment());
                fragmentTransaction.commit();
                break;

            case R.id.logout_menu :
                FirebaseAuth.getInstance().signOut();
                invalidateOptionsMenu();
                Intent intent = getIntent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("StartWithFragment", new MainActivity().FRAGMENT_HOME);
                startActivity(intent);
                break;

            default:


        }
        return true;
    }

    private void addListeners(){
        adminGateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminTabbedActivity.class);
                startActivity(intent);
            }
        });
    }
}
