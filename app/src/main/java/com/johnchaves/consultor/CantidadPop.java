package com.johnchaves.consultor;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CantidadPop extends Activity implements OnItemSelectedListener{

    EditText Cantidad;
    EditText Stock_Des;
    EditText Peso_Drenado;
    EditText Cod_Gondola;
    TextView Carax;
    Spinner Cod_Cara;
    String [] caras = {"Sin cara","Norte","Sur","Este","Oeste"};
    Button insertinto,btnback;
    TextView Cod_Art = MainActivity.getCod_Art();
    TextView Cod_Barra = MainActivity.getCod_Barra();
    TextView Sto_Art1 = MainActivity.getSto_Art1();
    TextView MAC = MainActivity.getMAC();
    TextView FETCHA = MainActivity.getFETCHA();
    TextView IP = MainActivity.getIP();
    TextView hostname = MainActivity.getHostname();
    TextView ubiUC = MainActivity.getUbiUC();
    TextView ubiUG = MainActivity.getUbiUG();
    TextView Des_Art = MainActivity.getDes_Art();
    TextView modox = MainActivity.getModox();
    TextView bodx = MainActivity.getBodx();
    TextView tipodoc = MainActivity.getTipodoc();
    TextView nrodoc = MainActivity.getLblnrodoc();
    TextView nroitem = MainActivity.getLblnroitem();
    EditText FecVen1, FecVen2, FecVen3;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cantidadwindow);

        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.85),(int)(height*.50));

        Cantidad        =   findViewById(R.id.Cantidad);
        Stock_Des       =   findViewById(R.id.Stock_Deseado);
        Peso_Drenado    =   findViewById(R.id.Peso_Drenado);
        Cod_Gondola     =   findViewById(R.id.Cod_Gondola);
        Carax           =   findViewById(R.id.carax);
        FecVen1         =   findViewById(R.id.FecVen1);
        FecVen2         =   findViewById(R.id.FecVen2);
        FecVen3         =   findViewById(R.id.FecVen3);
        insertinto      =   findViewById(R.id.insertinto);
        btnback         =   findViewById(R.id.back);
        Cod_Cara        =   findViewById(R.id.Cod_Cara);
        Cantidad.requestFocus();
        Cod_Cara.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item,caras);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Cod_Cara.setAdapter(aa);

        insertinto.setOnClickListener(v -> {
           nuevaCantidad();
           finish();
        });

        btnback.setOnClickListener(v -> finish());

        //esto era para bloquear el botón de ingresar cantidad si es que el campo de cantidad estaba vacío
        /*Cantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    insertinto.setEnabled(false);
                } else {
                    insertinto.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
        });*/
    }
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String care = parent.getSelectedItem().toString();

        if(care.equals("Sin Cara")){
            Carax.setText("NULL");
        }
        else if(care.equals("Norte")){
            Carax.setText("N");
        }
        else if(care.equals("Sur")){
            Carax.setText("S");
        }
        else if(care.equals("Este")){
            Carax.setText("E");
        }
        else if(care.equals("Oeste")){
            Carax.setText("O");
        }
    }
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
    public Connection conexionDB(){

        Connection conexion = null;

        try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String driver = Util.getProperty("db.driver",getApplicationContext());
            String url = Util.getProperty("db.url",getApplicationContext());

            Class.forName(""+driver+"").newInstance();

            conexion = DriverManager.getConnection(""+url+"");

        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"SIN CONEXIÓN A BASE DE DATOS",Toast.LENGTH_SHORT).show();
        }
        return conexion;
    }

    public void nuevaCantidad(){
        try{
            Statement pst = conexionDB().createStatement();
            pst.executeUpdate("EXEC Sp_i_ConsultorApp @Modo = 'I'," +
                    "@CodBod = '" + bodx.getText().toString() + "'," +
                    "@CodArt = '" + Cod_Art.getText().toString() + "'," +
                    "@CodBar = '" + Cod_Barra.getText().toString() + "'," +
                    "@Canti = '" + Cantidad.getText().toString() + "'," +
                    "@Fecha = '" + FETCHA.getText().toString() + "'," +
                    "@StockME = '" + Sto_Art1.getText().toString() + "'," +
                    "@IP = '" + IP.getText().toString() + "'," +
                    "@MAC = '" + MAC.getText().toString() + "'," +
                    "@HostName = '" + hostname.getText().toString() + "'," +
                    "@IDUG = '" + ubiUG.getText().toString() + "'," +
                    "@IDUC = '" + ubiUC.getText().toString() + "'," +
                    "@DesArt = '" + Des_Art.getText().toString() + "'," +
                    "@StockDes = '" + Stock_Des.getText().toString() + "'," +
                    "@PesoDre = '" + Peso_Drenado.getText().toString() + "'," +
                    "@CodGond = '" + Cod_Gondola.getText().toString() + "'," +
                    "@CodCara = '" + Carax.getText().toString() + "'," +
                    "@FecVenc1 = '" + FecVen1.getText().toString() + "'," +
                    "@FecVenc2 = '" + FecVen2.getText().toString() + "'," +
                    "@FecVenc3 = '" + FecVen3.getText().toString() + "'," +
                    "@Modox = '" + modox.getText().toString() + "'," +
                    "@TipoDoc = '" + tipodoc.getText().toString() + "'," +
                    "@NroDoc = '" + nrodoc.getText().toString() + "'," +
                    "@NroItem = '" + nroitem.getText().toString() + "' ");

            Toast.makeText(getApplicationContext(),"NUEVA INFORMACIÓN REGISTRADA CORRECTAMENTE",Toast.LENGTH_SHORT).show();

        }catch (SQLException e){
            Toast.makeText(getApplicationContext(),"ERROR EN INSERCIÓN: REVISAR LOS DATOS",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}

