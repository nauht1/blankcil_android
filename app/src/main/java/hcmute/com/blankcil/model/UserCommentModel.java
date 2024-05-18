package hcmute.com.blankcil.model;

import java.io.Serializable;

public class UserCommentModel implements Serializable {
    private int id;
    private String fullname;
    private String avatar_url;
    private String email;

    public UserCommentModel(int id, String fullname, String avatar_url, String email) {
        this.id = id;
        this.fullname = fullname;
        this.avatar_url = avatar_url;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
