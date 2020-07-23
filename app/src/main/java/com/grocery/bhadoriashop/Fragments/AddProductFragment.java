package com.grocery.bhadoriashop.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.grocery.bhadoriashop.Models.Product;
import com.grocery.bhadoriashop.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddProductFragment extends Fragment {
    Spinner productCategorySpinner, productSubCategorySpinner, totalWeigtCategorySpinner, weightCategorySpinner;
    EditText productNameEditText, brandEditText, totalWeightEditText, productCountEditText, productWeightEditText, mrpPriceEditText, sellingPriceEditText;
    Button saveProductBtn, addProductImageBtn;
    ImageButton productCloseImageBtn;
    LinearLayout weightBasedLinearLayout, countBasedLinearLayout, categoryLoadingLinearLayout, subcategoryLoadingLinearLayout, categoryContentLinearLayout, subcategoryContentLinearLayout, addImageLinearLayout;
    RelativeLayout subcategoryRelativeLayout;
    RadioGroup measureInRadioGroup;
    RadioButton measureWtLtrRadioBtn, measureCountRadioBtn, measureNARadioBtn;
    ImageView productImageView;
    ProgressBar saveProductProgressBar;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    ArrayList<String> arrayProductCategory, arrayProductSubCategory;
    View completeScopeView;
    Bitmap productImageBitmap;
    byte[] productImageByteArray;

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
                saveProductProgressBar.setVisibility(View.VISIBLE);
                saveProductBtn.setVisibility(View.GONE);
                if(validateDataBeforeSaving()){
                    saveProductIntoDB();
                }
                else{
                    saveProductProgressBar.setVisibility(View.GONE);
                    saveProductBtn.setVisibility(View.VISIBLE);
                    Log.d("Error","Error Occured while validating data");
                }
            }
        });

        addProductImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(getContext());
            }
        });

        productCloseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productCloseImageBtn.setVisibility(View.GONE);
                productImageView.setVisibility(View.GONE);
                addImageLinearLayout.setVisibility(View.VISIBLE);
                productImageByteArray = null;
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
        //Toast.makeText(getActivity(), "data validation success", Toast.LENGTH_LONG).show();
        return true;
    }

    private void saveProductIntoDB() {
        try {
            StorageReference imageStorageRef = storageRef.child("Products").child("Image_" + new Date().getTime());
            if(productImageByteArray != null){
                UploadTask uploadTask = imageStorageRef.putBytes(productImageByteArray);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getActivity(), "Image Upload failed", Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Get the image download url that needs to be stored in Realtime database
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //Store values in Realtime database of firebase
                                DatabaseReference productsReference = mDatabase.child("Products");
                                productsReference.push().setValue(getProductToSave(String.valueOf(uri)));
                                saveProductProgressBar.setVisibility(View.GONE);
                                saveProductBtn.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "Product is saved with image", Toast.LENGTH_LONG).show();
                                resetTheValues();
                            }
                        });
                    }
                });
            }
            else{
                DatabaseReference productsReference = mDatabase.child("Products");
                productsReference.push().setValue(getProductToSave(""));
                saveProductProgressBar.setVisibility(View.GONE);
                saveProductBtn.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Product is saved without image", Toast.LENGTH_LONG).show();
            }
            Log.d("TAG", "Product is saved");
        }
        catch (Exception ex) {
            saveProductProgressBar.setVisibility(View.GONE);
            saveProductBtn.setVisibility(View.VISIBLE);
            Log.d("Error", ex.getMessage());
        }
    }

    private void resetTheValues() {
        totalWeightEditText.setText("");
        mrpPriceEditText.setText("");
        sellingPriceEditText.setText("");
        productNameEditText.setText("");
        brandEditText.setText("");
        productCountEditText.setText("");
        productWeightEditText.setText("");
        //Image Reset
        productCloseImageBtn.setVisibility(View.GONE);
        productImageView.setVisibility(View.GONE);
        addImageLinearLayout.setVisibility(View.VISIBLE);
        productImageByteArray = null;
    }

    private Product getProductToSave(String productImageUrl) {
        double convertedTotalWeight = 0.0;
        String convertedTotalWeightMeasure = "Grams";

        if(totalWeigtCategorySpinner.getSelectedItem().toString().equals("Kgs")) {
            convertedTotalWeight = Double.parseDouble(isNullOrEmpty(totalWeightEditText.getText().toString()) ? "0" : totalWeightEditText.getText().toString()) * 1000;
            convertedTotalWeightMeasure = "Grams";
        }
        else if(totalWeigtCategorySpinner.getSelectedItem().toString().equals("Liters")){
            convertedTotalWeight = Double.parseDouble(isNullOrEmpty(totalWeightEditText.getText().toString()) ? "0" : totalWeightEditText.getText().toString()) * 1000;
            convertedTotalWeightMeasure = "Mls";
        }
        else{}

        return new Product(
                productNameEditText.getText().toString(),
                productCategorySpinner.getSelectedItem().toString(),
                productSubCategorySpinner.getSelectedItem().toString(),
                brandEditText.getText().toString(),
                ((RadioButton) completeScopeView.findViewById(measureInRadioGroup.getCheckedRadioButtonId())).getText().toString(),
                convertedTotalWeight,
                convertedTotalWeightMeasure,
                Integer.parseInt(isNullOrEmpty(productCountEditText.getText().toString()) ? "0" : productCountEditText.getText().toString()),
                Double.parseDouble(isNullOrEmpty(productWeightEditText.getText().toString()) ? "0" : productWeightEditText.getText().toString()),
                weightCategorySpinner.getSelectedItem().toString(),
                Double.parseDouble(isNullOrEmpty(mrpPriceEditText.getText().toString()) ? "0" : mrpPriceEditText.getText().toString()),
                Double.parseDouble(isNullOrEmpty(sellingPriceEditText.getText().toString()) ? "0" : sellingPriceEditText.getText().toString()),
                productImageUrl);
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        productImageBitmap = compressImageTo40(selectedImage);
                        productImageView.setImageBitmap(productImageBitmap);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImageURI =  data.getData();
                        //productImageView.setImageURI(selectedImage);
                        try {
                            Bitmap imageOriginalBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageURI);
                            productImageBitmap = compressImageTo40(imageOriginalBitmap);
                            productImageView.setImageBitmap(productImageBitmap);
                        }
                        catch (Exception ex){
                            Log.d("ERROR", "Error in galary picture: " + ex.getMessage());
                        }
                    }
                    break;
            }
            addImageLinearLayout.setVisibility(View.GONE);
            productCloseImageBtn.setVisibility(View.VISIBLE);
            productImageView.setVisibility(View.VISIBLE);
        }
    }

    public Bitmap compressImageTo40(Bitmap imageOriginalBitmap){
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        imageOriginalBitmap.compress(Bitmap.CompressFormat.JPEG,40,bytearrayoutputstream);
        productImageByteArray = bytearrayoutputstream.toByteArray();
        Bitmap bitmap2 = BitmapFactory.decodeByteArray(productImageByteArray,0,productImageByteArray.length);
        return bitmap2;
    }

    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.trim().isEmpty())
            return false;
        return true;
    }

    private void initializeAllTheElements(View rootView) {
        //Database Reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Storage Reference
        storageRef = FirebaseStorage.getInstance().getReference();
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
        addProductImageBtn = rootView.findViewById(R.id.add_product_image_btn);
        //ImageButtons
        productCloseImageBtn = rootView.findViewById(R.id.product_image_close_imgbtn);
        //LinearLayouts
        weightBasedLinearLayout = rootView.findViewById(R.id.weight_based_linearlayout);
        countBasedLinearLayout = rootView.findViewById(R.id.count_based_linearlayout);
        categoryLoadingLinearLayout = rootView.findViewById(R.id.category_loading_linearlayout);
        subcategoryLoadingLinearLayout = rootView.findViewById(R.id.subcategory_loading_linearlayout);
        categoryContentLinearLayout = rootView.findViewById(R.id.category_content_linearlayout);
        subcategoryContentLinearLayout = rootView.findViewById(R.id.subcategory_content_linearlayout);
        addImageLinearLayout = rootView.findViewById(R.id.add_image_linearlayout);
        //RelativeLayout
        subcategoryRelativeLayout = rootView.findViewById(R.id.subcategor_product_relativelayout);
        //RadioGroup
        measureInRadioGroup = rootView.findViewById(R.id.measure_product_radiogroup);
        //RadioButton
        measureCountRadioBtn =  rootView.findViewById(R.id.count_measure_radiobtn);
        measureWtLtrRadioBtn =  rootView.findViewById(R.id.wtltr_measure_radiobtn);
        measureNARadioBtn =  rootView.findViewById(R.id.na_measure_radiobtn);
        //ImageView
        productImageView = rootView.findViewById(R.id.product_imageview);
        //ProgressBar
        saveProductProgressBar = rootView.findViewById(R.id.save_product_progressbar);
    }
}
