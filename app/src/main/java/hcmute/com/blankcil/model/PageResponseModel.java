package hcmute.com.blankcil.model;

import java.util.List;

public class PageResponseModel<T> {
    private List<T> content;
    private int currentPage;
    private int totalPage;

    public PageResponseModel(List<T> content, int currentPage, int totalPage) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
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
