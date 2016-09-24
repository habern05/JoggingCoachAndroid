package at.haraldbernhard.joggingcoachandroid;

import android.util.Log;

public class Stopwatch {

    private int sec, min, hour;
    private String time;

    public Stopwatch(){

    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void startStopwatch(){
        while(true){
            while (min<60){
                while (sec<60){
                    try{
                        Thread.sleep(1000);
                        sec +=1;
                        setHour(hour);
                        setMin(min);
                        setSec(sec);
                        Log.i("Time", hour + " : " + min + " : " + sec);
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
                min +=1;
                sec = 0;
            }
            hour +=1;
            min = 0;
            sec = 0;
        }
    }
}
