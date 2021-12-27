package com.johnchaves.consultor;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Region;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String []   modos = {"Cuenta/Inventario","Recepción","Despacho","Merma","Fleje"};
    String []   bodegas = {"Bod 1 - Supermercado","Bod 2 - Sala de Proceso",
            "Bod 4 - Intermedia","Bod 5 - Verduras", "Bod 6 - Cecinas", "Bod 10 - De Transito", "Bod 20 - Particular"};
    String []   tipodocs = {"--TIPO DOC--","ZETA","REEXPEDICIÓN","T.U","DESPACHO"};
    EditText    CodProd, CodProd2;
    Button      Buscar, detdoc, asoccod, asocbar, asocfech, Buscar2;
    Spinner     Bod;
    FloatingActionButton Inventariar, Foto;
    private static Spinner Modo;
    private static Spinner TipoDoc;
    private static TextView Cod_Art;
    private static TextView Cod_Barra;
    private static TextView Sto_Art1;
    private static TextView MAC;
    private static TextView FETCHA;
    private static TextView IP;
    private static TextView hostname;
    private static TextView ubiUG;
    private static TextView ubiUC;
    private static TextView Des_Art;
    private static TextView modox;
    private static TextView bodx;
    private static TextView tipodoc;
    private static TextView nrodoc;
    private static TextView nroitem;
    TableRow        UbicJuanito,CodUbic,StockBod1, StockCrit, StockDese, StockBod20, CodigUbic, rowRecepcion, butonera, rowPrecioDrenado, rowPrecioxUM;
    TextView        Ovejita,Pre_Ven, Ubicacion, UniStock, Sto_Cri, Sto_Des, Sto_Art20, Pre_Oferta_Pesos, Cod_Ubicacion, Cap_Caja, Unidad,Precio_UnidadMedida, PrecioDrenado;
    LinearLayout    Botones;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Modo                = (Spinner)  findViewById(R.id.Modo);
        Bod                 = (Spinner)  findViewById(R.id.bod);
        TipoDoc             = (Spinner)  findViewById(R.id.TipoDoc);
        Buscar              = (Button)   findViewById(R.id.butbuscar);
        Buscar2             = (Button)   findViewById(R.id.butbuscar2);
        asoccod             = (Button)   findViewById(R.id.butcods);
        asocbar             = (Button)   findViewById(R.id.butbarras);
        asocfech            = (Button)   findViewById(R.id.butfechas);
        detdoc              = (Button)   findViewById(R.id.detdoc);
        CodProd             = (EditText) findViewById(R.id.inputCodProd);
        CodProd2            = (EditText) findViewById(R.id.inputCodProd2);
        Ovejita             = (TextView) findViewById(R.id.ovejita);
        Des_Art             = (TextView) findViewById(R.id.Des_Art);
        Cod_Art             = (TextView) findViewById(R.id.Cod_Art);
        Cod_Barra           = (TextView) findViewById(R.id.Cod_Barra);
        Pre_Ven             = (TextView) findViewById(R.id.Pre_Ven);
        Ubicacion           = (TextView) findViewById(R.id.Ubicacion);
        UniStock            = (TextView) findViewById(R.id.unidad);
        Sto_Art1            = (TextView) findViewById(R.id.Sto_Art1);
        Sto_Cri             = (TextView) findViewById(R.id.Sto_Cri);
        Sto_Des             = (TextView) findViewById(R.id.Sto_Des);
        Sto_Art20           = (TextView) findViewById(R.id.Sto_Art20);
        Pre_Oferta_Pesos    = (TextView) findViewById(R.id.Pre_Oferta_Pesos);
        Cod_Ubicacion       = (TextView) findViewById(R.id.Cod_Ubicacion);
        MAC                 = (TextView) findViewById(R.id.MAC);
        FETCHA              = (TextView) findViewById(R.id.FETCHA);
        IP                  = (TextView) findViewById(R.id.IP);
        hostname            = (TextView) findViewById(R.id.hostname);
        ubiUG               = (TextView) findViewById(R.id.ubiUG);
        ubiUC               = (TextView) findViewById(R.id.ubiUC);
        Cap_Caja            = (TextView) findViewById(R.id.CapCaja);
        modox               = (TextView) findViewById(R.id.modox);
        bodx                = (TextView) findViewById(R.id.bodx);
        UbicJuanito         = (TableRow) findViewById(R.id.UbicJuanito);
        CodUbic             = (TableRow) findViewById(R.id.CodUbic);
        StockBod1           = (TableRow) findViewById(R.id.StockBod1);
        StockCrit           = (TableRow) findViewById(R.id.StockCrit);
        StockDese           = (TableRow) findViewById(R.id.StockDese);
        StockBod20          = (TableRow) findViewById(R.id.StockBod20);
        CodigUbic           = (TableRow) findViewById(R.id.CodigUbic);
        rowRecepcion        = (TableRow) findViewById(R.id.rowRecepcion);
        butonera            = (TableRow) findViewById(R.id.butonera);
        rowPrecioDrenado    = (TableRow) findViewById(R.id.rowPrecioDrenado);
        rowPrecioxUM        = (TableRow) findViewById(R.id.rowPrecioxUM);
        tipodoc             = (TextView) findViewById(R.id.tipodoc);
        nrodoc              = (TextView) findViewById(R.id.nrodoc);
        nroitem             = (TextView) findViewById(R.id.nroitem);
        Unidad              = (TextView) findViewById(R.id.UnidadMedida);
        Precio_UnidadMedida = (TextView) findViewById(R.id.Precio_UnidadMedida);
        PrecioDrenado       = (TextView) findViewById(R.id.Precio_Drenado);
        Botones             = (LinearLayout) findViewById(R.id.Botones);
        Inventariar         = (FloatingActionButton) findViewById(R.id.Inventariar);
        Foto                = (FloatingActionButton) findViewById(R.id.Foto);
        final KonfettiView konfettiView = findViewById(R.id.konfettiView);

        CodProd.requestFocus();

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            int REQUEST_ENABLE_BT = 0;
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        String bluti = bluetoothAdapter.getName();
        hostname.setText(bluti);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        IP.setText(ipAddress);

        /* Solo funciona en API < 29
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String macAddress = info.getMacAddress();
        MAC.setText(macAddress);
        */

        //Solo utilizar en Android OS 10 (API 29) o menor

/*
        try {
            List<NetworkInterface> networkInterfaceList = Collections.list(NetworkInterface.getNetworkInterfaces());

            String stringMac = "";

            for (NetworkInterface networkInterface : networkInterfaceList) {
                if (networkInterface.getName().equalsIgnoreCase("wlan0")) {
                    for (int i = 0; i < networkInterface.getHardwareAddress().length; i++) {
                        String stringMacByte = Integer.toHexString(networkInterface.getHardwareAddress()[i] & 0xFF);

                        if (stringMacByte.length() == 1) {
                            stringMacByte = "0" + stringMacByte;
                        }
                        stringMac = stringMac + stringMacByte.toUpperCase() + ":";
                    }
                    break;
                }
                else {

                }
            }
            MAC.setText(stringMac.substring(0, stringMac.length() - 1));

        } catch (SocketException e) {
            e.printStackTrace();
        }
*/
        //aquí se acaba el try catch de MAC

        //Si la API es versión 30 (Android OS 11) o mayor, entonces enviar la MAC en duro

        //Relación hostname/MAC
        //region

        //01 - FCresp - Samsung Tab A10
        if (hostname.getText().toString().equals("CC_Tablet01")) {
            MAC.setText("80:86:D9:28:E5:54");
        }
        //02 - JSegovia - Samsung Tab A10
        else if(hostname.getText().toString().equals("CC_Tablet02")){
            MAC.setText("F8:F1:E6:12:47:D7");
        }
        //03 - Valentina - Samsung Tab A10
        else if(hostname.getText().toString().equals("CC_Tablet03")){
            MAC.setText("F8:F1:E6:1F:8D:93");
        }
        //04 - Alfredo - Alcatel 1T7
        else if(hostname.getText().toString().equals("CC_Tablet04")){
            MAC.setText("64:09:AC:D1:27:1C");
        }
        //05 - Christell - Alcatel 3T8
        else if(hostname.getText().toString().equals("CC_Tablet05")){
            MAC.setText("64:09:AC:4B:84:00");
        }
        //06 - Karen A - Alcatel 3T8
        else if(hostname.getText().toString().equals("CC_Tablet06")){
            MAC.setText("64:09:AC:23:3C:A5");
        }
        //07 - Aylin F - Alcatel 3T8
        else if(hostname.getText().toString().equals("CC_Tablet07")){
            MAC.setText("64:09:AC:2F:E7:FF");
        }
        //08 - Andrés J - Alcatel 3T8
        else if(hostname.getText().toString().equals("CC_Tablet08")){
            MAC.setText("64:09:AC:32:0A:0F");
        }
        //09 - xxx - Samsung Tab A8
        else if(hostname.getText().toString().equals("CC_Tablet09")){
            MAC.setText("54:21:9D:CD:CE:64");
        }
        //10 - xxx - Samsung Tab A8
        else if(hostname.getText().toString().equals("CC_Tablet10")){
            MAC.setText("54:21:9D:CA:AA:7E");
        }
        //11 - Patricio A - Virzo funtab7
        else if(hostname.getText().toString().equals("CC_Tablet11")){
            MAC.setText("00:27:15:68:79:A9");
        }
        //12 - Bladimir M - Virzo funtab7
        else if(hostname.getText().toString().equals("CC_Tablet12")){
            MAC.setText("00:27:15:52:06:12");
        }
        //13 - Fernanda R - Virzo funtab7
        else if(hostname.getText().toString().equals("CC_Tablet13")){
            MAC.setText("00:27:15:78:71:0D");
        }
        //14 - Scarleth N - Virzo funtab7
        else if(hostname.getText().toString().equals("CC_Tablet14")){
            MAC.setText("00:27:15:11:13:96");
        }
        //15 - Alicia M - Virzo funtab7
        else if(hostname.getText().toString().equals("CC_Tablet15")){
            MAC.setText("00:27:15:23:33:EB");
        }
        //16 - xxx - Virzo funtab7
        else if(hostname.getText().toString().equals("CC_Tablet16")){
            MAC.setText("00:27:15:63:83:A1");
        }
        //17 - xxx - Virzo funtab7
        else if(hostname.getText().toString().equals("CC_Tablet17")){
            MAC.setText("00:27:15:77:40:F3");
        }
        //18 - xxx - Virzo funtab7
        else if(hostname.getText().toString().equals("CC_Tablet18")){
            MAC.setText("00:27:15:19:80:DD");
        }
        //19 - xxx - Virzo funtab7
        else if(hostname.getText().toString().equals("CC_Tablet19")){
            MAC.setText("00:27:15:39:15:32");
        }
        //20 - xxx - Virzo funtab7
        else if(hostname.getText().toString().equals("CC_Tablet20")){
            MAC.setText("00:27:15:46:28:76");
        }
        //Lenovo Avansis
        else if(hostname.getText().toString().equals("Tablet_Avansis")){
            MAC.setText("98:0C:A5:9A:FE:33");
        }
        //Samsung Jchaves-Avansis
        else if(hostname.getText().toString().equals("Samsung_John")){
            MAC.setText("80:86:D9:28:D9:92");
        }
        //Samsung Vdelic-Avansis
        else if(hostname.getText().toString().equals("Samsung_VDelic")){
            MAC.setText("80:86:D9:28:DA:3E");
        }

        //endregion

        Modo.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter bb = new ArrayAdapter(this, android.R.layout.simple_spinner_item,modos);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Modo.setAdapter(bb);

        Bod.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter cc = new ArrayAdapter(this, android.R.layout.simple_spinner_item,bodegas);
        cc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Bod.setAdapter(cc);
        //para bloquear cambio de bodega
        Bod.setEnabled(false);

        TipoDoc.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter dd = new ArrayAdapter(this, android.R.layout.simple_spinner_item,tipodocs);
        dd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TipoDoc.setAdapter(dd);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
        }

        CodProd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    Buscar.setEnabled(false);
                } else {
                    Buscar.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
        });

        CodProd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == CodProd.getImeActionId()){

                    if (CodProd.length() <= 5){
                        buscarProducto();
                    }
                    else {
                        buscarxBarra();
                    }
                    handled = true;
                }
                return handled;
            }
        });

        CodProd2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    Buscar2.setEnabled(false);
                } else {
                    Buscar2.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
        });

        CodProd2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == CodProd2.getImeActionId()) {
                    if (CodProd2.length() <= 5) {
                        buscarFleje();
                    } else {
                        buscarxBarraFleje();
                    }
                    handled = true;
                    }
                    return false;
                }

        });

        nrodoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    detdoc.setEnabled(false);
                } else {
                    detdoc.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
        });

        Buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( CodProd.length() <= 5 ) {
                    buscarProducto();
                }
                else {
                    buscarxBarra();
                }
            }
        });

        Buscar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( CodProd2.length() <= 5) {
                    buscarFleje();
                } else {
                    buscarxBarraFleje();
                }
            }
        });


        asoccod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CodPop.class));
            }
        });

        asocbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,BarPop.class));
            }
        });

        asocfech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FecPop.class));
            }
        });

        detdoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DocPop.class));
            }
        });

        Inventariar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Modo.getSelectedItem().equals("Fleje")) {
                    Toast.makeText(getApplicationContext(),"INSERCIÓN DE DATOS DESHABILITADA PARA MODO FLEJE",Toast.LENGTH_SHORT).show();
                }
                    else{
                        startActivity(new Intent(MainActivity.this, CantidadPop.class));
                        FETCHA.setText(getCurrentDateandTime());
                    }
                }
        });
        Foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FotoPop.class));
            }
        });

        Ovejita.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int counter = 0;
                counter++;
                if(counter>=1)
                    konfettiView.build()
                            .addColors(Color.LTGRAY, Color.GREEN, Color.RED)
                            .setDirection(0.0, 359.0)
                            .setSpeed(1f, 5f)
                            .setFadeOutEnabled(true)
                            .setTimeToLive(1000L)
                            .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                            .addSizes(new Size(12,5))
                            .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                            .streamFor(300, 1000L);
                    Toast.makeText(MainActivity.this, "🎅 ¡Avansis te desea Feliz Navidad! 🎁", Toast.LENGTH_SHORT).show();

                Log.i("LOG_RESPONSE", "Screen touched!");
                return false;
            }
        });

    }

    private String getCurrentDateandTime(){
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.getDefault()).format(new Date());
    }
    public static Spinner  getModo(){ return Modo; }
    public static TextView getCod_Art(){ return Cod_Art; }
    public static TextView getCod_Barra(){ return Cod_Barra; }
    public static TextView getSto_Art1(){ return Sto_Art1; }
    public static TextView getMAC(){ return MAC; }
    public static TextView getFETCHA(){ return FETCHA; }
    public static TextView getIP(){ return IP; }
    public static TextView getHostname(){ return hostname; }
    public static TextView getUbiUG(){ return ubiUG; }
    public static TextView getUbiUC(){ return ubiUC; }
    public static TextView getDes_Art(){ return Des_Art; }
    public static TextView getBodx(){ return bodx; }
    public static TextView getTipodoc() { return tipodoc; }
    public static TextView getNrodoc() { return nrodoc; }
    public static TextView getNroitem() { return nroitem; }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String mode = parent.getSelectedItem().toString();

        if(mode.equals("Cuenta/Inventario")){
            modox.setText("C");
            rowRecepcion.setVisibility(View.GONE);
            TipoDoc.setSelection(0);
            tipodoc.setText(null);
            nrodoc.setText(null);
            nroitem.setText(null);
            CodProd.setVisibility(View.VISIBLE);
            CodProd2.setVisibility(View.GONE);
            Buscar.setVisibility(View.VISIBLE);
            Buscar2.setVisibility(View.GONE);
            rowPrecioDrenado.setVisibility(View.GONE);
            rowPrecioxUM.setVisibility(View.GONE);
            UbicJuanito.setVisibility(View.VISIBLE);
            CodUbic.setVisibility(View.VISIBLE);
            StockBod1.setVisibility(View.VISIBLE);
            StockCrit.setVisibility(View.VISIBLE);
            StockDese.setVisibility(View.VISIBLE);
            StockBod20.setVisibility(View.VISIBLE);
            CodigUbic.setVisibility(View.INVISIBLE);

            Des_Art.setText(null);
            Cod_Art.setText(null);
            Cod_Barra.setText(null);
            Pre_Ven.setText(null);
            Ubicacion.setText(null);
            UniStock.setText(null);
            Sto_Art1.setText(null);
            Sto_Cri.setText(null);
            Sto_Des.setText(null);
            Sto_Art20.setText(null);
            Pre_Oferta_Pesos.setText(null);
            Cod_Ubicacion.setText(null);
            ubiUG.setText(null);
            ubiUC.setText(null);
            Cap_Caja.setText(null);
            tipodoc.setText(null);
            nrodoc.setText(null);
            nroitem.setText(null);
            Unidad.setText(null);
            Precio_UnidadMedida.setText(null);
            PrecioDrenado.setText(null);

            CodProd.requestFocus();
        }
        else if(mode.equals("--TIPO DOC--")){
            tipodoc.setText(null);
        }
        else if(mode.equals("Recepción")){
            modox.setText("R");
            rowRecepcion.setVisibility(View.VISIBLE);
            TipoDoc.setSelection(0);
            CodProd.setVisibility(View.VISIBLE);
            CodProd2.setVisibility(View.GONE);
            Buscar2.setVisibility(View.GONE);
            Buscar.setVisibility(View.VISIBLE);
            rowPrecioDrenado.setVisibility(View.GONE);
            rowPrecioxUM.setVisibility(View.GONE);
            UbicJuanito.setVisibility(View.VISIBLE);
            CodUbic.setVisibility(View.VISIBLE);
            StockBod1.setVisibility(View.VISIBLE);
            StockCrit.setVisibility(View.VISIBLE);
            StockDese.setVisibility(View.VISIBLE);
            StockBod20.setVisibility(View.VISIBLE);
            CodigUbic.setVisibility(View.INVISIBLE);

            Des_Art.setText(null);
            Cod_Art.setText(null);
            Cod_Barra.setText(null);
            Pre_Ven.setText(null);
            Ubicacion.setText(null);
            UniStock.setText(null);
            Sto_Art1.setText(null);
            Sto_Cri.setText(null);
            Sto_Des.setText(null);
            Sto_Art20.setText(null);
            Pre_Oferta_Pesos.setText(null);
            Cod_Ubicacion.setText(null);
            ubiUG.setText(null);
            ubiUC.setText(null);
            Cap_Caja.setText(null);
            tipodoc.setText(null);
            nrodoc.setText(null);
            nroitem.setText(null);
            Unidad.setText(null);
            Precio_UnidadMedida.setText(null);
            PrecioDrenado.setText(null);

            CodProd.requestFocus();
        }
        else if(mode.equals("Despacho")){
            modox.setText("D");
            rowRecepcion.setVisibility(View.GONE);
            TipoDoc.setSelection(0);
            tipodoc.setText(null);
            nrodoc.setText(null);
            nroitem.setText(null);
            CodProd.setVisibility(View.VISIBLE);
            CodProd2.setVisibility(View.GONE);
            Buscar.setVisibility(View.VISIBLE);
            Buscar2.setVisibility(View.GONE);
            rowPrecioDrenado.setVisibility(View.GONE);
            rowPrecioxUM.setVisibility(View.GONE);
            UbicJuanito.setVisibility(View.VISIBLE);
            CodUbic.setVisibility(View.VISIBLE);
            StockBod1.setVisibility(View.VISIBLE);
            StockCrit.setVisibility(View.VISIBLE);
            StockDese.setVisibility(View.VISIBLE);
            StockBod20.setVisibility(View.VISIBLE);
            CodigUbic.setVisibility(View.INVISIBLE);

            Des_Art.setText(null);
            Cod_Art.setText(null);
            Cod_Barra.setText(null);
            Pre_Ven.setText(null);
            Ubicacion.setText(null);
            UniStock.setText(null);
            Sto_Art1.setText(null);
            Sto_Cri.setText(null);
            Sto_Des.setText(null);
            Sto_Art20.setText(null);
            Pre_Oferta_Pesos.setText(null);
            Cod_Ubicacion.setText(null);
            ubiUG.setText(null);
            ubiUC.setText(null);
            Cap_Caja.setText(null);
            tipodoc.setText(null);
            nrodoc.setText(null);
            nroitem.setText(null);
            Unidad.setText(null);
            Precio_UnidadMedida.setText(null);
            PrecioDrenado.setText(null);

            CodProd.requestFocus();
        }
        else if(mode.equals("Merma")){
            modox.setText("M");
            rowRecepcion.setVisibility(View.GONE);
            TipoDoc.setSelection(0);
            tipodoc.setText(null);
            nrodoc.setText(null);
            nroitem.setText(null);
            CodProd.setVisibility(View.VISIBLE);
            CodProd2.setVisibility(View.GONE);
            Buscar.setVisibility(View.VISIBLE);
            Buscar2.setVisibility(View.GONE);
            rowPrecioDrenado.setVisibility(View.GONE);
            rowPrecioxUM.setVisibility(View.GONE);
            UbicJuanito.setVisibility(View.VISIBLE);
            CodUbic.setVisibility(View.VISIBLE);
            StockBod1.setVisibility(View.VISIBLE);
            StockCrit.setVisibility(View.VISIBLE);
            StockDese.setVisibility(View.VISIBLE);
            StockBod20.setVisibility(View.VISIBLE);
            CodigUbic.setVisibility(View.INVISIBLE);

            Des_Art.setText(null);
            Cod_Art.setText(null);
            Cod_Barra.setText(null);
            Pre_Ven.setText(null);
            Ubicacion.setText(null);
            UniStock.setText(null);
            Sto_Art1.setText(null);
            Sto_Cri.setText(null);
            Sto_Des.setText(null);
            Sto_Art20.setText(null);
            Pre_Oferta_Pesos.setText(null);
            Cod_Ubicacion.setText(null);
            ubiUG.setText(null);
            ubiUC.setText(null);
            Cap_Caja.setText(null);
            tipodoc.setText(null);
            nrodoc.setText(null);
            nroitem.setText(null);
            Unidad.setText(null);
            Precio_UnidadMedida.setText(null);
            PrecioDrenado.setText(null);

            CodProd.requestFocus();
        }
        else if(mode.equals("Fleje")){
            modox.setText(null);
            rowRecepcion.setVisibility(View.GONE);
            TipoDoc.setSelection(0);
            tipodoc.setText(null);
            nrodoc.setText(null);
            nroitem.setText(null);
            CodProd.setVisibility(View.GONE);
            CodProd2.setVisibility(View.VISIBLE);
            Buscar.setVisibility(View.GONE);
            Buscar2.setVisibility(View.VISIBLE);
            rowPrecioDrenado.setVisibility(View.VISIBLE);
            rowPrecioxUM.setVisibility(View.VISIBLE);
            UbicJuanito.setVisibility(View.GONE);
            CodUbic.setVisibility(View.GONE);
            StockBod1.setVisibility(View.GONE);
            StockCrit.setVisibility(View.GONE);
            StockDese.setVisibility(View.GONE);
            StockBod20.setVisibility(View.GONE);
            CodigUbic.setVisibility(View.GONE);

            Des_Art.setText(null);
            Cod_Art.setText(null);
            Cod_Barra.setText(null);
            Pre_Ven.setText(null);
            Ubicacion.setText(null);
            UniStock.setText(null);
            Sto_Art1.setText(null);
            Sto_Cri.setText(null);
            Sto_Des.setText(null);
            Sto_Art20.setText(null);
            Pre_Oferta_Pesos.setText(null);
            Cod_Ubicacion.setText(null);
            ubiUG.setText(null);
            ubiUC.setText(null);
            Cap_Caja.setText(null);
            tipodoc.setText(null);
            nrodoc.setText(null);
            nroitem.setText(null);
            Unidad.setText(null);
            Precio_UnidadMedida.setText(null);
            PrecioDrenado.setText(null);

            CodProd2.requestFocus();
        }
        /*
        else if(mode.equals("John")){
            modox.setText("J");
            rowRepecion.setVisibility(View.GONE);
            TipoDoc.setSelection(0);
            tipodoc.setText(null);
            nrodoc.setText(null);
            nroitem.setText(null);
        }
        */
        else if (mode.equals("Bod 1 - Supermercado")){
            bodx.setText("1");
        }
        else if (mode.equals("Bod 2 - Sala de Proceso")){
            bodx.setText("2");
        }
        else if (mode.equals("Bod 4 - Intermedia")){
            bodx.setText("4");
        }
        else if (mode.equals("Bod 5 - Verduras")){
            bodx.setText("5");
        }
        else if (mode.equals("Bod 6 - Cecinas")){
            bodx.setText("6");
        }
        else if (mode.equals("Bod 10 - De Transito")){
            bodx.setText("10");
        }
        else if (mode.equals("Bod 20 - Particular")){
            bodx.setText("20");
        }
        else if (mode.equals("ZETA")) {
            tipodoc.setText("Z");
            nrodoc.requestFocus();
        }
        else if (mode.equals("REEXPEDICIÓN")) {
            tipodoc.setText("R");
            nrodoc.requestFocus();
        }
        else if (mode.equals("T.U")) {
            tipodoc.setText("T");
            nrodoc.requestFocus();
        }
        else if (mode.equals("DESPACHO")) {
            tipodoc.setText("B");
            nrodoc.requestFocus();
        }
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

    public void buscarProducto(){
        try{
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_C_ConsultorApp @Modo = 'C'," +
                    "@CodBod = '"+bodx.getText().toString()+"', @CodArt = '"+CodProd.getText().toString()+"' ");

            if(rs.next()){
                Des_Art.setText(rs.getString(1));
                Cod_Art.setText(rs.getString(2));
                Cod_Barra.setText(rs.getString(3));
                Pre_Ven.setText("$"+rs.getString(4));
                Pre_Oferta_Pesos.setText("$"+rs.getString(5));
                Sto_Art1.setText(rs.getString(6));
                Sto_Cri.setText(rs.getString(7));
                Ubicacion.setText(rs.getString(8));
                Sto_Art20.setText(rs.getString(9));
                Sto_Des.setText(rs.getString(10));
                Cod_Ubicacion.setText(rs.getString(11));
                ubiUG.setText(rs.getString(12));
                ubiUC.setText(rs.getString(13));
                Cap_Caja.setText(rs.getString(14));
                UniStock.setText(rs.getString(15));

            }
            else{
                Toast.makeText(getApplicationContext(),"CÓDIGO INVÁLIDO O INEXISTENTE EN SUPERMERCADO",Toast.LENGTH_SHORT).show();
            }
            CodProd.setText("");
            CodProd.requestFocus();
            Botones.setVisibility(View.VISIBLE);
            asoccod.setEnabled(true);
            asocbar.setEnabled(true);
            asocfech.setEnabled(true);

        }catch (Exception e){
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        CodProd.setText("");
        CodProd.requestFocus();
        Botones.setVisibility(View.VISIBLE);
    }

    public void buscarxBarra(){
        try{
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_C_ConsultorApp @Modo = 'B'," +
                    "@CodBod = '"+bodx.getText().toString()+"', @CodBar = '"+CodProd.getText().toString()+"' ");

            if(rs.next()){
                Des_Art.setText(rs.getString(1));
                Cod_Art.setText(rs.getString(2));
                Cod_Barra.setText(rs.getString(3));
                Pre_Ven.setText("$"+rs.getString(4));
                Pre_Oferta_Pesos.setText("$"+rs.getString(5));
                Sto_Art1.setText((int) rs.getFloat(6));
                Sto_Cri.setText(rs.getString(7));
                Ubicacion.setText(rs.getString(8));
                Sto_Art20.setText(rs.getString(9));
                Sto_Des.setText(rs.getString(10));
                Cod_Ubicacion.setText(rs.getString(11));
                ubiUG.setText(rs.getString(12));
                ubiUC.setText(rs.getString(13));
                Cap_Caja.setText(rs.getString(14));
                UniStock.setText(rs.getInt(15));

            }
            else{
                Toast.makeText(getApplicationContext(),"CÓDIGO INVÁLIDO O INEXISTENTE EN SUPERMERCADO",Toast.LENGTH_SHORT).show();
            }
            CodProd.setText("");
            CodProd.requestFocus();
            Botones.setVisibility(View.VISIBLE);

        }catch (Exception e){
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        CodProd.setText("");
        CodProd.requestFocus();
        Botones.setVisibility(View.VISIBLE);

    }

    public void buscarFleje(){
        try{
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_C_ConsultorApp @Modo = 'F'," +
                    "@CodBod = '"+bodx.getText().toString()+"', @CodArt = '"+CodProd2.getText().toString()+"' ");

            if(rs.next()){
                Des_Art.setText(rs.getString(2));
                Cod_Art.setText(rs.getString(1));
                Cod_Barra.setText(rs.getString(3));
                Pre_Ven.setText("$"+rs.getString(4));
                Pre_Oferta_Pesos.setText("$"+rs.getString(5));
                Cap_Caja.setText(rs.getString(6));
                Unidad.setText(rs.getString(17));
                Precio_UnidadMedida.setText("$"+rs.getString(10));
                PrecioDrenado.setText(rs.getString(16));

            }
            else{
                Toast.makeText(getApplicationContext(),"CÓDIGO INVÁLIDO O INEXISTENTE EN SUPERMERCADO",Toast.LENGTH_SHORT).show();
            }

            CodProd2.setText("");
            CodProd2.requestFocus();
            Botones.setVisibility(View.VISIBLE);
            asoccod.setEnabled(true);
            asocbar.setEnabled(true);
            asocfech.setEnabled(true);

        }catch (Exception e){
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        CodProd2.setText("");
        CodProd2.requestFocus();
        Botones.setVisibility(View.VISIBLE);
    }

    public void buscarxBarraFleje(){
        try{
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_C_ConsultorApp @Modo = 'G'," +
                    "@CodBod = '"+bodx.getText().toString()+"', @CodBar = '"+CodProd2.getText().toString()+"' ");

            if(rs.next()){
                Des_Art.setText(rs.getString(2));
                Cod_Art.setText(rs.getString(1));
                Cod_Barra.setText(rs.getString(3));
                Pre_Ven.setText("$"+rs.getString(4));
                Pre_Oferta_Pesos.setText("$"+rs.getString(5));
                Cap_Caja.setText(rs.getString(6));
                Unidad.setText(rs.getString(17));
                Precio_UnidadMedida.setText("$"+rs.getString(10));
                PrecioDrenado.setText(rs.getString(16));

            }
            else{
                Toast.makeText(getApplicationContext(),"CÓDIGO INVÁLIDO O INEXISTENTE EN SUPERMERCADO",Toast.LENGTH_SHORT).show();
            }
            CodProd2.setText("");
            CodProd2.requestFocus();
            Botones.setVisibility(View.VISIBLE);

        }catch (Exception e){
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        CodProd2.setText("");
        CodProd2.requestFocus();
        Botones.setVisibility(View.VISIBLE);

    }



    public static TextView getModox() { return modox; }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}