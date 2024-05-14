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

}
