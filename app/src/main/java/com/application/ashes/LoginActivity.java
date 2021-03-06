package com.application.ashes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.application.ashes.Activities.Dashboard;
import com.application.ashes.Activities.SignUpActivity;
import com.application.ashes.Models.LogInParam;
import com.application.ashes.Models.SignUpParam;
import com.application.ashes.Retrofit.ApiInterface;
import com.application.ashes.Retrofit.Models.LoginModel;
import com.application.ashes.Retrofit.Models.SignUp;
import com.application.ashes.Retrofit.ServiceGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private String username,password;
    String emailPattern = "[a-zA-Z0-9_\\.+]+@(gmail|yahoo|hotmail)(\\.[a-z]{2,3}){1,2}";
    private Button ok;
    TextView signup;

    private EditText editTextUsername,editTextPassword;
    private RadioButton saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        signup=findViewById(R.id.signup);
        signup.setOnClickListener(this);
        ok = (Button)findViewById(R.id.login);
        ok.setOnClickListener(this);
        editTextUsername = (EditText)findViewById(R.id.username);
        editTextPassword = (EditText)findViewById(R.id.password);
        saveLoginCheckBox = (RadioButton) findViewById(R.id.remember);
        saveLoginCheckBox.setOnClickListener(this);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            editTextUsername.setText(loginPreferences.getString("username", ""));
            editTextPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ok) {
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTextUsername.getWindowToken(), 0);

            username = editTextUsername.getText().toString().trim();
            password = editTextPassword.getText().toString();
            if(!username.matches(emailPattern) ){
                editTextUsername.setError("Enter a valid email address");
            }else if(password.isEmpty() || password.length()<6){
                editTextPassword.setError("Enter a valid password");
            }else {
                login();
                if (saveLoginCheckBox.isChecked()) {
                    loginPrefsEditor.putBoolean(Constants.SAVELOGIN, true);
                    loginPrefsEditor.putString(Constants.USERNAME, username);
                    loginPrefsEditor.putString(Constants.PASSWORD, password);

                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
            }


        }else if(v==saveLoginCheckBox){
            if(saveLogin){
                saveLoginCheckBox.setChecked(false);
                saveLogin=false;
            }else{
                saveLoginCheckBox.setChecked(true);
                saveLogin=true;
            }
        }else if(v==signup){
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        }
    }

    private void login() {

        LogInParam logInParam =new LogInParam(editTextUsername.getText().toString().trim(),editTextPassword.getText().toString().trim());
        ApiInterface apiInterface2 = ServiceGenerator.createService(ApiInterface.class);
        Call<LoginModel> call = apiInterface2.login(logInParam);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if(response.code()==401){
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                } else if(response.code()==200) {
                    LoginModel login = response.body();
                    loginPrefsEditor.putString(Constants.USERID, login.getUserId());
                    loginPrefsEditor.commit();
                    startActivity(new Intent(LoginActivity.this, Dashboard.class));
                }
                //  Log.d("TAG",response.body().toString());
                // Log.d("TAG",response.code()+"");

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
