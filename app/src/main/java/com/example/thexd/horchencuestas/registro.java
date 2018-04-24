package com.example.thexd.horchencuestas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Pattern;

public class registro extends AppCompatActivity {

    Spinner spiGenero;
    EditText txtPassword;
    EditText txtNick;
    EditText txtNumber;
    EditText txtCorreo;
    EditText txtEdad;
    String enlace="";
    EditText txtLocalidad;
    Button btnComprobarNick;

    ImageView fotoPerfil;
    Bitmap mapaImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        inicializador();
        String[] arrayGeneros = {"Mujer","Hombre","Genero Fluido","Agender","Otros"};
        spiGenero.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, arrayGeneros));

    }
    public void inicializador(){
        spiGenero=(Spinner) findViewById(R.id.spiRegGenero);
        txtPassword=(EditText) findViewById(R.id.txtRegPassWord);
        txtNick=(EditText) findViewById(R.id.txtRegnickNombre);
        txtEdad=(EditText) findViewById(R.id.txtRegEdad);
        txtNumber=(EditText) findViewById(R.id.txtRegNumero);
        txtCorreo=(EditText) findViewById(R.id.txtRegCorreoE);
        txtLocalidad=(EditText) findViewById(R.id.txtRegLocalidad);
        btnComprobarNick=(Button) findViewById(R.id.btnRegComprobar);
        fotoPerfil=(ImageView) findViewById(R.id.imgPerfil);
    }
    public void clicComprobar(View v){
        btnComprobarNick.setText("Loading...");
        final Handler mHandler = new Handler();
        new Thread(){
            @Override
            public void run() {
              final Boolean existencia=comprobador(txtNick.getText().toString());


             mHandler.post(new Runnable() {
                  @Override
                  public void run() {
                      if (existencia){

                          final Handler mHandler = new Handler();
                          new Thread(){
                              @Override
                              public void run() {
                                  final Bitmap existencia=urlImageToBitmap(enlace);
                                  mHandler.post(new Runnable() {
                                      @Override
                                      public void run() {
                                          fotoPerfil.setImageBitmap(existencia);
                                      }
                                  });}}.start();

                          btnComprobarNick.setText("COMPROBAR - Todo Correcto");


                      }else {
                          btnComprobarNick.setText("COMPROBAR - Error, No Existe");
                      }

                  }
              });

            }
        }.start();
    }

public void resgistrar(View v){
    String nombre=txtNick.getText().toString();
    String numero=txtNumber.getText().toString();
    String correo=txtCorreo.getText().toString();
    String localidad=txtLocalidad.getText().toString();
    String contrasenia=txtPassword.getText().toString();
    String edad=txtEdad.getText().toString();
    String genero;

    int x=0;
    if (btnComprobarNick.getText().toString().contains("CORRECTO")){
        x++;
    }
    if (numero.length()==9){
        x++;
    }
    if (!validarEmail(correo)){
        txtCorreo.setError("Email no válido");
    }else{
        x++;
    }
    if (localidad.length()>2){
        x++;
    }
    if (!(txtEdad.getText().toString().isEmpty())){
        x++;
    }
    if (!(txtPassword.getText().toString().isEmpty())){
        x++;
    }
    genero=spiGenero.getSelectedItem().toString();



    if (x==6){


    new ConexionBD().execute("Usu","i",nombre+"-"+numero+"-"+localidad+"-"+contrasenia+"-"+edad+"-"+correo+"-"+genero+"-"+enlace);}


}
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public Boolean comprobador(String nick){

        try {
            URL url=new URL("https://www.instagram.com/"+nick+"/");

            URLConnection conexion = url.openConnection();
            conexion.setDoInput(true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));


            String linea="";
            String codigo="";


            while ((linea = reader.readLine()) != null) {
                codigo += linea+"\n";
            }

            int x=0;
            int y=0;
            String busco="profile_pic_url_hd";
            boolean z=false;
            for (int i = 0; i < (codigo.length()-busco.length()-1); i++) {
                if (busco.equals(codigo.substring(i, i + busco.length()))) {
                    z = true;
                    x = i + busco.length() + 3;
                }

                if (z && codigo.substring(i, i + 4).equals(".jpg")) {
                    y = i + 4;
                    enlace=codigo.substring(x, y);

                    break;
                }

            }
                return true;

            } catch (Exception e) {

            return false;
        }


    }
    public Bitmap urlImageToBitmap(String urlImage) {
        Bitmap mIcon1 = null;
        URL url_value = null;
        try {
            url_value = new URL(urlImage);

            if (url_value != null) {
                mIcon1 = BitmapFactory.decodeStream(url_value.openConnection().getInputStream());
            }
            return mIcon1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}