package hcmute.com.blankcil.model;

import java.io.Serializable;

public class ResponseModel<P> implements Serializable {
    private Boolean status;
    private String message;
    private Object body;

    public ResponseModel(Boolean status, String message, Object body) {
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

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
