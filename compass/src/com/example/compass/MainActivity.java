package com.example.compass;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener  {
	  
	  private MyCompassView compassView;
	  private Sensor sensor;
	  Sensor accelerometer;
	  Sensor magnetometer;
	  private SensorManager mSensorManager;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    compassView = new MyCompassView(this);
	    setContentView(compassView);

	    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    //sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    accelerometer=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    magnetometer=mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	    
	   /* if (accelerometer != null && magnetometer != null) {
	    	{Log.i("Compass MainActivity", "Si estan los dos");}
	    	mSensorManager.registerListener(mySensorEventListener, accelerometer,
	            SensorManager.SENSOR_DELAY_NORMAL);
	    	mSensorManager.registerListener(mySensorEventListener,magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
	        //Log.i("Compass MainActivity", "Registerered for ORIENTATION Sensor");

	      } else {
	    	if(accelerometer == null)
	    	{Log.e("Compass MainActivity", "No hay acel;erometro");}
	    	if(magnetometer== null)
	        Log.e("Compass MainActivity", "No hay magnetic field");
	        finish();
	      }*/
	}//cierre on Create
	
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
	    mSensorManager.registerListener(this,magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
      }
     
    @Override
      protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
      }

	  @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    if (accelerometer != null&&magnetometer!=null) {
	    	mSensorManager.unregisterListener(this);
	    }
	  }//onDestroy   
	
	    @Override
	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    }
	    
	    float[] accelValues=new float[3];
	    float[] compassValues=new float[3];
	    boolean ready=false;
	    @Override
	    public void onSensorChanged(SensorEvent event) {
	      // angle between the magnetic north direction
	      // 0=North, 90=East, 180=South, 270=West
	      //float azimuth = event.values[0];
	      //compassView.updateData(azimuth);
	    	   switch (event.sensor.getType()) {
	    	    case Sensor.TYPE_ACCELEROMETER:
	    	        for(int i=0; i<3; i++){
	    	            accelValues[i] =  event.values[i];
	    	        }
	    	        if(compassValues[0] != 0)
	    	            ready = true;

	    	        break;

	    	    case Sensor.TYPE_MAGNETIC_FIELD:
	    	        for(int i=0; i<3; i++){
	    	            compassValues[i] = event.values[i];
	    	        }
	    	        if(accelValues[2] != 0)
	    	            ready = true;

	    	        break;
	    	    }//cierre switch
	    	
	    	if (ready) {
	    		Log.i("Compass MainActivity","Ninguno de los dos fue nulo, los registro los dos");
	    	    float R[] = new float[9];//matriz de rotacion
	    	    float I[] = new float[9];//inclinacion
	    	    boolean success = SensorManager.getRotationMatrix(R, I, accelValues, compassValues);
	    	    if (success) {
	    	    	  Log.i("Compass MainActivity","Fue exito");
	    	        float orientation[] = new float[3];
	    	        SensorManager.getOrientation(R, orientation);
	    	        System.out.println(""+orientation[0]);
	    	       compassView.updateData((float)Math.toDegrees(orientation[0]));//azimuth, rotation around the Z axis.
	    	   }
	    	    else{  Log.i("Compass MainActivity","Fue fracaso");}
	    	    
	    }
	  }


}//cierreClase

