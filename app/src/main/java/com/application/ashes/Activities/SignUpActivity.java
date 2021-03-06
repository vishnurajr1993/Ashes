package com.application.ashes.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.application.ashes.Models.SignUpParam;
import com.application.ashes.R;

import com.application.ashes.Retrofit.APIClient;
import com.application.ashes.Retrofit.ApiInterface;
import com.application.ashes.Retrofit.Models.SignUp;
import com.application.ashes.Retrofit.ServiceGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    EditText name,email,password,reEnterPassword;
    Button signUp;
    String emailPattern = "[a-zA-Z0-9_\\.+]+@(gmail|yahoo|hotmail)(\\.[a-z]{2,3}){1,2}";
    ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }

    private void init() {
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.pwd);
        reEnterPassword=findViewById(R.id.rpwd);
        signUp=findViewById(R.id.signup);
        apiInterface = APIClient.getClient().create(ApiInterface.class);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private void validate() {
        if (name.getText().toString().isEmpty()){
            name.setError("please enter name");
        }else if(!email.getText().toString().matches(emailPattern) ){
            email.setError("Enter a valid email address");
        }else if(password.getText().toString().isEmpty() || password.getText().toString().length()<6 ){
            password.setError("password must be at least 6 characters");
        }else if(!reEnterPassword.getText().toString().equals(password.getText().toString())){
            reEnterPassword.setError("Passwords doesn't match");
        }else{
            submit();
        }

    }

    private void submit() {

        SignUpParam signUpParam =new SignUpParam(name.getText().toString().trim(),email.getText().toString().trim(),password.getText().toString().trim());
        ApiInterface apiInterface2 = ServiceGenerator.createService(ApiInterface.class);
        Call<SignUp> call = apiInterface2.signUp(signUpParam);
        call.enqueue(new Callback<SignUp>() {
            @Override
            public void onResponse(Call<SignUp> call, Response<SignUp> response) {
                SignUp signUp=response.body();
              //  Log.d("TAG",response.body().toString());
               // Log.d("TAG",response.code()+"");

            }

            @Override
            public void onFailure(Call<SignUp> call, Throwable t) {
                call.cancel();
            }
        });
    }
    }

