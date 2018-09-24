package horch.ejemplo.thexd.horchencuestas;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Vector;

public class CrearEncuestaActivity extends AppCompatActivity {

    EditText pregunta;
    EditText res1;
    EditText res2;
    EditText res3;
    EditText res4;


    String[] textazo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_encuesta);
        //getSupportActionBar().hide();

        pregunta=(EditText) findViewById(R.id.insertPre);
        res1=(EditText) findViewById(R.id.insertRes1);
        res2=(EditText) findViewById(R.id.insertRes2);
        res3=(EditText) findViewById(R.id.insertRes3);
        res4=(EditText) findViewById(R.id.inserRes4);

    }


    public void click(View v){
        Boolean inicioSesion=false;

        try  {
            BufferedReader fin =new BufferedReader(new InputStreamReader(openFileInput("sesion.txt")));
            textazo = fin.readLine().split("-");
            fin.close();
            inicioSesion=true;
        }
        catch (Exception ex)  { }
        if(inicioSesion){

            String pre=pregunta.getText().toString();

            String R1=res1.getText().toString();
            String R2=res2.getText().toString();
            String R3=res3.getText().toString();
            String R4=res4.getText().toString();

            String[] arra={R1,R2,R3,R4};
            String total="";
            for (int x=0;x<4;x++){
                if (!(arra[x].equals(""))){
                    total+=arra[x]+"-";
                }
            }

            executor("pre","crear",pre+"/"+total.substring(0,total.length()-1));
        }else{
            Toast.makeText(this,"No puede registrar una pregunta sin tener cuenta ", Toast.LENGTH_LONG).show();

        }

        onBackPressed();
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

                String[] array=strings[2].split("/");

                insertarPregunta(array[0],array[1]);


                Vector<String> a=new Vector<String>();

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


    public void insertarPregunta(String preguntas,String respuestas){
        try {
            Connection con=ConexionBD();
            Statement st=con.createStatement();
            st.executeUpdate("insert into preguntas(preguntas,aprobadaPregunta,autor) values('"+preguntas+"',false,'"+textazo[0]+"')");
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





    public Connection ConexionBD(){
        Connection con=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= DriverManager.getConnection("jdbc:mysql://sql2.freesqldatabase.com:3306/sql2233658", "sql2233658", "null");

        } catch(Exception e) {
            e.printStackTrace();

        }
        return con;
    }


}
