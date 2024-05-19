package hcmute.com.blankcil.constants;

public interface Interface {
    public interface OnCommentCountChangeListener {
        void onCommentCountChanged(int podcastId, int newCommentCount);
    }

    public interface OnAvatarClickListener {
        void onAvatarClick(int userId);
    }
}
