package com.example.assemblyapp.view;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assemblyapp.R;
import com.example.assemblyapp.common.Utils;
import com.example.assemblyapp.config.AssemblyDatabase;
import com.example.assemblyapp.model.User;

public class RegisterActivity extends AppCompatActivity {
    AssemblyDatabase db;
    EditText name, email, password;
    Button register;
    boolean isNameValid, isEmailValid, isPasswordValid;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new AssemblyDatabase(this);
        getSupportActionBar().hide();
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        layout = findViewById(R.id.layout_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValidation();
            }
        });
    }

    public void SetValidation() {
        // Check for a valid name.
        if (name.getText().toString().isEmpty()) {
            Utils.showSnackbar(getResources().getString(R.string.name_error),layout);
            isNameValid = false;
        } else {
            isNameValid = true;
        }

        // Check for a valid email address.
        if (email.getText().toString().isEmpty()) {
            Utils.showSnackbar(getResources().getString(R.string.email_error),layout);
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            Utils.showSnackbar(getResources().getString(R.string.error_invalid_email),layout);
            isEmailValid = false;
        } else {
            isEmailValid = true;
        }


        // Check for a valid password.
        if (password.getText().toString().isEmpty()) {
            Utils.showSnackbar(getResources().getString(R.string.password_error),layout);
            isPasswordValid = false;
        } else if (password.getText().length() < 6) {
            Utils.showSnackbar(getResources().getString(R.string.error_invalid_password),layout);
            isPasswordValid = false;
        } else {
            isPasswordValid = true;
        }

        if (isNameValid && isEmailValid && isPasswordValid) {
            long a = db.insertUser(new User(email.getText().toString(),name.getText().toString(),password.getText().toString(),1));
            if(a>0)
                Utils.showSnackbar("Đăng ký thành công",layout);
            else
                Utils.showSnackbar("Đăng ký thất bại",layout);
        }

    }

}
