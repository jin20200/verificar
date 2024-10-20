package com.easyplexdemoapp.ui.users;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.auth.UserAuthInfo;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.databinding.ActivityPhoneVerificationBinding;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.login.LoginActivity;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.facebook.login.LoginManager;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;


public class PhoneAuthActivity extends AppCompatActivity implements Injectable {



    ActivityPhoneVerificationBinding binding;

    @Inject
    AuthRepository authRepository;

    @Inject
    AuthManager authManager;

    @Inject
    TokenManager tokenManager;



    private static final String TAG = "PhoneAuthActivity";

    private final long currentTimeOut = 60L;


    private CountDownTimer mCountDownTimer;

    private FirebaseAuth mAuth;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_verification);


        mAuth = FirebaseAuth.getInstance();


        binding.btnVerify.setVisibility(GONE);

        binding.btnSend.setOnClickListener(v -> {



            String fullcode = binding.countryCode.getText().toString()+binding.phone.getText().toString();


            if (binding.phone.getText().toString().isEmpty()){

                Toast.makeText(PhoneAuthActivity.this, "Please complete the 9 digits numbers", Toast.LENGTH_SHORT).show();
                return;

            }


            Toast.makeText(PhoneAuthActivity.this, fullcode, Toast.LENGTH_SHORT).show();


            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(@androidx.annotation.NonNull @NonNull PhoneAuthCredential credential) {

                    Timber.tag(TAG).d("onVerificationCompleted:%s", credential);
                    signInWithPhoneAuthCredential();

                }

                @Override
                public void onVerificationFailed(@androidx.annotation.NonNull @NonNull FirebaseException e) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
                    Timber.tag(TAG).w(e, "onVerificationFailed");

                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // Invalid request

                        Toast.makeText(PhoneAuthActivity.this, R.string.invalid_request, Toast.LENGTH_SHORT).show();

                    } else if (e instanceof FirebaseTooManyRequestsException) {

                        Toast.makeText(PhoneAuthActivity.this, R.string.quota_exceeded, Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(PhoneAuthActivity.this, "onVerificationFailed", Toast.LENGTH_SHORT).show();


                    binding.phone.setText("");
                    binding.btnVerify.setVisibility(GONE);
                    binding.timeOut.setVisibility(GONE);
                    binding.btnSend.setVisibility(VISIBLE);
                    binding.countryCode.setVisibility(VISIBLE);
                }

                @Override
                public void onCodeSent(@androidx.annotation.NonNull @NonNull String verificationId,
                                       @androidx.annotation.NonNull @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    Timber.tag(TAG).d("onCodeSent:%s", verificationId);

                    // Save verification ID and resending token so we can use them later
                    mVerificationId = verificationId;
                    mResendToken = token;


                    Toast.makeText(PhoneAuthActivity.this, R.string.sms_sent, Toast.LENGTH_SHORT).show();

                    binding.phone.setText("");
                    binding.btnVerify.setVisibility(VISIBLE);
                    binding.timeOut.setVisibility(VISIBLE);
                    binding.btnSend.setVisibility(GONE);
                    binding.countryCode.setVisibility(GONE);


                    mCountDownTimer = new CountDownTimer(60000, 1000) {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onTick(long millisUntilFinished) {


                            String text = String.format(Locale.getDefault(), "%02d:%02d",
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                            binding.timeOut.setText(text);


                        }

                        @Override
                        public void onFinish() {

                            binding.timeOut.setText("0");
                            binding.timeOut.setVisibility(GONE);

                            if (mCountDownTimer != null) {

                                mCountDownTimer.cancel();
                                mCountDownTimer = null;
                            }

                        }

                    };

                    mCountDownTimer.start();

                }
            };


            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(fullcode)
                            .setTimeout(currentTimeOut, TimeUnit.SECONDS)
                            .setActivity(PhoneAuthActivity.this)
                            .setCallbacks(mCallbacks)
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);

        });


        String fullcode = binding.countryCode.getText().toString()+binding.phone.getText().toString();


        binding.btnVerify.setOnClickListener(v -> {


            if (binding.phone.getText().toString().isEmpty()){

                Toast.makeText(PhoneAuthActivity.this, R.string.phone_is_empty, Toast.LENGTH_SHORT).show();
                return;

            }

            verifyPhoneNumberWithCode(fullcode);

        });
        binding.smsResend.setOnClickListener(v -> {


            if (mCountDownTimer == null) {

                resendVerificationCode(fullcode, mResendToken);

            }else {


                Toast.makeText(this, R.string.countdown_wait, Toast.LENGTH_SHORT).show();
            }



        });



    }


    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(currentTimeOut, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyPhoneNumberWithCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(task -> {


            if (task.isComplete() || task.isSuccessful()){

                authRepository.getAuth()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<>() {
                            @Override
                            public void onSubscribe(@NotNull Disposable d) {

                                //

                            }

                            @Override
                            public void onNext(@NonNull UserAuthInfo userAuthInfo) {



                                if (userAuthInfo.getVerified() != 1){


                                    authRepository.updateUserStatus(String.valueOf(authManager.getUserInfo().getId()))
                                            .subscribeOn(Schedulers.newThread())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Observer<>() {
                                                @Override
                                                public void onSubscribe(@NotNull Disposable d) {

                                                    //

                                                }

                                                @Override
                                                public void onNext(@NonNull UserAuthInfo userAuthInfo) {


                                                    signInWithPhoneAuthCredential();


                                                }


                                                @Override
                                                public void onError(@NotNull Throwable e) {


                                                    //

                                                }

                                                @Override
                                                public void onComplete() {

                                                    //

                                                }
                                            });


                                }else {


                                    signInWithPhoneAuthCredential();
                                }



                            }


                            @Override
                            public void onError(@NotNull Throwable e) {


                                //

                            }

                            @Override
                            public void onComplete() {

                                //

                            }
                        });





            }else {


                Toast.makeText(PhoneAuthActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void signInWithPhoneAuthCredential() {

        authRepository.getAuth()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                        //

                    }

                    @Override
                    public void onNext(@NonNull UserAuthInfo userAuthInfo) {




                        if (!userAuthInfo.getProfiles().isEmpty()){

                            startActivity(new Intent(PhoneAuthActivity.this, UserProfiles.class));
                            finish();


                        }else {


                            startActivity(new Intent(PhoneAuthActivity.this, BaseActivity.class));
                            finish();

                        }




                    }


                    @Override
                    public void onError(@NotNull Throwable e) {


                        LoginManager.getInstance().logOut();
                        tokenManager.deleteToken();
                        authManager.deleteAuth();
                        startActivity(new Intent(PhoneAuthActivity.this, LoginActivity.class));
                        finish();

                    }

                    @Override
                    public void onComplete() {

                        //

                    }
                });
    }
}
