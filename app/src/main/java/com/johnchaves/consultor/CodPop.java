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

public class CodPop extends Activity {

    TextView cod1, cod2, tipocod, despad;
    TextView Cod_Art = MainActivity.getCod_Art();
    Button   back;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.codlayout);

        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.75),(int)(height*.25));

        back    = findViewById(R.id.btnSalirCod);
        cod1    = findViewById(R.id.cod1);
        cod2    = findViewById(R.id.cod2);
        tipocod = findViewById(R.id.tipocod);
        despad  = findViewById(R.id.des_pad);

        buscarCods();

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

    public void buscarCods(){
        try{
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_C_ConsultorApp @Modo = 'A', @CodBod = 1, @CodArt = '"+Cod_Art.getText().toString()+"' ");

            if(rs.next()){
                cod1.setText(rs.getString(1));
                tipocod.setText(rs.getString(2));
                cod2.setText(rs.getString(3));
                despad.setText(rs.getString(4));
            }
            else{
                Toast.makeText(getApplicationContext(),"ERROR AL CONSULTAR FECHAS",Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
}
