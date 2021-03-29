package com.example.testehotspot;

import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.Manifest;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DEBUGIPS";
    TextView textView, ipCount;
    ProgressBar progressBar;
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        ipCount = findViewById(R.id.txtIpCount);
        Button btnAtualizar = findViewById(R.id.btnAtualizar);
        Button configZuc = findViewById(R.id.btnConfig);
        textView = findViewById(R.id.textView);
        TextView txtSSID = findViewById(R.id.txtSSID);
        TextView txtSenha = findViewById(R.id.txtSenha);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //ativaHotSpot(wifiManager);

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                textView.setText("");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        checkHosts(getIPAddress(true).substring(0, getIPAddress(true).lastIndexOf('.')));
                        ipCount.setText("OK");
                    }
                };
                Thread t = new Thread(runnable);
                t.start();
            }
        });

        configZuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txtSSID.getText().toString().isEmpty() && !txtSenha.getText().toString().isEmpty()){
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            final boolean connect = configZucchero(txtSSID.getText().toString(), txtSenha.getText().toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(connect){
                                        Toast.makeText(getApplicationContext(), "Zucchero configurado com sucesso!", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Erro ao configurar ZUcchero", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    };
                    Thread t = new Thread(runnable);
                    t.start();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Algum campo está vazio", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    private void checkHosts(String subnet)
    {
        try
        {
            int timeout=200;
            for (int i=1;i<255;i++)
            {
                int finalI = i;
                String host=subnet + "." + i;
                if (InetAddress.getByName(host).isReachable(timeout))
                {
                    //textView.append(host);
                    Log.d(TAG, "checkHosts() :: "+ host + " is reachable");
                    if(isZucchero(host)){
                        textView.append(host + " <- um Zucchero\n");
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        float porc = (float) finalI*100/255;
                        String aux = String.format("%.2f", porc);
                        ipCount.setText(aux + "%");
                    }
                });
            }
        }
        catch (UnknownHostException e)
        {
            Log.d(TAG, "checkHosts() :: UnknownHostException e : "+e);
            e.printStackTrace();
        }
        catch (IOException e)
        {
            Log.d(TAG, "checkHosts() :: IOException e : "+e);
            e.printStackTrace();
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public boolean configZucchero(String ssidConn, String passConn){
        String dataUrl = "http://192.168.4.1/record?ssid=" + ssidConn + "&senha=" + passConn;
        try {
            String aux = run(dataUrl);
        } catch (IOException e) {
            e.printStackTrace();
            if(e.getMessage().contains("Software caused connection abort")){
                return true;
            }
            else{
                return false;
            }
        }
        return true;
    }

    public boolean isZucchero(String host){
        boolean retorno = false;
        String dataUrl = "http://" + host + "/status";
        HttpURLConnection connection = null;
        try {
            URL url = new URL(dataUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.flush();
            wr.close();
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuffer response = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {response.append(line);response.append('\r');}
            rd.close();
            String responseStr = response.toString();
            Log.d("Server response", responseStr);
            if(responseStr.contains("rectime"))
                retorno = true;
        } catch (Exception e) {
            e.printStackTrace();
            retorno = false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return retorno;
    }

    public boolean ativaHotSpot(WifiManager wifiManager){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && wifiManager != null) {
            WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            Method[] wmMethods = wifi.getClass().getDeclaredMethods();
            for (Method method: wmMethods) {
                if (method.getName().equals("isWifiApEnabled")) {

                    try {
                        Boolean isWifiAPenabled = (Boolean) method.invoke(wifi);
                        Log.e("wifiManager", "para de assobiar :< " + isWifiAPenabled);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        return false;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return false;
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }

            wifiManager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {
                @Override
                public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                    super.onStarted(reservation);
                    Log.e("WifiManager", " startLocalOnlyHotspot onStarted");
                    Log.e("WifiManager", " startLocalOnlyHotspot onStarted [SSID]" + reservation.getWifiConfiguration().SSID +" [PreSharedKey]" + reservation.getWifiConfiguration().preSharedKey);
                    for (String key : reservation.getWifiConfiguration().wepKeys) {
                        Log.e("WifiManager", "Key: " + key);
                    }

                }

                @Override
                public void onStopped() {
                    super.onStopped();
                    Log.e("WifiManager", " startLocalOnlyHotspot onStopped");
                }

                @Override
                public void onFailed(int reason) {
                    super.onFailed(reason);
                    Log.e("WifiManager", " startLocalOnlyHotspot onFailed, Reason:" + reason);
                }
            }, null);
        }
        return true;
    }
}