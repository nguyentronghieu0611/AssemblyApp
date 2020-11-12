package com.example.assemblyapp.config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.assemblyapp.model.AmercementLevel;
import com.example.assemblyapp.model.MarkUser;
import com.example.assemblyapp.model.SearchHistory;
import com.example.assemblyapp.model.User;
import com.example.assemblyapp.model.Command;

import java.util.ArrayList;
import java.util.List;

public class AssemblyDatabase extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Contact.db";

    public AssemblyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        //tao bang lệnh
        db.execSQL(" CREATE TABLE " + "tblCommand" + " (" +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name" + " TEXT NOT NULL, " +
                "description" + " TEXT NOT NULL, " +
                "image" + " BLOB )");

        //tao bang ngắt
        db.execSQL(" CREATE TABLE " + "tblNgat" + " (" +
                "id" + " INTEGER, " +
                "function" + " TEXT UNIQUE NOT NULL, " +
                "use" + " TEXT NOT NULL, " +
                "image" + " BLOB )");

        //tao bang cấu trúc lệnh-macro
        db.execSQL(" CREATE TABLE " + "tblCommandConstruct" + " (" +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name" + " TEXT NOT NULL, " +
                "image" + " BLOB, " +
                "type" + " INTEGER)");

        //tao bang lệnh - trúc lệnh macro
        db.execSQL(" CREATE TABLE " + "tblCommandConstructMap" + " (" +
                "construct_id" + " INTEGER , " +
                "command_id" + " INTEGER, " +
                "description" + " TEXT NOT NULL )");

        //tao bang danh dau nguoi dung
        db.execSQL(" CREATE TABLE " + "tblHistorySearch" + " (" +
                "user_id" + " INTEGER, " +
                "keyword" + " TEXT NOT NULL )");

        //tao bang nguoi dung
        db.execSQL(" CREATE TABLE " + "tblUser" + " (" +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name" + " TEXT NOT NULL, " +
                "email" + " TEXT UNIQUE NOT NULL, " +
                "role" + " INTEGER, " +
                "password" + " TEXT NOT NULL )");

        //tao bang lich su tim kiem
        db.execSQL(" CREATE TABLE " + "tblSearchHistory" + " (" +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id" + " INTEGER, " +
                "search_text" + " TEXT NOT NULL )");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



    //thao tac voi user

    public boolean checkHasAdmin(){
        boolean hasAdmin = false;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("Select email from tblUser where email=?", new String[]{"admin1234@abc.com"});
        if(c.moveToFirst())
            hasAdmin=true;
        return hasAdmin;
    }

    public long insertUser(User user){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("name", user.getName());
        values.put("role", user.getRole());
        return sqLiteDatabase.insert("tblUser", null, values);
    }

    public List<User> login(String email, String password){
        boolean trueUser = false;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("Select * from tblUser where email=? and password=?", new String[]{email,password});
        List<User> result = new ArrayList<>();
        User user = null;
        if(c.moveToFirst()){
            do{
                int id = c.getInt(0);
                String name = c.getString(1);
                String email1 = c.getString(2);
                int role = c.getInt(3);
                String pass = c.getString(4);
                user = new User(id,email1,name,pass,role);
                result.add(user);
            }while (c.moveToNext());
        }
        return result;
    }

    public int updateUser(User user){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", user.getEmail());
        values.put("name", user.getName());
        return sqLiteDatabase.update("tblUser",values,"id = ?",new String[]{String.valueOf(user.getId())});
    }

    public int changePassword(User user){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", user.getPassword());
        return sqLiteDatabase.update("tblUser",values,"id = ?",new String[]{String.valueOf(user.getId())});
    }

    public int updateUserFull(User user){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", user.getEmail());
        values.put("name", user.getName());
        values.put("password", user.getPassword());
        return sqLiteDatabase.update("tblUser",values,"id = ?",new String[]{String.valueOf(user.getId())});
    }

    public List<SearchHistory> getHistory(int user_id1){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("Select * from tblSearchHistory where user_id=? order by id desc limit 5", new String[]{String.valueOf(user_id1)});
        List<SearchHistory> result = new ArrayList<>();
        SearchHistory searchHistory;
        if(c.moveToFirst()){
            do{
                int id = c.getInt(0);
                int user_id = c.getInt(1);
                String content = c.getString(2);
                searchHistory = new SearchHistory(id,user_id,content);
                result.add(searchHistory);
            }while (c.moveToNext());
        }
        return result;
    }

    public Long insertHistory(SearchHistory searchHistory){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", searchHistory.getUser_id());
        values.put("search_text", searchHistory.getSearch_txt());
        return sqLiteDatabase.insert("tblSearchHistory", null, values);
    }


    //thao tac voi bang loi
    public List<Command> getCommand(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("Select * from tblCommand", null);
        List<Command> result = new ArrayList<>();
        Command command;
        if(c.moveToFirst()){
            do{
                int id = c.getInt(0);
                String name = c.getString(1);
                String description = c.getString(2);
                byte[] image = c.getBlob(3);
                command = new Command(id,name,description,image);
                result.add(command);
            }while (c.moveToNext());
        }
        return result;
    }

    public boolean countCommand(){
        boolean hasData = false;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("Select * from tblCommand", null);
        int count=0;
        if(c.moveToFirst()){
            hasData = true;
        }
        return hasData;
    }

    public List<Command> searchCommand(String searchStr){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("Select * from tblCommand where upper(name) like '%"+searchStr+"%'", null);
        List<Command> result = new ArrayList<>();
        Command command;
        if(c.moveToFirst()){
            do{
                int id = c.getInt(0);
                String name = c.getString(1);
                String description = c.getString(2);
                byte[] image = c.getBlob(3);
                command = new Command(id,name,description,image);
                result.add(command);
            }while (c.moveToNext());
        }
        return result;
    }

    public long insertCommand(Command command){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", command.getName());
        values.put("description", command.getDescription());
        values.put("image", command.getImage());
        return sqLiteDatabase.insert("tblCommand", null, values);
    }

    public long deleteError(int id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("tblCommand","id=?",new String[]{String.valueOf(id)});
    }

    public int updateCommand(Command command){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", command.getName());
        values.put("description", command.getDescription());
        values.put("image", command.getImage());
        return sqLiteDatabase.update("tblCommand",values,"id = ?",new String[]{String.valueOf(command.getId())});
    }

    //thao tac voi bang muc phat
    public List<AmercementLevel> getListAmercement(int error_id){
        List<AmercementLevel> listRs = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("Select * from tblAmercementLevel where error_id = ?", new String[]{String.valueOf(error_id)});
        AmercementLevel amercementLevel = null;
        if(c.moveToFirst()){
            do{
                int error_id1 = c.getInt(0);
                String vehical = c.getString(1);
                String amercement = c.getString(2);
                amercementLevel = new AmercementLevel(error_id1,vehical,amercement);
                listRs.add(amercementLevel);
            }while (c.moveToNext());
        }
        return listRs;
    }

    public long insertAmercement(AmercementLevel amercementLevel){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("error_id", amercementLevel.getError_id());
        values.put("vehicle", amercementLevel.getVehical());
        values.put("amercement", amercementLevel.getAmercement());
        return sqLiteDatabase.insert("tblAmercementLevel", null, values);
    }



    public long deleteAmercement(AmercementLevel amercementLevel){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("tblAmercementLevel","error_id=? and vehical=?",new String[]{String.valueOf(amercementLevel.getError_id()),amercementLevel.getVehical()});
    }


    //thao tac voi bang danh dau nguoi dung
    public long insertMarkUser(MarkUser markUser){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", markUser.getUser_id());
        values.put("error_id", markUser.getError_id());
        values.put("time", markUser.getTime());
        return sqLiteDatabase.insert("tblMarkUser", null, values);
    }
}
