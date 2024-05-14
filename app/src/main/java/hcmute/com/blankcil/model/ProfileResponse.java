package hcmute.com.blankcil.model;

import java.io.Serializable;

public class ProfileResponse implements Serializable {
    private Boolean status;
    private String message;
    private UserModel body;

    public ProfileResponse(Boolean status, String message, UserModel body) {
        this.status = status;
        this.message = message;
        this.body = body;
    }

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

    public UserModel getBody() {
        return body;
    }

    public void setBody(UserModel body) {
        this.body = body;
    }
}
