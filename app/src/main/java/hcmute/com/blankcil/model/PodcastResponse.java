package hcmute.com.blankcil.model;

import java.io.Serializable;
import java.util.List;

public class PodcastResponse implements Serializable {
    private Boolean status;
    private String message;
    private Body body;

    // Constructor, Getter và Setter methods

    public PodcastResponse(Boolean status, String message, Body body) {
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

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public static class Body implements Serializable {
        private List<PodcastModel> content;
        private int currentPage;
        private int totalPage;

        // Constructor, Getter và Setter methods

        public Body(List<PodcastModel> content, int currentPage, int totalPage) {
            this.content = content;
            this.currentPage = currentPage;
            this.totalPage = totalPage;
        }

        public List<PodcastModel> getContent() {
            return content;
        }

        public void setContent(List<PodcastModel> content) {
            this.content = content;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }
    }
}
