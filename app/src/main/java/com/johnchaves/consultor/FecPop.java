package com.johnchaves.consultor;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class FecPop extends Activity {

    TextView qui_sto, cri_sto, ult_com, ult_ven, fec_ven;
    TextView Cod_Art = MainActivity.getCod_Art();
    Button back;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feclayout);

        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.7),(int)(height*.35));

        back    = findViewById(R.id.btnSalirFec);
        qui_sto = findViewById(R.id.qui_sto);
        cri_sto = findViewById(R.id.cri_sto);
        ult_com = findViewById(R.id.ult_com);
        ult_ven = findViewById(R.id.ult_ven);
        fec_ven = findViewById(R.id.fec_ven);

        buscarFechas();

        back.setOnClickListener(v -> finish());
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

    public void buscarFechas(){
        try{
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_C_ConsultorApp @Modo = 'D', @CodBod = 1, @CodArt = '"+Cod_Art.getText().toString()+"' ");

            if(rs.next()){
                qui_sto.setText(rs.getString(2));
                cri_sto.setText(rs.getString(3));
                ult_com.setText(rs.getString(4));
                ult_ven.setText(rs.getString(5));
                fec_ven.setText(rs.getString(6)+", Saldo:"+rs.getString(7));
            }
            else{
                Toast.makeText(getApplicationContext(),"ERROR AL CONSULTAR FECHAS",Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
}
