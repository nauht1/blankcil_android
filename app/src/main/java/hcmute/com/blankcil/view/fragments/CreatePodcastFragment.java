package hcmute.com.blankcil.view.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.config.RetrofitClient;
import hcmute.com.blankcil.constants.APIService;
import hcmute.com.blankcil.model.PodcastModel;
import hcmute.com.blankcil.model.PodcastResponse;
import hcmute.com.blankcil.model.ResponseModel;
import hcmute.com.blankcil.utils.SharedPrefManager;
import hcmute.com.blankcil.view.activities.MainActivity;
import hcmute.com.blankcil.view.adapter.PodcastAdapter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class CreatePodcastFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_AUDIO_REQUEST = 2;
    private static final String TAG = "CreatePodcastFragment";
    private Uri imageUri;
    private Uri audioUri;

    private ImageView imageView;
    private EditText titleEditText;
    private EditText contentEditText;
    private Button selectImageButton;
    private Button selectAudioButton;
    private Button createPodcastButton;
    private APIService apiService;
    private Activity activity;
    ImageButton closeBtn;
    ProgressBar progressBar;
    FloatingActionButton fabCreatePodcast;
    private TextView audioFileNameTextView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_podcast, container, false);
        Log.d(TAG, "onCreateView: Fragment Created");
        activity = getActivity();

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        apiService = retrofitClient.getApi();

        progressBar = ((MainActivity) getActivity()).findViewById(R.id.uploadingProgress);
        fabCreatePodcast = ((MainActivity) getActivity()).findViewById(R.id.fabCreatePodcast);
        fabCreatePodcast.setVisibility(View.GONE);
        imageView = view.findViewById(R.id.imageView);
        titleEditText = view.findViewById(R.id.titleEditText);
        contentEditText = view.findViewById(R.id.contentEditText);
        selectImageButton = view.findViewById(R.id.selectImageButton);
        selectAudioButton = view.findViewById(R.id.selectAudioButton);
        createPodcastButton = view.findViewById(R.id.createPodcastButton);
        audioFileNameTextView = view.findViewById(R.id.audioFileNameTextView);
        selectImageButton.setOnClickListener(v -> openFileChooser(PICK_IMAGE_REQUEST));
        selectAudioButton.setOnClickListener(v -> openFileChooser(PICK_AUDIO_REQUEST));



        closeBtn = view.findViewById(R.id.closeButton);

        // Xử lý nút đóng
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
                fabCreatePodcast.setVisibility(View.VISIBLE);
            }
        });

        createPodcastButton.setOnClickListener(v -> {
            try {
                uploadPodcast();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return view;
    }

    private void closeFragment() {
        getParentFragmentManager().beginTransaction().remove(CreatePodcastFragment.this).commit();

        // Hiển thị lại ViewPager2 và BottomNavigationView
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.findViewById(R.id.viewPager).setVisibility(View.VISIBLE);
            mainActivity.findViewById(R.id.bottomNav).setVisibility(View.VISIBLE);
            mainActivity.findViewById(R.id.bottomAppBar).setVisibility(View.VISIBLE);
        }
    }

    private void openFileChooser(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (requestCode == PICK_IMAGE_REQUEST) {
            intent.setType("image/*");
        } else if (requestCode == PICK_AUDIO_REQUEST) {
            intent.setType("audio/*");
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            } else if (requestCode == PICK_AUDIO_REQUEST) {
                audioUri = data.getData();
                audioFileNameTextView.setText(getFileName(audioUri));
            }
        }
    }

    private void uploadPodcast() throws IOException {
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();

        if (imageUri == null) {
            Toast.makeText(activity, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        if (audioUri == null) {
            Toast.makeText(activity, "Vui lòng chọn audio", Toast.LENGTH_SHORT).show();
            return;
        }

        if (title.isEmpty()) {
            titleEditText.setError("Vui lòng nhập tiêu đề");
            return;
        }

        //Dong form khi an tao
        closeFragment();
        progressBar.setVisibility(View.VISIBLE);
        fabCreatePodcast.setVisibility(View.GONE);

        MultipartBody.Part imagePart = prepareFilePart("imageFile", imageUri);
        MultipartBody.Part audioPart = prepareFilePart("audioFile", audioUri);

        MultipartBody.Part titlePart = MultipartBody.Part.createFormData("title", title);
        MultipartBody.Part contentPart = MultipartBody.Part.createFormData("content", content);

        // Assuming you have a Retrofit instance and API interface set up
        apiService.createPodcast("Bearer " + SharedPrefManager.getInstance(getContext()).getAccessToken(),
                titlePart, contentPart, imagePart, audioPart).enqueue(new Callback<ResponseModel<PodcastModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<PodcastModel>> call, retrofit2.Response<ResponseModel<PodcastModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    fabCreatePodcast.setVisibility(View.VISIBLE);
                    Toast.makeText(activity, "Đã đăng tải thành công!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(activity, "Thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<PodcastModel>> call, Throwable t) {
                Log.d("CreatPodcast", "Failed" + t.getMessage());
            }
        });
    }

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) throws IOException {
        InputStream inputStream = getContext().getContentResolver().openInputStream(fileUri);
        byte[] fileContent = new byte[inputStream.available()];
        inputStream.read(fileContent);
        RequestBody requestBody = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(fileUri)), fileContent);
        return MultipartBody.Part.createFormData(partName, getFileName(fileUri), requestBody);
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}