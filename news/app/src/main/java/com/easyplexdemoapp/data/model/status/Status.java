
package com.easyplexdemoapp.data.model.status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {



    @SerializedName("item")
    @Expose
    private Item item;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("buyer")
    @Expose
    private String buyer;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }



}
