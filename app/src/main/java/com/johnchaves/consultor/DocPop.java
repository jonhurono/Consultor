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

public class DocPop extends Activity {

    TextView coddoc, codartt, cantt;
    TextView Cod_Art = MainActivity.getCod_Art();
    TextView nrodoc = MainActivity.getLblnrodoc();
    Button   back;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doclayout);

        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.6),(int)(height*.25));

        back    = findViewById(R.id.btnSalirDoc);
        coddoc  = findViewById(R.id.coddoc);
        codartt = findViewById(R.id.codartt);
        cantt   = findViewById(R.id.cantt);

        buscarDocs();

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
