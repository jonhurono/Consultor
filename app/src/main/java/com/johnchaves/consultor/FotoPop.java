package com.johnchaves.consultor;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class FotoPop extends Activity {

    ImageView   imgArt;
    Button      back;
    TextView    Cod_Art = MainActivity.getCod_Art();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fotowindow);

        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.75),(int)(height*.65));


        imgArt = findViewById(R.id.imgArt);
        back   = findViewById(R.id.btnSalirImg);
        Picasso.get().load("http://192.168.0.18/fotoarticulo/"+Cod_Art.getText()+".png")
                //.resize(500, 500)
                .fit()
                .into(imgArt);

        //new SearchImage(findViewById(R.id.imgArt)).execute("http://192.168.0.18/fotoarticulo/"+Cod_Art.getText()+".png");

        back.setOnClickListener(v -> finish());

    }

    /*
    private class SearchImage extends AsyncTask<String, Void, Bitmap> {
        ImageView imgArt;

        public SearchImage(ImageView imgArt) {
            this.imgArt=imgArt;
            //Toast.makeText(getApplicationContext(), "Por favor espere",Toast.LENGTH_SHORT).show();
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error:", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imgArt.setImageBitmap(result);
        }
    }*/
}
