package com.easyplexdemoapp.ui.payment;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.easyplexdemoapp.util.Constants.ARG_PAYMENT;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.easyplexdemoapp.BuildConfig;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.plans.Plan;
import com.easyplexdemoapp.data.remote.ErrorHandling;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.databinding.PaymentActivityBinding;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.splash.SplashActivity;
import com.easyplexdemoapp.ui.viewmodels.LoginViewModel;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.Tools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.PaymentButtonIntent;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.StripeIntent.Status;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

/**
 * EasyPlex - Movies - Live Streaming - TV Series, Anime
 *
 * @author @Y0bEX
 * @package EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2023 Y0bEX,
 * @license <a href="http://codecanyon.net/wiki/support/legal-terms/licensing-terms/">...</a>
 * @profile <a href="https://codecanyon.net/user/yobex">...</a>
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/


public class Payment extends AppCompatActivity {



    PaymentActivityBinding binding;

    @Inject
    AuthRepository authRepository;

    private LoginViewModel loginViewModel;


    private Stripe mStripe;


    @Inject
    SettingsManager settingsManager;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private Plan plan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.payment_activity);

        onInitPaypal();
        onInitPagseguro();

        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        Intent intent = getIntent();

        plan = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? intent.getParcelableExtra(ARG_PAYMENT, Plan.class) : intent.getParcelableExtra(ARG_PAYMENT);


        Tools.hideSystemPlayerUi(this, true, 0);

        Tools.setSystemBarTransparent(this);

        if (settingsManager.getSettings().getStripePublishableKey() !=null ) {
            PaymentConfiguration.init(Payment.this, settingsManager.getSettings().getStripePublishableKey());
        }

        if (settingsManager.getSettings().getStripePublishableKey() !=null ) {
            mStripe = new Stripe(getApplicationContext(), settingsManager.getSettings().getStripePublishableKey());
        }

            // Hook up the pay button to the card widget and stripe instance

            binding.sumbitSubscribe.setOnClickListener(v -> {

                binding.formContainer.setVisibility(View.GONE);
                binding.loader.setVisibility(View.VISIBLE);


                PaymentMethodCreateParams params = binding.cardInputWidget.getPaymentMethodCreateParams();

                binding.cardInputWidget.setCardHint("12121");

                mStripe.createPaymentMethod(params, new ApiResultCallback<>() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onSuccess(@NonNull PaymentMethod result) {


                        onSuccessPayment(result.id, null);

                    }

                    @Override
                    public void onError(@NonNull Exception e) {

                        Toast.makeText(Payment.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            });



        onLoadPaypal();

    }

    private void onInitPagseguro() {




    }


    private void onInitPaypal() {


        binding.paypalMethodButton.setOnClickListener(v -> binding.paypalMethod.performClick());


        if (settingsManager.getSettings().getPaypalClientId() !=null &&

                !settingsManager.getSettings().getPaypalClientId().isEmpty() &&
                settingsManager.getSettings().getPaypalCurrency() !=null &&
                !settingsManager.getSettings().getPaypalCurrency().isEmpty()) {

            CheckoutConfig config = new CheckoutConfig(getApplication(),settingsManager.getSettings().getPaypalClientId(),
                    Environment.LIVE,CurrencyCode.valueOf(settingsManager.getSettings().getPaypalCurrency()),UserAction.PAY_NOW,
                    PaymentButtonIntent.CAPTURE,new SettingsConfig(true,false),String.format("%s://paypalpay", BuildConfig.APPLICATION_ID));
            PayPalCheckout.setConfig(config);
        }
    }


    private void onLoadPaypal() {


        binding.paypalMethod.setVisibility(View.GONE);

        binding.paypalMethod.setup(
                createOrderActions -> {
                    ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();

                    purchaseUnits.add(
                            new PurchaseUnit.Builder()
                                    .amount(
                                            new Amount.Builder()
                                                    .currencyCode(CurrencyCode.valueOf(settingsManager.getSettings().getPaypalCurrency()))
                                                    .value(plan.getPrice())
                                                    .build()
                                    ).description(plan.getDescription())
                                    .build()
                    );


                    OrderRequest order = new OrderRequest(
                            OrderIntent.CAPTURE,
                            new AppContext.Builder()
                                    .userAction(UserAction.PAY_NOW)
                                    .build(),
                            purchaseUnits
                    );

                    createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                },
                approval -> approval.getOrderActions().capture(result -> {
                    Timber.i("CaptureOrderResult: %s", result);

                    loginViewModel.setSubscription(String.valueOf(plan.getId()), "1", plan.getName(), plan.getPackDuration(), "paypal").observe(Payment.this, login -> {

                        if (login.status == ErrorHandling.Status.SUCCESS) {


                            binding.formContainer.setVisibility(View.GONE);
                            binding.loader.setVisibility(View.GONE);


                            final Dialog dialog = new Dialog(this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_success_payment);
                            dialog.setCancelable(false);
                            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());

                            lp.gravity = Gravity.BOTTOM;
                            lp.width = MATCH_PARENT;
                            lp.height = MATCH_PARENT;


                            dialog.findViewById(R.id.btn_start_watching).setOnClickListener(v -> {

                                Intent intent = new Intent(Payment.this, SplashActivity.class);
                                startActivity(intent);
                                finish();


                            });


                            dialog.show();
                            dialog.getWindow().setAttributes(lp);
                            dialog.findViewById(R.id.bt_close).setOnClickListener(x -> {
                                Intent intent = new Intent(Payment.this, SplashActivity.class);
                                startActivity(intent);
                                finish();
                            });
                            dialog.show();
                            dialog.getWindow().setAttributes(lp);


                        } else {


                            binding.formContainer.setVisibility(View.VISIBLE);
                            binding.loader.setVisibility(View.GONE);

                            DialogHelper.erroPayment(Payment.this);


                        }

                    });
                })


        );
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Handle the result of stripe.confirmPayment
        mStripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
        super.onActivityResult(requestCode, resultCode, data);

    }




    private class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
        private final WeakReference<Payment> activityRef;

        PaymentResultCallback(@NonNull Payment activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final Payment activity = activityRef.get();
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

        loginViewModel.getSubscribePlan(paymentMethodId, String.valueOf(plan.getId()), plan.getStripePlanPrice(), plan.getName(), plan.getPackDuration()).observe(this, login -> {

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

                    Intent intent = new Intent(Payment.this, SplashActivity.class);
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