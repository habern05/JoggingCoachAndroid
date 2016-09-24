package at.haraldbernhard.joggingcoachandroid;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateUserActivity extends AppCompatActivity implements TextWatcher{
    DbHelper dbHelper;
    EditText etUsername, etAge, etSize, etWeight;
    Button createAvatar;
    String username;
    int age, size, weight;
    //Shared Preferences
    public static SharedPreferences sharedPref;
    public static final String PREFERENCE_FILE_NAME ="UserPreference";
    public static final String PREFERENCE_KEY_USERNAME="username";
    public static final String PREFERENCE_KEY_WEIGHT="weight";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        setTitle("Create new User");
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPref = getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        createAvatar = (Button) findViewById(R.id.buttonCreateAvatar);
        createAvatar.setEnabled(false);
        etUsername = (EditText) findViewById(R.id.editTextUsername);
        etAge = (EditText) findViewById(R.id.editTextAge);
        etSize = (EditText) findViewById(R.id.editTextSize);
        etWeight = (EditText) findViewById(R.id.editTextWeight);
        etUsername.addTextChangedListener(this);
        etAge.addTextChangedListener(this);
        etSize.addTextChangedListener(this);
        etWeight.addTextChangedListener(this);
    }

    public void createNewAvatar(View view){
        User user = new User (username, age, size, weight);
        dbHelper = new DbHelper(this);
        dbHelper.insertUser(user);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREFERENCE_KEY_USERNAME, etUsername.getText().toString());
        editor.putInt(PREFERENCE_KEY_WEIGHT, Integer.parseInt(etWeight.getText().toString()));
        editor.commit();
        Toast.makeText(this, "User '" + username + "' successfully created", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LaunchActivity.class);
        startActivity(intent);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        username = etUsername.getText().toString();
        age = Integer.parseInt(etAge.getText().toString()+0)/10;
        size = Integer.parseInt(etSize.getText().toString()+0)/10;
        weight = Integer.parseInt(etWeight.getText().toString()+0)/10;
        if(!username.isEmpty() && age>0 && age<=100 && size>0 &size<250 && weight>0 && weight<200 ){
            createAvatar.setEnabled(true);
        }
        else{
            createAvatar.setEnabled(false);
        }
    }
}
