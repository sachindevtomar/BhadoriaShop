package com.grocery.bhadoriashop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.grocery.bhadoriashop.Adapter.AdminCategoryListViewHolder;
import com.grocery.bhadoriashop.Adapter.UserProductListViewHolder;
import com.grocery.bhadoriashop.Models.AdminProductList;
import com.grocery.bhadoriashop.Models.ProductCategory;

public class CategoryListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        initView();
    }

    @Override
    public void onStart() {
        super.onStart();

        final FirebaseRecyclerOptions<ProductCategory> options =
                new FirebaseRecyclerOptions.Builder<ProductCategory>()
                        .setQuery(mRef, ProductCategory.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ProductCategory, AdminCategoryListViewHolder>(options) {
            @Override
            public AdminCategoryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_admin_categorylist, parent, false);

                return new AdminCategoryListViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(AdminCategoryListViewHolder holder, int position, ProductCategory model) {
                // Bind the image_details object to the BlogViewHolder
                holder.setDetails(model.getCategoryName(), model.getCategoryImageURL());

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

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.categorylist_admin_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //send Query to FirebaseDatabase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("ProductCategories");
    }
}
