package ud.example.acelerometro;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager Sensores;
    private Sensor SensorAce;
    private float X, Y, Z;
    private TextView ValorX, ValorY, ValorZ, LogText;
    private ScrollView scrollview;
    int latigo=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LogText = findViewById(R.id.textView2);
        ValorX = findViewById(R.id.textView3);
        ValorY = findViewById(R.id.textView6);
        ValorZ = findViewById(R.id.textView8);
        scrollview = findViewById(R.id.scrollView);

        ValorX.setText("0");ValorY.setText("0");ValorZ.setText("0");
        X=0;Y=0;Z=0;

        Sensores = (SensorManager) getSystemService(SENSOR_SERVICE);
        SensorAce = Sensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensores.registerListener(this,SensorAce,SensorManager.SENSOR_DELAY_NORMAL);
        if(Sensores == null);
        finish();
        List<Sensor> listSensores = Sensores.getSensorList(Sensor.TYPE_ALL);
        for(Sensor sensor:listSensores){
            log("Sensor: " + sensor.getName().toString()) ;
        }

    }

    private void log(String s){
        LogText.append("\n" + s);
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
            try {
                float Xa = sensorEvent.values[0];
                float Ya = sensorEvent.values[1];
                float Za = sensorEvent.values[2];
                if(Math.abs(Xa-X)>= 1 || Math.abs(Ya-Y)>= 1 || Math.abs(Za-Z)>= 1 ){
                    ValorX.setText(String.valueOf(sensorEvent.values[0]));
                    ValorY.setText(String.valueOf(sensorEvent.values[1]));
                    ValorZ.setText(String.valueOf(sensorEvent.values[2]));
                }
                X = sensorEvent.values[0];
                Y = sensorEvent.values[1];
                Z = sensorEvent.values[2];
            } catch (Exception ex){}

            System.out.println("Valor del giro"+X);
            if(X<-5 && latigo==0){
                latigo++;


            }
            else if (X>5 && latigo==1){
                latigo++;

            }
            if(latigo==2){
                sound();
                latigo=0;}
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void sound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.latigo);
        mediaPlayer.start();
    }
}