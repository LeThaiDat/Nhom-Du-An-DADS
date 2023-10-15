package com.example.du_an_dads.View.User;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

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

        listUser = new ArrayList<>();
        btn_login = findViewById(R.id.btn_Login);

        getlistUser();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Action();

                clickLogin();
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