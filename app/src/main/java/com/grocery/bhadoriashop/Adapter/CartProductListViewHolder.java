package com.grocery.bhadoriashop.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    LinearLayout cartEditableLinearLayout, cartEditedLinearLayout;
    TextView productQuantityTextView, updatedCartItemCountTextView;
    int currentCartItemValue = 1;

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
        productQuantityTextView = mView.findViewById(R.id.product_cart_quantity_textview);
        TextView productPriceWeightTextView = mView.findViewById(R.id.product_cart_price_weight_textview);
        ImageButton editCartProductImageBtn = mView.findViewById(R.id.product_cart_edit_qty_imgbtn);
        ImageButton deleteCartProductImageBtn = mView.findViewById(R.id.product_cart_delete_imgbtn);
        cartEditableLinearLayout = mView.findViewById(R.id.cart_editable_linear_layout);
        cartEditedLinearLayout = mView.findViewById(R.id.cart_edited_linear_layout);
        ImageButton decreaseCartItemBtn = mView.findViewById(R.id.decrease_cart_product_count_imgbtn);
        ImageButton increaseCartItemBtn = mView.findViewById(R.id.increase_cart_product_count_imgbtn);
        updatedCartItemCountTextView = mView.findViewById(R.id.cart_product_count_textview);
        ImageButton saveUpdatedCartItemCountImgBtn = mView.findViewById(R.id.product_cart_edit_qty_save_imgbtn);

        //Add Listener on card view elements
        deleteCartProductImageBtn.setOnClickListener(this);
        editCartProductImageBtn.setOnClickListener(this);
        saveUpdatedCartItemCountImgBtn.setOnClickListener(this);
        decreaseCartItemBtn.setOnClickListener(this);
        increaseCartItemBtn.setOnClickListener(this);

        //set data to views
        productNameTextView.setText(ProductName);
        productQuantityTextView.setText("Qty:"+ProductCount);
        updatedCartItemCountTextView.setText(String.valueOf(ProductCount));
        currentCartItemValue = ProductCount;
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
                break;
            }
            case R.id.product_cart_edit_qty_imgbtn:
            {
                cartEditedLinearLayout.setVisibility(View.GONE);
                cartEditableLinearLayout.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.decrease_cart_product_count_imgbtn:
            {
                if(currentCartItemValue > 1)
                    currentCartItemValue--;
                updatedCartItemCountTextView.setText(String.valueOf(currentCartItemValue));
                break;
            }
            case R.id.increase_cart_product_count_imgbtn:
            {
                if(currentCartItemValue < 10)
                    currentCartItemValue++;
                updatedCartItemCountTextView.setText(String.valueOf(currentCartItemValue));
                break;
            }
            case R.id.product_cart_edit_qty_save_imgbtn:
            {
                try {
                    mDatabaseCart.child(firebaseAuth.getCurrentUser().getUid()).child(this.CartItemDBKey).child("itemCount").setValue(currentCartItemValue);
                    //Revert back the layout visibility
                    cartEditedLinearLayout.setVisibility(View.VISIBLE);
                    cartEditableLinearLayout.setVisibility(View.GONE);
                    productQuantityTextView.setText("Qty: "+currentCartItemValue);
                    //Callback to update the activity UI with the updated cart quantity values
                    mCartItemListener.onCartItemDeleted();
                }
                catch(Exception ex){
                    //If there is any error occured at the time of updating values in the firebase realtime database
                    Log.d("Update Error", "Error occured while updating cart item count in the realtime database");
                }
                break;
            }
        }
    }

}

