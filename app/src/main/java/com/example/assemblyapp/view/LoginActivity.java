package com.example.assemblyapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.assemblyapp.R;
import com.example.assemblyapp.common.Utils;
import com.example.assemblyapp.config.AssemblyDatabase;
import com.example.assemblyapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences preferences;
    AssemblyDatabase db;
    EditText email, password;
    Button login;
    TextView register;
    boolean isEmailValid, isPasswordValid;
    LinearLayout layout;
    String MY_PREF = "my_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences(MY_PREF,MODE_PRIVATE);
        if(preferences.contains("user_id")){
            startActivity(new Intent(LoginActivity.this, CommandActivity.class));
            finish();
        }
        db = new AssemblyDatabase(this);
//        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        getSupportActionBar().hide();
        layout = findViewById(R.id.layout_login);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValidation();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to RegisterActivity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void SetValidation() {
        // Check for a valid email address.
        if (email.getText().toString().isEmpty()) {
            Utils.showSnackbar(getResources().getString(R.string.email_error),layout);
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            Utils.showSnackbar(getResources().getString(R.string.error_invalid_email),layout);
            isEmailValid = false;
        } else  {
            isEmailValid = true;
        }

        // Check for a valid password.
        if (password.getText().toString().isEmpty()) {
            Utils.showSnackbar(getResources().getString(R.string.password_error),layout);
            isPasswordValid = false;
        } else if (password.getText().length() < 6) {
            Utils.showSnackbar(getResources().getString(R.string.error_invalid_password),layout);
            isPasswordValid = false;
        } else  {
            isPasswordValid = true;
        }

        if (isEmailValid && isPasswordValid) {
            List<User> lsRs = new ArrayList<>();
            lsRs = db.login(email.getText().toString(),password.getText().toString());
            if(lsRs.size()>0){
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("user_id",lsRs.get(0).getId());
                editor.putString("email",lsRs.get(0).getEmail());
                editor.putString("name",lsRs.get(0).getName());
                editor.commit();
                startActivity(new Intent(LoginActivity.this, CommandActivity.class));
                finish();
            }
            else
                Utils.showSnackbar("Đăng nhập thất bại",layout);
        }

    }

}