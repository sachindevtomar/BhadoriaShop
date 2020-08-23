package com.grocery.bhadoriashop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.grocery.bhadoriashop.Adapter.UserProductListViewHolder;
import com.grocery.bhadoriashop.Helper.AddCategoryAdminDialog;
import com.grocery.bhadoriashop.Helper.SelectCategoryDialog;
import com.grocery.bhadoriashop.Models.AdminProductList;

import es.dmoral.toasty.Toasty;

public class ShowAllProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_product);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.show_all_product_toolbar);
        setSupportActionBar(myToolbar);
        initView();
    }


    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.productlist_user_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        //send Query to FirebaseDatabase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Products");
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.product_filter_menu, menu);
        //set onClickListener on that actionLayout to receive callback
        menu.findItem(R.id.product_filter_menu_item).getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.success(getApplicationContext(), "Clicked worked", Toast.LENGTH_LONG, true).show();
                FragmentManager fm = getSupportFragmentManager();
                SelectCategoryDialog dialogFragment=new SelectCategoryDialog(getParent());
                dialogFragment.show(fm, "dialog_fragment_select_category");
            }
        });
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        final FirebaseRecyclerOptions<AdminProductList> options =
                new FirebaseRecyclerOptions.Builder<AdminProductList>()
                        .setQuery(mRef, AdminProductList.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AdminProductList, UserProductListViewHolder>(options) {
            @Override
            public UserProductListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_user_productlist, parent, false);

                return new UserProductListViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(UserProductListViewHolder holder, int position, AdminProductList model) {
                // Bind the image_details object to the BlogViewHolder
                holder.setDetails(getApplicationContext(), model.getProductName(), model.getProductCategory(), model.getProductSubCategory(),
                        model.getProductBrand(), model.getProductImageURL(), model.getMeasureIn(), model.getTotalWeightIn(), model.getItemWeightIn(), model.getMRPPricePerUnit(), model.getSellingPricePerUnit(), model.getTotalWeight(), model.getItemWeight(), model.getItemCount());

            }
        };

        //set adapter to recyclerview
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
}
