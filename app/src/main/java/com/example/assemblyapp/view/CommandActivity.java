package com.example.assemblyapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.assemblyapp.R;
import com.example.assemblyapp.adapter.CommandAdapter;
import com.example.assemblyapp.adapter.SearhHistoryAdapter;
import com.example.assemblyapp.common.Utils;
import com.example.assemblyapp.config.AssemblyDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.assemblyapp.model.DataChange;
import com.example.assemblyapp.model.Command;
import com.example.assemblyapp.model.SearchHistory;

public class CommandActivity extends AppCompatActivity implements DataChange {
    AssemblyDatabase db;
    ListView lvError,lvSearchHistory;
    List<Command> listCommand = new ArrayList<>();
    List<SearchHistory> historyList = new ArrayList<>();
    FragmentManager fragmentManager;
    FrameLayout layoutHistory;
    private final int READ_STORAGE = 146;
    private final int WRITE_STORAGE = 178;
    private ConstraintLayout layout;
    private int typeMenu = 1;
    private int role=0;
    SharedPreferences preferences;
    int user_id=0;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);
        initControl();
        bindEvent();
    }

    private void initControl(){
        getSupportActionBar().setTitle("Danh sách lệnh");
        layoutHistory = findViewById(R.id.layout_search);
        layout = findViewById(R.id.layout_main);
        preferences = getSharedPreferences(Utils.MY_REF,MODE_PRIVATE);
        role = preferences.getInt("role",0);
        user_id = preferences.getInt("user_id",0);
        fragmentManager = getSupportFragmentManager();
        lvError = findViewById(R.id.listview);
        lvSearchHistory = findViewById(R.id.listviewHistory);
        db = new AssemblyDatabase(this);
        insertDemoData();
        listCommand = db.getCommand();
        lvError.setAdapter(new CommandAdapter(listCommand,this,fragmentManager,db));
        int permissionCheckWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

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
                t.add(R.id.fragmentError, new CommandFragment(listCommand.get(position), db, role), "TAG");
                t.addToBackStack(null);
                t.commit();
            }
        });

        lvSearchHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchView.setQuery(historyList.get(position).getSearch_txt(),true);
                layoutHistory.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if(typeMenu==1 && role == 0){
            initSearch(R.menu.menu,menuInflater,menu);
        }
        else if(typeMenu==1 && role == 1){
            initSearch(R.menu.menu_user_command,menuInflater,menu);
        }
        else if(typeMenu==2 && role == 0)
            menuInflater.inflate(R.menu.menu_command,menu);
        else if(typeMenu==3 && role == 0)
            menuInflater.inflate(R.menu.menu_add_command,menu);
        else if(typeMenu==2 && role == 1)
            menuInflater.inflate(R.menu.menu_add_command,menu);
        else if(typeMenu==3 && role == 1)
            menuInflater.inflate(R.menu.menu_add_command,menu);
        return true;
    }

    private void initSearch(int menuId, MenuInflater menuInflater, Menu menu){
        menuInflater.inflate(menuId,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView =
                (SearchView) searchItem.getActionView();
        searchView.setQueryHint(Html.fromHtml("<font color = #AEAEAE>" + getResources().getString(R.string.hintSearchMess) + "</font>"));
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","CLICKED SEARCH");
                initStateSearch(true);
            }
        });

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                initStateSearch(true);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.d("TAG","CLICKED CLOSE SEARCH");
                listCommand = db.getCommand();
                lvError.setAdapter(new CommandAdapter(listCommand,CommandActivity.this,fragmentManager,db));
                initStateSearch(false);
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                initStateSearch(false);
                return false;
            }
        });
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("query",query);
                listCommand = db.searchCommand(query.toUpperCase());
                lvError.setAdapter(new CommandAdapter(listCommand,CommandActivity.this,fragmentManager,db));
                db.insertHistory(new SearchHistory(user_id,query));
                initStateSearch(false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if(!newText.isEmpty())
                    initStateSearch(true);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d("newText",newText);
//                        listError = db.searchError(newText.toUpperCase());
//                        lvError.setAdapter(new ErrorAdapter(listError,ErrorActivity.this,fragmentManager,db));
//                        db.insertHistory(new SearchHistory(user_id,newText));
//                    }
//                },1500);

                return true;
            }
        });
    }

    private void initStateSearch(boolean isSearch){
        if(isSearch){
            historyList = db.getHistory(user_id);
            lvSearchHistory.setAdapter(new SearhHistoryAdapter(historyList,this,db));
            layoutHistory.setVisibility(View.VISIBLE);
//            lvError.setVisibility(View.GONE);
        }
        else {
            layoutHistory.setVisibility(View.GONE);
//            lvError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.btnAddCommand:
                getSupportActionBar().setTitle("Thêm lệnh");
                FragmentTransaction t = fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                t.add(R.id.fragmentError, new CommandFragment(null, db, role), "TAG");
                t.addToBackStack(null);
                t.commit();
                typeMenu=3;
                invalidateOptionsMenu();
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
        if (requestCode == WRITE_STORAGE) {
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

    private void insertDemoData(){
        if(!db.countCommand()){
            db.insertCommand(new Command("Lệnh điều kiện IF - ELSE","Lệnh điều kiện",null));
            db.insertCommand(new Command("Lệnh lặp for","Lệnh lặp for",null));
            db.insertCommand(new Command("Lệnh lặp while do","Lệnh lặp while do",null));
        }
    }


}