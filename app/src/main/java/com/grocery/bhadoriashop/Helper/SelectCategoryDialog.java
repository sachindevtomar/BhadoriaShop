package com.grocery.bhadoriashop.Helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.grocery.bhadoriashop.Adapter.HomeProductCategoryListViewHolder;
import com.grocery.bhadoriashop.Adapter.SelectCategoryListViewHolder;
import com.grocery.bhadoriashop.Adapter.UserProductListViewHolder;
import com.grocery.bhadoriashop.Models.AdminProductList;
import com.grocery.bhadoriashop.Models.ProductCategory;
import com.grocery.bhadoriashop.R;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class SelectCategoryDialog extends DialogFragment implements View.OnClickListener {

    public Activity parentActivity;

    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    public SelectCategoryDialog(Activity a) {
        // TODO Auto-generated constructor stub
        this.parentActivity = a;
    }

    public interface CategoryNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_select_category_filter, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_category_admin_dialog_btn:

                break;
            case R.id.add_category_admin_dialog_image_btn:

                break;
            case R.id.category_image_admin_dialog_close_imgbtn:

                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        final FirebaseRecyclerOptions<ProductCategory> options =
                new FirebaseRecyclerOptions.Builder<ProductCategory>()
                        .setQuery(mRef, ProductCategory.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ProductCategory, SelectCategoryListViewHolder>(options) {
            @Override
            public SelectCategoryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_select_categorylist, parent, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(v instanceof CardView) {
                            TextView selectedCategoryTextView = v.findViewById(R.id.select_category_card_textview);
                            CategoryNameDialogListener activity = (CategoryNameDialogListener) getActivity();
                            activity.onFinishEditDialog(selectedCategoryTextView.getText().toString());
                            getDialog().dismiss();
                        }
                    }
                });
                return new SelectCategoryListViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(SelectCategoryListViewHolder holder, int position, ProductCategory model) {
                // Bind the image_details object to the BlogViewHolder
                holder.setDetails(getContext(), model.getCategoryImageURL(), model.getCategoryName());
            }
        };

        //set adapter to recyclerview
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    private void initViews(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.select_category_dialog_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        //send Query to FirebaseDatabase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("ProductCategories");
    }
}
