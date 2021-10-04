package com.johnchaves.consultor;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DocPop extends Activity {

    TextView coddoc, codartt, cantt;
    TextView Cod_Art = MainActivity.getCod_Art();
    TextView nrodoc = MainActivity.getNrodoc();

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doclayout);

        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.6),(int)(height*.25));

        coddoc  = (TextView) findViewById(R.id.coddoc);
        codartt = (TextView) findViewById(R.id.codartt);
        cantt   = (TextView) findViewById(R.id.cantt);

        buscarDocs();
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

    public void buscarDocs(){
        try{
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_C_ConsultorApp @Modo = 'I', @CodBod = 1, @CodArt = '"+Cod_Art.getText().toString()+"', " +
                    "@NumTR = '"+nrodoc.getText().toString()+"' ");

            if(rs.next()){
                coddoc.setText(rs.getString(1));
                codartt.setText(rs.getString(2));
                cantt.setText(rs.getString(3));

            }
            else{
                Toast.makeText(getApplicationContext(),"NO SE ENCUENTRA PRODUCTO EN TR ITEMIZADO PENDIENTE",Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
}
