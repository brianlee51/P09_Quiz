package sg.edu.rp.c346.p09_quiz;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    Button btnSave, btnRead, btnMap;
    EditText etCor;
    TextView tv;
    String folderLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSave = findViewById(R.id.btnSave);
        btnRead = findViewById(R.id.btnRead);
        btnMap = findViewById(R.id.btnMap);
        tv = findViewById(R.id.tv);
        etCor = findViewById(R.id.etCor);

        int permissionCheck = PermissionChecker.checkSelfPermission
                (MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PermissionChecker.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            return;
        }
        folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder";

        File folder = new File(folderLocation);
        if (folder.exists() == false){
            boolean result = folder.mkdir();
            if (result == true){
                Log.d("File Read/Write", "Folder created");
            }
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coordinates = etCor.getText().toString();
                if (coordinates.length() == 0) {
                    Toast.makeText(getBaseContext(), "Cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    if (checkPermission() == true) {
                        try {
                            folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder";
                            File targetFile = new File(folderLocation, "quiz.txt");
                            FileWriter writer = new FileWriter(targetFile, false);
                            writer.write(coordinates);
                            writer.flush();
                            writer.close();
                            Toast.makeText(MainActivity.this, "File Created", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Failed to write", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder";
                File targetFile = new File(folderLocation, "quiz.txt");
                if (targetFile.exists() == true) {
                    String data = "";
                    try {
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br = new BufferedReader(reader);
                        String line = br.readLine();
                        while (line != null) {
                            data += line;
                            line = br.readLine();
                        }
                        br.close();
                        reader.close();
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), "Failed to read", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    Log.d("content", data);
                    tv.setText(data);
                }
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), SecondActivity.class);
                String coordinates = tv.getText().toString();
                if (coordinates.length() == 0) {
                    Toast.makeText(getBaseContext(), "Nothing to pass", Toast.LENGTH_LONG).show();
                } else {
                    i.putExtra("coordinates", coordinates);
                    startActivity(i);
                }
            }
        });
    }
    private boolean checkPermission(){
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}
