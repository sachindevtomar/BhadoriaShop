package com.grocery.bhadoriashop.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.bhadoriashop.R;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;

public class SelectCategoryListViewHolder extends RecyclerView.ViewHolder{

    View mView;

    public SelectCategoryListViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

    }

    //set details to recycler view row
    public void setDetails(Context ctx, String ProductCategoryImageURL, String ProductCategoryName, String existingFilterCategory ){
        //Views
        ImageView productCategoryImageURL = mView.findViewById(R.id.select_category_card_imageview);
        TextView productCategoryName = mView.findViewById(R.id.select_category_card_textview);

        //set data to views
        productCategoryName.setText(ProductCategoryName);
        if(ProductCategoryImageURL != null && !ProductCategoryImageURL.isEmpty())
            Picasso.get().load(ProductCategoryImageURL).into(productCategoryImageURL);

        if(existingFilterCategory.length()>0 && existingFilterCategory.equals(ProductCategoryName)){
            CardView cardView = (CardView)mView;
            cardView.setCardBackgroundColor(ContextCompat.getColor(ctx,
                    R.color.buttonCommonColorDark));
            productCategoryName.setTextColor(ContextCompat.getColor(ctx,
                    R.color.white));
            productCategoryName.setTypeface(null, Typeface.BOLD);
        }
    }
}

