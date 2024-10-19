package com.easyplexdemoapp.ui.payment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.plans.Plan;
import com.easyplexdemoapp.data.remote.ErrorHandling;
import com.easyplexdemoapp.databinding.PaymentStripeBinding;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.splash.SplashActivity;
import com.easyplexdemoapp.ui.viewmodels.LoginViewModel;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.Tools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.StripeIntent.Status;
import com.stripe.android.view.CardInputWidget;
import org.jetbrains.annotations.NotNull;
import java.lang.ref.WeakReference;
import javax.inject.Inject;
import dagger.android.AndroidInjection;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.easyplexdemoapp.util.Constants.ARG_PAYMENT;

/**
 * HoneyStream - Android Movie Portal App
 * @package     HoneyStream - Android Movie Portal App
 * @author      @Y0bEX
 * @copyright   Copyright (c) 2020 Y0bEX,
 * @license     http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile     https://codecanyon.net/user/yobex
 * @link        yobexd@gmail.com
 * @skype       yobexd@gmail.com
 **/


public class PaymentStripe extends AppCompatActivity {
    PaymentStripeBinding binding;
    private LoginViewModel loginViewModel;

    private Stripe mStripe;


    @Inject
    SettingsManager settingsManager;

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    private String planId;
    private String planPrice;
    private String planName;
    private String planDuraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidInjection.inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.payment_stripe);

        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);


        Intent intent = getIntent();
        Plan plan = intent.getParcelableExtra(ARG_PAYMENT);


        this.planId =  plan.getstripePlanId();
        this.planPrice = plan.getStripePlanPrice();
        this.planName = plan.getName();
        this.planDuraction = plan.getPackDuration();


        Tools.hideSystemPlayerUi(this, true, 0);

        Tools.setSystemBarTransparent(this);

        if (settingsManager.getSettings().getStripePublishableKey() !=null ) {
            PaymentConfiguration.init(PaymentStripe.this, settingsManager.getSettings().getStripePublishableKey());
        }

        if (settingsManager.getSettings().getStripePublishableKey() !=null ) {
            mStripe = new Stripe(PaymentStripe.this, settingsManager.getSettings().getStripePublishableKey());
        }

            // Hook up the pay button to the card widget and stripe instance

        binding.sumbitSubscribe.setOnClickListener(v -> {

            binding.formContainer.setVisibility(View.GONE);
            binding.loader.setVisibility(View.VISIBLE);


                PaymentMethodCreateParams params = binding.cardInputWidget.getPaymentMethodCreateParams();

                 binding.cardInputWidget.setCardHint("12121");

                if (params == null) {
                    return;
                }
                mStripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onSuccess(@NonNull PaymentMethod result) {
                        
                        onSuccessPayment(result.id, null);
                    }

                    @Override
                    public void onError(@NonNull Exception e) {

                        Toast.makeText(PaymentStripe.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Handle the result of stripe.confirmPayment
        super.onActivityResult(requestCode, resultCode, data);
        mStripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));

    }

    private class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
        private final WeakReference<PaymentStripe> activityRef;

        PaymentResultCallback(@NonNull PaymentStripe activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final PaymentStripe activity = activityRef.get();
            if (activity == null) {
                return;
            }

            PaymentIntent paymentIntent = result.getIntent();
            Status status = paymentIntent.getStatus();
            if (status == Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String data = gson.toJson(paymentIntent);

                onSuccessPayment(data, status);

            } else if (status == Status.RequiresPaymentMethod) {
                Toast.makeText(activity, "Payment failed", Toast.LENGTH_SHORT).show();
            }
        }





        @Override
        public void onError(@NotNull Exception e) {


            //

        }
    }



    public void onSuccessPayment(@Nullable String paymentMethodId, Status status){

        loginViewModel.getSubscribePlan(paymentMethodId, planId, planPrice, planName, planDuraction).observe(this, login -> {

            if (login.status == ErrorHandling.Status.SUCCESS) {


                binding.formContainer.setVisibility(View.GONE);
                binding.loader.setVisibility(View.GONE);


                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog.setContentView(R.layout.dialog_success_payment);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WRAP_CONTENT;
                lp.height = WRAP_CONTENT;

                dialog.findViewById(R.id.btn_start_watching).setOnClickListener(v -> {


                    Intent intent = new Intent(PaymentStripe.this, SplashActivity.class);
                    startActivity(intent);
                    finish();


                });


                dialog.show();
                dialog.getWindow().setAttributes(lp);


            } else {


                binding.formContainer.setVisibility(View.VISIBLE);
                binding.loader.setVisibility(View.GONE);

                DialogHelper.erroPayment(this);


            }

        });


    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}