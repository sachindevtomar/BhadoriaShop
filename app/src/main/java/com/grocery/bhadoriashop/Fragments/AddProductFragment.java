package com.grocery.bhadoriashop.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grocery.bhadoriashop.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductFragment extends Fragment {
    Spinner productCategorySpinner, totalWeigtCategorySpinner, weightCategorySpinner;
    EditText productNameEditText, brandEditText, totalWeightEditText, productCountEditText, productWeightEditText, priceEditText;
    Button addProductBtn;
    LinearLayout weightBasedLinearLayout, countBasedLinearLayout;
    private DatabaseReference mDatabase;
    ArrayList<String> arrayProductCategory;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_product, container, false);

        initializeAllTheElements(rootView);
        fetchProductCategories(rootView);

        String[] weightCategoryArray = getResources().getStringArray(R.array.weight_category_array);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),R.layout.weight_category_spinner_items,R.id.spinner_item, weightCategoryArray);
        totalWeigtCategorySpinner.setAdapter(adapter);
        weightCategorySpinner.setAdapter(adapter);
        // Inflate the layout for this fragment
        return rootView;
    }

    private void fetchProductCategories(View rootView) {
        arrayProductCategory = new ArrayList<>();
        mDatabase.child("ProductCategory").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                        arrayProductCategory.add(childDataSnapshot.getValue().toString());
                    }
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.weight_category_spinner_items,R.id.spinner_item,  arrayProductCategory);
                productCategorySpinner.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeAllTheElements(View rootView) {
        //Database Reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Spiners
        productCategorySpinner = rootView.findViewById(R.id.product_category_spinner);
        totalWeigtCategorySpinner = rootView.findViewById(R.id.total_weight_category_spinner);
        weightCategorySpinner = rootView.findViewById(R.id.weight_category_spinner);
        //EditText
        productNameEditText = rootView.findViewById(R.id.name_product_edittext);
        brandEditText = rootView.findViewById(R.id.brand_product_edittext);
        totalWeightEditText = rootView.findViewById(R.id.total_weight_edittext);
        productCountEditText = rootView.findViewById(R.id.count_product_edittext);
        productWeightEditText = rootView.findViewById(R.id.weight_product_edittext);
        priceEditText = rootView.findViewById(R.id.price_product_edittext);
        //Buttons
        addProductBtn = rootView.findViewById(R.id.add_product_btn);
        //Layouts
        weightBasedLinearLayout = rootView.findViewById(R.id.weight_based_linearlayout);
        countBasedLinearLayout = rootView.findViewById(R.id.count_based_linearlayout);
    }
}
