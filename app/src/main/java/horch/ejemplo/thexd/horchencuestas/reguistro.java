package horch.ejemplo.thexd.horchencuestas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;



import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import java.util.regex.Pattern;

public class reguistro extends AppCompatActivity {

    final Handler comunicadorConUI = new Handler();

    //Registro
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


    //Visualizacion
    ScrollView inicio;
    ScrollView registro;
    RelativeLayout relativo;

    //Inicio
    EditText txtIniCorreo;
    EditText txtIniPass;


   static Boolean exiteElUsuario=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //getSupportActionBar().hide();
        inicializador();
        String[] arrayGeneros = {"Mujer","Hombre","Genero Fluido","Agender","Otros"};
        spiGenero.setAdapter(new ArrayAdapter<String>(this, android.R.layout.preference_category, arrayGeneros));

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
        inicio=(ScrollView) findViewById(R.id.scrollInicio);
        registro=(ScrollView) findViewById(R.id.scrollRegistro);
        relativo=(RelativeLayout) findViewById(R.id.relativoCuenta);

        //inicio---------------------------
        txtIniCorreo=(EditText) findViewById(R.id.txtInicioNick);
        txtIniPass=(EditText) findViewById(R.id.txtInicioPass);
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

        Toast.makeText(this,btnComprobarNick.getText().toString(), Toast.LENGTH_LONG).show();

        if (btnComprobarNick.getText().toString().contains("Correcto")) {
            String nombre=txtNick.getText().toString();
            String numero=txtNumber.getText().toString();
            String correo=txtCorreo.getText().toString();
            String localidad=txtLocalidad.getText().toString();
            String contrasenia=txtPassword.getText().toString();
            String edad=txtEdad.getText().toString();
            String genero;


            int x=0;

            if (btnComprobarNick.getText().toString().contains("Correcto")) {
                x++;
            }
            if (numero.length() == 9) {
                x++;

            }
            if (!validarEmail(correo)) {
                txtCorreo.setError("Email no válido");
            } else {
                x++;

            }
            if (localidad.length() > 2) {
                x++;

            }
            if (!(txtEdad.getText().toString().equals(""))) {
                x++;

            }
            if (!(txtPassword.getText().toString().equals(""))) {
                x++;

            }
            genero=spiGenero.getSelectedItem().toString();


            if (x == 6) {

                executor("usu", "i", nombre + "-" + numero + "-" + localidad + "-" + contrasenia + "-" + edad + "-" + correo + "-" + genero + "-" + enlace);

                //Accion despues del registro

                executor("usu","e",nombre+"-"+contrasenia);
                Toast.makeText(this,"Iniciando...", Toast.LENGTH_LONG).show();

                onBackPressed();
            } else {
                Toast.makeText(this,"No esta completo todo el formulario", Toast.LENGTH_LONG).show(); }
        }

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

    public void inicio(View v){
        relativo.setVisibility(View.GONE);
        inicio.setVisibility(View.VISIBLE);
    }
    public void resgistro(View v){
        relativo.setVisibility(View.GONE);
        registro.setVisibility(View.VISIBLE);
    }

    public void iniciarSesion(View v){
           try {

                executor("usu","e",txtIniCorreo.getText().toString()+"-"+txtIniPass.getText().toString());
                Toast.makeText(this,"Iniciando...", Toast.LENGTH_LONG).show();
                onBackPressed();
            }catch (Exception e){

            }
    }


    //------------------------pruebas------------------------------------

    String[] extractor=null;



    @SuppressLint("StaticFieldLeak")
    public void executor(String lugar, String modo, String datos){
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
                if (strings[0].equals("COMPRO")){
                    examinador(strings[1]);
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

    public void examinador(String nick){
        Connection con=ConexionBD();
        try {
            Statement st=con.createStatement();
            ResultSet s=st.executeQuery("select * from usuarios where nickInstagram='" + nick + "'");
            s.next();



            con.close();
            comunicadorConUI.post(new Runnable() {
                @Override
                public void run() {
                    exiteElUsuario=true;
                }
            });

        }catch (Exception e){
            comunicadorConUI.post(new Runnable() {
                @Override
                public void run() {
                    exiteElUsuario=false;
                }
            });

        }
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
                resultado.first();
                String[] extrar={resultado.getString(1),resultado.getString(2),resultado.getString(3),resultado.getString(4),
                        resultado.getString(5),resultado.getString(6),resultado.getString(7)};
                extractor=extrar;
                con.close();
                guardar();
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
            st.executeQuery("select idPreguntas from preguntas where idPreguntas not in (select preguntas_idPreguntasFK from respuestas where idRespuestas in (select respuestas_idRespuestas from usuarios_respuestaNM where usuarios_idUsuarios="+nickPersona+")) limit 1");

            con.close();
        }
        catch(Exception e) {}

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
         File fichero = new File("sesion.txt");
        fichero.delete();
        try {
            OutputStreamWriter fout= new OutputStreamWriter(openFileOutput("sesion.txt", Context.MODE_PRIVATE));
            for (int x=0;x<extractor.length;x++){
                fout.write(extractor[x]+"-");

            }
            fout.close();
        }
        catch (Exception ex)   {  }
    }

}