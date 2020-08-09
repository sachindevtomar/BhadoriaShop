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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.grocery.bhadoriashop.Models.ProductCategory;
import com.grocery.bhadoriashop.R;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddCategoryAdminDialog extends DialogFragment implements View.OnClickListener {

    public Activity c;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    public Button addCategoryBtn, addCategoryImagebtn;
    public EditText addCategoryEditText;
    public LinearLayout addImageLinearLayout;
    public RelativeLayout addImageRelativeLayout;
    public ImageView categoryImageView;
    public ImageButton categoryimageCloseBtn;
    Bitmap categoryImageBitmap;
    byte[] categoryImageByteArray;
    public ProgressBar saveCategoryProgressBar;

    public AddCategoryAdminDialog(Activity a) {
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_admin_add_category, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_category_admin_dialog_btn:
                saveCategoryProgressBar.setVisibility(View.VISIBLE);
                addCategoryBtn.setVisibility(View.GONE);
                saveCategoryIntoDB(addCategoryEditText.getText().toString());
//                c.finish();
                break;
            case R.id.add_category_admin_dialog_image_btn:
                selectImage(getContext());
                break;
            case R.id.category_image_admin_dialog_close_imgbtn:
                addImageRelativeLayout.setVisibility(View.GONE);
                addImageLinearLayout.setVisibility(View.VISIBLE);
                categoryImageByteArray = null;
                break;
            default:
                break;
        }
    }

    private void saveCategoryIntoDB(String categoryName) {
        if(categoryName.trim().isEmpty())
        {
            Toasty.error(getContext(), R.string.required_category_name, Toast.LENGTH_LONG, true).show();
            saveCategoryProgressBar.setVisibility(View.GONE);
            addCategoryBtn.setVisibility(View.VISIBLE);
            return;
        }

        try {
            StorageReference imageStorageRef = storageRef.child("ProductCategories").child("Image_" + new Date().getTime());
            if(categoryImageByteArray != null){
                UploadTask uploadTask = imageStorageRef.putBytes(categoryImageByteArray);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toasty.error(getActivity(), R.string.image_upload_failed, Toast.LENGTH_LONG, true).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Get the image download url that needs to be stored in Realtime database
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //Store values in Realtime database of firebase
                                DatabaseReference categoryReference = mDatabase.child("ProductCategories");
                                categoryReference.child(addCategoryEditText.getText().toString()).setValue(new ProductCategory(addCategoryEditText.getText().toString(), String.valueOf(uri)));

                                saveCategoryProgressBar.setVisibility(View.GONE);
                                addCategoryBtn.setVisibility(View.VISIBLE);
                                Toasty.success(getActivity(), R.string.category_saved_with_image, Toast.LENGTH_LONG, true).show();
                                dismiss();
                            }
                        });
                    }
                });
            }
            else{
                DatabaseReference productsReference = mDatabase.child("ProductCategories");
                productsReference.child(addCategoryEditText.getText().toString()).setValue(new ProductCategory(addCategoryEditText.getText().toString(), ""));
                saveCategoryProgressBar.setVisibility(View.GONE);
                addCategoryBtn.setVisibility(View.VISIBLE);
                Toasty.success(getActivity(), R.string.category_saved_without_image, Toast.LENGTH_LONG, true).show();
                dismiss();
            }
            Log.d("TAG", "Product is saved");
        }
        catch (Exception ex) {
            saveCategoryProgressBar.setVisibility(View.GONE);
            addCategoryBtn.setVisibility(View.VISIBLE);
            Toasty.error(getActivity(), R.string.failed_to_save_category, Toast.LENGTH_LONG, true).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        categoryImageBitmap = compressImageTo40(selectedImage);
                        categoryImageView.setImageBitmap(categoryImageBitmap);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImageURI =  data.getData();
                        //productImageView.setImageURI(selectedImage);
                        try {
                            Bitmap imageOriginalBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageURI);
                            categoryImageBitmap = compressImageTo40(imageOriginalBitmap);
                            categoryImageView.setImageBitmap(categoryImageBitmap);
                        }
                        catch (Exception ex){
                            Log.d("ERROR", "Error in galary picture: " + ex.getMessage());
                        }
                    }
                    break;
            }
            addImageLinearLayout.setVisibility(View.GONE);
            addImageRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    public Bitmap compressImageTo40(Bitmap imageOriginalBitmap){
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        imageOriginalBitmap.compress(Bitmap.CompressFormat.JPEG,40,bytearrayoutputstream);
        categoryImageByteArray = bytearrayoutputstream.toByteArray();
        Bitmap bitmap2 = BitmapFactory.decodeByteArray(categoryImageByteArray,0,categoryImageByteArray.length);
        return bitmap2;
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

    private void initViews(View v) {
        addCategoryBtn = v.findViewById(R.id.add_category_admin_dialog_btn);
        addCategoryEditText = v.findViewById(R.id.addcategory_admin_dialog_edittext);
        addCategoryImagebtn = v.findViewById(R.id.add_category_admin_dialog_image_btn);
        addImageLinearLayout = v.findViewById(R.id.add_image_admin_dialog_linearlayout);
        categoryImageView = v.findViewById(R.id.category_admin_dialog_imageview);
        categoryimageCloseBtn = v.findViewById(R.id.category_image_admin_dialog_close_imgbtn);
        addImageRelativeLayout = v.findViewById(R.id.add_image_admin_dialog_relativelayout);
        saveCategoryProgressBar = v.findViewById(R.id.save_category_progressbar);

        //Listens
        addCategoryBtn.setOnClickListener(this);
        addCategoryImagebtn.setOnClickListener(this);
        categoryimageCloseBtn.setOnClickListener(this);
        //Database Reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Storage Reference
        storageRef = FirebaseStorage.getInstance().getReference();
    }
}