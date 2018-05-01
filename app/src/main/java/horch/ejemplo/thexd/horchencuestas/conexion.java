package horch.ejemplo.thexd.horchencuestas;

import android.support.v7.app.AppCompatActivity;

public class conexion extends AppCompatActivity {

Boolean x;
/*

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
                String[] extractor={resultado.getString(1),resultado.getString(2),resultado.getString(3),resultado.getString(4),
                        resultado.getString(5),resultado.getString(6),resultado.getString(7)};
                Log.e("IniciarPersonas()","si es igual el :"+password+"("+password.length()+") con la mierda de "+pass+"("+pass.length()+")");

                for (int x=0;x<extractor.length;x++)
                //    sesion[x]=resultado.getString(x+1);

                con.close();
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
            con= DriverManager.getConnection("jdbc:mysql://sql2.freesqldatabase.com:3306/sql2233658", "sql2233658", "uK4*dD2%");

        } catch(Exception e) {
            e.printStackTrace();

        }
        return con;
    }
*/
}
