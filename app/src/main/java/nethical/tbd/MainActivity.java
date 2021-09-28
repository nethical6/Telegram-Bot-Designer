package nethical.tbd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
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
    public static SharedPreferences handledUpdates;

    public static String bot_tokenz;

    public static TextView logs;
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
            logs.setText("Starting Host ");
            Toast.makeText(MainActivity.this, "Starting hosting", Toast.LENGTH_SHORT).show();
            bot_tokenz = bot_token.getText().toString();
            Intent serviceIntent = new Intent(this, foregroundService.class);
            serviceIntent.putExtra("inputExtra", "Your bot is live");
            ContextCompat.startForegroundService(this, serviceIntent);

        }

    }

}