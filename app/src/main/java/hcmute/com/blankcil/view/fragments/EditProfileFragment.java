package hcmute.com.blankcil.view.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.config.RetrofitClient;
import hcmute.com.blankcil.constants.APIService;
import hcmute.com.blankcil.model.PodcastModel;
import hcmute.com.blankcil.model.ResponseModel;
import hcmute.com.blankcil.model.UserModel;
import hcmute.com.blankcil.utils.SharedPrefManager;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditProfileFragment extends Fragment {
    private static final int PICK_AVATAR_REQUEST = 1;
    private static final int PICK_BANNER_REQUEST = 2;
    private static final String TAG = "EditProfileFragment";
    private Uri avatarUri;
    private Uri bannerUri;
    private ImageView imgBack;
    private ImageView imgBanner;
    private ImageView imgAvatar;
    private ImageView imgCalendar;
    private TextView txtChangeBanner;
    private TextView txtChangeAvatar;
    private TextView txtEmal;
    private EditText edtFullname;
    private EditText edtBirthday;
    private EditText edtAddress;
    private EditText edtPhone;
    private Button btnSave;
    private APIService apiService;
    private Activity activity;

    @Override
    public void onResume() {
        //Chỉnh cho cố định màn hình khi click vào EditText
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_editprofile, container, false);
        Log.d(TAG, "onCreateView: Fragment Created");
        activity = getActivity();
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        apiService = retrofitClient.getApi();

        imgBack = viewRoot.findViewById(R.id.imgBack);
        imgBanner = viewRoot.findViewById(R.id.imgEditBanner);
        imgAvatar = viewRoot.findViewById(R.id.imgEditAvatar);
        imgCalendar = viewRoot.findViewById(R.id.imgCalendar);
        txtChangeAvatar = viewRoot.findViewById(R.id.txtChangeAvatar);
        txtChangeBanner = viewRoot.findViewById(R.id.txtChangeBanner);

        txtChangeAvatar.setOnClickListener(v->openFileChooser(PICK_AVATAR_REQUEST));
        txtChangeBanner.setOnClickListener(v->openFileChooser(PICK_BANNER_REQUEST));

        txtEmal = viewRoot.findViewById(R.id.txtEmail);
        edtFullname = viewRoot.findViewById(R.id.edtFullname);
        edtBirthday = viewRoot.findViewById(R.id.edtBirthday);
        edtAddress = viewRoot.findViewById(R.id.edtAddress);
        edtPhone = viewRoot.findViewById(R.id.edtPhone);
        btnSave = viewRoot.findViewById(R.id.btnSave);

        imgCalendar = viewRoot.findViewById(R.id.imgCalendar);
        imgCalendar.setOnClickListener(v->{
            // Lấy ngày sinh nhật hiện tại hoặc ngày mặc định nếu không có
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            // Tạo DatePickerDialog và thiết lập ngày mặc định
            DatePickerDialog datePickerDialog = new DatePickerDialog(activity, (view, year1, monthOfYear, dayOfMonth1) -> {
                // Cập nhật EditText edtBirthday với ngày mới được chọn
                String newBirthday = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth1, monthOfYear + 1, year1);
                edtBirthday.setText(newBirthday);
            }, year, month, dayOfMonth);

            // Hiển thị DatePickerDialog
            datePickerDialog.show();
        });

        imgBack = viewRoot.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy ra FragmentManager
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                // Xác định Fragment AccountFragment
                Fragment accountFragment = new AccountFragment();

                // Thay thế Fragment hiện tại bằng Fragment AccountFragment
                fragmentManager.beginTransaction()
                        .replace(R.id.container, accountFragment)
                        .commit();
            }
        });

        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getContext());
        loadUserProfile(sharedPrefManager);
        btnSave.setOnClickListener(v->{
            try{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    updateProfile();
                }
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        });
        return viewRoot;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateProfile() throws IOException {
        String fullname = edtFullname.getText().toString();
        String birthdayString = edtBirthday.getText().toString();
        String address = edtAddress.getText().toString();
        String phone = edtPhone.getText().toString();

        // Chuyển đổi chuỗi ngày tháng sang LocalDate
        LocalDate birthday = null;
        try {
            DateTimeFormatter formatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                birthday = LocalDate.parse(birthdayString, formatter);
            }
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Invalid birthday format!", Toast.LENGTH_SHORT).show();
            return;
        }

        MultipartBody.Part imgAvatar = null;
        if (avatarUri != null) {
            imgAvatar = prepareFilePart("avatarImage", avatarUri);
        }

        MultipartBody.Part imgBanner = null;
        if (bannerUri != null) {
            imgBanner = prepareFilePart("coverImage", bannerUri);
        }

        MultipartBody.Part fullnamePart = MultipartBody.Part.createFormData("fullname", fullname);
        MultipartBody.Part birthdayPart = MultipartBody.Part.createFormData("birthday", birthday.toString());
        MultipartBody.Part addressPart = MultipartBody.Part.createFormData("address", address);
        MultipartBody.Part phonePart = MultipartBody.Part.createFormData("phone", phone);

        apiService.updateUserProfile("Bearer " + SharedPrefManager.getInstance(getContext()).getAccessToken(),
                fullnamePart, birthdayPart, addressPart, phonePart, imgAvatar, imgBanner).enqueue(new Callback<ResponseModel<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<UserModel>> call, Response<ResponseModel<UserModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(activity, "Update profile successfully!", Toast.LENGTH_SHORT).show();
                    loadUserProfile(SharedPrefManager.getInstance(getContext()));
                    SharedPrefManager.getInstance(getContext()).saveUserModel(response.body().getBody());
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                    // Xác định Fragment AccountFragment
                    Fragment accountFragment = new AccountFragment();

                    // Thay thế Fragment hiện tại bằng Fragment AccountFragment
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, accountFragment)
                            .commit();

                }
                else {
                    Toast.makeText(activity, "Update profile failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<UserModel>> call, Throwable t) {
                Log.d("UpdateProfile", "Failed" + t.getMessage());
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

    private void openFileChooser(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (requestCode == PICK_AVATAR_REQUEST) {
            intent.setType("image/*");
        } else if (requestCode == PICK_BANNER_REQUEST) {
            intent.setType("image/*");
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            if (requestCode == PICK_AVATAR_REQUEST) {
                avatarUri = data.getData();
                imgAvatar.setImageURI(avatarUri);
            } else if (requestCode == PICK_BANNER_REQUEST) {
                bannerUri = data.getData();
                imgBanner.setImageURI(bannerUri);
            }
        }
    }

    private void loadUserProfile(SharedPrefManager sharedPrefManager){
        UserModel userModel = sharedPrefManager.getUserModel();
        if(userModel!=null){
            edtFullname.setText(userModel.getFullname());
            txtEmal.setText(userModel.getEmail());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            if (userModel.getBirthday() != null) {
                String formattedBirthday = sdf.format(userModel.getBirthday());
                edtBirthday.setText(formattedBirthday);
            }
            edtAddress.setText(userModel.getAddress());
            edtPhone.setText(userModel.getPhone());
            String avatarURL = userModel.getAvatar_url();
            String coverURL = userModel.getCover_url();

            if(avatarURL!=null && !avatarURL.isEmpty()){
                Glide.with(this)
                        .load(avatarURL)
                        .into(imgAvatar);
            } else {
                //nếu avatar chưa có ảnh thì để ảnh mặc định theo link bên dưới
                Glide.with(this)
                        .load("https://cdn3.vectorstock.com/i/1000x1000/80/92/avatar-user-basic-blue-dotted-line-icon-vector-25358092.jpg")
                        .into(imgAvatar);
            }

            if(coverURL!=null && !coverURL.isEmpty()){
                Glide.with(this)
                        .load(coverURL)
                        .into(imgBanner);
            } else {
                //nếu banner chưa có ảnh thì để ảnh mặc định theo link bên dưới
                Glide.with(this)
                        .load("https://as1.ftcdn.net/v2/jpg/04/62/76/74/1000_F_462767413_4gRTpH3q2U9DUXdK3UubkOeFXsnUlENd.jpg")
                        .into(imgBanner);
            }

        }
    }
}
