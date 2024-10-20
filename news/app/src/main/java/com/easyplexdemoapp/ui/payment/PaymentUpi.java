package com.easyplexdemoapp.ui.payment;

import static com.easyplexdemoapp.util.Constants.ARG_PAYMENT;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.plans.Plan;
import com.easyplexdemoapp.databinding.PaymentPaypalBinding;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.viewmodels.LoginViewModel;
import com.easyplexdemoapp.util.Tools;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

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


public class PaymentUpi extends AppCompatActivity {


    PaymentPaypalBinding binding;


    @Inject
    SettingsManager settingsManager;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private LoginViewModel loginViewModel;

    private Plan plan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);


        binding = DataBindingUtil.setContentView(this, R.layout.payment_paypal);


        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);


        Intent intent = getIntent();
        plan = intent.getParcelableExtra(ARG_PAYMENT);

        Tools.hideSystemPlayerUi(this, true, 0);

        Tools.setSystemBarTransparent(this);


        onInitialize();



    }

    private void onInitialize() {


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}