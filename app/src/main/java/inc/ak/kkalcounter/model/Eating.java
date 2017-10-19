package inc.ak.kkalcounter.model;

import java.io.Serializable;


public class Eating implements Serializable {

    private String name;
    private String type;
    private String id;
    private String fat;
    private String carb;
    private String prot;
    private String kcal;
    private String img;
    private String date;
    private String prodName;
    private String userId;


    public Eating(String type, String fat, String carb, String prot, String kcal,String date,String user_id){
        this.name=name;
        this.type=type;
        this.fat=fat;
        this.carb=carb;
        this.prot=prot;
        this.kcal=kcal;
        this.date=date;
        this.userId=user_id;
    }

    public String getUser_id() {
        return userId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getCarb() {
        return carb;
    }

    public String getType() {
        return type;
    }

    public String getFat() {
        return fat;
    }

    public String getId() {
        return id;
    }

    public String getProt() {
        return prot;
    }

    public void setProt(String prot) {
        this.prot = prot;
    }

    public void setCarb(String carb) {
        this.carb = carb;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }
}
