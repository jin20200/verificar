package com.easyplexdemoapp.ui.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.plans.Plan;
import com.easyplexdemoapp.data.remote.ErrorHandling;
import com.easyplexdemoapp.databinding.ActivityPaymentDetailsBinding;
import com.easyplexdemoapp.ui.viewmodels.LoginViewModel;
import com.easyplexdemoapp.ui.splash.SplashActivity;
import com.easyplexdemoapp.util.DialogHelper;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;
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


public class PaymentDetails extends AppCompatActivity {




    ActivityPaymentDetailsBinding binding;

    private LoginViewModel loginViewModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_details);


        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);


        Intent intent = getIntent();
        Plan plan = intent.getParcelableExtra(ARG_PAYMENT);


        try {
            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(intent.getStringExtra("PaymentDetails")));
            showDetails(jsonObject.getJSONObject("response"),plan);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        binding.btnStartWatching.setOnClickListener(view -> {

            startActivity(new Intent(PaymentDetails.this, SplashActivity.class));
            finish();

        });

    }

    private void showDetails(JSONObject response, Plan plan) throws JSONException {


            binding.paymentId.setText(response.getString("id"));
            binding.paymentStatus.setText(response.getString("state"));
            loginViewModel.setSubscription(String.valueOf(plan.getId()),response.getString("id"),
                    plan.getName(),plan.getPackDuration(),"paypal").observe(this, login -> {

            if (login.status == ErrorHandling.Status.SUCCESS) {


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


                    Intent intent = new Intent(PaymentDetails.this, SplashActivity.class);
                    startActivity(intent);
                    finish();


                });


                dialog.show();
                dialog.getWindow().setAttributes(lp);


            } else {


                DialogHelper.erroPayment(this);


            }

        });




    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this, SplashActivity.class));
        finish();

    }
}