package com.example.siddhartha.infobasic;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LinearLayout infoList;
    Button more;
    String URL = "http://registermemory.esy.es/getdata.php?val=";
    int totalEntries;
    int clickCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoList = (LinearLayout)findViewById(R.id.llInfoList);
        more = (Button)findViewById(R.id.bMore);
        getCount();
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount++;
                getData();
            }
        });
        more.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String LOGIN_URL = "http://registermemory.esy.es/login.php";
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.adminlogin);
                dialog.setTitle("Admin Login");
                final EditText user = (EditText)dialog.findViewById(R.id.etUsername);
                final EditText pass = (EditText)dialog.findViewById(R.id.etPassword);
                Button login = (Button)dialog.findViewById(R.id.bLogin);
                dialog.show();
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String username = user.getText().toString();
                        final String password = pass.getText().toString();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.contentEquals("success")){
                                            Intent intent = new Intent(MainActivity.this,NewNotice.class);
                                            dialog.dismiss();
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(MainActivity.this, "Invalid Entry!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<>();
                                map.put("username",username);
                                map.put("password",password);
                                return map;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                        requestQueue.add(stringRequest);
                    }
                });
                return false;
            }
        });
    }

    private void getData() {
        if (totalEntries-10*clickCount>0) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + (totalEntries - 10 * clickCount),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            processJSON(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(MainActivity.this, "No items to display!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void getCount() {
        String URL = "http://registermemory.esy.es/countall.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject object = jsonArray.getJSONObject(0);
                            totalEntries = Integer.parseInt(object.getString("total"));
                            getData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error in receiving count!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    protected void processJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for (int i = 0; i<10; i++){
                try {
                    JSONObject info = jsonArray.getJSONObject(i);
                    setCard(info.getString("notice"),info.getString("postedby"),info.getString("dateandtime"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void setCard(String title, String postedBy, String dateAndTime) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        card.setBackgroundResource(R.drawable.cardsmall);

        LinearLayout bottomLine = new LinearLayout(this);
        bottomLine.setOrientation(LinearLayout.HORIZONTAL);
        bottomLine.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        bottomLine.setWeightSum(100);

        TextView tTitle = new TextView(this);
        TextView tPostedBy = new TextView(this);
        TextView tDateAndTime = new TextView(this);

        tTitle.setText(title);
        tTitle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tTitle.setTextColor(Color.BLACK);
        tTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);

        LinearLayout.LayoutParams postedByParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 40);
        tPostedBy.setText(postedBy);
        tPostedBy.setLayoutParams(postedByParam);
        tPostedBy.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

        LinearLayout.LayoutParams dateAndTimeParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 60);
        tDateAndTime.setText(dateAndTime);
        tDateAndTime.setLayoutParams(dateAndTimeParam);
        tDateAndTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

        bottomLine.addView(tPostedBy);
        bottomLine.addView(tDateAndTime);

        card.addView(tTitle);
        card.addView(bottomLine);

        infoList.addView(card);
    }
}
