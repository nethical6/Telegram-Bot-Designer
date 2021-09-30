package nethical.tbd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


// created by Nethical on  25 sep 2021

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences handledUpdates;
    SharedPreferences alive ;

    public static String bot_token;

    public TextView logs;
    ToggleButton btn_host;
    EditText bot_token_edittext;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logs = findViewById(R.id.logs);
        bot_token_edittext = findViewById(R.id.bot_token);
        handledUpdates = getSharedPreferences("datum", Activity.MODE_PRIVATE);
        alive = getSharedPreferences("isAlive", Activity.MODE_PRIVATE);
        btn_host = findViewById(R.id.hostbot);

        if (alive.getString("isAlive","empty").equals("empty")) {
            alive.edit().putString("isAlive", "n").apply();
        }
        if (alive.getString("isAlive","").equals("y")){
            btn_host.setChecked(true);
        }

        btn_host.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (TextUtils.isEmpty(bot_token_edittext.getText().toString())) {
                        btn_host.setChecked(false);
                        Toast.makeText(MainActivity.this, "Enter your Bot Token", Toast.LENGTH_SHORT).show();
                    } else {
                        alive.edit().putString("isAlive", "y").apply();
                        logs.setText(R.string.startHost);
                        Toast.makeText(context, "Starting hosting", Toast.LENGTH_SHORT).show();
                        bot_token = bot_token_edittext.getText().toString();
                        Intent serviceIntent = new Intent(context, foregroundService.class);
                        serviceIntent.putExtra("inputExtra", "Your bot is live");
                        ContextCompat.startForegroundService(context, serviceIntent);
                    }

                } else {
                    alive.edit().putString("isAlive", "n").commit();
                    // to be improved
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        });

    }

}