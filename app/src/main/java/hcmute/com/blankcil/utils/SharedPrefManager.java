package hcmute.com.blankcil.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import hcmute.com.blankcil.model.UserModel;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_USER_MODEL = "user_model";
    private static final String KEY_USER_EMAIL = "user_email";
    private static SharedPrefManager instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void saveTokens(String accessToken, String refreshToken) {
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.apply();
    }

    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null);
    }

    public void saveUserModel(UserModel userModel) {
        Gson gson = new Gson();
        String userModelJson = gson.toJson(userModel);
        editor.putString(KEY_USER_MODEL, userModelJson);
        editor.apply();
    }

    public UserModel getUserModel() {
        Gson gson = new Gson();
        String userModelJson = sharedPreferences.getString(KEY_USER_MODEL, null);
        return gson.fromJson(userModelJson, UserModel.class);
    }

    public void saveEmail(String email) {
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    public void clear() {
        editor.clear().apply();
    }
}
