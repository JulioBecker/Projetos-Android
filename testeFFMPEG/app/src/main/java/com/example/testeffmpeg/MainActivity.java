package com.example.testeffmpeg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.FFmpegExecution;

import java.io.File;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    AudioRecorder recorder = new AudioRecorder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView status = findViewById(R.id.txtStatus);
        TextView ip1 = findViewById(R.id.txtIp1);
        TextView ip2 = findViewById(R.id.txtIp2);
        TextView ip3 = findViewById(R.id.txtIp3);
        TextView ip4 = findViewById(R.id.txtIp4);

        /*ip1.setText("http://192.168.1.103/stream");
        ip2.setText("http://192.168.1.109:4747/video");
        ip3.setText("http://192.168.1.105:4747/video");
        ip4.setText("http://192.168.1.103/preview");*/

        Button btnIniciar = findViewById(R.id.btnIniciar);
        Button btnParar = findViewById(R.id.btnParar);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numCameras = 0;
                if(!ip1.getText().toString().isEmpty()) numCameras++;
                if(!ip2.getText().toString().isEmpty()) numCameras++;
                if(!ip3.getText().toString().isEmpty()) numCameras++;
                if(!ip4.getText().toString().isEmpty()) numCameras++;
                if(numCameras == 0){
                    Toast.makeText(getApplicationContext(), "Nenhuma câmera selecionada", Toast.LENGTH_SHORT).show();
                    return;
                }
                String comando = "";
                switch (numCameras){
                    case 1:
                        comando = "-y -f mjpeg -r 12 -i " + ip1.getText().toString() + " -r 12 " + getDiretorioPadrao() + "teste.mp4";
                        break;
                    case 2:
                        comando = "-y -f mjpeg -r 12 -i " + ip1.getText().toString() + " -f mjpeg -r 12 -i " + ip2.getText().toString() + " -filter_complex xstack=inputs=2:layout=0_0|0_h0|w0_0|w0_h0 -r 12 " + getDiretorioPadrao() + "teste.mp4";
                        break;
                    case 3:
                        comando = "-y -f mjpeg -r 12 -i " + ip1.getText().toString() + " -f mjpeg -r 12 -i " + ip2.getText().toString() + " -f mjpeg -r 12 -i " + ip3.getText().toString() + " -filter_complex xstack=inputs=3:layout=0_0|0_h0|w0_0|w0_h0 -r 12 " + getDiretorioPadrao() + "teste.mp4";
                        break;
                    case 4:
                        comando = "-y -f mjpeg -r 12 -i " + ip1.getText().toString() + " -f mjpeg -r 12 -i " + ip2.getText().toString() + " -f mjpeg -r 12 -i " + ip3.getText().toString() +  " -f mjpeg -r 12 -i " + ip4.getText().toString() + " -filter_complex xstack=inputs=4:layout=0_0|0_h0|w0_0|w0_h0 -r 12 " + getDiretorioPadrao() + "teste.mp4";
                        break;
                }

                FFmpeg.executeAsync(comando, new ExecuteCallback() {
                    @Override
                    public void apply(long executionId, int returnCode) {
                    }
                });

                status.setText("Gravação em execução...");

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        recorder.start("audio");
                    }
                });
            }
        });

        btnParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status.setText("Finalizando vídeo, aguarde...");
                FFmpeg.cancel();
                recorder.stop();
                String comando = "-y -i " + getDiretorioPadrao() + "teste.mp4 -i " + getDiretorioPadrao() + "audio.3gp " + getDiretorioPadrao() + "videoFinal.mp4";
                FFmpeg.executeAsync(comando, new ExecuteCallback() {
                    @Override
                    public void apply(long executionId, int returnCode) {
                        status.setText("Pronto!");
                    }
                });
            }
        });

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO},
                101);

    }

    public static String getDiretorioPadrao() {
        //Versão 2 deixar oculta a pasta
        File f = new File(Environment.getExternalStorageDirectory() + "/.exata");
        if(!f.isDirectory()) {
            File directory = new File(Environment.getExternalStorageDirectory()+"/.exata");
            directory.mkdirs();
        }
        return Environment.getExternalStorageDirectory()+"/.exata/";
    }
}