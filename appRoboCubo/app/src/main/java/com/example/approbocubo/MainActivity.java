package com.example.approbocubo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter meuBluetoothAdapter;
    BluetoothDevice meuDevice;
    BluetoothSocket meuSocket;

    ConnectedThread connectedThread;

    static final int SOLICITA_ATIVACAO = 1;
    static final int SOLICITA_CONEXAO  = 2;
    static final int MESSAGE_READ      = 3;

    static Handler mHandler;
    StringBuilder dadosBluetooth = new StringBuilder();

    boolean conexao = false;
    UUID meuUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    Button conectarBT;
    TextView txtStatusConn;
    volatile ArrayList<String> dadosRecebidos;

    Button btnFrente, btnTras, btnEsquerda, btnDireita, btnChute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtStatusConn = findViewById(R.id.txtStatusConn);
        conectarBT = findViewById(R.id.btnConectar);

        btnFrente = findViewById(R.id.btnFrente);
        btnTras = findViewById(R.id.btnTras);
        btnEsquerda = findViewById(R.id.btnEsquerda);
        btnDireita = findViewById(R.id.btnDireita);
        btnChute = findViewById(R.id.btnChute);

        meuBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(meuBluetoothAdapter == null){
            Toast.makeText(getApplicationContext(), "Não há suporte para Bluetooth no dispositivo.", Toast.LENGTH_LONG).show();
        }
        else if(!meuBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, SOLICITA_ATIVACAO);
        }

        conectarBT.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(conexao){
                    try{
                        meuSocket.close();
                        conexao = false;
                        conectarBT.setText("Conectar");
                        Toast.makeText(getApplicationContext(), "Bluetooth foi desconectado.", Toast.LENGTH_LONG).show();
                        txtStatusConn.setText("Status Conexão: Desconectado");
                    }
                    catch (IOException erro){
                        Toast.makeText(getApplicationContext(), "Erro ao fechar conexão: " + erro, Toast.LENGTH_LONG).show();
                        txtStatusConn.setText("Status Conexão: Conectado");
                    }
                }
                else{
                    txtStatusConn.setText("Status Conexão: Conectando...");
                    Intent abreLista = new Intent(MainActivity.this, ListaDispositivos.class);
                    startActivityForResult(abreLista, SOLICITA_CONEXAO);
                }
            }
        });

        btnChute.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(conexao) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            connectedThread.write("c");
                            break;
                        case MotionEvent.ACTION_UP:
                            connectedThread.write("p");
                            break;
                    }
                }
                return true;
            }
        });

        btnFrente.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(conexao) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            connectedThread.write("f");
                            break;
                        case MotionEvent.ACTION_UP:
                            connectedThread.write("p");
                            break;
                    }
                }
                return true;
            }
        });

        btnTras.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(conexao) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            connectedThread.write("t");
                            break;
                        case MotionEvent.ACTION_UP:
                            connectedThread.write("p");
                            break;
                    }
                }
                return true;
            }
        });

        btnEsquerda.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(conexao) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            connectedThread.write("e");
                            break;
                        case MotionEvent.ACTION_UP:
                            connectedThread.write("p");
                            break;
                    }
                }
                return true;
            }
        });

        btnDireita.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(conexao){
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            connectedThread.write("d");
                            break;
                        case MotionEvent.ACTION_UP:
                            connectedThread.write("p");
                            break;
                    }
                }
                return true;
            }
        });

        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if(msg.what == MESSAGE_READ){
                    String recebido = (String) msg.obj;

                    dadosBluetooth.append(recebido);
                    int fimInformacao = dadosBluetooth.indexOf("}");
                    if(fimInformacao > 0){
                        String dadosCompletos = dadosBluetooth.substring(0, fimInformacao);
                        int tamanhoInformacao = dadosCompletos.length();
                        if(dadosBluetooth.charAt(0) == '{'){
                            String dadosFinais = dadosBluetooth.substring(1, tamanhoInformacao);
                            if(dadosRecebidos == null) {
                                dadosRecebidos = new ArrayList<>();
                                dadosRecebidos.add("");
                                dadosRecebidos.add("");
                                dadosRecebidos.add("");
                                dadosRecebidos.add("");
                                dadosRecebidos.add("");
                                //dadosRecebidos.add("");
                                //dadosRecebidos.add("");
                                //dadosRecebidos.add("");
                            }
                            dadosRecebidos.set(0,dadosFinais.substring(0, 3));
                            dadosRecebidos.set(1,dadosFinais.substring(3, 6));
                            dadosRecebidos.set(2,dadosFinais.substring(6, 9));
                            dadosRecebidos.set(3,dadosFinais.substring(9, 12));
                            dadosRecebidos.set(4, dadosFinais.substring(12));
                            //atualizaValoresLidos(dadosFinais.substring(12));
                            //dadosRecebidos.set(5,dadosFinais.substring(15, 18));
                            //dadosRecebidos.set(6,dadosFinais.substring(18, 21));
                            //dadosRecebidos.set(7,dadosFinais.substring(21, 24));
                            //atualizaListaDadosRecebidos(dadosRecebidos);
                        }
                        dadosBluetooth.delete(0, dadosBluetooth.length());
                    }
                }
            }
        };
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SOLICITA_ATIVACAO:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(getApplicationContext(), "Bluetooth ativado.", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Bluetooth não ativado, o aplicativo será encerrado.", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case SOLICITA_CONEXAO:
                if(resultCode == Activity.RESULT_OK){
                    assert data != null;
                    String MAC = data.getExtras().getString(ListaDispositivos.ENDERECO_MAC);
                    meuDevice = meuBluetoothAdapter.getRemoteDevice(MAC);
                    try{
                        meuSocket = meuDevice.createInsecureRfcommSocketToServiceRecord(meuUUID);
                        meuSocket.connect();
                        conexao = true;
                        conectarBT.setText("Desconectar");

                        connectedThread = new ConnectedThread(meuSocket);
                        connectedThread.start();

                        txtStatusConn.setText("Status Conexão: Conectado");

                        //if(conexao) {
                        //    if(!enviaComandos.isAlive())
                        //enviaComandos.start();
                        //}

                        Toast.makeText(getApplicationContext(), "Conetado!", Toast.LENGTH_LONG).show();
                    }
                    catch (IOException erro){
                        conexao = false;
                        conectarBT.setText("Conectar");
                        Toast.makeText(getApplicationContext(), "Erro ao conectar." /*+ erro*/, Toast.LENGTH_LONG).show();
                        txtStatusConn.setText("Status Conexão: Desconectado");
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Nenhum dispositivo Bluetooth selecionado.", Toast.LENGTH_LONG).show();
                    conectarBT.setText("Conectar");
                    txtStatusConn.setText("Status Conexão: Desconectado");
                }
                break;
        }
    }

    private class ConnectedThread extends Thread {

        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException ignored) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        @SuppressWarnings("InfiniteLoopStatement")
        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    String dadosBT = new String(buffer, 0, bytes);

                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, dadosBT)
                            .sendToTarget();
                } catch (IOException ignored) {
                    //break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        void write(String enviar) {
            byte[] msg = enviar.getBytes();
            try {
                mmOutStream.write(msg);
            } catch (IOException ignored) {

            }
        }
    }
}
