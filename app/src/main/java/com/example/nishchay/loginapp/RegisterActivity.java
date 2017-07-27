package com.example.nishchay.loginapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextView loginTextView;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;

    private String registerURL="http://192.168.225.223:3000/register";
    private RequestQueue registerRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText= findViewById(R.id.nameEditText);
        emailEditText= findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginTextView= findViewById(R.id.loginTextView);
        registerButton= findViewById(R.id.registerBtn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });
    }

    private void goToLogin(){
        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
    }


    private void register() {
        if (registerRequestQueue == null)
            registerRequestQueue = Volley.newRequestQueue(RegisterActivity.this);

        HashMap<String,String>params=new HashMap<String,String>();
        params.put("name",nameEditText.getText().toString().trim());
        params.put("email",emailEditText.getText().toString().trim());
        params.put("username",usernameEditText.getText().toString().trim());
        params.put("password",passwordEditText.getText().toString().trim());

        JsonObjectRequest registerRequest = new JsonObjectRequest(registerURL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response.optString("success").matches("true")){
                            Toast.makeText(RegisterActivity.this, "Successfully registered..", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(RegisterActivity.this, "Error registering, Please try again.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        registerRequest.setRetryPolicy(new DefaultRetryPolicy(
                2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        registerRequestQueue.add(registerRequest);
    }
}
