package hcmute.com.blankcil.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserModel implements Serializable {
    private Integer id;
    private String fullname;
    private String avatar_url;
    private String cover_url;
    private String email;
    private Date birthday;
    private String address;
    private String phone;
    private List<PodcastModel> podcasts;

    public UserModel(Integer id, String fullname, String avatar_url, String cover_url, String email, Date birthday, String address, String phone, List<PodcastModel> podcasts) {
        this.id = id;
        this.fullname = fullname;
        this.avatar_url = avatar_url;
        this.cover_url = cover_url;
        this.email = email;
        this.birthday = birthday;
        this.address = address;
        this.phone = phone;
        this.podcasts = podcasts;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<PodcastModel> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(List<PodcastModel> podcasts) {
        this.podcasts = podcasts;
    }
}
