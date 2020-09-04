package com.grocery.bhadoriashop.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.grocery.bhadoriashop.OnCustomDeleteCartItemListener;
import com.grocery.bhadoriashop.R;
import com.grocery.bhadoriashop.UserCartActivity;
import com.squareup.picasso.Picasso;

public class CartProductListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    String CartItemDBKey;
    OnCustomDeleteCartItemListener mCartItemListener;

    DatabaseReference mDatabaseCart = FirebaseDatabase.getInstance().getReference("ProductCart");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public CartProductListViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

    }

    //set details to recycler view row
    public void setDetails(Context ctx, String ProductName, String ProductImageURL, double SellingPricePerUnit, double ItemWeight, String ItemWeightIn, int ProductCount, String CartItemDBKey, Context currentActivityCtx ){
        //Views
        TextView productNameTextView = mView.findViewById(R.id.product_cart_name_textview);
        ImageView productImageView = mView.findViewById(R.id.product_cart_imageview);
        TextView productQuantityTextView = mView.findViewById(R.id.product_cart_quantity_textview);
        TextView productPriceWeightTextView = mView.findViewById(R.id.product_cart_price_weight_textview);
        ImageButton editCartProductImageBtn = mView.findViewById(R.id.product_cart_edit_qty_imgbtn);
        ImageButton deleteCartProductImageBtn = mView.findViewById(R.id.product_cart_delete_imgbtn);
        //Add Listener on card view elements
        deleteCartProductImageBtn.setOnClickListener(this);
        //set data to views
        productNameTextView.setText(ProductName);
        productQuantityTextView.setText("Qty:"+ProductCount);
        productPriceWeightTextView.setText("Rs. "+SellingPricePerUnit+" / "+ItemWeight+""+ItemWeightIn);
        if(ProductImageURL!=null && !ProductImageURL.isEmpty())
            Picasso.get().load(ProductImageURL).into(productImageView);

        this.CartItemDBKey = CartItemDBKey;
        this.mCartItemListener = (OnCustomDeleteCartItemListener) currentActivityCtx;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.product_cart_delete_imgbtn:
            {
                mDatabaseCart.child(firebaseAuth.getCurrentUser().getUid()).child(this.CartItemDBKey).removeValue();
                //this listener notify the parent activity about the change
                mCartItemListener.onCartItemDeleted();
            }
        }
    }

}

