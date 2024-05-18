package hcmute.com.blankcil.model;

import java.io.Serializable;
import java.util.List;

public class CommentModel implements Serializable {
    private int id;
    private String content;
    private String timestamp;
    private UserCommentModel user_comment;
    private int totalLikes;
    private int totalReplies;
    private CommentModel parentComment;
    private List<CommentModel> replies;

    public CommentModel(int id, String content, String timestamp, UserCommentModel user_comment, int totalLikes, int totalReplies, CommentModel parentComment, List<CommentModel> replies) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.user_comment = user_comment;
        this.totalLikes = totalLikes;
        this.totalReplies = totalReplies;
        this.parentComment = parentComment;
        this.replies = replies;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public UserCommentModel getUserComment() {
        return user_comment;
    }

    public void setUserComment(UserCommentModel user_comment) {
        this.user_comment = user_comment;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public int getTotalReplies() {
        return totalReplies;
    }

    public void setTotalReplies(int totalReplies) {
        this.totalReplies = totalReplies;
    }

    public CommentModel getParentComment() {
        return parentComment;
    }

    public void setParentComment(CommentModel parentComment) {
        this.parentComment = parentComment;
    }

    public List<CommentModel> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentModel> replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "CommentModel{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", user_comment='" + user_comment + '\'' +
                ", totalLikes=" + totalLikes +
                ", totalReplies=" + totalReplies +
                ", parentComment=" + parentComment +
                ", replies=" + replies +
                '}';
    }
}
