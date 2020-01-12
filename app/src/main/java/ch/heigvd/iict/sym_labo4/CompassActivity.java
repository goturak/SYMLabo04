package ch.heigvd.iict.sym_labo4;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import ch.heigvd.iict.sym_labo4.gl.OpenGLRenderer;

public class CompassActivity extends AppCompatActivity {

    //opengl
    private OpenGLRenderer  opglr           = null;
    private GLSurfaceView   m3DView         = null;


    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magneticSensor;

    private float[] accelerometerValues= new float[3];
    private float[] magneticSensorValues= new float[3];

    private float[] currentRotationMatrix= new float[16];
    private float[] targetRotationMatrix= new float[16];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // we need fullscreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // we initiate the view
        setContentView(R.layout.activity_compass);

        //we create the renderer
        this.opglr = new OpenGLRenderer(getApplicationContext());

        // link to GUI
        this.m3DView = findViewById(R.id.compass_opengl);

        //init opengl surface view
        this.m3DView.setRenderer(this.opglr);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor= sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    final SensorEventListener sensorListener= new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            switch(sensorEvent.sensor.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                    accelerometerValues=sensorEvent.values;
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    magneticSensorValues=sensorEvent.values;
            }

            SensorManager.getRotationMatrix(targetRotationMatrix,null,accelerometerValues,magneticSensorValues);
            //simple interpolation to dampen the twitchy arrow we get (probably from noise iin the values from the sensors)
            for(int i=0;i<16;i++){
                currentRotationMatrix[i] = (targetRotationMatrix[i]-currentRotationMatrix[i])*0.05f+currentRotationMatrix[i];
            }
            opglr.swapRotMatrix(currentRotationMatrix);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }


    };

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(sensorListener,accelerometer,SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(sensorListener,magneticSensor,SensorManager.SENSOR_DELAY_GAME);
    }



    /* TODO
        your activity need to register to accelerometer and magnetometer sensors' updates
        then you may want to call
        this.opglr.swapRotMatrix()
        with the 4x4 rotation matrix, everytime a new matrix is computed
        more information on rotation matrix can be found on-line:
        https://developer.android.com/reference/android/hardware/SensorManager.html#getRotationMatrix(float[],%20float[],%20float[],%20float[])
    */
}
