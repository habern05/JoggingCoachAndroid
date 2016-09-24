package at.haraldbernhard.joggingcoachandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LaunchActivity extends AppCompatActivity {

    TextView greeting, createUser, personalGreeting, information1, information2;
    Button start ;
    DbHelper dbHelper;
    String username, lastTraining;
    //Shared Preferences
    public static SharedPreferences sharedPref;
    public static final String PREFERENCE_FILE_NAME ="UserPreference";
    public static final String PREFERENCE_KEY_USERNAME="username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        sharedPref = getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        username = sharedPref.getString(PREFERENCE_KEY_USERNAME, "");
        greeting = (TextView) findViewById(R.id.textViewGreeting) ;
        createUser = (TextView) findViewById(R.id.textViewCreateNewUser);
        start = (Button) findViewById(R.id.buttonStartTraining);
        personalGreeting = (TextView) findViewById(R.id.textViewPersonalGreeting);
        information1 = (TextView) findViewById(R.id.textViewInformation1);
        information2 = (TextView) findViewById(R.id.textViewInformation2);
        dbHelper = new DbHelper(this);
        lastTraining = dbHelper.getLastTrainingDate();

        if(!username.isEmpty()) {
            if(lastTraining !="") {
                personalGreeting.setText("Hello " + username + " \n \n \n Welcome back, \n your last training was on \n" + lastTraining);
            }
            else{
                personalGreeting.setText("Hello " + username + " \n \n Welcome to JoggingCoach");
            }
            greeting.setVisibility(View.INVISIBLE);
            createUser.setVisibility(View.INVISIBLE);
            information1.setVisibility(View.INVISIBLE);
            information2.setVisibility(View.INVISIBLE);
        }
        else{
            start.setVisibility(View.INVISIBLE);
            personalGreeting.setVisibility(View.INVISIBLE);
        }
    }

    public void startCreateUserActivity(View view){
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }

    public void startBasicActivity(View view){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
