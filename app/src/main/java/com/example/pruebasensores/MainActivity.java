package com.example.pruebasensores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private static final String TAG = "MainActivity";

    private SensorManager sensorManager ;
    private Sensor acelerometro, proximidad, luz  ;
    private float   acelVal ;   //CURRENTE ACCELERATION VALUE AND GRAVITY
    private float   acelLast ;  //LAST ACCELERATION VALUE AND GRAVITY
    private float   shake ;     //ACCELERATION VALUE differ FROM GRAVITY

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG,"onCreate: iniciando sensor services");

        // Creamos el objeto para acceder al servicio de sensores
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Iniciando sensores
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(acelerometro != null){

            sensorManager.registerListener(MainActivity.this,acelerometro,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG,"onCreate: registered acelerometro listener") ;

        }
        else{

            Toast.makeText(this, "Sensor acelerometro no soportado", Toast.LENGTH_SHORT).show();
        }

        proximidad = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(proximidad != null){

            sensorManager.registerListener(MainActivity.this,proximidad,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG,"onCreate: registered proximidad listener") ;

        }
        else{
            Toast.makeText(this, "Sensor de proximidad no soportado", Toast.LENGTH_SHORT).show();

        }

        luz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(luz != null){

            sensorManager.registerListener(MainActivity.this,luz,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG,"onCreate; registered luz listener");
        }
        else{
            Toast.makeText(this, "Sensor de luz no soportado", Toast.LENGTH_SHORT).show();
        }

        //MAGIA
        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f ;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // Metodo que escucha el cambio de los sensores
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor sensor = sensorEvent.sensor;

        // Cada sensor puede lanzar un thread que pase por aqui
        // Para asegurarnos ante los accesos simultaneos sincronizamos esto

        synchronized (this) {

            switch (sensor.getType()) {

                case Sensor.TYPE_ACCELEROMETER:

                    float x = sensorEvent.values[0] ;
                    float y = sensorEvent.values[1] ;
                    float z = sensorEvent.values[2] ;

                    acelLast = acelVal ;
                    acelVal = (float) Math.sqrt((double) (x*x + y*y + z*z) );
                    float delta = acelVal - acelLast ;
                    shake = shake * 0.9f + delta;

                    if(shake > 12){

                    //DO WHAT YOU WANT ****aca se mandaria una "a" por bluetooth****
                    Toast toast = Toast.makeText(getApplicationContext(),"DO NOT SHAKE ME", Toast.LENGTH_LONG);
                    toast.show();
                    }

                    break;

                case Sensor.TYPE_PROXIMITY:

                    if(sensorEvent.values[0]<sensor.getMaximumRange()){
                        //DO WHAT YOU WANT ****aca se mandaria una "d" por bluetooth****
                        Toast.makeText(this, "Proximidad activado", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case Sensor.TYPE_LIGHT:

                    /*luzValue.setText("Luz: "+sensorEvent.values[0]);
                     */
                    if(sensorEvent.values[0]<sensor.getMaximumRange()) {
                        //DO WHAT YOU WANT ****aca se mandaria una "e" por bluetooth****
                        Toast.makeText(this, " ", Toast.LENGTH_SHORT).show();
                    }
                    break;

                 default:
                     break;
            }

        }

    }


}
