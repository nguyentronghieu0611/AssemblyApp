package com.example.assemblyapp.view;

import androidx.appcompat.app.AppCompatActivity;

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

public class ChangePasswordActivity extends AppCompatActivity {
    String MY_PREF = "my_pref";
    SharedPreferences preferences;
    EditText pass, repass;
    Button btnChangePass;
    AssemblyDatabase db;
    LinearLayout layout;
    int user_id = 0;
    boolean isPasswordValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initControl();
        bindEvent();
    }

    private void initControl() {
        getSupportActionBar().setTitle("Đổi mật khẩu");
        preferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        if (preferences.contains("user_id")) {
            user_id = preferences.getInt("user_id", 0);
        }
        layout = findViewById(R.id.layout_info);
        db = new AssemblyDatabase(this);
        pass = findViewById(R.id.password);
        repass = findViewById(R.id.repassword);
        btnChangePass = findViewById(R.id.btnChangePass);
    }

    private void bindEvent(){
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass.getText().toString().isEmpty()) {
                    Utils.showSnackbar(getResources().getString(R.string.password_error),layout);
                    isPasswordValid = false;
                } else if (pass.getText().length() < 6) {
                    Utils.showSnackbar(getResources().getString(R.string.error_invalid_password),layout);
                    isPasswordValid = false;
                }
                if (repass.getText().toString().isEmpty()) {
                    Utils.showSnackbar(getResources().getString(R.string.password_error),layout);
                    isPasswordValid = false;
                } else if (repass.getText().length() < 6) {
                    Utils.showSnackbar(getResources().getString(R.string.error_invalid_password),layout);
                    isPasswordValid = false;
                }else if(!pass.getText().toString().equals(repass.getText().toString())){
                    Utils.showSnackbar("Mật khẩu không trùng khớp",layout);
                    isPasswordValid = true;
                }
                else {
                    User user = new User(user_id,"","",repass.getText().toString());
                    int a = db.changePassword(user);
                    if (a > 0) {
                        Utils.showSnackbar("Cập nhật thành công!", layout);
                    } else
                        Utils.showSnackbar("Cập nhật không thành công!", layout);
                }
            }
        });
    }
}