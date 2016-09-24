package at.haraldbernhard.joggingcoachandroid;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Harald Bernhard on 24.07.2016.
 */
public class TrainingDiaryAdapter extends CursorAdapter{

    private LayoutInflater inflator;
    int ciDate, ciTime, ciDistance, ciCalorie, ciSpeedA;

    TrainingDiaryAdapter (Context context, Cursor cursor){
        super(context, cursor);
        inflator = LayoutInflater.from(context);
        ciDate = cursor.getColumnIndex(DbHelper.DATE);
        ciTime = cursor.getColumnIndex(DbHelper.TIME);
        ciDistance = cursor.getColumnIndex(DbHelper.DISTANCE);
        ciCalorie = cursor.getColumnIndex(DbHelper.CALORIE);
        ciSpeedA = cursor.getColumnIndex(DbHelper.SPEEDA);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup){
        return inflator.inflate(R.layout.training_result, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvDate = (TextView) view.findViewById(R.id.textViewDateAdapter);
        tvDate.setText("Date: " + cursor.getString(ciDate));
        TextView tvTime = (TextView) view.findViewById(R.id.textViewTimeAdapter);
        tvTime.setText("Time: " + cursor.getString(ciTime));
        TextView tvDistance = (TextView) view.findViewById(R.id.textViewDistanceAdapter);
        tvDistance.setText("Distance: " + cursor.getString(ciDistance));
        TextView tvCalorie = (TextView) view.findViewById(R.id.textViewCalorieAdapter);
        tvCalorie.setText("Calorie: " + cursor.getString(ciCalorie));
        TextView tvSpeedA = (TextView) view.findViewById(R.id.textViewSpeedAAdapter);
        tvSpeedA.setText("Speed: " + cursor.getString(ciSpeedA));
    }
}
