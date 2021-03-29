package com.example.appmosaico;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ftpButton = findViewById(R.id.buttonFTP);
        Button mosaicoButton = findViewById(R.id.buttonMosaico);

        ftpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.setProperty(FTPClient.FTP_SYSTEM_TYPE_DEFAULT, FTPClientConfig.SYST_UNIX);

                FTPClient con = new FTPClient();
                String host = "192.168.4.1";
                String usuario = "suporte";
                String senha = "daysoft";

                try{
                    con.connect(host);
                    if(con.login(usuario, senha)){
                        con.enterLocalPassiveMode();
                        con.setFileType(FTPClient.BINARY_FILE_TYPE);
                        con.setBufferSize(1024*1024);
                        con.changeWorkingDirectory("/files");
                        List<FTPFile> files = Arrays.asList(con.mlistDir());
                        Collections.reverse(files);

                        File defaultDirectory = new File(getDiretorioPadrao());
                        File[] defaultDirectoryFiles = defaultDirectory.listFiles();

                        for (FTPFile file : files) {
                            String outpath = getDiretorioPadrao() + file.getName();
                            FileOutputStream output = new FileOutputStream(outpath);
                            output.flush();
                            IOUtils.closeQuietly(output);
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Senha ou usuário incorreto", Toast.LENGTH_LONG).show();
                    }

                    con.logout();
                    con.disconnect();
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Sem conexão FTP", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    return;
                }
            }
        });

        mosaicoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File diretorioVideos = new File(getDiretorioPadrao());
                File[] files = diretorioVideos.listFiles();

                if(files == null){
                    Toast.makeText(getApplicationContext(), "Nenhum arquivo encontrado", Toast.LENGTH_LONG).show();
                    return;
                }

                if (files.length >= 2) {
                    if (files[0].getName().contains("avi") && files[1].getName().contains("avi")) {
                        String comando = "-i " + files[0].getPath() + " -i " + files[1].getPath() + " -filter_complex vstack=inputs=2 " + "mosaicoFinal.mp4";
                        int rc = FFmpeg.execute(comando);
                        if (rc == RETURN_CODE_SUCCESS) {
                            Log.i(Config.TAG, "Command execution completed successfully.");
                        } else if (rc == RETURN_CODE_CANCEL) {
                            Log.i(Config.TAG, "Command execution cancelled by user.");
                        } else {
                            Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", rc));
                            Config.printLastCommandOutput(Log.INFO);
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Nenhum arquivo .avi encontrado", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
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