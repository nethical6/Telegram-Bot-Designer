package nethical.tbd;

import android.content.Context;
import android.content.SharedPreferences;

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

public class TelegramS {
    final Context context;

    final String bot_token = MainActivity.bot_token;

    private HashMap<String, Object> map = new HashMap<>();
    private String data = "";
    private HashMap<String, Object> map2 = new HashMap<>();
    private HashMap<String, Object> map3 = new HashMap<>();
    private HashMap<String, Object> map4 = new HashMap<>();
    private String chatid = "";
    private ArrayList<HashMap<String, Object>> updates = new ArrayList<>();
    final SharedPreferences handledUpdates = MainActivity.handledUpdates;
    public TelegramS(Context context){
        this.context = context;
    }
    public void polling(){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.telegram.org/bot" + bot_token.concat("/getUpdates"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        map = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>() {
                        }.getType());
                        data = map.get("result").toString();


                        data = (new Gson()).toJson(map.get("result"), new TypeToken<ArrayList<HashMap<String, Object>>>() {
                        }.getType());
                        updates = new Gson().fromJson(data, new TypeToken<ArrayList<HashMap<String, Object>>>() {
                        }.getType());
                        if (updates.size() == 0) {
                            map2 = updates.get((1));
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
                        int val = Double.valueOf(chatid).intValue();

                        // finds whether the incoming message is handled
                        if (!handledUpdates.getString("handle", "").equals(map2.get("update_id").toString())) {
                            handledUpdates.edit().putString("handle", map2.get("update_id").toString()).commit();
                            if (map3.get("text").toString().contains("/start")) {
                                sendMessage(bot_token,"sent from TBD",Integer.toString(val));
                            }
                        } else {
                            polling();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                polling();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void sendMessage(String token,String msg, String chatid){
        String url = "https://api.telegram.org/bot" + token + "/sendMessage" + "?chat_id=" + chatid + "&text=" + msg;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        polling();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                polling();
            }
        });
        queue.add(stringRequest);
    }

}
