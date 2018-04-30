package com.example.thexd.horchencuestas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

public class MainActivity extends conexion  implements NavigationView.OnNavigationItemSelectedListener {

    static Vector<String> infoSesion=new Vector<String>();

    TextView pregunta;

    Button btnrespuesta1;
    Button btnrespuesta2;
    Button btnrespuesta3;
    Button btnrespuesta4;

    Button btnHacerVotacion;
    Button btnSiguiente;

    final Handler comunicadorConUI = new Handler();
    final Handler comunicadorConUIRespuesta = new Handler();

    String idRespuesta1="";
    String idRespuesta2="";
    String idRespuesta3="";
    String idRespuesta4="";

    String idelegido="";
    String idPregunta="";
    String usuario="";

    BackgroundColorSpan clrSeleccion =new BackgroundColorSpan(Color.RED);
    BackgroundColorSpan clrEstandar =new BackgroundColorSpan(Color.WHITE);

    TextView txtRes1;
    TextView txtRes2;
    TextView txtRes3;
    TextView txtRes4;

    ProgressBar prbprogress1;
    ProgressBar prbprogress2;
    ProgressBar prbprogress3;
    ProgressBar prbprogress4;


    static int votosTotales=0;
    static int votos1=0;
    static int votos2=0;
    static int votos3=0;
    static int votos4=0;



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

        inicializador();
    }
    public void inicializador(){
        pregunta=(TextView) findViewById(R.id.txtrPregunta);

        btnrespuesta1=(Button) findViewById(R.id.btnRepsuesta1);
        btnrespuesta2=(Button) findViewById(R.id.btnrespuesta2);
        btnrespuesta3=(Button) findViewById(R.id.btnRespuesta3);
        btnrespuesta4=(Button) findViewById(R.id.btnRespuesta4);

        btnrespuesta1.setBackgroundColor(clrEstandar.getBackgroundColor());
        btnrespuesta1.setTransitionName("res1");
        btnrespuesta2.setBackgroundColor(clrEstandar.getBackgroundColor());
        btnrespuesta2.setTransitionName("res2");
        btnrespuesta3.setBackgroundColor(clrEstandar.getBackgroundColor());
        btnrespuesta3.setTransitionName("res3");
        btnrespuesta4.setBackgroundColor(clrEstandar.getBackgroundColor());
        btnrespuesta4.setTransitionName("res4");


        btnHacerVotacion=(Button) findViewById(R.id.btnHacerVotacion);
        btnSiguiente=(Button) findViewById(R.id.btnSiguientesEncu);

        //Resultado

        txtRes1=(TextView) findViewById(R.id.txtRelUno);
        txtRes2=(TextView) findViewById(R.id.txtRelUno2);
        txtRes3=(TextView) findViewById(R.id.txtRelUno3);
        txtRes4=(TextView) findViewById(R.id.txtRelUno4);


        prbprogress1=(ProgressBar) findViewById(R.id.progreRelUno);
        prbprogress2=(ProgressBar) findViewById(R.id.progreRelUno2);
        prbprogress3=(ProgressBar) findViewById(R.id.progreRelUno3);
        prbprogress4=(ProgressBar) findViewById(R.id.progreRelUno4);


    }

    @Override
    public void onStart(){
        super.onStart();
        Boolean existeArchivo=false;
        String textazo="";

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try  {
            BufferedReader fin =new BufferedReader(new InputStreamReader(openFileInput("sesion.txt")));
            textazo = fin.readLine();

            fin.close();
            existeArchivo=true;
        }
        catch (Exception ex)  {
            existeArchivo=false;
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }




        if (existeArchivo){
            String[] informacion=textazo.split("-");
            Toast.makeText(this,"Inicio sesion "+informacion[0], Toast.LENGTH_LONG).show();
            usuario=informacion[0];

           executor("pre","select",informacion[0]);
        }else{
            Toast.makeText(this,"No se inicio sesion ", Toast.LENGTH_LONG).show();
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
            Intent abrirRegistro=new Intent(this, reguistro.class);
            startActivity(abrirRegistro);
        } else if (id == R.id.nav_encuesta) {
            Intent abrirRegistro=new Intent(this, CrearEncuestaActivity.class);
            startActivity(abrirRegistro);
        } else if (id == R.id.nav_respuesta) {

        } else if (id == R.id.nav_modoNochee) {

        } else if (id == R.id.nav_Compartir) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer=(DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void votador(View v){
        btnHacerVotacion.setEnabled(true);

    }

    public void hacerVotacion(View v){
        btnrespuesta1.setBackgroundColor(clrEstandar.getBackgroundColor());
        btnrespuesta2.setBackgroundColor(clrEstandar.getBackgroundColor());
        btnrespuesta3.setBackgroundColor(clrEstandar.getBackgroundColor());
        btnrespuesta4.setBackgroundColor(clrEstandar.getBackgroundColor());

        btnrespuesta1.setEnabled(false);
        btnrespuesta2.setEnabled(false);
        btnrespuesta3.setEnabled(false);
        btnrespuesta4.setEnabled(false);


        btnHacerVotacion.setEnabled(true);
        btnSiguiente.setEnabled(false);
    Button btn=(Button) v;
    btn.setBackgroundColor(clrSeleccion.getBackgroundColor());
        if (btn.getTransitionName().equals("res1"))
            idelegido=idRespuesta1;
        if (btn.getTransitionName().equals("res2"))
            idelegido=idRespuesta2;
        if (btn.getTransitionName().equals("res3"))
            idelegido=idRespuesta3;
        if (btn.getTransitionName().equals("res4"))
            idelegido=idRespuesta4;

    }

    public void votarPregunta(View v){
        btnHacerVotacion.setEnabled(false);
        btnSiguiente.setEnabled(true);

        txtRes1.setText(btnrespuesta1.getText());
        txtRes2.setText(btnrespuesta2.getText());
        txtRes3.setText(btnrespuesta3.getText());
        txtRes4.setText(btnrespuesta4.getText());


        executor("pre","insertRes",usuario+"-"+idelegido);

    }

    public void siguiente(View v){

        btnrespuesta1.setBackgroundColor(clrEstandar.getBackgroundColor());
        btnrespuesta2.setBackgroundColor(clrEstandar.getBackgroundColor());
        btnrespuesta3.setBackgroundColor(clrEstandar.getBackgroundColor());
        btnrespuesta4.setBackgroundColor(clrEstandar.getBackgroundColor());

        btnrespuesta1.setEnabled(true);
        btnrespuesta2.setEnabled(true);
        btnrespuesta3.setEnabled(true);
        btnrespuesta4.setEnabled(true);


        super.onStart();
        Boolean existeArchivo=false;
        String textazo="";

        try  {
            BufferedReader fin =new BufferedReader(new InputStreamReader(openFileInput("sesion.txt")));
            textazo = fin.readLine();

            fin.close();
            existeArchivo=true;
        }
        catch (Exception ex)  {
            existeArchivo=false;
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }



        if (existeArchivo){
            String[] informacion=textazo.split("-");
            Toast.makeText(this,"Inicio sesion "+informacion[0], Toast.LENGTH_LONG).show();
            usuario=informacion[0];
            executor("pre","select",informacion[0]);
        }else{
            Toast.makeText(this,"No se inicio sesion ", Toast.LENGTH_LONG).show();
        }
    }

    //---------------------------------------BD-------------------------------------

    String[] extractor=null;


    @SuppressLint("StaticFieldLeak")
    AsyncTask<String,String,Vector<String>> tareaDBResponde=new AsyncTask<String, String, Vector<String>>() {
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
                    iniciarPersona(informacion[0],informacion[1]);
                }
            }
            if (strings[0].equals("pre")){
                if (strings[1].equals("select")){
                    extractorPregunta(strings[2]);
                }
                if (strings[1].equals("insertRes")){
                    //responder pregunta
                    String[] informacion=strings[2].split("-");
                    PersonaResponde(informacion[0],informacion[1]);
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

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    };

    @SuppressLint("StaticFieldLeak")
    AsyncTask<String,String,Vector<String>> tareaDB=new AsyncTask<String, String, Vector<String>>() {
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
                    iniciarPersona(informacion[0],informacion[1]);
                }
            }
            if (strings[0].equals("pre")){
                if (strings[1].equals("select")){
                    extractorPregunta(strings[2]);
                }
                if (strings[1].equals("insertRes")){
                    //responder pregunta
                    String[] informacion=strings[2].split("-");
                    PersonaResponde(informacion[0],informacion[1]);
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

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    };

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
            st.executeUpdate("insert into usuarios_respuestaNM(usuarios_idUsuarios,respuestas_idRespuestas) values('"+nickPersona+"',"+idRespuesta+")");

            ResultSet rs=null;

            if (idRespuesta1.equals("")==false) {
                rs = st.executeQuery("select count(respuestas_idRespuestas) from usuarios_respuestaNM where respuestas_idRespuestas=" + idRespuesta1);
                rs.first();
                votos1 = Integer.parseInt(rs.getString(1));
            }
            if (idRespuesta2.equals("")==false) {
                rs = st.executeQuery("select count(respuestas_idRespuestas) from usuarios_respuestaNM where respuestas_idRespuestas=" + idRespuesta2);
                rs.first();
                votos2 = Integer.parseInt(rs.getString(1));
            }
            if (idRespuesta3.equals("")==false) {
                rs = st.executeQuery("select count(respuestas_idRespuestas) from usuarios_respuestaNM where respuestas_idRespuestas=" + idRespuesta3);
                rs.first();
                votos3 = Integer.parseInt(rs.getString(1));
            }
            if (idRespuesta4.equals("")==false) {
                rs = st.executeQuery("select count(respuestas_idRespuestas) from usuarios_respuestaNM where respuestas_idRespuestas=" + idRespuesta4);
                rs.first();
                votos4 = Integer.parseInt(rs.getString(1));
            }
            votosTotales=votos1+votos2+votos3+votos4;

            comunicadorConUI.post(new Runnable() {
                @Override
                public void run() {
                    prbprogress1.setMax(votosTotales);
                    prbprogress2.setMax(votosTotales);
                    prbprogress3.setMax(votosTotales);
                    prbprogress4.setMax(votosTotales);

                    prbprogress1.setProgress(votos1);
                    prbprogress2.setProgress(votos2);
                    prbprogress3.setProgress(votos3);
                    prbprogress4.setProgress(votos4);

                }
            });

            con.close();
        }catch(Exception e) {Log.e("error votos",e.getMessage());}
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
                resultado.first();
                String[] extrar={resultado.getString(1),resultado.getString(2),resultado.getString(3),resultado.getString(4),
                        resultado.getString(5),resultado.getString(6),resultado.getString(7)};
                extractor=extrar;
                Log.e("IniciarPersonas()","si es igual el :"+password+"("+password.length()+") con la mierda de "+pass+"("+pass.length()+")");
                con.close();
                guardar();
                return true;
            }else{
                con.close();
                return false;
            }

        }
        catch(Exception e) {
            Log.e("IniciarPersonas()","Nos vamos a al excepcion-"+e.getMessage());

            return false;
        }
    }

    public String extractorPregunta(String nickPersona){

        try {
            Connection con=ConexionBD();
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery("select idPreguntas from preguntas where idPreguntas not in (select preguntas_idPreguntasFK from respuestas where idRespuestas in (select respuestas_idRespuestas from usuarios_respuestaNM where usuarios_idUsuarios='"+nickPersona+"')) AND aprobadaPregunta=1 limit 1");
            rs.first();
            String id=rs.getString(1);

            rs=st.executeQuery("select preguntas from preguntas where idPreguntas="+id);
            rs.first();
            final String cuestion=rs.getString(1);


            rs=st.executeQuery("select respuestas,idRespuestas from respuestas where preguntas_idPreguntasFK="+id);
            rs.first();

            String respuesta1;String respuesta2;String respuesta3;String respuesta4;
            try{
                respuesta1 = rs.getString(1);
                idRespuesta1=rs.getString(2);
                rs.next();}catch (Exception e){respuesta1="---";}

            try{
                respuesta2 = rs.getString(1);
                idRespuesta2=rs.getString(2);
                rs.next();}catch (Exception e){respuesta2="---";}

            try{
                respuesta3 = rs.getString(1);
                idRespuesta3=rs.getString(2);
                rs.next();}catch (Exception e){respuesta3="---";}

            try{
                respuesta4 = rs.getString(1);
                idRespuesta4=rs.getString(2);
            }catch (Exception e){respuesta4="---";}

            final String respuestaDev1=respuesta1;
            final String respuestaDev2=respuesta2;
            final String respuestaDev3=respuesta3;
            final String respuestaDev4=respuesta4;



            comunicadorConUI.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            pregunta.setText(" Pregunta: "+cuestion);
                            btnrespuesta1.setText(respuestaDev1);
                            btnrespuesta2.setText(respuestaDev2);
                            btnrespuesta3.setText(respuestaDev3);
                            btnrespuesta4.setText(respuestaDev4);

                            if (respuestaDev1.equals("---"))
                                btnrespuesta1.setEnabled(false);
                            else
                                btnrespuesta1.setEnabled(true);

                            if (respuestaDev2.equals("---"))
                                btnrespuesta2.setEnabled(false);
                            else
                                btnrespuesta2.setEnabled(true);

                            if (respuestaDev3.equals("---"))
                                btnrespuesta3.setEnabled(false);
                            else
                                btnrespuesta3.setEnabled(true);

                            if (respuestaDev4.equals("---"))
                                btnrespuesta4.setEnabled(false);
                            else
                                btnrespuesta4.setEnabled(true);
                        }
                    }
            );

            btnSiguiente.setEnabled(false);
            con.close();
        }
        catch(Exception e) {

            Log.e("extraxctorpregunta()"," error:" +e.getMessage());

            if (e.getMessage().contains("empty")){
                comunicadorConUI.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                pregunta.setText("No hay preguntas sin responder ahora mismo, Podria crear una nueva encuesta y esperar su aprobación");

                                btnrespuesta1.setBackgroundColor(clrEstandar.getBackgroundColor());
                                btnrespuesta2.setBackgroundColor(clrEstandar.getBackgroundColor());
                                btnrespuesta3.setBackgroundColor(clrEstandar.getBackgroundColor());
                                btnrespuesta4.setBackgroundColor(clrEstandar.getBackgroundColor());

                                btnrespuesta1.setText("---");
                                btnrespuesta2.setText("---");
                                btnrespuesta3.setText("---");
                                btnrespuesta4.setText("---");

                                btnrespuesta1.setEnabled(false);
                                btnrespuesta2.setEnabled(false);
                                btnrespuesta3.setEnabled(false);
                                btnrespuesta4.setEnabled(false);

                            }
                        }
                );


            }

        }

        return "";
    }

    public Connection ConexionBD(){
        Connection con=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= DriverManager.getConnection("jdbc:mysql://sql2.freesqldatabase.com:3306/sql2233658", "sql2233658", "uK4*dD2%");

        } catch(Exception e) {
            e.printStackTrace();

        }
        return con;
    }



    //  OPCIONALLLLL
    public void guardar(){
        Log.e("Ficheros", "Escribiendo...");

        try {
            OutputStreamWriter fout= new OutputStreamWriter(openFileOutput("sesion.txt", Context.MODE_PRIVATE));
            for (int x=0;x<extractor.length;x++){
                fout.write(extractor[x]+"-");

            }
            fout.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");

        }
    }


    @SuppressLint("StaticFieldLeak")
    public void executor(String lugar,String modo, String datos){
        new AsyncTask<String, String, Vector<String>>() {
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
                        iniciarPersona(informacion[0],informacion[1]);
                    }
                }
                if (strings[0].equals("pre")){
                    if (strings[1].equals("select")){
                        extractorPregunta(strings[2]);
                    }
                    if (strings[1].equals("insertRes")){
                        //responder pregunta
                        String[] informacion=strings[2].split("-");
                        PersonaResponde(informacion[0],informacion[1]);
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

            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

        }.execute(lugar,modo,datos);
    }

}

