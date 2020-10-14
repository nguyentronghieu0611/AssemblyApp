package com.example.assemblyapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.assemblyapp.R;
import com.example.assemblyapp.adapter.CommandAdapter;
import com.example.assemblyapp.common.Utils;
import com.example.assemblyapp.config.AssemblyDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.assemblyapp.model.DataChange;
import com.example.assemblyapp.model.Command;

public class CommandActivity extends AppCompatActivity implements DataChange {
    AssemblyDatabase db;
    ListView lvError;
    List<Command> listCommand = new ArrayList<>();
    FragmentManager fragmentManager;
    private final int READ_STORAGE = 146;
    private final int WRITE_STORAGE = 178;
    private ConstraintLayout layout;
    private int typeMenu = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);
        initControl();
        bindEvent();
    }

    private void initControl(){
        getSupportActionBar().setTitle("Danh sách lệnh");
        layout = findViewById(R.id.layout_main);
        fragmentManager = getSupportFragmentManager();
        lvError = findViewById(R.id.listview);
        db = new AssemblyDatabase(this);
        listCommand = db.getCommand();
        lvError.setAdapter(new CommandAdapter(listCommand,this,fragmentManager,db));
        int permissionCheckREAD = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheckWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheckREAD != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE);
        }

        if (permissionCheckWrite != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE);
        }
    }

    private void bindEvent(){
        lvError.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getSupportActionBar().setTitle("Chi tiết lệnh");
                typeMenu=2;
                invalidateOptionsMenu();
                FragmentTransaction t = fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                t.add(R.id.fragmentError, new CommandFragment(listCommand.get(position), db), "TAG");
                t.addToBackStack(null);
                t.commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if(typeMenu==1)
            menuInflater.inflate(R.menu.menu,menu);
        else if(typeMenu==2)
            menuInflater.inflate(R.menu.menu_error,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.btnAddCommand:
                getSupportActionBar().setTitle("Thêm lệnh");
                FragmentTransaction t = fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                t.add(R.id.fragmentError, new CommandFragment(null, db), "TAG");
                t.addToBackStack(null);
                t.commit();
                break;
            case R.id.btnUpdateInfo:
                startActivity(new Intent(CommandActivity.this,UserInfoActivity.class));
                break;
            case R.id.btnChangePass:
                startActivity(new Intent(CommandActivity.this,ChangePasswordActivity.class));
                break;
            case R.id.btnLogOut:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                startActivity(new Intent(CommandActivity.this, LoginActivity.class));
                                SharedPreferences settings = getSharedPreferences("my_pref", Context.MODE_PRIVATE);
                                settings.edit().clear().commit();
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
                builder.setMessage("Bạn có muốn đăng xuất?").setPositiveButton("Có", dialogClickListener)
                        .setNegativeButton("Không", dialogClickListener).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_STORAGE) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.phonenummber));
//                startActivity(intent);
            } else {
                Utils.showSnackbar(getString(R.string.please_grant_read),layout);
                Toast.makeText(CommandActivity.this,getString(R.string.please_grant_read),Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == WRITE_STORAGE) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contact.phonenummber, null)));
            } else {
                Utils.showSnackbar(getString(R.string.please_grant_write),layout);
//                Toast.makeText(CommandActivity.this,getString(R.string.please_grant_write),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onErrorChange() {
        listCommand = db.getCommand();
        lvError.setAdapter(new CommandAdapter(listCommand,this,fragmentManager,db));
        onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        typeMenu = 1;
        invalidateOptionsMenu();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Danh sách lệnh");
    }
}