package com.example.giaothongapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.giaothongapp.R;
import com.example.giaothongapp.adapter.ErrorAdapter;
import com.example.giaothongapp.common.Utils;
import com.example.giaothongapp.config.TrafficDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.giaothongapp.model.DataChange;
import com.example.giaothongapp.model.Error;
import com.google.android.material.snackbar.Snackbar;

public class ErrorActivity extends AppCompatActivity implements DataChange {
    TrafficDatabase db;
    ListView lvError;
    List<Error> listError = new ArrayList<>();
    FragmentManager fragmentManager;
    private final int READ_STORAGE = 146;
    private final int WRITE_STORAGE = 178;
    private Snackbar snackbar;
    private ConstraintLayout layout;
    private int typeMenu = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        initControl();
        bindEvent();
    }

    private void initControl(){
        layout = findViewById(R.id.layout);
        fragmentManager = getSupportFragmentManager();
        lvError = findViewById(R.id.listview);
        db = new TrafficDatabase(this);
        listError = db.getError();
        lvError.setAdapter(new ErrorAdapter(listError,this,fragmentManager,db));
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
                typeMenu=2;
                invalidateOptionsMenu();
                FragmentTransaction t = fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                t.add(R.id.fragmentError, new ErrorFragment(listError.get(position), db), "TAG");
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
        if(itemId == R.id.btnAddError){
            FragmentTransaction t = fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            t.add(R.id.fragmentError, new ErrorFragment(null, db), "TAG");
            t.addToBackStack(null);
            t.commit();
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
                Toast.makeText(ErrorActivity.this,getString(R.string.please_grant_read),Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == WRITE_STORAGE) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contact.phonenummber, null)));
            } else {
//                Utils.showSnackbar(getString(R.string.please_grant_write),snackbar,layout);
                Toast.makeText(ErrorActivity.this,getString(R.string.please_grant_write),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onErrorChange() {
        listError = db.getError();
        lvError.setAdapter(new ErrorAdapter(listError,this,fragmentManager,db));
        onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Danh sách lỗi");
    }
}