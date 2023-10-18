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

import com.example.du_an_dads.HomeActivity;
import com.example.du_an_dads.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class SigninActivity extends AppCompatActivity {
    EditText edtEmail, edtPass;

    Button btnLogin;
    TextView tvsignup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edtEmail = findViewById(R.id.edt_mailLogin);
        edtPass = findViewById(R.id.edt_passLogin);
        btnLogin = findViewById(R.id.btn_Login);

        tvsignup = findViewById(R.id.tv_signup);
        tvsignup.setOnClickListener(view -> startActivity(new Intent(SigninActivity.this, SignupActivity.class)));
        btnLogin.setOnClickListener(view -> {
            clickLogin();
        });
    }

    private void clickLogin() {
        String url = "https://foodapp-7o77.onrender.com/v1/api/access/login";
        String email = edtEmail.getText().toString();
        String password = edtPass.getText().toString();
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
                if (result.getInt("status") == 200) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_SHORT).show();

                    //Đăng nhập thành công sẽ trả về 1 cái token

                    String token = result.getString("token");

                    //Lưu token khi đăng nhập thành công

                    SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("access_token", token);
                    editor.apply();
                } else {
                    Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_SHORT).show();
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
}
