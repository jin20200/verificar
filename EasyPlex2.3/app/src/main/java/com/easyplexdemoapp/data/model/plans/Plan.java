package com.easyplexdemoapp.data.model.plans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Plan implements Parcelable {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;



    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("price")
    @Expose
    private String price;


    @SerializedName("stripe_plan_id")
    @Expose
    private String stripePlanId;

    public String getRazorpayPlanId() {
        return razorpayPlanId;
    }

    public void setRazorpayPlanId(String razorpayPlanId) {
        this.razorpayPlanId = razorpayPlanId;
    }

    @SerializedName("razorpay_plan_id")
    @Expose
    private String razorpayPlanId;


    @SerializedName("stripe_price_id")
    @Expose
    private String stripePlanPrice;

    @SerializedName("currency_plan_app")
    @Expose
    private String currency;



    @SerializedName("pack_duration")
    @Expose
    private String packDuration;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;


    protected Plan(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        description = in.readString();
        price = in.readString();
        stripePlanId = in.readString();
        stripePlanPrice = in.readString();
        currency = in.readString();
        packDuration = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    public static final Creator<Plan> CREATOR = new Creator<Plan>() {
        @Override
        public Plan createFromParcel(Parcel in) {
            return new Plan(in);
        }

        @Override
        public Plan[] newArray(int size) {
            return new Plan[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getstripePlanId() {
        return stripePlanId;
    }

    public void setStripePlanId(String stripePlanId) {
        this.stripePlanId = stripePlanId;
    }

    public String getStripePlanPrice() {
        return stripePlanPrice;
    }

    public void setStripePlanPrice(String stripePlanPrice) {
        this.stripePlanPrice = stripePlanPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPackDuration() {
        return packDuration;
    }

    public void setPackDuration(String packDuration) {
        this.packDuration = packDuration;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(price);
        dest.writeString(stripePlanId);
        dest.writeString(stripePlanPrice);
        dest.writeString(currency);
        dest.writeString(packDuration);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }
}
