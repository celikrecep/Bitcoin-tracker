package com.loyer.bitcoin_tracker;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {


    //https://apiv2.bitcoinaverage.com/#ticker-data-per-symbol sitedeki örneğe göre şekillendiriyoruz
    //Sadece BTC'de bırakmamızın sebebi spinnerdan seçilen birimi sonuna ekleyeceğiz
    //bu sayede seçtiğimiz birimin json isteğini döndürecek
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";


    TextView priceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        priceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);
        //spinner itemları için arrayadapter oluşturduk
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);
        //seçenek listesi görüntülendiğinde kullanılacak düzen
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("Bitcoin","chosen unit :" + adapterView.getItemAtPosition(i));
                Log.d("Bitcoin","Position is :" + i);

                String finalUrl = BASE_URL + adapterView.getItemAtPosition(i);
                Log.d("Bitcoin","Final url is :" + finalUrl);
                letsDoSomeNetworking(finalUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("Bitcoin","Nothing selected!");
            }
        });

    }

    private void letsDoSomeNetworking(String url) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Bitcoin", "JSON: " + response.toString());
                    // json objemizden ilgili olanı çekip ekrana yazdırma işlemini yapıyoruz
                try {
                    String price = response.getString("last");
                    priceTextView.setText(price);

                }catch (JSONException e){
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Log.d("Bitcoin", "Request fail! Status code: " + statusCode);
                Log.d("Bitcoin", "Fail response: " + response);
                Log.e("ERROR", e.toString());
            }
        });


    }


}
