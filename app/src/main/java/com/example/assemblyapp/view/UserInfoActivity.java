package com.example.assemblyapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.assemblyapp.R;
import com.example.assemblyapp.config.AssemblyDatabase;
import com.example.assemblyapp.model.User;

public class UserInfoActivity extends AppCompatActivity {
    String MY_PREF = "my_pref";
    SharedPreferences preferences;
    EditText name, email, password, repassword;
    Button btnUpdate;
    AssemblyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initControl();
        bindEvent();
        preferences = getSharedPreferences(MY_PREF,MODE_PRIVATE);
        if(preferences.contains("user_id")){
            name.setText(preferences.getString("name",""));
            email.setText(preferences.getString("email",""));
        }
    }

    private void initControl(){
        db = new AssemblyDatabase(this);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        btnUpdate = findViewById(R.id.update);
    }

    private void bindEvent(){
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().isEmpty() && repassword.getText().toString().isEmpty()){
                    User user = new User(email.getText().toString(),name.getText().toString(),password.getText().toString());
                    db.updateUser(user);
                }
            }
        });
    }
}