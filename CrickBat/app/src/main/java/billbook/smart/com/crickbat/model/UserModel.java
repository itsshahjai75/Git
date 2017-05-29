package billbook.smart.com.crickbat.model;

/**
 * Created by Jay-Andriod on 14-Apr-17.
 */

public class UserModel {

    String Name,Mob,email;

    public UserModel(String name, String mob, String email) {
        Name = name;
        Mob = mob;
        this.email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMob() {
        return Mob;
    }

    public void setMob(String mob) {
        Mob = mob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
