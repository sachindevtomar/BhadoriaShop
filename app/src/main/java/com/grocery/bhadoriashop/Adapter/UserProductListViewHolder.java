package com.grocery.bhadoriashop.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        TextView productMRPTextView = mView.findViewById(R.id.product_mrp_usercard_textview);
        TextView productSPTextView = mView.findViewById(R.id.product_sp_usercard_textview);
        TextView productDiscountTextView = mView.findViewById(R.id.product_discount_usercard_textview);
        TextView productWeightTextView = mView.findViewById(R.id.product_weight_usercard_textview);
        Button decreaseCardItemBtn = mView.findViewById(R.id.decrease_card_product_count_btn);
        Button increaseCardItemBtn = mView.findViewById(R.id.increase_card_product_count_btn);
        decreaseCardItemBtn.setOnClickListener(this);
        increaseCardItemBtn.setOnClickListener(this);
        //set data to views
        productNameTextView.setText(ProductName + " : " + ProductBrand);
        productSPTextView.setText("Rs."+String.valueOf(SellingPricePerUnit));
        productMRPTextView.setText("MRP."+String.valueOf(MRPPricePerUnit));
        productMRPTextView.setPaintFlags(productMRPTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        int discount = (int)(((MRPPricePerUnit - SellingPricePerUnit)/MRPPricePerUnit)*100);
        productDiscountTextView.setText(String.valueOf(discount)+"% OFF");
        productWeightTextView.setText(String.valueOf(ItemWeight) + " " + ItemWeightIn);
        Picasso.get().load(ProductImageURL).into(productImageView);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.decrease_card_product_count_btn:
            {
                EditText productCardCountEditText = mView.findViewById(R.id.card_product_count_edittext);
                int currentValue = Integer.parseInt(productCardCountEditText.getText().toString());
                if(currentValue>1)
                    currentValue--;
                productCardCountEditText.setText(String.valueOf(currentValue));
                break;
            }
            case R.id.increase_card_product_count_btn:
            {
                EditText productCardCountEditText = mView.findViewById(R.id.card_product_count_edittext);
                int currentValue = Integer.parseInt(productCardCountEditText.getText().toString());
                if(currentValue<10)
                    currentValue++;
                productCardCountEditText.setText(String.valueOf(currentValue));
                break;
            }
        }
    }

}
