package com.easyplexdemoapp.ui.plans;

import static com.easyplexdemoapp.util.Constants.PAYMENT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.easyplexdemoapp.data.model.plans.Plan;
import com.easyplexdemoapp.databinding.ItemPlansBinding;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.payment.Payment;
import com.easyplexdemoapp.ui.payment.PaymentPaypal;
import com.easyplexdemoapp.ui.payment.PaymentStripe;
import com.easyplexdemoapp.util.DialogHelper;

import java.util.List;

/**
 * Adapter for  Plans
 *
 * @author Yobex.
 */
public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.UpcomingViewHolder> {

    private List<Plan> planList;
    private SettingsManager settingsManager;


    @SuppressLint("NotifyDataSetChanged")
    public void addCasts(List<Plan> castList, SettingsManager settingsManager) {
        this.planList = castList;
        this.settingsManager = settingsManager;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ItemPlansBinding binding = ItemPlansBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new PlansAdapter.UpcomingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingViewHolder holder, int position) {
        holder.onBind(position);

    }

    @Override
    public int getItemCount() {
        if (planList != null) {
            return planList.size();
        } else {
            return 0;
        }
    }

    class UpcomingViewHolder extends RecyclerView.ViewHolder {

        private final ItemPlansBinding binding;


        UpcomingViewHolder (@NonNull ItemPlansBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        void onBind(final int position) {

            final Plan plan = planList.get(position);

            Context context = binding.cardviewPayment.getContext();

            binding.planName.setText(plan.getName());

            binding.planPrice.setText(plan.getPrice()+" "+plan.getCurrency());

            binding.planDescription.setText(plan.getDescription());

            binding.cardviewPayment.setOnClickListener(v -> {


                String defaultPayment = settingsManager.getSettings().getDefaultPayment();


                if ("All".equals(defaultPayment)) {

                    Intent intent = new Intent(context, Payment.class);
                    intent.putExtra(PAYMENT, plan);
                    context.startActivity(intent);

                }else if ("Paypal".equals(defaultPayment)) {


                    if (settingsManager.getSettings().getPaypalClientId() !=null) {

                        Intent intent = new Intent(context, PaymentPaypal.class);
                        intent.putExtra(PAYMENT, plan);
                        context.startActivity(intent);

                    } else {

                        DialogHelper.showPaypalWarning(context);


                    }

                }else if ("Stripe".equals(defaultPayment)){

                    Intent intent = new Intent(context, PaymentStripe.class);
                    intent.putExtra(PAYMENT, plan);
                    context.startActivity(intent);


                }

            });
        }
    }


}
