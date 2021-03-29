package com.example.approbocubo;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class ListaDispositivos extends ListActivity {

    private BluetoothAdapter meuBluetoothAdapter;
    static String ENDERECO_MAC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> arrayBluetooth = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        meuBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> dispositivosPareados = meuBluetoothAdapter.getBondedDevices();

        if(dispositivosPareados.size() > 0){
            for(BluetoothDevice dispositivo:dispositivosPareados){
                String nomeBT = dispositivo.getName();
                String macBT = dispositivo.getAddress();
                arrayBluetooth.add(nomeBT + "\n" + macBT);
            }
        }
        setListAdapter(arrayBluetooth);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String informacaoGeral = ((TextView) v).getText().toString();
        String enderecoMAC = informacaoGeral.substring(informacaoGeral.length() - 17);

        Intent retornaMAC = new Intent();
        retornaMAC.putExtra(ENDERECO_MAC, enderecoMAC);
        setResult(RESULT_OK, retornaMAC);
        finish();
    }
}
