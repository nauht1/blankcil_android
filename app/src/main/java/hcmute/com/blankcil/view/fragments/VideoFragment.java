package hcmute.com.blankcil.view.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.model.PodcastModel;

public class VideoFragment extends Fragment {
    private static final String ARG_PODCAST = "podcast";
    private PodcastModel podcast;

    public static VideoFragment newInstance(PodcastModel podcast) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PODCAST, podcast);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            podcast = (PodcastModel) getArguments().getSerializable(ARG_PODCAST);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_item_video, container, false);

        VideoView videoView = view.findViewById(R.id.videoView);
        TextView videoTitle = view.findViewById(R.id.videoTitle);
        TextView videoContent = view.findViewById(R.id.videoContent);

        if (podcast != null) {
            // Set video URI and media controller
            videoView.setVideoURI(Uri.parse(podcast.getAudio_url()));
            MediaController mediaController = new MediaController(getContext());
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.start();

            videoTitle.setText(podcast.getTitle());
            videoContent.setText(podcast.getContent());
        }

        return view;
    }
}
