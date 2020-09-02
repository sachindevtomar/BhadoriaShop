package com.grocery.bhadoriashop.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.grocery.bhadoriashop.R;
import com.squareup.picasso.Picasso;

public class CartProductListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;

    public CartProductListViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

    }

    //set details to recycler view row
    public void setDetails(Context ctx, String ProductName, String ProductImageURL, double SellingPricePerUnit, double ItemWeight, String ItemWeightIn, int ProductCount ){
        //Views
        TextView productNameTextView = mView.findViewById(R.id.product_cart_name_textview);
        ImageView productImageView = mView.findViewById(R.id.product_cart_imageview);
        TextView productQuantityTextView = mView.findViewById(R.id.product_cart_quantity_textview);
        TextView productPriceWeightTextView = mView.findViewById(R.id.product_cart_price_weight_textview);
        ImageButton editCartProductImageBtn = mView.findViewById(R.id.product_cart_edit_qty_imgbtn);
        ImageButton deleteCartProductImageBtn = mView.findViewById(R.id.product_cart_delete_imgbtn);

        //set data to views
        productNameTextView.setText(ProductName);
        productQuantityTextView.setText("Qty:"+ProductCount);
        productPriceWeightTextView.setText("Rs. "+SellingPricePerUnit+" / "+ItemWeight+""+ItemWeightIn);
        if(ProductImageURL!=null && !ProductImageURL.isEmpty())
            Picasso.get().load(ProductImageURL).into(productImageView);
    }

    @Override
    public void onClick(View v) {

    }

}
