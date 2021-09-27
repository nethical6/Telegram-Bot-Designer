package nethical.tbd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

// created by Nethical on  25 sep 2021

public class MainActivity extends AppCompatActivity {

    private HashMap<String, Object> map = new HashMap<>();
    private String data = "";
    private HashMap<String, Object> map2 = new HashMap<>();
    private HashMap<String, Object> map3 = new HashMap<>();
    private HashMap<String, Object> map4 = new HashMap<>();
    private String chatid = "";
    private ArrayList<HashMap<String, Object>> updates = new ArrayList<>();
    private SharedPreferences handledUpdates;

    String bot_tokenz;

    TextView logs;
    Button host;
    EditText bot_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        host = findViewById(R.id.host);
        logs = findViewById(R.id.logs);
        bot_token = findViewById(R.id.bot_token);

        handledUpdates = getSharedPreferences("datum", Activity.MODE_PRIVATE);

    }

    public void btn_host(View view){
        if (TextUtils.isEmpty(bot_token.getText().toString())){
            Toast.makeText(MainActivity.this, "Enter your Bot Token", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "shit", Toast.LENGTH_SHORT).show();
            bot_tokenz = bot_token.getText().toString();
            polling(bot_tokenz);

        }
    }

    public void polling(String token){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.telegram.org/bot".concat(token.concat("/getUpdates")),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        logs.setText(logs.getText().toString().concat("\n" + response));

                        map = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>() {
                        }.getType());
                            data = map.get("result").toString();


                            data = (new Gson()).toJson(map.get("result"), new TypeToken<ArrayList<HashMap<String, Object>>>() {
                            }.getType());
                            updates = new Gson().fromJson(data, new TypeToken<ArrayList<HashMap<String, Object>>>() {
                            }.getType());
                            if (updates.size() == 0) {
                                map2 = updates.get((0));
                            } else {
                                map2 = updates.get((int) updates.size() - 1);
                            }


                            data = (new Gson()).toJson(map2.get("message"), new TypeToken<HashMap<String, Object>>() {
                            }.getType());
                            map3 = new Gson().fromJson(data, new TypeToken<HashMap<String, Object>>() {
                            }.getType());


                            data = (new Gson()).toJson(map3.get("chat"), new TypeToken<HashMap<String, Object>>() {
                            }.getType());
                            map4 = new Gson().fromJson(data, new TypeToken<HashMap<String, Object>>() {
                            }.getType());

                            chatid = map4.get("id").toString();

                            // for some reason the output is in a scientific notation so we need to convert it to normal integer
                            int val = new Double(chatid).intValue();

                            // finds whether the incomming message is handled
                            if (!handledUpdates.getString("handle", "").equals(map2.get("update_id").toString())) {
                                handledUpdates.edit().putString("handle", map2.get("update_id").toString()).commit();
                                // send message according to trigger
                                if (map3.get("text").toString().contains("/start")) {
                                    logs.setText("message recieved");
                                    sendMessage(bot_tokenz,"sent from TBD",Integer.toString(val));
                                }
                            } else {
                                polling(bot_tokenz);
                            }
                        }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logs.setText(logs.getText().toString().concat("\n" + error.toString()));
                polling(bot_tokenz);
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    
    public void sendMessage(String token,String msg, String chatid){
        String url = "https://api.telegram.org/bot" + token + "/sendMessage" + "?chat_id=" + chatid + "&text=" + msg;
        logs.setText(url);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        logs.setText(logs.getText().toString().concat("\n"+response));
                        polling(bot_tokenz);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logs.setText(logs.getText().toString().concat("\n" + error.toString()));
                polling(bot_tokenz);
            }
        });
        queue.add(stringRequest);
    }

}