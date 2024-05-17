package hcmute.com.blankcil.model;

import java.io.Serializable;
import java.util.List;

public class SearchResponse implements Serializable {
    private Boolean status;
    private String message;
    private Body body;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public static class Body implements Serializable {
        private List<UserModel> users;
        private List<PodcastModel> podcasts;

        public List<UserModel> getUsers() {
            return users;
        }

        public void setUsers(List<UserModel> users) {
            this.users = users;
        }

        public List<PodcastModel> getPodcasts() {
            return podcasts;
        }

        public void setPodcasts(List<PodcastModel> podcasts) {
            this.podcasts = podcasts;
        }
    }
}
