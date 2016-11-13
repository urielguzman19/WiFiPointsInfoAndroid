package com.example.uriel.apsinformation;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class AccessPoints extends ActionBarActivity implements View.OnClickListener {

    TextView textConnected,textNetworks,textMAC_ADDRESS;
    Button botonEscaneo,botonDetenerEscaneo;
    private static final String TAG="activity_main";
    private final Handler handler = new Handler();
    WifiManager wifi;
    WifiInfo myWifiInfo;
    Button nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_points);

        textConnected =(TextView)findViewById(R.id.txtConnected);
        textNetworks = (TextView)findViewById(R.id.txt_NetworksAvailable);
        textMAC_ADDRESS =(TextView)findViewById(R.id.txt_MAC);
        botonEscaneo = (Button) findViewById(R.id.botonStarScan);
        botonEscaneo.setOnClickListener(this);
        botonDetenerEscaneo = (Button) findViewById(R.id.botonStopScan);
        botonDetenerEscaneo.setOnClickListener(this);
        nextActivity =(Button) findViewById(R.id.next_activity);
        nextActivity.setOnClickListener(this);

        this.registerReceiver(ListNetworksChangeReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        StarScan();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.botonStarScan:
                StarScan();
                break;
            case R.id.botonStopScan:
                StopScan();
                break;
            case R.id.next_activity:
                Intent i = new Intent(AccessPoints.this, AccessPoints2.class);
                startActivity(i);
                break;

        }
    }
    public void StarScan() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
               myWifiInfo = wifi.getConnectionInfo();
              /*Verifica si la conexión Wi-Fi del
              *dispositivo esta activa*/
                if (wifi.isWifiEnabled()) {
                    List<ScanResult> list = wifi.getScanResults();
                    StringBuffer scanList = new StringBuffer();
                    if (list != null) {
                        /*ForEach para recorrer los elementos de la lista*/
                        for (ScanResult scanResult : list) {
                            /*Guarda los elementos en String Buffer*/
                            scanList.append(scanResult.SSID + " " + scanResult.level + " dbM" + " " + scanResult.BSSID + "\n");
                        }

                        textConnected.setText("----Wi-Fi Enabled----");
                        textNetworks.setText(scanList);
                        textMAC_ADDRESS.setText(myWifiInfo.getMacAddress());

                    } else {
                        Toast.makeText(getApplicationContext(), "No Access Points are available", Toast.LENGTH_LONG).show();
                        textNetworks.setText("--------");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Wi-Fi connection disabled.Please enable it :(", Toast.LENGTH_LONG).show();
                    textConnected.setText("----Wi-Fi Disabled----");
                    textMAC_ADDRESS.setText("---------");
                }
            }
            /*Retardo en Milisegundos para que el Handler
             * vuelva a ejecutar el Runabble
             *5000 ms = 5 s */
        },5000);
    }
    public void StopScan(){
        Toast.makeText(getApplicationContext(),"Scan for APs has stopped¡",Toast.LENGTH_LONG).show();
        textNetworks.setText("--------");
        textMAC_ADDRESS.setText("---------");

    }
    /*Determina los cambios en los APs*/
    private BroadcastReceiver ListNetworksChangeReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            StarScan();
        }
    };
     /*Ciclo de Vida de la Actividad*/
    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }
    @Override
    protected void onResume() {
        registerReceiver(ListNetworksChangeReceiver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
        Log.i(TAG, "onResume");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_access_points, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
