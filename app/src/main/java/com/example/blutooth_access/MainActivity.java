package com.example.blutooth_access;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    TextView activeBlutoothStatus, pairedBlutoothDevices;
    ImageView blutoothIcon;
    Button turnOnBtn, turnOffBtn, discoverableBtn, pairedDeviceBtn;

    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activeBlutoothStatus = findViewById(R.id.statusBlutoothTv);
        pairedBlutoothDevices = findViewById(R.id.PairedDevicesText);
        blutoothIcon = findViewById(R.id.blutoothIcon);
        turnOnBtn = findViewById(R.id.blutoothOnBtn);
        turnOffBtn = findViewById(R.id.blutoothOffBtn);
        discoverableBtn = findViewById(R.id.discoverableBtn);
        pairedDeviceBtn = findViewById(R.id.PairedDeviceBtn);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            activeBlutoothStatus.setText("Blutooth is not available !!");
        } else {

            activeBlutoothStatus.setText("Blutooth is available !!");
        }

//        checking whether blutooth is enabled or not

        if (bluetoothAdapter.isEnabled()) {
            blutoothIcon.setImageResource(R.mipmap.blutooth_on);
        } else {
            blutoothIcon.setImageResource(R.mipmap.blutooth_off);
        }

        //enaling button to on

        turnOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bluetoothAdapter.isEnabled()) {
                    Toast.makeText(MainActivity.this, "Enabling blutooth....", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        startActivityForResult(intent, REQUEST_ENABLE_BT);
                        return;
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Blutooth is already Enable", Toast.LENGTH_SHORT).show();

                }
            }
        });

//        turn Off blutooth
        turnOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter.isEnabled()) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        bluetoothAdapter.disable();
                        blutoothIcon.setImageResource(R.mipmap.blutooth_off);

                        return;
                    }

                    Toast.makeText(MainActivity.this, "Blutooth is Disabled !!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Blutooth is Already Disabled !!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //discoverable on click listener

        discoverableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    if (!bluetoothAdapter.isDiscovering()) {
                        Toast.makeText(MainActivity.this, "Making your Deivces Discoverable...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        startActivityForResult(intent, REQUEST_DISCOVER_BT);
                        return;
                    }
                }

            }
        });

        //paired devices on click lisenter

        pairedDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothAdapter.isEnabled()) {
                    pairedBlutoothDevices.setText("Paired Devices");
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                        for(BluetoothDevice device:devices){
                            pairedBlutoothDevices.append("\nDevices"+device.getName()+","+device);
                        }
                        return;
                    }


                }else{
                    Toast.makeText(MainActivity.this, "Turn On Your Blutooth To get the Paired Device list...", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_ENABLE_BT){
            if(resultCode==RESULT_OK){
                blutoothIcon.setImageResource(R.mipmap.blutooth_on);
                Toast.makeText(MainActivity.this,"Blutooth is On..",Toast.LENGTH_SHORT).show();
            }
            else {
                blutoothIcon.setImageResource(R.mipmap.blutooth_off);
                Toast.makeText(MainActivity.this,"Blutooth is off..",Toast.LENGTH_SHORT).show();
            }
        }
    }
}