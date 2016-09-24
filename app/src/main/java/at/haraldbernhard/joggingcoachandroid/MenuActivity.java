package at.haraldbernhard.joggingcoachandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    Button startNewTraining, diary, startNewIntervalTraining;
    TextView greeting;
    SharedPreferences sharedPref;
    DbHelper dbHelper;
    public static final String preferenceFileName ="UserPreference";
    public static final String preferenceKeyId="id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        sharedPref = getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        dbHelper = new DbHelper(this);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        greeting = (TextView) findViewById(R.id.textViewGreeting);
        int id = sharedPref.getInt(preferenceKeyId, 0);


        startNewTraining = (Button) findViewById(R.id.buttonStartNewTraining);
        startNewIntervalTraining = (Button)findViewById(R.id.buttonStartIntervalTraining);
        diary = (Button) findViewById(R.id.buttonDiary);
    }


    public void startTrainingSession(View view){
        Intent intent = new Intent(this, TrainingModeActivity.class);
        startActivity(intent);

    }

    public void startDiary(View view){
        Intent intent = new Intent(this, TrainingDiaryActivity.class);
        startActivity(intent);
    }

    public void startIntervalTraining(View view){
        Intent intent = new Intent(this, IntervalModeActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_exit:
                finish();
                System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }


}
