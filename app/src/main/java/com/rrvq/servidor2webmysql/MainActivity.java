package com.rrvq.servidor2webmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    Button btnIngresar, btnBuscar, btnModificar, btnEliminar;

    EditText etcodigo, etproducto, etprecio, etfabricante;

    Intent intent;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);

        etcodigo = findViewById(R.id.etcodigo);
        etproducto = findViewById(R.id.etproducto);
        etprecio = findViewById(R.id.etprecio);
        etfabricante = findViewById(R.id.etfabricante);

        btnIngresar = findViewById(R.id.btnIngresar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnModificar = findViewById(R.id.btnModificar);
        btnEliminar = findViewById(R.id.btnEliminar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etcodigo.getText().toString().isEmpty()) {
                    progressDialog.setMessage("Cargando...");
                    progressDialog.show();
                    ingresar("http://rrvqprueba.freevar.com/registrarProducto.php");
                }else {
                    etcodigo.setError("Dejar Campo vacio");
                }

            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etcodigo.getText().toString().isEmpty()){
                    etcodigo.setError("Complete el Campo");
                }else {
                    progressDialog.setMessage("Cargando...");
                    progressDialog.show();
                    //se envia como parametro el etcodigo para buscar en el web services
                    buscar("http://rrvqprueba.freevar.com/buscarProducto.php?codigo="+etcodigo.getText()+"");
                }
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etcodigo.getText().toString().isEmpty()){
                    etcodigo.setError("Complete el Campo");
                }else {
                    progressDialog.setMessage("Cargando...");
                    progressDialog.show();
                    //se envia como parametro el etcodigo para buscar en el web services
                    modificar("http://rrvqprueba.freevar.com/modificarProducto.php");
                }

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etcodigo.getText().toString().isEmpty()){
                    etcodigo.setError("Complete el Campo");
                }else {
                    progressDialog.setMessage("Cargando...");
                    progressDialog.show();
                    //se envia como parametro el etcodigo para buscar en el web services
                    eliminar("http://rrvqprueba.freevar.com//eliminarProducto.php");
                }

            }
        });


    }


    // tratar de ver que peticiones usan las mismas variable sy usar el mismo metodo asi ahorra codigo

    private void ingresar(String URL){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                String r = response;
                Toast.makeText(getApplicationContext(), "Operacion Exitosa", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String e = error.toString();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<String, String>();

                String nom = etproducto.getText().toString();
                String pre = etprecio.getText().toString();
                String fa = etfabricante.getText().toString();

                parametros.put("producto", nom);
                parametros.put("precio", pre);
                parametros.put("fabricante", fa);


                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void buscar(String URL){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {

                    try {

                        jsonObject = response.getJSONObject(i);

                        etproducto.setText(jsonObject.getString("producto"));
                        etprecio.setText(jsonObject.getString("precio"));
                        etfabricante.setText(jsonObject.getString("fabricante"));

                        progressDialog.dismiss();


                        /*etproducto.setText(pro);
                        etprecio.setText(pre);
                        etfabricante.setText(fa);*/

                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // esto se puede dar mensaje de error de conexion
                Toast.makeText(getApplicationContext(), "Comprueba tu conexion a internet", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


    // se podria usar el metodo de ingresar ya que es el mismo en tiempo d eejecucion ymismas variables
    private void modificar(String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                String r = response;
                Toast.makeText(getApplicationContext(), "Operacion Exitosa", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String e = error.toString();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("codigo", etcodigo.getText().toString());
                parametros.put("producto", etproducto.getText().toString());
                parametros.put("precio", etprecio.getText().toString());
                parametros.put("fabricante", etfabricante.getText().toString());


                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void eliminar(String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                String r = response;
                Toast.makeText(getApplicationContext(), "Operacion Exitosa", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String e = error.toString();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("codigo", etcodigo.getText().toString());


                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }







}
