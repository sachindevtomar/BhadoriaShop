package com.grocery.bhadoriashop.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grocery.bhadoriashop.Models.CartProduct;
import com.grocery.bhadoriashop.R;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;

public class UserProductListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    String ObjectKey, ProductName, ItemWeightIn, ProductImageURL;
    double ItemWeight, ProductSellingPrice;
    DatabaseReference mDatabaseCart = FirebaseDatabase.getInstance().getReference("ProductCart");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    int currentCartItemCount = 1;
    EditText productCardCountEditText;
    Context ctx;

    public UserProductListViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

    }

    //set details to recycler view row
    public void setDetails(Context ctx, String ProductName, String ProductCategory, String ProductSubCategory, String ProductBrand, String ProductImageURL, String MeasureIn, String TotalWeightIn, String ItemWeightIn, double MRPPricePerUnit, double SellingPrice, double TotalWeight, double ItemWeight, int ItemCount, String ObjectKey ){
        //Views
        TextView productNameTextView = mView.findViewById(R.id.product_name_usercard_textview);
        ImageView productImageView = mView.findViewById(R.id.product_image_usercard_imageview);
        TextView productMRPTextView = mView.findViewById(R.id.product_mrp_usercard_textview);
        TextView productSPTextView = mView.findViewById(R.id.product_sp_usercard_textview);
        TextView productDiscountTextView = mView.findViewById(R.id.product_discount_usercard_textview);
        TextView productWeightTextView = mView.findViewById(R.id.product_weight_usercard_textview);
        ImageButton decreaseCardItemBtn = mView.findViewById(R.id.decrease_card_product_count_btn);
        ImageButton increaseCardItemBtn = mView.findViewById(R.id.increase_card_product_count_btn);
        Button addCartBtn = mView.findViewById(R.id.product_addcart_usercard_btn);
        decreaseCardItemBtn.setOnClickListener(this);
        increaseCardItemBtn.setOnClickListener(this);
        addCartBtn.setOnClickListener(this);
        //set data to views
        productNameTextView.setText(ProductName + " : " + ProductBrand);
        productSPTextView.setText("Rs."+String.valueOf(SellingPrice));
        productMRPTextView.setText("MRP."+String.valueOf(MRPPricePerUnit));
        productMRPTextView.setPaintFlags(productMRPTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        int discount = (int)(((MRPPricePerUnit - SellingPrice)/MRPPricePerUnit)*100);
        productDiscountTextView.setText(String.valueOf(discount)+"% OFF");
        productWeightTextView.setText(String.valueOf(ItemWeight) + " " + ItemWeightIn);
        //Set product image
        if(ProductImageURL !=null && !ProductImageURL.isEmpty()) {
            productImageView.setBackgroundResource(0);
            Picasso.get().load(ProductImageURL).into(productImageView);
        }

        this.ObjectKey = ObjectKey;
        this.ProductName = ProductName;
        this.ItemWeight = ItemWeight;
        this.ItemWeightIn = ItemWeightIn;
        productCardCountEditText = mView.findViewById(R.id.card_product_count_edittext);
        this.currentCartItemCount = Integer.parseInt(productCardCountEditText.getText().toString());
        this.ctx = ctx;
        this.ProductImageURL = ProductImageURL;
        this.ProductSellingPrice = SellingPrice;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.decrease_card_product_count_btn:
            {
                if(currentCartItemCount > 1)
                    currentCartItemCount--;
                productCardCountEditText.setText(String.valueOf(currentCartItemCount));
                break;
            }
            case R.id.increase_card_product_count_btn:
            {
                if(currentCartItemCount < 10)
                    currentCartItemCount++;
                productCardCountEditText.setText(String.valueOf(currentCartItemCount));
                break;
            }
            case R.id.product_addcart_usercard_btn:
            {
                if(firebaseAuth.getCurrentUser() == null) {
                    Toasty.error(this.ctx, R.string.login_required, Toast.LENGTH_LONG, true).show();
                    break;
                }
                try {
                    Log.d("ObjectId",this.ObjectKey);
                    mDatabaseCart.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                boolean itemFoundInCart = false;
                                CartProduct existingCartProduct = null;
                                String itemFoundIndexKey = null;
                                //Check if item is already there in cart or not
                                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                    existingCartProduct = dataSnapshot.getValue(CartProduct.class);
                                    if(existingCartProduct.getProductObjectKey().equals(ObjectKey)){
                                        itemFoundInCart = true;
                                        itemFoundIndexKey = dataSnapshot.getKey();
                                        break;
                                    }
                                }
                                //Update or show info if item is found
                                if(itemFoundInCart && existingCartProduct != null){
                                    if(existingCartProduct.getItemCount() != currentCartItemCount) {
                                        existingCartProduct.setItemCount(currentCartItemCount);
                                        mDatabaseCart.child(firebaseAuth.getCurrentUser().getUid()).child(itemFoundIndexKey).setValue(existingCartProduct);
                                        Toasty.success(ctx, R.string.item_updated_cart, Toast.LENGTH_SHORT, true).show();
                                    }
                                    else{
                                        Toasty.info(ctx, R.string.item_already_cart, Toast.LENGTH_SHORT, true).show();
                                    }
                                }
                                //insert new item in cart if it was not there earlier
                                else{
                                    CartProduct saveCartItem = new CartProduct(ProductName, ItemWeight, ItemWeightIn, ObjectKey, currentCartItemCount, System.currentTimeMillis(), ProductImageURL, ProductSellingPrice);
                                    mDatabaseCart.child(firebaseAuth.getCurrentUser().getUid()).push().setValue(saveCartItem);
                                    Toasty.success(ctx, R.string.item_added_cart, Toast.LENGTH_SHORT, true).show();
                                }
                            }
                            else{
                                CartProduct saveCartItem = new CartProduct(ProductName, ItemWeight, ItemWeightIn, ObjectKey, currentCartItemCount, System.currentTimeMillis(), ProductImageURL, ProductSellingPrice);
                                mDatabaseCart.child(firebaseAuth.getCurrentUser().getUid()).push().setValue(saveCartItem);
                                Toasty.success(ctx, R.string.item_added_cart, Toast.LENGTH_SHORT, true).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                catch (Exception ex){
                    Toasty.error(this.ctx, R.string.unable_add_item_cart, Toast.LENGTH_LONG, true).show();
                }
                break;
            }
        }
    }

}
