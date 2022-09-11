package ks.app.foodilize;

public class ObjectNC {
    private int id;
    private String name;
    private String address;
    private String contact_name;
    private String contact_number;
    private double lat;
    private double lon;
    private String imgUrl;
    private int type; //0 is for NGO and 1 is for Supplier

    public ObjectNC(int id, String name, String address, String contact_name, String contact_number, double lat, double lon, String imgUrl, int type) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact_name = contact_name;
        this.contact_number = contact_number;
        this.lat = lat;
        this.lon = lon;
        this.imgUrl = imgUrl;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
