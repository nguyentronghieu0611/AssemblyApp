package com.example.assemblyapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.assemblyapp.R;
import com.example.assemblyapp.common.Utils;
import com.example.assemblyapp.config.AssemblyDatabase;
import com.example.assemblyapp.model.User;
import com.google.android.material.snackbar.Snackbar;

public class UserInfoActivity extends AppCompatActivity {
    String MY_PREF = "my_pref";
    SharedPreferences preferences;
    EditText name, email;
    Button btnUpdate;
    AssemblyDatabase db;
    LinearLayout layout;
    int user_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initControl();
        bindEvent();
    }

    private void initControl() {
        getSupportActionBar().setTitle("Thông tin tài khoản");
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        preferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        if (preferences.contains("user_id")) {
            name.setText(preferences.getString("name", ""));
            email.setText(preferences.getString("email", ""));
            user_id = preferences.getInt("user_id", 0);
        }
        layout = findViewById(R.id.layout_info);
        db = new AssemblyDatabase(this);
        btnUpdate = findViewById(R.id.update);
    }

    private void bindEvent() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(user_id, email.getText().toString(), name.getText().toString(), "");
                int a = db.updateUser(user);
                if (a > 0) {
                    Utils.showSnackbar("Cập nhật thành công!", layout);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email", email.getText().toString());
                    editor.putString("name", name.getText().toString());
                    editor.commit();
                } else
                    Utils.showSnackbar("Cập nhật không thành công!", layout);
            }
        });
    }
}