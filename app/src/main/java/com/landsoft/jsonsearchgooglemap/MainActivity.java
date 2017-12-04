package com.landsoft.jsonsearchgooglemap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.landsoft.jsonsearchgooglemap.BroadcastReceiver.SettingReceiver;
import com.landsoft.jsonsearchgooglemap.DATA.DownloadDataJson;
import com.landsoft.jsonsearchgooglemap.Preference.SettingPreference;

import java.util.ArrayList;
import java.util.List;

import static com.landsoft.jsonsearchgooglemap.Constants.Contant.ANY;
import static com.landsoft.jsonsearchgooglemap.Constants.Contant.LIST_PREFERENCE;
import static com.landsoft.jsonsearchgooglemap.Constants.Contant.REQUEST_CODE;
import static com.landsoft.jsonsearchgooglemap.Constants.Contant.RESULT_CODE;
import static com.landsoft.jsonsearchgooglemap.Constants.Contant.SETTING_PREFERENCE;
import static com.landsoft.jsonsearchgooglemap.Constants.Contant.WIFI;

public class MainActivity extends AppCompatActivity {
    private EditText edtAddress;
    private Button btnGetAddress;
    TextView tvLongItude, tvLatItude; //kinh do, vi do

    private boolean refreshDislay = false;
    private boolean isConnectWifi = false;
    private boolean isConnectMobile = false;
    private String mPreference = "";
    String urlPath="https://maps.googleapis.com/maps/api/geocode/json?address=";

    private SettingReceiver settingReceiver;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtAddress = findViewById(R.id.edt_address);
        btnGetAddress = findViewById(R.id.btn_get_address);
        tvLongItude = findViewById(R.id.tv_lng);
        tvLatItude = findViewById(R.id.tv_lat);
        checkAndRequestPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //tim gia tri luu mat dinh cua Shared Preference
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPreference = preferences.getString(LIST_PREFERENCE,"Wi-Fi");

        //dang ky broadcast receiver
        settingReceiver = new SettingReceiver(mPreference,refreshDislay);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(settingReceiver,filter);

        //kiem tra ket noi internet
        checkOnConnectNetwork();

        if(refreshDislay || isConnectWifi) {
            loadPage();
        }
    }

    private void loadPage() {
        if( (ANY.equals(mPreference) && (isConnectWifi || isConnectMobile))||
         (WIFI.equals(mPreference) && isConnectWifi))
        {
            setViewData();
        }else
            Toast.makeText(this, " No connect Internet, please check Network",Toast.LENGTH_LONG).show();
    }

    private void setViewData() {
        btnGetAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mUrl = urlPath + edtAddress.getText().toString();
                mUrl = mUrl.replace(" ","%20");
                DownloadDataJson dataJson = new DownloadDataJson(tvLatItude,tvLongItude);
                dataJson.execute(mUrl);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (settingReceiver != null){
            unregisterReceiver(settingReceiver);
        }

    }

    private void checkOnConnectNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            isConnectWifi = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            isConnectMobile = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            isConnectWifi = false;
            isConnectMobile = false;
        }
    }

    // xin quyen
    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }
    // thiet lap menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }
    //thiet lap khi chon menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mn_setting:
                Intent intent = new Intent(MainActivity.this, SettingPreference.class);
                startActivityForResult(intent,REQUEST_CODE);
                return true;
            case R.id.mn_refresh:
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
    // ket qua tra ve sau khi goi thiet lap prefarence
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE){
            refreshDislay = data.getBooleanExtra(SETTING_PREFERENCE,false);
        }
    }
}
