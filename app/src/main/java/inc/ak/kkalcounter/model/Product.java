package inc.ak.kkalcounter.model;

import java.io.Serializable;



public class Product implements Serializable {

    private String name;
    private String id;
    private String fat;
    private String carb;
    private String prot;
    private String kcal;
    private String img;

    public Product(String name,String fat,String carb,String prot,String kcal,String img){
        this.name=name;
        this.fat=fat;
        this.carb=carb;
        this.prot=prot;
        this.kcal=kcal;
        this.img=img;
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

    public String getCarb() {
        return carb;
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
