package com.example.appinvoicing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    EditText  name, email, phone, passwd;
    Button btnadd, btnsearch, btnupdate, btndelete,btnlist;
    //RequestQueue rq;
    //JsonRequest jrq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //referenciar los objetos
        name = findViewById(R.id.etname);
        email = findViewById(R.id.etemail);
        phone = findViewById(R.id.etphone);
        passwd = findViewById(R.id.etpasswd);
        btnadd = findViewById(R.id.btnadd);
        btnsearch = findViewById(R.id.btnsearch);
        btnupdate = findViewById(R.id.btnupdate);
        btndelete = findViewById(R.id.btndelete);
        btnlist = findViewById(R.id.btnlist);
        //
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchCustomer(email.getText().toString());
            }
        });
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcustomer(name.getText().toString(), email.getText().toString(),phone.getText().toString(),passwd.getText().toString());
            }
        });
    }

    private void searchCustomer(String memail) {
        String url = "http://172.16.59.102:8081/invoicing/readcustomer.php?email="+memail;
        // requermiento del servidor a traves de una api por el metodo get, manda la informacion en formato jSON ingresa en on response
        JsonRequest jrq = new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                customer mcustomer=new customer();

                JSONArray jsonArray=response.optJSONArray("customer");
                JSONObject jsonObject=null;

                try {
                    jsonObject = jsonArray.getJSONObject(0);//posición 0 del arreglo....
                    mcustomer.setIdcust(jsonObject.optString("idcust"));
                    mcustomer.setName(jsonObject.optString("name"));
                    mcustomer.setEmail(jsonObject.optString("email"));
                    mcustomer.setPhone(jsonObject.optString("phone"));
                    mcustomer.setPasswd(jsonObject.optString("passwd"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                name.setText(mcustomer.getName());//SE MODIFICA
                email.setText(mcustomer.getEmail());//SE MODIFIC
                phone.setText(mcustomer.getPhone());//SE MODIFICA
                passwd.setText(mcustomer.getPasswd());//SE MODIFICA
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error, no se encuentra el Email: "+memail, Toast.LENGTH_LONG).show();
            }
        });
        // hacer la peticion por el metdo GET
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(jrq); // manda a ejecutar la linea anterior

    }

    private void addcustomer( String name, String email, String phone, String passwd) {
        if(!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !passwd.isEmpty()){
            //guardar el cliente
            String url = "http://172.16.59.102:8081/invoicing/addcustomer.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "Registro de usuario realizado correctamente!", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Registro de usuario incorrecto!", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name",name);
                    params.put("email", email);
                    params.put("phone",phone);
                    params.put("passwd",passwd);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        }
        else{
            Toast.makeText(getApplicationContext(), "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
        }
    }
}