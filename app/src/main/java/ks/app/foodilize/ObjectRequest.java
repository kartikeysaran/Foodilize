package ks.app.foodilize;

public class ObjectRequest {
    private int id;
    //TODO: Name of NGO and Supplier can be fetched using id from database
    private int ngoId;
    private int suppId;
    private String ngoName;
    private String suppName;
    private int deliveryStatus; //0 for order received, 1 for on way to receive order, 2 for order delivered
    private String time;
    private int quantity; //in kgs

    public ObjectRequest(int id, int ngoId, int suppId, String ngoName, String suppName, int deliveryStatus, String time, int quantity) {
        this.id = id;
        this.ngoId = ngoId;
        this.suppId = suppId;
        this.ngoName = ngoName;
        this.suppName = suppName;
        this.deliveryStatus = deliveryStatus;
        this.time = time;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNgoId() {
        return ngoId;
    }

    public void setNgoId(int ngoId) {
        this.ngoId = ngoId;
    }

    public int getSuppId() {
        return suppId;
    }

    public void setSuppId(int suppId) {
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
