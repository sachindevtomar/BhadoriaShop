package com.grocery.bhadoriashop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.grocery.bhadoriashop.Adapter.UserProductListViewHolder;
import com.grocery.bhadoriashop.Helper.AddCategoryAdminDialog;
import com.grocery.bhadoriashop.Helper.SelectCategoryDialog;
import com.grocery.bhadoriashop.Models.AdminProductList;

import es.dmoral.toasty.Toasty;

public class ShowAllProductActivity extends AppCompatActivity implements SelectCategoryDialog.CategoryNameDialogListener {
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    String currentSelectedCategoryFilter = "";
    Toolbar myToolbar;

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

        //find the search menu item
        MenuItem searchMenuItem = menu.findItem(R.id.product_search_menu_item);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Filter as you type
                //firebaseSearch(newText);
                Log.d("searchquery","testing");
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
