package at.haraldbernhard.joggingcoachandroid;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TrainingModeActivity extends AppCompatActivity {

    //Labels
    TextView tvLabelTime, tvLabelDistance, tvLabelCalorie, tvLabelSpeed;
    //TextViews for display
    TextView tvTime, tvDistance, tvCalorie, tvSpeedAverage, tvSpeedImmidiate;
    //Buttons
    Button startTraining, stopTraining, pauseTraining;
    //Strings for display information
    static String time = "0:0:0", distance = "0,0", calorie = "0,0", speedA = "0,0", speedI = "0,0 ";
    //is start button pressed?
    boolean isActive = false;
    Stopwatch stopwatch;
    //Database
    DbHelper dbHelper;
    //AsyncTask
    StopwatchTask stopwatchTask;
    UpdateUITask updateUITask;
    public static final int LOCATION_REQUEST = 1337;
    public static SharedPreferences sharedPref;
    public static final String PREFERENCE_FILE_NAME = "UserPreference";
    public static final String PREFERENCE_KEY_WEIGHT = "weight";

    private static int tmHour, tmMin, tmSec;
    private static double tmDistance, tmSpeedA, tmSpeedI, tmCalorie;
    private static int tmKilo = 75;  //TODO SHARED PREF

    public static double getTmSpeedI() {
        return tmSpeedI;
    }

    public static void setTmSpeedI(double tmSpeedI) {
        TrainingModeActivity.tmSpeedI = tmSpeedI;
    }

    public static double getTmSpeedA() {
        return tmSpeedA;
    }

    public static void setTmSpeedA(double tmSpeedA) {
        TrainingModeActivity.tmSpeedA = tmSpeedA;
    }

    public static double getTmDistance() {
        return tmDistance;
    }

    public static void setTmDistance(double tmDistance) {
        TrainingModeActivity.tmDistance = tmDistance;
    }

    public static double getTmCalorie() {
        return tmCalorie;
    }

    public static void setTmCalorie(double tmCalorie) {
        TrainingModeActivity.tmCalorie = tmCalorie;
    }

    public static int getTmKilo() {
        return tmKilo;
    }

    public static int getTmSec() {
        return tmSec;
    }

    public static void setTmSec(int tmSec) {
        TrainingModeActivity.tmSec = tmSec;
    }

    public static int getTmMin() {
        return tmMin;
    }

    public static void setTmMin(int tmMin) {
        TrainingModeActivity.tmMin = tmMin;
    }

    public static int getTmHour() {
        return tmHour;
    }

    public static void setTmHour(int tmHour) {
        TrainingModeActivity.tmHour = tmHour;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_mode);
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Labels
        tvLabelTime = (TextView) findViewById(R.id.textViewLabelTime);
        tvLabelDistance = (TextView) findViewById(R.id.textViewLabelDistance);
        tvLabelCalorie = (TextView) findViewById(R.id.textViewLabelCalorie);
        tvLabelSpeed = (TextView) findViewById(R.id.textViewLabelSpeed);
        //TextViews for display
        tvTime = (TextView) findViewById(R.id.textViewTime);
        tvDistance = (TextView) findViewById(R.id.textViewDistance);
        tvCalorie = (TextView) findViewById(R.id.textViewCalorie);
        tvSpeedImmidiate = (TextView) findViewById(R.id.textViewSpeedI);
        tvSpeedAverage = (TextView) findViewById(R.id.textViewSpeedA);
        //Starting values
        tvTime.setText(time);
        tvDistance.setText(distance);
        tvCalorie.setText(calorie);
        tvSpeedImmidiate.setText(speedI);
        tvSpeedAverage.setText(speedA);
        sharedPref = getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        tmKilo = sharedPref.getInt(PREFERENCE_KEY_WEIGHT, 0);
        Log.e("log", tmKilo+"");
        //Buttons
        startTraining = (Button) findViewById(R.id.buttonStart);
        stopTraining = (Button) findViewById(R.id.buttonStop);
        pauseTraining = (Button) findViewById(R.id.buttonPause);
        pauseTraining.setVisibility(View.INVISIBLE);
        stopTraining.setEnabled(false);
        gpsPermission();
    }

    public void gpsPermission() {
        // Check if the GPS permission is already available
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Initialize Location Manager", Toast.LENGTH_LONG).show();
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener ll = new AndroidLocationListener();
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 50, ll);
        }
        // Permission is not available
        else {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously*
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Intent intent = new Intent(this, MenuActivity.class);
                    startActivity(intent);
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_ResultDiary);
        builder.setMessage(R.string.message_ResultDiary);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked YES button
                //Date
                Calendar calendar = new GregorianCalendar();
                String date = calendar.get(Calendar.DAY_OF_MONTH) + ". " + calendar.get(Calendar.MONTH) + ". " + calendar.get(Calendar.YEAR);
                time = tvTime.getText()+"";
                distance = tvDistance.getText()+"";
                calorie = tvCalorie.getText()+"";
                speedA = tvSpeedAverage.getText()+"";
                //Insert into Database
                dbHelper = new DbHelper(TrainingModeActivity.this);
                dbHelper.insertTraining(date, time, distance, calorie, speedA);
                finish();
                System.exit(0);
                Intent intent = new Intent(TrainingModeActivity.this, MenuActivity.class);
                startActivity(intent);
                Toast.makeText(TrainingModeActivity.this, R.string.save_result, Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked NO button
                finish();
                System.exit(0);
                Intent intent = new Intent(TrainingModeActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //AsyncTask for Stopwatch
    class StopwatchTask extends AsyncTask<Void, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            stopwatch = new Stopwatch();
            stopwatch.startStopwatch();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }


    //AsyncTask For Update User Interface
    class UpdateUITask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... voids) {

            while (isActive) {
                try {
                    TrainingModeActivity.setTmHour(stopwatch.getHour());
                    TrainingModeActivity.setTmMin(stopwatch.getMin());
                    TrainingModeActivity.setTmSec(stopwatch.getSec());
                    TrainingModeActivity.setTmSpeedA(TrainingModeActivity.getTmDistance()/(((double)TrainingModeActivity.getTmHour())+((double)TrainingModeActivity.getTmMin()/60)+((double)TrainingModeActivity.getTmSec()/3600))) ;
                    TrainingModeActivity.setTmCalorie(TrainingModeActivity.getTmKilo()*TrainingModeActivity.getTmDistance());
                    Thread.sleep(1000);
                    publishProgress();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.e("LOG", tmHour + ":" + tmMin +":" + tmSec+ " | " + tmDistance + " | " + tmCalorie + " | " + tmSpeedA + " | " + tmSpeedI + " ");
            Log.e("Kilo", TrainingModeActivity.getTmKilo()+"");
            tvTime.setText(TrainingModeActivity.getTmHour() + " : " + TrainingModeActivity.getTmMin() + " : " + TrainingModeActivity.getTmSec() );
            tvDistance.setText(String.format("%.3f", TrainingModeActivity.getTmDistance()));
            tvSpeedAverage.setText( String.format("%.2f", TrainingModeActivity.getTmSpeedA()));
            tvSpeedImmidiate.setText(String.format("%.2f", TrainingModeActivity.getTmSpeedI()));
            tvCalorie.setText(String.format("%.1f", TrainingModeActivity.getTmCalorie()));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }

    public void buttonStartTraining(View view) {
        startTraining.setEnabled(false);
        pauseTraining.setEnabled(true);
        stopTraining.setEnabled(true);
        isActive = true;
        stopwatchTask = (StopwatchTask) new StopwatchTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        updateUITask = (UpdateUITask) new UpdateUITask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void buttonStopTraining(View view) {
        createAlertDialog();
    }

    public void buttonPauseTraining(View view) {
        onPause();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public static void setTime(String stopwatchTime) {
        time = stopwatchTime;
    }

    public static void setDistance(String locationDistance) {
        distance = locationDistance;
    }

    public static void setSpeedA(String locationSpeed) {
        speedA = locationSpeed;
    }

    public static void setSpeedI(String locationSpeed) {
        speedI = locationSpeed;
    }


    public static String getInformationAsJSON() {
        return "{\"time\":\"" + time + " \", \"distance\":\"" + distance + "\", \"speed\": \"" + speedA + "\"}";
    }

    public class AndroidLocationListener implements LocationListener {
        double speedI, distance;

        @Override
        public void onLocationChanged(Location location) {

            if (location != null && isActive) {
                if (location.getLongitude() != 0 && location.getLatitude() != 0) {
                    distance += 0.050;
                }
                speedI = location.getSpeed();
                TrainingModeActivity.setTmDistance(distance);
                TrainingModeActivity.setTmSpeedI(speedI);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }
}
