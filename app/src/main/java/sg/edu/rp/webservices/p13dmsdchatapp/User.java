package sg.edu.rp.webservices.p13dmsdchatapp;

/**
 * Created by 15017206 on 15/8/2017.
 */

public class User {
    String id;
    String username;

    public User(String uid, String display_name) {
        this.id = uid;
        this.username = display_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
