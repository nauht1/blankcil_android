package hcmute.com.blankcil.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class PodcastModel implements Serializable {
    private int id;
    private String title;
    private String thumbnail_url;
    private String audio_url;
    private String content;
    private boolean hasLiked;
    private Date createDay;
    private int numberOfComments;
    private int numberOfLikes;
    private UserModel user_podcast;

    public PodcastModel(int id, String title, String thumbnail_url, String audio_url, String content, boolean hasLiked, Date createDay, int numberOfComments, int numberOfLikes, UserModel user_podcast) {
        this.id = id;
        this.title = title;
        this.thumbnail_url = thumbnail_url;
        this.audio_url = audio_url;
        this.content = content;
        this.hasLiked = hasLiked;
        this.createDay = createDay;
        this.numberOfComments = numberOfComments;
        this.numberOfLikes = numberOfLikes;
        this.user_podcast = user_podcast;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isHasLiked() {
        return hasLiked;
    }

    public void setHasLiked(boolean hasLiked) {
        this.hasLiked = hasLiked;
    }

    public Date getCreateDay() {
        return createDay;
    }

    public void setCreateDay(Date createDay) {
        this.createDay = createDay;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public UserModel getUser_podcast() {
        return user_podcast;
    }

    public void setUser_podcast(UserModel user_podcast) {
        this.user_podcast = user_podcast;
    }

    @Override
    public String toString() {
        return "PodcastModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", thumbnail_url='" + thumbnail_url + '\'' +
                ", audio_url='" + audio_url + '\'' +
                ", content='" + content + '\'' +
                ", hasLiked=" + hasLiked +
                ", createDay=" + createDay +
                ", numberOfComments=" + numberOfComments +
                ", numberOfLikes=" + numberOfLikes +
                ", user_podcast=" + user_podcast +
                '}';
    }
}
