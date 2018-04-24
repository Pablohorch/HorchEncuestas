package com.example.thexd.horchencuestas;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    static Vector<String> infoSesion=new Vector<String>();

    Window ventanaPrincipal=getWindow();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer=(DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView=(NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onStart(){
        super.onStart();
        if (infoSesion.size()>0){
            guardar();
        }else{
            Toast.makeText(this,"Error que ni cristo sabe cual es", Toast.LENGTH_LONG).show();

        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer=(DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id=item.getItemId();

        if (id == R.id.nav_sesion) {
            Intent abrirRegistro=new Intent(this, registro.class);
            startActivity(abrirRegistro);
        } else if (id == R.id.nav_encuesta) {

        } else if (id == R.id.nav_respuesta) {

        } else if (id == R.id.nav_modoNochee) {

        } else if (id == R.id.nav_Compartir) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer=(DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void guardar(){
        try {
            OutputStreamWriter fout= new OutputStreamWriter(openFileOutput("sesion.txt", Context.MODE_PRIVATE));
            for (int x=0;x<infoSesion.size();x++){
                fout.write(infoSesion.elementAt(x));
            }
            fout.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
    }



}

//-------------------------------------------------------------------------------------------------------------------------------------

class ConexionBD extends AsyncTask<String,String,Vector<String>> {
    public Vector<String> getSalida() {
        return salida;
    }

    public void setSalida(Vector<String> salida) {
        this.salida = salida;
    }

    Vector<String> salida;
    public ConexionBD() {
        super();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Vector<String> doInBackground(String... strings) {
        Vector<String> a=new Vector<String>();
        if (strings[0].equals("usu")){
            if (strings[1].equals("i")){
                String[] informacion=strings[2].split("-");
                insertarPersonas(informacion[0],informacion[1],informacion[2],informacion[3],informacion[4],informacion[5],informacion[6],informacion[7]);
            }
            if (strings[1].equals("e")){
                String[] informacion=strings[2].split("-");
                Boolean boo=iniciarPersona(informacion[0],informacion[1]);
                Log.e("---------------","EL TAMAÑO ES :"+boo);
                if (boo){
                    a.add("inicio");
                }else {
                    a.add("error");
                }

            }
        }
        return a;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Vector<String> strings) {
          setSalida(strings);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }


    public void insertarPregunta(String preguntas,String respuestas){
        try {
            Connection con=ConexionBD();
            Statement st=con.createStatement();
            st.executeUpdate("insert into preguntas(preguntas,aprobadaPregunta) values('"+preguntas+"',false)");
            con.close();

            String[] respu=respuestas.split("-");
            for (int x=0;x<respu.length;x++)
                insertarRespuesta(respu[x]);

        }
        catch(Exception e) {
            e.printStackTrace();

        }
    }

    public void insertarRespuesta(String respuesta){
        try {
            Connection con=ConexionBD();
            Statement st=con.createStatement();
            st.executeUpdate("insert into respuestas(respuestas,preguntas_idPreguntasFK) values('"+respuesta+"',(select max(idPreguntas) from preguntas))");
            con.close();
        }
        catch(Exception e) {
            e.printStackTrace();

        }
    }

    public void insertarPersonas(String nickPersona,String numeroTelf,String localidad,String password,String edad,String correo, String sexo,String enlaceFoto){
        try {
            Connection con=ConexionBD();
            Statement st=con.createStatement();
            st.executeUpdate("insert into usuarios(nickInstagram,numeroTelefono,localidad,contrasenia,edad," +
                    "correoElectronico,sexo,url_imagen_instagram) values('"+nickPersona+"',"+numeroTelf
                    +",'"+localidad+"','"+password+"','"+edad+"','"+correo+"','"+sexo+"','"+enlaceFoto+"')");
            con.close();
        }
        catch(Exception e) {
            e.printStackTrace();

        }
    }

    public void PersonaResponde(String nickPersona,String idRespuesta){
        try {
            Connection con=ConexionBD();
            Statement st=con.createStatement();
            st.executeUpdate("insert into usuarios_respuestaNM(usuarios_idUsuarios,respuestas_idRespuestas) values("+nickPersona+","+idRespuesta+")");
            con.close();
        }
        catch(Exception e) {
            e.printStackTrace();

        }
    }

    public Boolean iniciarPersona(String nickInstagram, String password){
        Connection con=ConexionBD();
        try {

            Statement st=con.createStatement();
            ResultSet s=st.executeQuery("select contrasenia from usuarios where nickInstagram='"+nickInstagram+"'");

            s.next();
            String pass=s.getString(1);

            if (password.equals(pass)){
                ResultSet resultado=st.executeQuery("select * from usuarios where nickInstagram='"+nickInstagram+"'");
                String[] extractor={resultado.getString(1),resultado.getString(2),resultado.getString(3),resultado.getString(4),
                        resultado.getString(5),resultado.getString(6),resultado.getString(7)};

                for (int x=0;x<extractor.length;x++)
                    MainActivity.infoSesion.addElement(extractor[x]);

                con.close();
                return true;
            }else{
                con.close();
                return false;
            }

        }
        catch(Exception e) {

            return false;
        }
    }

    public String extractorPregunta(String nickPersona){

        try {
            Connection con=ConexionBD();
            Statement st=con.createStatement();
            st.executeQuery("select idPreguntas from preguntas where idPreguntas not in (select preguntas_idPreguntasFK from respuestas where idRespuestas in (select respuestas_idRespuestas from usuarios_respuestaNM where usuarios_idUsuarios="+nickPersona+"))");

            con.close();
        }
        catch(Exception e) {}

        return "";
    }

    public Connection ConexionBD(){
        Connection con=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://sql2.freesqldatabase.com:3306/sql2233658", "sql2233658", "uK4*dD2%");

        } catch(Exception e) {
                e.printStackTrace();

            }
        return con;
    }
}
