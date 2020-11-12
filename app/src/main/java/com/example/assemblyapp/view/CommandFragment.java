package com.example.assemblyapp.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.assemblyapp.R;
import com.example.assemblyapp.config.AssemblyDatabase;
import com.example.assemblyapp.model.DataChange;
import com.example.assemblyapp.model.Command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class CommandFragment extends Fragment {
    FrameLayout layoutFrame;
    ImageView imgAvatar;
    TextView txtIcon;
    Command command;
    private Button btnSave;
    private byte[] image;
    EditText edtName, edtDescription;
    AssemblyDatabase db;
    public static final int PICK_IMAGE = 1997;
    DataChange dataChange;
    int role=0;

    public CommandFragment(Command command, AssemblyDatabase db, int role){
        this.command = command;
        this.db = db;
        this.role = role;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_command, null, false);
        initControl(view);
        bindEvent();
        return view;
    }

    private void bindEvent() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(command ==null){
                    long a = db.insertCommand(new Command(edtName.getText().toString(),edtDescription.getText().toString(),image));
                    if(a>0){
                        Toast.makeText(getContext(),"Thành công",Toast.LENGTH_SHORT).show();
                        dataChange.onErrorChange();
                    }
                    else
                        Toast.makeText(getContext(),"Thất bại",Toast.LENGTH_SHORT).show();
                }
                else {
                    command.setName(edtName.getText().toString());
                    command.setDescription(edtDescription.getText().toString());
                    if(image != null && image.length>0)
                        command.setImage(image);
                    long a = db.updateCommand(command);
                    if(a>0){
                        Toast.makeText(getContext(),"Thành công",Toast.LENGTH_SHORT).show();
                        dataChange.onErrorChange();
                    }
                    else
                        Toast.makeText(getContext(),"Thất bại",Toast.LENGTH_SHORT).show();
                }
            }
        });

        layoutFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    private void initControl(final View view) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        assert actionBar != null;
        imgAvatar = view.findViewById(R.id.imgAvatar);
        txtIcon = view.findViewById(R.id.txtIcon);
        layoutFrame = view.findViewById(R.id.layoutLeft);
        edtName = view.findViewById(R.id.edtName);
        edtDescription = view.findViewById(R.id.edtDescription);
        btnSave = view.findViewById(R.id.btnSave);
        if(command ==null)
            actionBar.setTitle("Thêm lệnh");
        else{
            actionBar.setTitle("Chi tiết");
            edtName.setText(command.getName());
            edtDescription.setText(command.getDescription());
            if(command.getImage()!=null && command.getImage().length>0){
                imgAvatar.setVisibility(View.VISIBLE);
                txtIcon.setVisibility(View.GONE);
                Glide.with(getContext()).load(command.getImage()).into(imgAvatar);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Log.d("RESULT", "OK");
            assert data != null;
            Uri imageUri = data.getData();
            try {
                final Bitmap bitmapSelection = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), imageUri);
                final Bitmap bitmapScale = Bitmap.createScaledBitmap(bitmapSelection, 150, bitmapSelection.getHeight() * 150 / bitmapSelection.getWidth(), false);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmapScale.compress(Bitmap.CompressFormat.PNG, 100, stream);
                image = stream.toByteArray();
                imgAvatar.setImageBitmap(bitmapScale);
                imgAvatar.setVisibility(View.VISIBLE);
                txtIcon.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataChange = (DataChange) context;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuID = item.getItemId();
        if(menuID==R.id.btnDelete){
            long a = db.deleteError(command.getId());
            if(a>0){
                Toast.makeText(getContext(),"Thành công",Toast.LENGTH_SHORT).show();
                dataChange.onErrorChange();
            }
            else
                Toast.makeText(getContext(),"Thất bại",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
