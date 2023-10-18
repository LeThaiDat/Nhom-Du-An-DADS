package com.example.du_an_dads.View.User;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.du_an_dads.Api.Api;
import com.example.du_an_dads.Model.User_model;
import com.example.du_an_dads.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SigninActivity extends AppCompatActivity {

    EditText edt_mail, edt_pass;
    Button btn_login;
    TextView tvsignup;
    private List<User_model> listUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        edt_mail = findViewById(R.id.edt_mailLogin);
        edt_pass = findViewById(R.id.edt_passLogin);
        String url = "https://foodapp-7o77.onrender.com/v1/api/access/login";
        listUser = new ArrayList<>();
        btn_login = findViewById(R.id.btn_Login);

      //  getlistUser();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Action();

                //clickLogin();

                String email = edt_mail.getText().toString();
                String password = edt_pass.getText().toString();
                try {
                    JSONObject jsonData = new JSONObject();
                    jsonData.put("email", email);
                    jsonData.put("password", password);

                    PostAsyncTask postAsyncTask = new PostAsyncTask(url);
                    postAsyncTask.execute(jsonData);
                    JSONObject result = postAsyncTask.get();
                    if (result != null) {
                        // Handle the successful result
                        //Khi đăng nhập thành công sẽ có status = 200 còn không thì sẽ xuất hiện message
                        if(result.getInt("status") == 200){
                            Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_SHORT).show();

                            //Đăng nhập thành công sẽ trả về 1 cái token

                            String token = result.getString("token");

                            //Lưu token khi đăng nhập thành công

                            SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("access_token", token);
                            editor.apply();
                        }else {
                            Toast.makeText(getApplicationContext(), result.getString("message") , Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle the error case
                        Toast.makeText(getApplicationContext(), "Failed to make the API request", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error creating JSON data", Toast.LENGTH_SHORT).show();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        tvsignup = findViewById(R.id.tv_signup);
        tvsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigninActivity.this, SignupActivity.class));
            }
        });
    }

    private void clickLogin() {
        String Email = edt_mail.getText().toString().trim();
        String Password = edt_pass.getText().toString().trim();

        if (Email.isEmpty()) {
            edt_mail.setError("Không được để trống");
            edt_mail.requestFocus();
            return;
        }
        if (Password.isEmpty()) {
            edt_pass.setError("Không được để trống");
            edt_pass.requestFocus();
            return;
        }
        if (listUser == null || listUser.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
        }
        boolean isHasUser = false;
        for (User_model user_model : listUser) {
            if (Email.equals(user_model.getEmail()) && Password.equals(user_model.getPassword())) {
                isHasUser = true;
                break;
            }
        }
        if (isHasUser) {
            Toast.makeText(getApplicationContext(), "Đăng nhập thành công rồi đấy nhé", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Đăng nhập fail rồi", Toast.LENGTH_SHORT).show();
        }
    }



    public class PostAsyncTask extends AsyncTask<JSONObject, Void, JSONObject> {
        private static final String TAG = "PostAsyncTask";
        private final String apiUrl;

        public PostAsyncTask(String apiUrl) {
            this.apiUrl = apiUrl;
        }

        @Override
        protected JSONObject doInBackground(JSONObject... jsonObjects) {
            if (jsonObjects.length == 0) {
                return null;
            }

            JSONObject jsonObject = jsonObjects[0];
            String jsonData = jsonObject.toString();

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                os.write(jsonData.getBytes("UTF-8"));
                os.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    return new JSONObject(response.toString());
                } else {
                    Log.e(TAG, "POST request failed with response code: " + responseCode);
                }

                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            return null;
        }
    }


    private void getlistUser() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://foodapp-7o77.onrender.com/v1/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        api.getListUser("").enqueue(new Callback<List<User_model>>() {
            @Override
            public void onResponse(Call<List<User_model>> call, Response<List<User_model>> response) {
                listUser = response.body();

                Toast.makeText(getApplicationContext(), "API thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<User_model>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "API Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void Action() {
//        String Email = edt_mail.getText().toString().trim();
//        String Password = edt_pass.getText().toString().trim();
//        if (Email.isEmpty()) {
//            edt_mail.setError("Không được để trống");
//            edt_mail.requestFocus();
//            return;
//        }
//        if (Password.isEmpty()) {
//            edt_pass.setError("Không được để trống");
//            edt_pass.requestFocus();
//            return;
//        }
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://foodapp-7o77.onrender.com/v1/api/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        Api api = retrofit.create(Api.class);
//
//        api.loginUser(Email, Password).enqueue(new Callback<User_model>() {
//            @Override
//            public void onResponse(Call<User_model> call, Response<User_model> response) {
//                if (response.isSuccessful()) {  //xử lý khi đăng nhập thành công
//                    User_model user_model = response.body();
//                    Toast.makeText(getApplicationContext(), "đăng nhập thành công", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "đăng nhập thất bại", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User_model> call, Throwable t) {
//                //xử lí lỗi khi k thể kết nói tới api
//                Toast.makeText(getApplicationContext(), "API Fail", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//
//    }
}