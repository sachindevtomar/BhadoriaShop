package com.grocery.bhadoriashop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.grocery.bhadoriashop.Adapter.CartProductListViewHolder;
import com.grocery.bhadoriashop.Adapter.UserProductListViewHolder;
import com.grocery.bhadoriashop.Models.AdminProductList;
import com.grocery.bhadoriashop.Models.CartProduct;

public class UserCartActivity extends AppCompatActivity implements OnCustomDeleteCartItemListener{
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    TextView totalAmountTextView, totalPayableAmountTextView, deliveryAmountTextView;
    double totalCartAmount = 0;
    Context currentActivityCtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);
        initView();
    }

    private void initView() {
        //initialize views
        totalAmountTextView = (TextView) findViewById(R.id.total_cart_amount_textview);
        totalPayableAmountTextView = (TextView) findViewById(R.id.total_payable_cart_amount_textview);
        deliveryAmountTextView = (TextView) findViewById(R.id.delivery_cart_amount_textview);
        recyclerView = (RecyclerView) findViewById(R.id.cart_product_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //send Query to FirebaseDatabase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //Need to change hardcoded UserID with logged in userID
        mRef = mFirebaseDatabase.getReference("ProductCart").child("oCp0hwMIhUhUmVHyYnurEFLm03q2");
    }

    @Override
    public void onStart() {
        super.onStart();
        updateTotalAmountOnUIAfterCartModification();
        fetchDataForRecyclerView();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    @Override
    public void onCartItemDeleted() {
        updateTotalAmountOnUIAfterCartModification();
    }

    private void fetchDataForRecyclerView(){
        currentActivityCtx = this;
        FirebaseRecyclerOptions<CartProduct> options ;
        options = new FirebaseRecyclerOptions.Builder<CartProduct>()
                .setQuery(mRef, CartProduct.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CartProduct, CartProductListViewHolder>(options) {
            @Override
            public CartProductListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_cart_productlist, parent, false);

                return new CartProductListViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(CartProductListViewHolder holder, int position, CartProduct model) {
                // Bind the image_details object to the BlogViewHolder
                holder.setDetails(getApplicationContext(), model.getProductName(), model.getProductImageURL(), model.getProductSellingPrice(), model.getQuantity(), model.getQuantityIn(), model.getItemCount(), firebaseRecyclerAdapter.getRef(position).getKey(), currentActivityCtx);
            }
        };

        //set adapter to recyclerview
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    private void updateTotalAmountOnUIAfterCartModification(){
        totalCartAmount = 0;
        totalPayableAmountTextView.setText("Rs."+String.valueOf(totalCartAmount + 50)+"/-");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot childDataSnapShot : snapshot.getChildren()){
                        CartProduct cartItem = childDataSnapShot.getValue(CartProduct.class);
                        totalCartAmount += (cartItem.getProductSellingPrice()*cartItem.getItemCount());
                    }
                    //set TextViews values
                    totalAmountTextView.setText("Rs."+String.valueOf(totalCartAmount));
                    deliveryAmountTextView.setText("Rs."+String.valueOf(50));
                    totalPayableAmountTextView.setText("Rs."+String.valueOf(totalCartAmount + 50)+"/-");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
