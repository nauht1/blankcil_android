package hcmute.com.blankcil.model;

import java.io.Serializable;
import java.util.List;

public class CommentResponse implements Serializable {
    private Boolean status;
    private String message;
    private List<CommentModel> body; // Thay đổi từ Body thành List<CommentModel>

    // Constructor, Getter và Setter methods
    public CommentResponse(Boolean status, String message, List<CommentModel> body) {
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

    public List<CommentModel> getBody() {
        return body;
    }

    public void setBody(List<CommentModel> body) {
        this.body = body;
    }

}
