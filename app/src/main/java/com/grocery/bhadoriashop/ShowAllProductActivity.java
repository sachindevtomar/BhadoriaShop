package com.grocery.bhadoriashop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.grocery.bhadoriashop.Adapter.UserProductListViewHolder;
import com.grocery.bhadoriashop.Models.AdminProductList;

public class ShowAllProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_product);
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
                Log.d("GridView","Working 4");
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
