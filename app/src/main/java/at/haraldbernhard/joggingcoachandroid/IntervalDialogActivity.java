package at.haraldbernhard.joggingcoachandroid;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import org.w3c.dom.Text;

public class IntervalDialogActivity extends Activity{

    private static int  secWorkout, secRecreation, intervals;
    NumberPicker npWorkSec, npRecSec, npInterval;
    Button btStart, btCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_dialog);
        npWorkSec = (NumberPicker) findViewById(R.id.nPWorkoutSec);
        npRecSec = (NumberPicker) findViewById(R.id.npRecreationSec);
        npInterval = (NumberPicker) findViewById(R.id.npIntervals);
        btStart = (Button) findViewById(R.id.buttonIntervalDialogStart);
        btCancel = (Button)findViewById(R.id.buttonIntervalDialogCancel);
        npWorkSec.setMinValue(0);
        npWorkSec.setMaxValue(300);
        npRecSec.setMinValue(0);
        npRecSec.setMaxValue(300);
        npInterval.setMinValue(0);
        npInterval.setMaxValue(10);
    }

    public void onStart(View view){
        //Store Input
        secWorkout = npWorkSec.getValue();
        secRecreation = npRecSec.getValue();
        intervals = npInterval.getValue();
        //Exit Dialog
        IntervalModeActivity.showDialog=true;
        Intent intent = new Intent (this, IntervalModeActivity.class);
        startActivity(intent);
    }

    public void onCancel(View view){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public static int getSecWorkout() {return secWorkout;}
    public static int getSecRecreation() {
        return secRecreation;
    }
    public static int getIntervals() {
        return intervals;
    }

}
