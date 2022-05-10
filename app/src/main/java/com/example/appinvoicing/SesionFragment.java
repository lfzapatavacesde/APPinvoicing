package com.example.appinvoicing;
//sesion fragment para poner un fragmento en cualquier parte para conectar
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SesionFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

//instanciar y referenciar lo que tenemos en xml de sesion fragmet
    EditText correo, clave;
    Button ingresar;
    TextView registrar;
    RequestQueue rq; //peticion
    JsonRequest jrq; //recibir la informaciom en formato JSON

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_sesion, container, false);
        //tiene que retornar un view porque es un fragment

        View iniciarsesion = inflater.inflate(R.layout.fragment_sesion,container,false);
        correo = iniciarsesion.findViewById(R.id.etcorreo);
        clave = iniciarsesion.findViewById(R.id.etclave);
        ingresar = iniciarsesion.findViewById(R.id.btningresar);
        registrar = iniciarsesion.findViewById(R.id.tvregistrar);
        rq = Volley.newRequestQueue(getContext());//requerimiento Volley estamos en fragment por eso es get context hace la peticion al servidor

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), register.class);
                startActivity(i);
            }
        });
        
        //evento click del boton ingresar
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarsesion(correo.getText().toString(), clave.getText().toString());
                
            }
        });

        return iniciarsesion;
    }

    private void iniciarsesion(String correo, String clave) {
        String url = "http://172.16.59.102:8081/invoicing/searchcustomer.php?email="+correo+"&passwd="+clave;

        // requermiento del servidor a traves de una api por el metodo get, manda la informacion en formato jSON ingresa en on response
        jrq = new JsonObjectRequest(Request.Method.GET,url,null, this, this); // hacer la peticion por el metdo GET
        rq.add(jrq); // manda a ejecutar la linea anterior
    }

    @Override
    public void onErrorResponse(VolleyError error) {
//sevidor no escucho por alguna razon, debe haber algo que no encontro
        Toast.makeText(getContext(), "el usuario con correo"+correo.getText().toString()+" NO se ha encontrado, email y/o contraseña invalido", Toast.LENGTH_SHORT). show();
    }

    @Override
    public void onResponse(JSONObject response) {
        //Toast.makeText(getContext(), "el usuario con correo"+correo.getText().toString()+" SI se ha encontrado", Toast.LENGTH_SHORT). show();
        // voley devuelve si encontro o no el usuario
    customer custom = new customer();
        JSONArray jsonCustomer = response.optJSONArray("customer");
        JSONObject customerObject = null;

        try {
            customerObject = jsonCustomer.getJSONObject(0);//posición 0 del arreglo....
            //custom.setIdcust(customerObject.optString("idcust"));
            custom.setName(customerObject.optString("name"));
            //custom.setEmail(customerObject.optString("email"));
            //custom.setPhone(customerObject.optString("phone"));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        Intent intentMain = new Intent(getContext(),main.class);
        intentMain.putExtra("myname",custom.getName());//cojerlo en ua variable que cuando llegue alli se llame my name y cohja getname
        startActivity(intentMain);
    }
}
