package com.example.antitheftass;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {

    Button showMap;
    Button Access;

    private static final int REQ_CODE= 200;

    @SuppressLint("MissingInflatedId")
    @Override

    //ring
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //for access
        Access = findViewById(R.id.Access);

        final MediaPlayer ring = MediaPlayer.create(this, R.raw.ring);


        CardView ringbtn = (CardView) this.findViewById(R.id.ringbtn);

        ringbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ring.start();
            }
        });



        showMap = findViewById(R.id.showMap);

        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        //for access file
        Access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPer()){
                    Toast.makeText(MainActivity.this,"Permission  Already Granted",Toast.LENGTH_SHORT).show();
                }else {

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{ACCESS_FINE_LOCATION,READ_SMS,READ_EXTERNAL_STORAGE},REQ_CODE );
                }


            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_CODE) {

            if (grantResults.length>0){
                int loc = grantResults[0];
                int msg = grantResults[1];
                int est = grantResults[2];


                boolean checkLoc = loc == PackageManager.PERMISSION_GRANTED;
                boolean checkMsg = msg == PackageManager.PERMISSION_GRANTED;
                boolean checkEst = est == PackageManager.PERMISSION_GRANTED;


                if (checkLoc && checkMsg && checkEst ){
                    Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public boolean checkPer(){
        int resultLock = ActivityCompat.checkSelfPermission(this,ACCESS_FINE_LOCATION);

        int resultMsgRead = ActivityCompat.checkSelfPermission(this,READ_SMS);

        int resultEst = ActivityCompat.checkSelfPermission(this,READ_EXTERNAL_STORAGE);


        return resultLock == PackageManager.PERMISSION_GRANTED && resultMsgRead == PackageManager.PERMISSION_GRANTED
                && resultEst == PackageManager.PERMISSION_GRANTED ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.menu_example, menu);
        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contact:
                Intent intent = new Intent(MainActivity.this, contact.class);
                startActivity(intent);
                break;
            case R.id.about:
                Intent intent1 = new Intent(MainActivity.this, About.class);
                startActivity(intent1);
                break;

            case R.id.logout:
                logout();
                break;

        }
        return false;
    }
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}