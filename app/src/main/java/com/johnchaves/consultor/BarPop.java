package com.johnchaves.consultor;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BarPop extends Activity {

    TextView barasoc1, barasoc2, barasoc3, cod1, mainbarra;
    TextView Cod_Art = MainActivity.getCod_Art();
    TextView Cod_Bar = MainActivity.getCod_Barra();
    Button   back;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barlayout);


        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.6),(int)(height*.25));

        back        = (Button) findViewById(R.id.btnSalirBar);
        cod1        = (TextView) findViewById(R.id.cod1);
        mainbarra   = (TextView) findViewById(R.id.mainbarra);
        barasoc1    = (TextView) findViewById(R.id.barasoc1);
        barasoc2    = (TextView) findViewById(R.id.barasoc2);
        barasoc3    = (TextView) findViewById(R.id.barasoc3);

        buscarBars();

        cod1.setText(Cod_Art.getText());
        mainbarra.setText(Cod_Bar.getText());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public Connection conexionDB(){
        Connection conexion=null;
        try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion= DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.11;databaseName=Terra;user=Movil;password=Mv2021;");

        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"SIN CONEXIÓN A BASE DE DATOS",Toast.LENGTH_SHORT).show();
        }
        return conexion;
    }

    public void buscarBars(){
        try{
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_C_ConsultorApp @Modo = 'E', @CodBod = 1, @CodArt = '"+Cod_Art.getText().toString()+"' ");

            if(rs.next()){
                barasoc1.setText(rs.getString(3));
                barasoc2.setText(rs.getString(4));
                barasoc3.setText(rs.getString(5));
            }
            else{
                Toast.makeText(getApplicationContext(),"ERROR AL CONSULTAR FECHAS",Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

}
