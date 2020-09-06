package com.grocery.bhadoriashop.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.grocery.bhadoriashop.MainActivity;
import com.grocery.bhadoriashop.Models.User;
import com.grocery.bhadoriashop.Models.UserAddress;
import com.grocery.bhadoriashop.R;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ProfileFragment extends Fragment {
    EditText emailProfileEditText, fullNameProfileEditText, phoneProfileEditText, addressProfileEditText, addressPincodeProfileEditText;
    RadioGroup genderProfileRadioGroup;
    Button updateProfileBtn;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRefUser;
    FirebaseAuth firebaseAuth;
    View completeScopeView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        completeScopeView = rootView;
        initView(rootView);
        addListeners(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        emailProfileEditText = rootView.findViewById(R.id.email_profile_edittext);
        fullNameProfileEditText = rootView.findViewById(R.id.fullname_profile_edittext);
        phoneProfileEditText = rootView.findViewById(R.id.phone_profile_edittext);
        addressProfileEditText = rootView.findViewById(R.id.address_profile_edittext);
        addressPincodeProfileEditText = rootView.findViewById(R.id.address_pincode_profile_edittext);
        genderProfileRadioGroup = rootView.findViewById(R.id.gender_profile_radiogroup);
        updateProfileBtn = rootView.findViewById(R.id.update_profile_btn);

        //initialize database references
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRefUser = mFirebaseDatabase.getReference("Users");

        //Set Data
        phoneProfileEditText.setText(firebaseAuth.getCurrentUser().getPhoneNumber());
    }

    private void addListeners(View rootView) {
        phoneProfileEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toasty.info(getActivity(), R.string.mobile_number_cannot_update, Toast.LENGTH_LONG, true).show();
                phoneProfileEditText.setEnabled(false);
                return true;
            }
        });

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput()){
                    //Create Address model
                    UserAddress primaryAddress = new UserAddress(addressProfileEditText.getText().toString().trim(),addressPincodeProfileEditText.getText().toString().trim(),true);
                    //prepare address list
                    ArrayList<UserAddress> addressList = new ArrayList<UserAddress>();
                    addressList.add(primaryAddress);
                    //Create final user object
                    User insertUser = new User(firebaseAuth.getCurrentUser().getUid(),
                            fullNameProfileEditText.getText().toString().trim(),
                            phoneProfileEditText.getText().toString().trim(),
                            System.currentTimeMillis(),
                            emailProfileEditText.getText().toString().trim(),
                            ((RadioButton) completeScopeView.findViewById(genderProfileRadioGroup.getCheckedRadioButtonId())).getText().toString().equals("Male"),
                            false,
                            addressList);

                    saveUserDataIntoDB(insertUser);
                }
            }
        });
    }

    private void saveUserDataIntoDB(User userToBeInserted){
        if(firebaseAuth.getCurrentUser() != null) {
            mRefUser.child(firebaseAuth.getCurrentUser().getUid()).setValue(userToBeInserted);
            //Refresh activity on successful profile update and open home fragment
            Intent intent = getActivity().getIntent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("StartWithFragment", new MainActivity().FRAGMENT_HOME);
            startActivity(intent);
        }
        else{
            Toasty.error(getActivity(), R.string.login_required, Toast.LENGTH_LONG, true).show();
        }
    }

    private boolean validateInput(){
        if(emailProfileEditText.getText().toString().trim().isEmpty() || fullNameProfileEditText.getText().toString().trim().isEmpty() || phoneProfileEditText.getText().toString().trim().isEmpty() || addressProfileEditText.getText().toString().trim().isEmpty() || addressPincodeProfileEditText.getText().toString().trim().isEmpty()) {
            Toasty.error(getActivity(), R.string.fill_required_data, Toast.LENGTH_LONG, true).show();
            return false;
        }

        if(addressPincodeProfileEditText.getText().toString().trim().length() != 6){
            Toasty.error(getActivity(), R.string.enter_valid_pincode, Toast.LENGTH_LONG, true).show();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailProfileEditText.getText().toString().trim()).matches()){
            Toasty.error(getActivity(), R.string.enter_valid_email, Toast.LENGTH_LONG, true).show();
            return false;
        }
        return true;
    }
}
