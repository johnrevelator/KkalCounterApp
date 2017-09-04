package inc.ak.kkalcounter.model;

import java.io.Serializable;

/**
 * Created by alexanderklimov on 9/2/17.
 */

public class User implements Serializable {
    private String id;
    private String userName;
    private String email;
    private String height;
    private String weight;
    private String age;
    private Integer sex;

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getHeight() {
        return height;
    }

    public String getId() {
        return id;
    }

    public Integer getSex() {
        return sex;
    }

    public String getUserName() {
        return userName;
    }

    public String getWeight() {
        return weight;
    }
    public User(String userName,String email,String height,String weight,String age, Integer sex){
        this.age=age;
        this.id=id;
        this.userName=userName;
        this.email=email;
        this.height=height;
        this.weight=weight;
        this.sex=sex;
    }
}
