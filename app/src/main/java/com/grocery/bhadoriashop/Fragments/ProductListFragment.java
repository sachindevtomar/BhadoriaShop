package com.grocery.bhadoriashop.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.grocery.bhadoriashop.Adapter.AdminProductListViewHolder;
import com.grocery.bhadoriashop.Adapter.CardAdapterAdmin;
import com.grocery.bhadoriashop.Models.AdminProductList;
import com.grocery.bhadoriashop.Models.Planet;
import com.grocery.bhadoriashop.R;

import java.util.ArrayList;

public class ProductListFragment extends Fragment {
    private RecyclerView recyclerView;
    private CardAdapterAdmin adapter;
    private ArrayList<Planet> planetArrayList;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);
        initView(rootView);

        return rootView;
    }
    private void initView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        planetArrayList = new ArrayList<>();
//        adapter = new CardAdapterAdmin(getActivity(), planetArrayList);
        recyclerView.setAdapter(adapter);
        // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        createListData();
        //send Query to FirebaseDatabase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Products");
    }

    //search data
    private void firebaseSearch(String searchText) {

        //convert string entered in SearchView to lowercase
        String query = searchText.toLowerCase();

        Query firebaseSearchQuery = mRef.orderByChild("search").startAt(query).endAt(query + "\uf8ff");

        FirebaseRecyclerOptions<AdminProductList> options =
                new FirebaseRecyclerOptions.Builder<AdminProductList>()
                        .setQuery(firebaseSearchQuery, AdminProductList.class)
                        .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AdminProductList, AdminProductListViewHolder>(options) {
            @Override
            public AdminProductListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_admin_productlist, parent, false);

                return new AdminProductListViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(AdminProductListViewHolder holder, int position, AdminProductList model) {
                // Bind the image_details object to the BlogViewHolder
                // ...
            }
        };

        //set adapter to recyclerview
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("TAG","onStart 1");
        FirebaseRecyclerOptions<AdminProductList> options =
                new FirebaseRecyclerOptions.Builder<AdminProductList>()
                        .setQuery(mRef, AdminProductList.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AdminProductList, AdminProductListViewHolder>(options) {
            @Override
            public AdminProductListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_admin_productlist, parent, false);

                return new AdminProductListViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(AdminProductListViewHolder holder, int position, AdminProductList model) {
                // Bind the image_details object to the BlogViewHolder
                holder.setDetails(getContext(), model.getProductName(), model.getProductCategory(), model.getProductSubCategory(),
                        model.getProductBrand(), model.getProductImageURL(), model.getMeasureIn(), model.getTotalWeightIn(), model.getItemWeightIn(), model.getMRPPricePerUnit(), model.getSellingPricePerUnit(), model.getTotalWeight(), model.getItemWeight(), model.getItemCount());
                Log.d("TAG","onStart position: "+ position);
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

    private void createListData() {
        Planet planet = new Planet("Earth", 150, 10, 12750);
        planetArrayList.add(planet);
        planet = new Planet("Jupiter", 778, 26, 143000);
        planetArrayList.add(planet);
        planet = new Planet("Mars", 228, 4, 6800);
        planetArrayList.add(planet);
        planet = new Planet("Pluto", 5900, 1, 2320);
        planetArrayList.add(planet);
        planet = new Planet("Venus", 108, 9, 12750);
        planetArrayList.add(planet);
        planet = new Planet("Saturn", 1429, 11, 120000);
        planetArrayList.add(planet);
        planet = new Planet("Mercury", 58, 4, 4900);
        planetArrayList.add(planet);
        planet = new Planet("Neptune", 4500, 12, 50500);
        planetArrayList.add(planet);
        planet = new Planet("Uranus", 2870, 9, 52400);
        planetArrayList.add(planet);
        adapter.notifyDataSetChanged();
    }
}
