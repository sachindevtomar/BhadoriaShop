package com.grocery.bhadoriashop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.grocery.bhadoriashop.Adapter.UserProductListViewHolder;
import com.grocery.bhadoriashop.Helper.AddCategoryAdminDialog;
import com.grocery.bhadoriashop.Helper.SelectCategoryDialog;
import com.grocery.bhadoriashop.Models.AdminProductList;

import es.dmoral.toasty.Toasty;

public class ShowAllProductActivity extends AppCompatActivity implements SelectCategoryDialog.CategoryNameDialogListener {
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef, mRefCart;
    String currentSelectedCategoryFilter = "";
    Toolbar myToolbar;
    TextView menuCartCountTextView;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_product);
        initView();
    }


    private void initView() {
        myToolbar = (Toolbar) findViewById(R.id.show_all_product_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.productlist_user_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        //send Query to FirebaseDatabase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Products");
        mRefCart = mFirebaseDatabase.getReference("ProductCart");
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.product_filter_menu, menu);
        //find the filter menu item
        MenuItem filterMenuItem = menu.findItem(R.id.product_filter_menu_item);
        //set onClickListener on that actionLayout to receive callback
        filterMenuItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                SelectCategoryDialog dialogFragment=new SelectCategoryDialog(currentSelectedCategoryFilter);
                dialogFragment.show(fm, "dialog_fragment_select_category");
            }
        });

        //get cart icon with number of items in the cart
        RelativeLayout customCartWithCountLayout = (RelativeLayout) menu.findItem(R.id.product_cart_menu_item).getActionView();
        menuCartCountTextView = (TextView) customCartWithCountLayout.findViewById(R.id.menu_item_cart_count_textview);
        if(firebaseAuth.getCurrentUser() != null){
            mRefCart.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        menuCartCountTextView.setText(String.valueOf(snapshot.getChildrenCount()));
                    }
                    else{
                        menuCartCountTextView.setText("0");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //add listener on the cart icon
            customCartWithCountLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(firebaseAuth.getCurrentUser() != null && !menuCartCountTextView.getText().toString().isEmpty() && !menuCartCountTextView.getText().toString().equals("0")) {
                        Intent i = new Intent(getApplicationContext(), UserCartActivity.class);
                        startActivity(i);
                    }
                    else if(menuCartCountTextView.getText().toString().equals("0")){
                        Toasty.info(getApplicationContext(), R.string.empty_cart, Toast.LENGTH_LONG, true).show();
                    }
                    else{
                        Toasty.error(getApplicationContext(), R.string.login_required, Toast.LENGTH_LONG, true).show();
                    }
                }
            });
        }

        //find the search menu item
        MenuItem searchMenuItem = menu.findItem(R.id.product_search_menu_item);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //will add code here;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Temporary solution to manage product category filter
                currentSelectedCategoryFilter = "";
                filterProductsBasedOnSearchText(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onFinishEditDialog(String selectedCategory) {
        //Toasty.success(getApplicationContext(), "Message From Dialog: "+selectedCategory, Toast.LENGTH_LONG, true).show();
        currentSelectedCategoryFilter = selectedCategory;
        if(currentSelectedCategoryFilter==null || currentSelectedCategoryFilter.isEmpty())
            myToolbar.setTitle("All");
        else
            myToolbar.setTitle(currentSelectedCategoryFilter);
        setSupportActionBar(myToolbar);
        filterProductsBasedOnCategory(currentSelectedCategoryFilter);
    }

    private void filterProductsBasedOnCategory(String selectedcategory){
        FirebaseRecyclerOptions<AdminProductList> options ;
        if(selectedcategory.length()>0){
            Query queryCategoryFilter = mRef.orderByChild("ProductCategory").equalTo(selectedcategory);
            options = new FirebaseRecyclerOptions.Builder<AdminProductList>()
                            .setQuery(queryCategoryFilter, AdminProductList.class)
                            .build();
        }
        else {
           options = new FirebaseRecyclerOptions.Builder<AdminProductList>()
                            .setQuery(mRef, AdminProductList.class)
                            .build();
        }
        filterProducts(options);
    }

    private void filterProductsBasedOnSearchText(String query){
        if(query != null && query.length()>0){
            char[] letters=query.toCharArray();
            String firstLetter = String.valueOf(letters[0]).toUpperCase();
            String remainingLetters = query.substring(1);
            query=firstLetter+remainingLetters;
        }

        Query firebaseSearchQuery = mRef.orderByChild("ProductName").startAt(query).endAt(query + "\uf8ff");
        FirebaseRecyclerOptions<AdminProductList> options =
                new FirebaseRecyclerOptions.Builder<AdminProductList>()
                        .setQuery(firebaseSearchQuery, AdminProductList.class)
                        .build();
        filterProducts(options);
    }

    private void filterProducts(FirebaseRecyclerOptions<AdminProductList> options){
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
                        model.getProductBrand(), model.getProductImageURL(), model.getMeasureIn(), model.getTotalWeightIn(), model.getItemWeightIn(), model.getMRPPricePerUnit(), model.getSellingPricePerUnit(), model.getTotalWeight(), model.getItemWeight(), model.getItemCount(),firebaseRecyclerAdapter.getRef(position).getKey());

            }
        };

        //set adapter to recyclerview
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }
    @Override
    public void onStart() {
        super.onStart();
        if(getIntent().getStringExtra("filterCategory") !=null) {
            currentSelectedCategoryFilter =getIntent().getStringExtra("filterCategory");
            myToolbar.setTitle(currentSelectedCategoryFilter);
            filterProductsBasedOnCategory(currentSelectedCategoryFilter);
        }
        else {
            myToolbar.setTitle("All");
            filterProductsBasedOnCategory("");
        }
        setSupportActionBar(myToolbar);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
}
