package com.grocery.bhadoriashop.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
    Spinner productCategorySpinner, productSubCategorySpinner, totalWeigtCategorySpinner, weightCategorySpinner;
    EditText productNameEditText, brandEditText, totalWeightEditText, productCountEditText, productWeightEditText, mrpPriceEditText, sellingPriceEditText;
    Button saveProductBtn;
    LinearLayout weightBasedLinearLayout, countBasedLinearLayout, categoryLoadingLinearLayout, subcategoryLoadingLinearLayout, categoryContentLinearLayout, subcategoryContentLinearLayout;
    RelativeLayout subcategoryRelativeLayout;
    RadioGroup measureInRadioGroup;
    RadioButton measureWtLtrRadioBtn, measureCountRadioBtn, measureNARadioBtn;
    private DatabaseReference mDatabase;
    ArrayList<String> arrayProductCategory, arrayProductSubCategory;
    View completeScopeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_product, container, false);
        completeScopeView = rootView;

        initializeAllTheElements(rootView);
        fetchProductCategoriesForSpinner(rootView);

        String[] weightCategoryArray = getResources().getStringArray(R.array.weight_category_array);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),R.layout.weight_category_spinner_items,R.id.spinner_item, weightCategoryArray);
        totalWeigtCategorySpinner.setAdapter(adapter);
        weightCategorySpinner.setAdapter(adapter);

        setListenersBasedOnBusinessLogic(rootView);
        // Inflate the layout for this fragment
        return rootView;
    }

    private void fetchProductCategoriesForSpinner(View rootView) {
        arrayProductCategory = new ArrayList<>();
        mDatabase.child("ProductCategory").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //Fetch Category
                    for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                        arrayProductCategory.add(childDataSnapshot.getKey().toString());
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.weight_category_spinner_items,R.id.spinner_item,  arrayProductCategory);
                    productCategorySpinner.setAdapter(arrayAdapter);
                    categoryLoadingLinearLayout.setVisibility(View.GONE);
                    categoryContentLinearLayout.setVisibility(View.VISIBLE);
                    subcategoryRelativeLayout.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setListenersBasedOnBusinessLogic(View rootView) {

        productCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subcategoryContentLinearLayout.setVisibility(View.GONE);
                subcategoryLoadingLinearLayout.setVisibility(View.VISIBLE);
                Log.d("TAG","Product Category Spinner Item -> " + productCategorySpinner.getSelectedItem().toString());
                arrayProductSubCategory = new ArrayList<>();
                mDatabase.child("ProductCategory").child(productCategorySpinner.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            //Fetch SubCategory
                            for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                                arrayProductSubCategory.add(childDataSnapshot.getValue().toString());
                            }
                            ArrayAdapter<String> arraySubCategoryAdapter = new ArrayAdapter<String>(getContext(), R.layout.weight_category_spinner_items,R.id.spinner_item,  arrayProductSubCategory);
                            productSubCategorySpinner.setAdapter(arraySubCategoryAdapter);
                            subcategoryContentLinearLayout.setVisibility(View.VISIBLE);
                            subcategoryLoadingLinearLayout.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateDataBeforeSaving()){

                }
                else{
                    Log.d("Error","Error Occured ");
                }
            }
        });

        measureInRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedRadioButoonId) {
                if(checkedRadioButoonId == R.id.count_measure_radiobtn){
                    countBasedLinearLayout.setVisibility(View.VISIBLE);
                    weightBasedLinearLayout.setVisibility(View.GONE);
                    totalWeightEditText.setText(null);
                }
                else if(checkedRadioButoonId == R.id.wtltr_measure_radiobtn){
                    countBasedLinearLayout.setVisibility(View.GONE);
                    weightBasedLinearLayout.setVisibility(View.VISIBLE);
                    productCountEditText.setText(null);
                    productWeightEditText.setText(null);
                }
                else{
                    countBasedLinearLayout.setVisibility(View.GONE);
                    weightBasedLinearLayout.setVisibility(View.GONE);
                    productCountEditText.setText(null);
                    productWeightEditText.setText(null);
                    totalWeightEditText.setText(null);
                }
            }
        });

        weightCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.d("TAG","Spinner Item -> " + weightCategorySpinner.getSelectedItem().toString());
                if(weightCategorySpinner.getSelectedItem().toString().equals("NA")){
                    productWeightEditText.setText(null);
                    productWeightEditText.setVisibility(View.GONE);
                }
                else
                    productWeightEditText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private boolean validateDataBeforeSaving() {
        if(isNullOrEmpty(productCategorySpinner.getSelectedItem().toString()) || isNullOrEmpty(productSubCategorySpinner.getSelectedItem().toString()) || isNullOrEmpty(productNameEditText.getText().toString()) || isNullOrEmpty(brandEditText.getText().toString()) || isNullOrEmpty(((RadioButton) completeScopeView.findViewById(measureInRadioGroup.getCheckedRadioButtonId())).getText().toString()) || isNullOrEmpty(mrpPriceEditText.getText().toString()) || isNullOrEmpty(sellingPriceEditText.getText().toString())) {
            Toast.makeText(getActivity(), R.string.fill_required_data, Toast.LENGTH_LONG).show();
            return false;
        }
        if(((RadioButton) completeScopeView.findViewById(measureInRadioGroup.getCheckedRadioButtonId())).getText().toString().equals("Count") && (isNullOrEmpty(productCountEditText.getText().toString()) || ( !weightCategorySpinner.getSelectedItem().toString().equals("NA") && isNullOrEmpty(productWeightEditText.getText().toString())))){
            Toast.makeText(getActivity(), R.string.fill_data_for_count_measurein, Toast.LENGTH_LONG).show();
            return false;
        }
        if(((RadioButton) completeScopeView.findViewById(measureInRadioGroup.getCheckedRadioButtonId())).getText().toString().equals("Wt/Ltr") && (isNullOrEmpty(totalWeightEditText.getText().toString()))){
            Toast.makeText(getActivity(), R.string.fill_required_wtltr_measurein, Toast.LENGTH_LONG).show();
            return false;
        }
        if(isNullOrEmpty(totalWeightEditText.getText().toString()))
            Log.d("Error",((RadioButton) completeScopeView.findViewById(measureInRadioGroup.getCheckedRadioButtonId())).getText().toString());
        Toast.makeText(getActivity(), "data validation success", Toast.LENGTH_LONG).show();
        return true;
    }

    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.trim().isEmpty())
            return false;
        return true;
    }

    private void initializeAllTheElements(View rootView) {
        //Database Reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Spiners
        productCategorySpinner = rootView.findViewById(R.id.product_category_spinner);
        productSubCategorySpinner = rootView.findViewById(R.id.product_subcategory_spinner);
        totalWeigtCategorySpinner = rootView.findViewById(R.id.total_weight_category_spinner);
        weightCategorySpinner = rootView.findViewById(R.id.weight_category_spinner);
        //EditText
        productNameEditText = rootView.findViewById(R.id.name_product_edittext);
        brandEditText = rootView.findViewById(R.id.brand_product_edittext);
        totalWeightEditText = rootView.findViewById(R.id.total_weight_edittext);
        productCountEditText = rootView.findViewById(R.id.count_product_edittext);
        productWeightEditText = rootView.findViewById(R.id.weight_product_edittext);
        mrpPriceEditText = rootView.findViewById(R.id.mrpprice_product_edittext);
        sellingPriceEditText = rootView.findViewById(R.id.sellingprice_product_edittext);
        //Buttons
        saveProductBtn = rootView.findViewById(R.id.add_product_btn);
        //LinearLayouts
        weightBasedLinearLayout = rootView.findViewById(R.id.weight_based_linearlayout);
        countBasedLinearLayout = rootView.findViewById(R.id.count_based_linearlayout);
        categoryLoadingLinearLayout = rootView.findViewById(R.id.category_loading_linearlayout);
        subcategoryLoadingLinearLayout = rootView.findViewById(R.id.subcategory_loading_linearlayout);
        categoryContentLinearLayout = rootView.findViewById(R.id.category_content_linearlayout);
        subcategoryContentLinearLayout = rootView.findViewById(R.id.subcategory_content_linearlayout);
        //RelativeLayout
        subcategoryRelativeLayout = rootView.findViewById(R.id.subcategor_product_relativelayout);
        //RadioGroup
        measureInRadioGroup = rootView.findViewById(R.id.measure_product_radiogroup);
        //RadioButton
        measureCountRadioBtn =  rootView.findViewById(R.id.count_measure_radiobtn);
        measureWtLtrRadioBtn =  rootView.findViewById(R.id.wtltr_measure_radiobtn);
        measureNARadioBtn =  rootView.findViewById(R.id.na_measure_radiobtn);
    }
}
