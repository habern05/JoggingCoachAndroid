package at.haraldbernhard.joggingcoachandroid;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class TrainingDiaryActivity extends ListActivity {

    TrainingDiaryAdapter tdAdapter;
    DbHelper dbHelper;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DbHelper(this);
        cursor = dbHelper.getAllTraining();
        tdAdapter = new TrainingDiaryAdapter(this, cursor);
        setListAdapter(tdAdapter);
    }
}
