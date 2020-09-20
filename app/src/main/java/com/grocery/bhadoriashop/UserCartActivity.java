package com.grocery.bhadoriashop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.grocery.bhadoriashop.Adapter.CartProductListViewHolder;
import com.grocery.bhadoriashop.Adapter.UserProductListViewHolder;
import com.grocery.bhadoriashop.Models.AdminProductList;
import com.grocery.bhadoriashop.Models.CartProduct;
import com.grocery.bhadoriashop.Models.Order;
import com.grocery.bhadoriashop.Models.OrderStatus;
import com.grocery.bhadoriashop.Models.User;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class UserCartActivity extends AppCompatActivity implements OnCustomDeleteCartItemListener{
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRefProductCart, mRefUser, mRefOrder;
    TextView totalAmountTextView, totalPayableAmountTextView, deliveryAmountTextView;
    double totalCartAmount = 0;
    Context currentActivityCtx;
    FirebaseAuth firebaseAuth;
    Button checkoutProductsBtn;
    List<CartProduct> currentCartProducts = new ArrayList<CartProduct>();
    User currentUser;
    ProgressBar checkoutProgressBar;

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
        checkoutProductsBtn = (Button) findViewById(R.id.checkout_products_btn);
        checkoutProgressBar = (ProgressBar) findViewById(R.id.checkout_progress_bar);
        //send Query to FirebaseDatabase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        //Need to change hardcoded UserID with logged in userID
        if(firebaseAuth.getCurrentUser()!=null) {
            mRefProductCart = mFirebaseDatabase.getReference("ProductCart").child(firebaseAuth.getCurrentUser().getUid());
            mRefUser = mFirebaseDatabase.getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
            mRefOrder = mFirebaseDatabase.getReference("Orders");
        }

        //addListeners
        addListenersCustomMethod();
    }

    private void addListenersCustomMethod() {
        mRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    currentUser = snapshot.getValue(User.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        checkoutProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutProgressBar.setVisibility(View.VISIBLE);
                checkoutProductsBtn.setVisibility(View.GONE);
                Order orderToBePlaced = formOrderObject();

                if(orderToBePlaced != null && orderToBePlaced.getCreatedBy() !=null && orderToBePlaced.getProducts() != null && !orderToBePlaced.getCreatedBy().getAddresses().isEmpty()){
                    // read the index key
                    String placedOrderId = mRefOrder.push().getKey();
                    orderToBePlaced.setOrderId(placedOrderId);
                    mRefOrder.child(placedOrderId).setValue(orderToBePlaced);
                    Toasty.success(getApplicationContext(), "Congratulations, Your order has been placed", Toast.LENGTH_LONG, true).show();

                    //Remove the Cart items once order is placed
                    mRefProductCart.removeValue();

                    //Refresh activity on successful order place and open home fragment
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("StartWithFragment", new MainActivity().FRAGMENT_HOME);
                    startActivity(intent);
                }
                else{
                    Toasty.error(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG, true).show();
                    checkoutProgressBar.setVisibility(View.GONE);
                    checkoutProductsBtn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private Order formOrderObject() {
        Order order = new Order();
        order.setCreatedBy(currentUser);
        order.setProducts(currentCartProducts);
        order.setStatus(OrderStatus.Placed);
        order.setCreatedDateEPoch(System.currentTimeMillis());
        //now add half an hour, 1 800 000 miliseconds = 30 minutes
        long defaultExpectedDeliveryTime = System.currentTimeMillis() + 86400000;
        order.setExpectedDeliveryTime(defaultExpectedDeliveryTime);
        return order;
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
                .setQuery(mRefProductCart, CartProduct.class)
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
        mRefProductCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot childDataSnapShot : snapshot.getChildren()){
                        CartProduct cartItem = childDataSnapShot.getValue(CartProduct.class);
                        //this list will be sent at the time of checkout
                        currentCartProducts.add(cartItem);
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
