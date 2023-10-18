package com.example.du_an_dads.View.User;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.du_an_dads.Api.Api;
import com.example.du_an_dads.Model.User_model;
import com.example.du_an_dads.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {
    TextView tvsignin;
    EditText edtusername, edtmail, edtphone, edtpass, edtaddress;
    Button btnsignup;
    String URL = "https://foodapp-7o77.onrender.com/v1/api/";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        tvsignin = findViewById(R.id.tv_signin);
        edtusername = findViewById(R.id.edt_UsernameReg);
        edtmail = findViewById(R.id.edt_mailReg);
        edtphone = findViewById(R.id.edt_phoneReg);
        edtpass = findViewById(R.id.edt_passReg);
        edtaddress = findViewById(R.id.edt_addressReg);
        btnsignup = findViewById(R.id.btn_signupReg);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Action();
            }
        });


        tvsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, SigninActivity.class));
            }
        });
    }

    public void Action() {
        String Username = edtusername.getText().toString().trim();
        String Email = edtmail.getText().toString().trim();
        String Phone = edtphone.getText().toString().trim();
        String Pass = edtpass.getText().toString().trim();
        String Address = edtaddress.getText().toString().trim();
        if (Username.isEmpty()) {
            edtmail.setError("Không được để trống");
            edtmail.requestFocus();
            return;
        }
        if (Email.isEmpty()) {
            edtmail.setError("Không được để trống");
            edtmail.requestFocus();
            return;
        }
        if (Phone.isEmpty()) {
            edtphone.setError("Không được để trống");
            edtphone.requestFocus();
            return;
        }
        if (Pass.isEmpty()) {
            edtpass.setError("Không được để trống");
            edtpass.requestFocus();
            return;
        }
        if (Address.isEmpty()) {
            edtaddress.setError("Không được để trống");
            edtaddress.requestFocus();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<User_model> call = api.addPerson(Username, Email, Phone, Address, Pass);
        call.enqueue(new Callback<User_model>() {
            @Override
            public void onResponse(Call<User_model> call, Response<User_model> response) {
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), SigninActivity.class));
            }

            @Override
            public void onFailure(Call<User_model> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}