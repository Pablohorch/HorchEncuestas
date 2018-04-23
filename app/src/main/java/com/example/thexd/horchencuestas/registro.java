package com.example.thexd.horchencuestas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class registro extends AppCompatActivity {

    Spinner spiGenero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        inicializador();
        String[] arrayGeneros = {"Mujer","Hombre","Indefinido"};
        spiGenero.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, arrayGeneros));

    }
    public void inicializador(){
        spiGenero=(Spinner) findViewById(R.id.spinner);
    }
    public void clicComprobar(View v){

    }


    public Boolean comprobador(String nick){
        try {
            URL url=new URL("https://www.instagram.com/"+nick+"/");

            URLConnection conexion = url.openConnection();
            conexion.setDoInput(true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            System.out.println("Existe");

            String linea="";
            String codigo="";


            /*while ((linea = reader.readLine()) != null) {
                codigo += linea+"\n";
            }
            if (codigo.contains(",\"is_private\":false,\"")) {
                System.out.println("Cuenta publica");
            }else {
                System.out.println("Cuenta privada");
            }*/

            int x=0;
            int y=0;
            String enlace="";
            String busco="profile_pic_url_hd";
            boolean z=false;
            for (int i = 0; i < (codigo.length()-busco.length()-1); i++) {
                if (busco.equals(codigo.substring(i, i+busco.length()))) {
                    z=true;
                    x=i+busco.length()+3;
                }

                if (z && codigo.substring(i,i+4).equals(".jpg")) {
                    y=i+4;
                    enlace=codigo.substring(x,y);

                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("no existe");
        }



        return null;
    }
}
