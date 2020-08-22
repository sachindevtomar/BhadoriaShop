package com.grocery.bhadoriashop.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.grocery.bhadoriashop.R;
import com.squareup.picasso.Picasso;

public class HomeProductCategoryListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;

    public HomeProductCategoryListViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

    }

    //set details to recycler view row
    public void setDetails(Context ctx, String ProductCategoryImageURL, String ProductCategoryName ){
        //Views
        ImageView productCategoryImageURL = mView.findViewById(R.id.product_category_card_imageview);
        TextView productCategoryName = mView.findViewById(R.id.product_category_card_textview);

        //set data to views
        productCategoryName.setText(ProductCategoryName);
        if(ProductCategoryImageURL != null && !ProductCategoryImageURL.isEmpty())
            Picasso.get().load(ProductCategoryImageURL).into(productCategoryImageURL);
    }

    @Override
    public void onClick(View v) {

    }

}

