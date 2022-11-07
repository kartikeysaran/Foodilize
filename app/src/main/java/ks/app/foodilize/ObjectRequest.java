package ks.app.foodilize;

import java.io.Serializable;

public class ObjectRequest implements Serializable {
    private String id;
    private String ngoId;
    private String suppId;
    private String ngoName;
    private String suppName;
    private int deliveryStatus; //0 for order received, 1 for on way to receive order, 2 for order delivered
    private String time;
    private String desc;
    private int quantity; //in kgs
    private String expiryTime;

    public String getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(String expirtTime) {
        this.expiryTime = expirtTime;
    }

    public String getDesc() {
        return desc;
    }

    public ObjectRequest() {

    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ObjectRequest(String ngoId, String suppId, String ngoName, String suppName, int deliveryStatus, String time, int quantity, String desc) {
        this.ngoId = ngoId;
        this.suppId = suppId;
        this.ngoName = ngoName;
        this.suppName = suppName;
        this.deliveryStatus = deliveryStatus;
        this.time = time;
        this.quantity = quantity;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNgoId() {
        return ngoId;
    }

    public void setNgoId(String ngoId) {
        this.ngoId = ngoId;
    }

    public String getSuppId() {
        return suppId;
    }

    public void setSuppId(String suppId) {
        this.suppId = suppId;
    }

    public String getNgoName() {
        return ngoName;
    }

    public void setNgoName(String ngoName) {
        this.ngoName = ngoName;
    }

    public String getSuppName() {
        return suppName;
    }

    public void setSuppName(String suppName) {
        this.suppName = suppName;
    }

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
