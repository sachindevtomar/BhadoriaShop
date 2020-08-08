package com.grocery.bhadoriashop.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.grocery.bhadoriashop.R;
import com.squareup.picasso.Picasso;

public class UserProductListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;

    public UserProductListViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

    }

    //set details to recycler view row
    public void setDetails(Context ctx, String ProductName, String ProductCategory, String ProductSubCategory, String ProductBrand, String ProductImageURL, String MeasureIn, String TotalWeightIn, String ItemWeightIn, double MRPPricePerUnit, double SellingPricePerUnit, double TotalWeight, double ItemWeight, int ItemCount ){
        //Views
        TextView productNameTextView = mView.findViewById(R.id.product_name_usercard_textview);
        ImageView productImageView = mView.findViewById(R.id.product_image_usercard_imageview);
        TextView productPriceTextView = mView.findViewById(R.id.product_price_usercard_textview);

        //set data to views
        productNameTextView.setText(ProductName + " : " + ProductBrand);
        productPriceTextView.setText(String.valueOf(MRPPricePerUnit));
        Picasso.get().load(ProductImageURL).into(productImageView);
    }

    @Override
    public void onClick(View v) {

    }

}
