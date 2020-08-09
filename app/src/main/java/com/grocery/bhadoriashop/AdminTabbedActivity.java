package com.grocery.bhadoriashop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.grocery.bhadoriashop.Fragments.AddProductFragment;
import com.grocery.bhadoriashop.Fragments.ProductListFragment;
import com.grocery.bhadoriashop.Helper.AddCategoryAdminDialog;

import es.dmoral.toasty.Toasty;

public class AdminTabbedActivity extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_EXTERNAL_READ_STORAGE_REQUEST_CODE = 200;
    private static final int MY_EXTERNAL_WRITE_STORAGE_REQUEST_CODE = 300;
    private final String LOG_TAG = AdminTabbedActivity.class.getSimpleName();

    // Titles of the individual pages (displayed in tabs)
    private final String[] PAGE_TITLES = new String[] {
            "Add Product",
            "Product List"
    };

    // The fragments that are used as the individual pages
    private final Fragment[] PAGES = new Fragment[] {
            new AddProductFragment(),
            new ProductListFragment()
    };

    // The ViewPager is responsible for sliding pages (fragments) in and out upon user input
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tabbed);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.admin_toolbar);
//        setSupportActionBar(toolbar);

        // Set the Toolbar as the activity's app bar (instead of the default ActionBar)
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // Connect the ViewPager to our custom PagerAdapter. The PagerAdapter supplies the pages
        // (fragments) to the ViewPager, which the ViewPager needs to display.
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(new AdminTabbedActivity.MyPagerAdapter(getSupportFragmentManager()));

        // Connect the tabs with the ViewPager (the setupWithViewPager method does this for us in
        // both directions, i.e. when a new tab is selected, the ViewPager switches to this page,
        // and when the ViewPager switches to a new page, the corresponding tab is selected)
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_EXTERNAL_READ_STORAGE_REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_EXTERNAL_WRITE_STORAGE_REQUEST_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.add_category_menu_item:
                FragmentManager fm = getSupportFragmentManager();
                AddCategoryAdminDialog dialogFragment=new AddCategoryAdminDialog(this);
                dialogFragment.show(fm, "dialog_fragment_admin");

                return true;
            case R.id.show_category_menu_item:
                Toasty.success(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG, true).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* PagerAdapter for supplying the ViewPager with the pages (fragments) to display. */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return PAGES[position];
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == MY_EXTERNAL_READ_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "External Storage read permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "External Storage read permission denied", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == MY_EXTERNAL_WRITE_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "External Storage write permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "External Storage writepermission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}

