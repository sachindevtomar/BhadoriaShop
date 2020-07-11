package com.grocery.bhadoriashop.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.grocery.bhadoriashop.R;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class LoginFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    EditText phoneEditText, otpEditText;
    CountryCodePicker countryCodePicker;
    Button sendOTPBtn, loginBtn;
    ProgressBar loadingOTPProgressBar;
    String verificationId;
    PhoneAuthProvider.ForceResendingToken forceToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        //Initializing objects
        firebaseAuth = FirebaseAuth.getInstance();
        phoneEditText = rootView.findViewById(R.id.phone_login_edittext);
        otpEditText = rootView.findViewById(R.id.otp_login_edittext);
        countryCodePicker = rootView.findViewById(R.id.country_code_picker);
        sendOTPBtn = rootView.findViewById(R.id.sendotp_login_btn);
        loginBtn = rootView.findViewById(R.id.login_login_btn);
        loadingOTPProgressBar = rootView.findViewById(R.id.sendotp_login_progressbar);

        sendOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!phoneEditText.getText().toString().isEmpty() && phoneEditText.getText().toString().length() == 10){
                    String completePhoneNumber =  countryCodePicker.getSelectedCountryCodeWithPlus()+phoneEditText.getText().toString();
                    Log.d("TAG","OnClick: Phone No -> "+completePhoneNumber);
                    loadingOTPProgressBar.setVisibility(View.VISIBLE);
                    sendOTPBtn.setVisibility(View.GONE);
                    requestOTP(completePhoneNumber);
                }
                else{
                    Toast.makeText(getContext(), R.string.EnterValidPhoneNumber, Toast.LENGTH_LONG);
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpEntered = otpEditText.getText().toString();
                if(!otpEntered.isEmpty() && otpEntered.length() == 6){
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otpEntered);
                    verifyAuth(credential);
                }
                else{
                    Toast.makeText(getContext(), R.string.EnterTheOTP, Toast.LENGTH_LONG);
                }
            }
        });

        return rootView;
    }

    private void requestOTP(String completePhoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(completePhoneNumber, 60L, TimeUnit.SECONDS, getActivity(), new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                forceToken = forceResendingToken;
                Log.d("TAG","OTP is received");
                loginBtn.setEnabled(true);
                loadingOTPProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                loadingOTPProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d("TAG","Auth is done in the OnVerificationCompleted Method");
                otpEditText.setText(phoneAuthCredential.getSmsCode());
                verifyAuth(phoneAuthCredential);
                loadingOTPProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getContext(), "Can not create account : "+ e.getMessage(), Toast.LENGTH_LONG).show();
                sendOTPBtn.setVisibility(View.VISIBLE);
                loadingOTPProgressBar.setVisibility(View.GONE);
            }
        });
    }


    private void verifyAuth(PhoneAuthCredential credential) {
        Log.d("TAG","Reached to verify auth");
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    NavigationView navigationView = getActivity().findViewById(R.id.navigationView);
                    navigationView.getMenu().findItem(R.id.logout_menu).setVisible(true);
                    navigationView.getMenu().findItem(R.id.login_menu).setVisible(false);
                    Log.d("TAG","Reached to verify auth Success");
                }
                else {
                    Log.d("TAG","Reached to verify auth Failure");
                }
            }
        });
    }
}
