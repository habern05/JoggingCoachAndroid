package at.haraldbernhard.joggingcoachandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IntervalModeActivity extends AppCompatActivity {


    Button btCancel, btStart;
    TextView workout, recreation, tvWorkout, tvRecreation;


    public static boolean showDialog;
    boolean workoutIsActive, recreationIsActive = true;

    Stopwatch stopwatch;
    StopwatchTask stopwatchTask;
    UpdateWorkoutUITask updateWorkoutUITask;
    UpdateRecreationUITask updateRecreationUITask;

    //Input from user
    private static int userInterval, userWork, userRec;
    //Control variable
    private static int imInterval, imWork, imRec;

    public static int getUserInterval() {
        return userInterval;
    }

    public static void setUserInterval(int userInterval) {
        IntervalModeActivity.userInterval = userInterval;
    }

    public static int getUserWork() {
        return userWork;
    }

    public static void setUserWork(int userWork) {
        IntervalModeActivity.userWork = userWork;
    }

    public static int getUserRec() {
        return userRec;
    }

    public static void setUserRec(int userRec) {
        IntervalModeActivity.userRec = userRec;
    }

    public static int getImInterval() {
        return imInterval;
    }

    public static void setImInterval(int imInterval) {
        IntervalModeActivity.imInterval = imInterval;
    }

    public static int getImWork() {
        return imWork;
    }

    public static void setImWork(int imWork) {
        IntervalModeActivity.imWork = imWork;
    }

    public static int getImRec() {
        return imRec;
    }

    public static void setImRec(int imRec) {
        IntervalModeActivity.imRec = imRec;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_mode);
        btCancel = (Button) findViewById(R.id.buttonIntervalCancel);
        btStart = (Button) findViewById(R.id.buttonIntervalStart);
        workout = (TextView) findViewById(R.id.textViewWorkout);
        recreation = (TextView) findViewById(R.id.textViewRecreation);
        tvWorkout = (TextView) findViewById(R.id.tvWorkout);
        tvRecreation = (TextView) findViewById(R.id.tvRecreation);
        btStart.setEnabled(false);
        IntervalModeActivity.setImInterval(IntervalModeActivity.getUserInterval()*2);
        if(showDialog){
            btStart.setEnabled(true);
        }
        userWork = IntervalDialogActivity.getSecWorkout();
        userRec = IntervalDialogActivity.getSecRecreation();
        userInterval = IntervalDialogActivity.getIntervals();

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //showIntervalDialog();
        if(!showDialog) {
            showDialog = true;
            Intent intent = new Intent(this, IntervalDialogActivity.class);
            startActivity(intent);
        }
    }
    //Button Cancel
    public void cancelIntervalTraining (View view){
        showDialog= false;
        finish();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);

    }
    //Button Start
    public void startIntervalTraining (View view){
        startTraining();
    }


    public void startTraining(){
        recreationIsActive = true;
        startTimerWorkout();
    }

    public void startTimerWorkout(){
        btStart.setEnabled(false);
        if(IntervalModeActivity.getImInterval() ==0) {
            stopwatchTask = (StopwatchTask) new StopwatchTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        updateWorkoutUITask = (UpdateWorkoutUITask) new UpdateWorkoutUITask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
    public void startTimerRecreation(){
        updateRecreationUITask = (UpdateRecreationUITask) new UpdateRecreationUITask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    //AsyncTask for Stopwatch
    class StopwatchTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            stopwatch = new Stopwatch();
            stopwatch.startStopwatch();
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }

    //AsyncTask For Update Worker User Interface
    class UpdateWorkoutUITask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (IntervalModeActivity.getImInterval() < IntervalModeActivity.getUserInterval()  ) {
                while (IntervalModeActivity.getUserWork() <= IntervalModeActivity.getImWork()){
                    try {
                        Thread.sleep(1000);
                        IntervalModeActivity.setImWork(stopwatch.getSec());
                        publishProgress();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("INTERVAL", IntervalModeActivity.getImInterval() + "");
                IntervalModeActivity.setImInterval(IntervalModeActivity.getImInterval() + 1);
                workoutIsActive = false;
                recreationIsActive = true;
                IntervalModeActivity.setImWork(0);
                stopwatch.setSec(0);
                publishProgress();
                startTimerRecreation();
            }
            else{
                Log.i("FINISH", "Finish");
                finish();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            tvWorkout.setText(IntervalModeActivity.getImWork() + "");
            Log.e("WORKOUT", IntervalModeActivity.getImWork() + "/" + IntervalModeActivity.getUserWork());
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }

    //AsyncTask For Update Recreation User Interface
    class UpdateRecreationUITask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(IntervalModeActivity.getUserInterval() != IntervalModeActivity.getImInterval()) {
                do {
                    try {
                        Thread.sleep(1000);
                        IntervalModeActivity.setImRec(stopwatch.getSec());
                        publishProgress();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while (IntervalModeActivity.getUserRec() != IntervalModeActivity.getImRec());
            }
            Log.e("RECREATION", "Initialize");
            IntervalModeActivity.setImInterval(IntervalModeActivity.getImInterval() + 1);
            workoutIsActive = true;
            recreationIsActive = false;
            IntervalModeActivity.setImRec(0);
            stopwatch.setSec(0);
            publishProgress();
            startTimerWorkout();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            tvRecreation.setText(IntervalModeActivity.getImRec() + "");
            Log.e("RECREATION", IntervalModeActivity.getImRec() + "/" + IntervalModeActivity.getUserRec());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }
}
