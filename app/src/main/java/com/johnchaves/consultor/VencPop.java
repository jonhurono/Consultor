package com.johnchaves.consultor;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VencPop extends Activity {

    TextView fv_codart;
    Button volver;
    private ArrayList<VencimientoItem> vencimientoItemArrayList;
    private MyVencimientoAdapter myVencimientoAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView Cod_Art = MainActivity.getCod_Art();

    @Override
    protected void onCreate (Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.vencimientoslayout);

        fv_codart       = findViewById(R.id.fv_codart);
        volver          = findViewById(R.id.btnok);
        recyclerView    = findViewById(R.id.recycler);

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        vencimientoItemArrayList = new ArrayList<>();
        fv_codart.setText("código: "+Cod_Art.getText());

        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.85),(int)(height*.45));

        GetVencimiento();

        volver.setOnClickListener(v -> finish());
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
    public void GetVencimiento(){

        try {
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("exec sp_par_c_infSaldos  0,20,"+Cod_Art.getText().toString()+",'D2'");

            if (rs != null) // if resultset not null, I add items to itemArraylist using class created
            {
                while (rs.next()) {
                    try {
                        vencimientoItemArrayList.add(new VencimientoItem (
                                rs.getString(1),
                                rs.getString(2),
                                rs.getString(4),
                                rs.getString(5),
                                rs.getString(7),
                                rs.getString(8)));
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "SIN REGISTROS", Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "NO SE ENCONTRARON RESULTADOS", Toast.LENGTH_SHORT).show();
            }
            myVencimientoAdapter = new MyVencimientoAdapter(vencimientoItemArrayList, VencPop.this);
            recyclerView.setAdapter(myVencimientoAdapter);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public class MyVencimientoAdapter extends RecyclerView.Adapter<MyVencimientoAdapter.ViewHolder> {
        private List<VencimientoItem> values;
        public Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView fv_tipodoc, fv_numvisa, fv_numitem, fv_saldo, fv_fechaven, fv_bodega;
            public View layout;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                fv_tipodoc = v.findViewById(R.id.fv_tipodoc);
                fv_numvisa = v.findViewById(R.id.fv_numvisa);
                fv_numitem = v.findViewById(R.id.fv_numitem);
                fv_saldo    = v.findViewById(R.id.fv_saldo);
                fv_fechaven = v.findViewById(R.id.fv_fechaven);
                fv_bodega   = v.findViewById(R.id.fv_bodega);
            }
        }

        // Constructor
        public MyVencimientoAdapter(List<VencimientoItem> myDataset, Context context) {
            values = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager) and inflates
        @Override
        public MyVencimientoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.fechasvenclayout, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Binding items to the view
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            final VencimientoItem classListItems = values.get(position);
            holder.fv_tipodoc.setText(classListItems.getFv_tipodoc());
            holder.fv_numvisa.setText(classListItems.getFv_numvisa());
            holder.fv_numitem.setText(classListItems.getFv_numitem());
            holder.fv_saldo.setText(classListItems.getFv_saldo());
            holder.fv_fechaven.setText(classListItems.getFv_fechaven());
            holder.fv_bodega.setText(classListItems.getFv_bodega());
        }

        // get item count returns the list item count
        @Override
        public int getItemCount() {
            return values.size();
        }
    }
}

