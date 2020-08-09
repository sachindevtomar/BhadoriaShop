package com.grocery.bhadoriashop.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grocery.bhadoriashop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import me.gujun.android.taggroup.TagGroup;

public class AdminCategoryListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    TextView categoryNameTextView;
    ImageView categoryImageView;
    ImageButton addSubCategoryImageBtn;
    EditText subCategoryEditText;
    ProgressBar saveSubCategoryProgressBar;
    DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("ProductCategories");

    public AdminCategoryListViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

    }

    //set details to recycler view row
    public void setDetails(String CategoryName, String CategoryImageURL){
        //Views
        categoryNameTextView = mView.findViewById(R.id.category_name_admincard_textview);
        categoryImageView = mView.findViewById(R.id.category_image_admincard_imageview);
        addSubCategoryImageBtn = mView.findViewById(R.id.add_subcategory_admin_imagbtn);
        subCategoryEditText = mView.findViewById(R.id.add_subcategory_admin_edittext);
        saveSubCategoryProgressBar = mView.findViewById(R.id.save_subcategory_progressbar);

        categoryNameTextView.setText(CategoryName);
        if(!CategoryImageURL.isEmpty())
            Picasso.get().load(CategoryImageURL).into(categoryImageView);
        addSubCategoryImageBtn.setOnClickListener(this);
        final ArrayList<String> arraySubCategory = new ArrayList<>();
        mDatabaseRef.child(categoryNameTextView.getText().toString()).child("subCategory").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        arraySubCategory.add(childDataSnapshot.getValue().toString());
                    }
                }
                TagGroup mTagGroup = (TagGroup) mView.findViewById(R.id.subcategory_tag_group_cardview);
                mTagGroup.setTags(arraySubCategory);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_subcategory_admin_imagbtn:
                saveSubCategoryProgressBar.setVisibility(View.VISIBLE);
                addSubCategoryImageBtn.setVisibility(View.GONE);
                saveSubCategoryIntoDB(subCategoryEditText.getText().toString());
//                c.finish();
                break;
            default:
                break;
        }
    }

    private void saveSubCategoryIntoDB(String subCategoryName){
        if(subCategoryName.trim().isEmpty()) {
            saveSubCategoryProgressBar.setVisibility(View.GONE);
            addSubCategoryImageBtn.setVisibility(View.VISIBLE);
            return;
        }
        Log.d("Data","SubCategory" + subCategoryName);
        mDatabaseRef.child(categoryNameTextView.getText().toString()).child("subCategory").push().setValue(subCategoryName);
        saveSubCategoryProgressBar.setVisibility(View.GONE);
        addSubCategoryImageBtn.setVisibility(View.VISIBLE);
    }
}
