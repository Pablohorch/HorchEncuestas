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
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        new ConexionBD().execute("I","Pre","Pablo.-Pablo.");


        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer=(DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView=(NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @SuppressWarnings("StatementWithEmptyBody")
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


}

class ConexionBD extends AsyncTask<String,String,Vector<String>>{

    public ConexionBD() {
        super();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Vector<String> doInBackground(String... strings) {
        String[] x=strings[2].split("-");
        Boolean inicio=iniciarPersona(x[0], x[1]);

        return new Vector<String>();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Vector<String> strings) {

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
            ResultSet s=st.executeQuery("select contrasenia from usuarios where nickPersona='"+nickInstagram+"'");

            String pass=s.getString(0);

            if (password.equals(pass)){
                con.close();
                return true;
            }else{
                con.close();
                return false;
            }

        }
        catch(Exception e) {
            try {
                con.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
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
