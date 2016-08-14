package com.example.siddhartha.infobasic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class NewNotice extends AppCompatActivity {

    EditText notice,postedBy,dateAndTime;
    Button submit;
    String POST_URL = "http://registermemory.esy.es/post.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notice);
        notice = (EditText)findViewById(R.id.etNotice);
        postedBy = (EditText)findViewById(R.id.etPostedBy);
        dateAndTime = (EditText)findViewById(R.id.etDateAndTime);
        submit = (Button)findViewById(R.id.bSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contentEquals("success")){
                                    Toast.makeText(NewNotice.this, "Notice added successfully!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(NewNotice.this, "Submission Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(NewNotice.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<>();
                        map.put("notice",notice.getText().toString());
                        map.put("postedby",postedBy.getText().toString());
                        map.put("dateandtime",dateAndTime.getText().toString());
                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(NewNotice.this);
                requestQueue.add(stringRequest);
            }
        });
    }
}
