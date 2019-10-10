package com.example.teht5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    RequestQueue jono;
    private Button btnGet;
    private ListView listview;
    private CharSequence errorText = "Yhteys ei käytettävissä!";
    private CharSequence testi = "Onnistui!";
    private int duration = Toast.LENGTH_SHORT;
    private boolean connected = false;
    private Context context;
    //private Gson gson;
    private ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGet = findViewById(R.id.btn1);
        listview = findViewById(R.id.listview1);
        context = getApplicationContext();
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        jono = Volley.newRequestQueue(this);
        btnGet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkConnectivity();
            }
        });

    }

    private void checkConnectivity() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(
                        ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            Toast toast = Toast.makeText(context, testi, duration);
            toast.show();
            final ArrayList<String> myArrayList = new ArrayList<>();
            StringRequest request = new StringRequest(Request.Method.GET,
                    "https://webd.savonia.fi/home/ktkoiju/j2me/test_json.php?dates&delay=1",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jo = jsonArray.getJSONObject(i);
                                    String pvm = jo.getString("pvm");
                                    String nimi = jo.getString("nimi");
                                    myArrayList.add(pvm + " " + nimi);

                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                            myAdapter.clear();
                            myAdapter.addAll(myArrayList);
                            myAdapter.notifyDataSetChanged();
                            listview.setAdapter(myAdapter);
                            /*for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject row = response.getJSONObject(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }*/
                            /*ArrayList<String> myArrayList = new ArrayList<>();

                            try {
                                JSONArray shapes = response.getJSONObject(1).getJSONArray("");
                                for(int i=0; i<shapes.length(); i++) {
                                    myArrayList.add(shapes.getJSONObject(i).getString("nimi") +
                                            shapes.getJSONObject(i).getString("pvm"));
                                }
                            } catch (JSONException e){
                                String message = "Virhe!";
                                Toast.makeText(context, message, duration).show();
                            }
                            */
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
            jono.add(request);
        }
        else {
            connected = false;
            Toast toast = Toast.makeText(context, errorText, duration);
            toast.show();
        }



    }





}
