package com.grocery.bhadoriashop.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.grocery.bhadoriashop.R;
import com.squareup.picasso.Picasso;

public class AdminProductListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;

    public AdminProductListViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

    }

    //set details to recycler view row
    public void setDetails(Context ctx, String ProductName, String ProductCategory, String ProductSubCategory, String ProductBrand, String ProductImageURL, String MeasureIn, String TotalWeightIn, String ItemWeightIn, double MRPPricePerUnit, double SellingPricePerUnit, double TotalWeight, double ItemWeight, int ItemCount ){
        //Views
        TextView productNameTextView = mView.findViewById(R.id.product_name_admincard_textview);
        ImageView productImageView = mView.findViewById(R.id.product_image_admincard_imageview);
        TextView productCategoryTextView = mView.findViewById(R.id.product_category_admincard_textview);
        TextView productSubCategoryTextView = mView.findViewById(R.id.product_subcategory_admincard_textview);
        TextView productItemLeftTextView = mView.findViewById(R.id.product_itemleft_admincard_textview);
        TextView productItemLeftMeasureTextView = mView.findViewById(R.id.product_itemleftmeasure_admincard_textview);
        TextView productMRPTextView = mView.findViewById(R.id.product_mrp_admincard_textview);
        TextView productSPTextView = mView.findViewById(R.id.product_sp_adminlist_textview);

        //set data to views
        productNameTextView.setText(ProductName + " : " + ProductBrand);
        productCategoryTextView.setText(ProductCategory);
        productSubCategoryTextView.setText(ProductSubCategory);
        productMRPTextView.setText(String.valueOf(MRPPricePerUnit));
        productSPTextView.setText(String.valueOf(SellingPricePerUnit));

        if(MeasureIn.equals("Count")){
            productItemLeftTextView.setText(String.valueOf(ItemCount) + " Items " + "(" +String.valueOf(ItemWeight) + " "+ ItemWeightIn + ")");
            productItemLeftMeasureTextView.setText("");
        }
        else if(MeasureIn.equals("Wt/Ltr")){
            productItemLeftTextView.setText(String.valueOf(TotalWeight) + " " + TotalWeightIn);
            productItemLeftMeasureTextView.setText("");
        }
        else{

        }
        if(ProductImageURL !=null && !ProductImageURL.isEmpty()) {
            productImageView.setBackgroundResource(0);
            Picasso.get().load(ProductImageURL).into(productImageView);
        }
    }

    @Override
    public void onClick(View v) {

    }

}
