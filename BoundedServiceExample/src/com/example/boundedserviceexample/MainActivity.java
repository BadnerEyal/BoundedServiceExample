package com.example.boundedserviceexample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements DownloadListener {

	public static final String INTENT_ACTION_DOWNLOAD_COMPLETED = "com.intent.servicee.DOWNLOAD_COMPLETED";

	public static final String MY_PIC = "my_pic.jpg";
	
	private boolean isBounded = false;
	private ImageView imageView;
	
	private MyServiceConnection serviceConnection = new MyServiceConnection() ;
	
	private DownloadImageBinder downloadImageBinder;
	private Button button = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imageView = (ImageView)findViewById(R.id.imageView);
		button = (Button) findViewById(R.id.button1);

		button.setEnabled(false);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String target = MY_PIC;
				String urlPath = "https://fbcdn-sphotos-b-a.akamaihd.net/hphotos-ak-ash3/t1.0-9/1896773_10152249104200829_4748683226336263456_n.jpg";
				//בגלל שיצרנו אוביקט מסוג DownloadImageBinder
				//ניתן לגשת לפונקציות שלו
				//נשלח איתו כתובת להורדה איפה נשמור ומי מאזין אליו שיגמור
				//downloadImageBinder.downloadImage(urlPath, target, listener)
				downloadImageBinder.downloadImage(urlPath, target, MainActivity.this);
			}
		});
		
		
	}
	
	
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		//Serviceהתחברות ל
		Intent intent = new Intent(this, HelloBoundedService.class);
		bindService(intent, serviceConnection,  Context.BIND_AUTO_CREATE);
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		//Serviceניתוק ל
		//במקרה שאף אחד לא מחובר אליו אז הוא ימות
		unbindService(serviceConnection);
	}

	//לפה נגיע אחרי שנחזור מסיום קריאה ושמירת הקובץ למכשיר
	//בגלל ש implements DownloadListener
	//אז אנחנו חיבים לממש פונקציה זו
	@Override
	public void onDownloadCompleted(String absPath) {
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Log.d("MainActivity$BroadcastReceiver","onReceive()");
				//Toast.makeText(this, "Download Completed", Toast.LENGTH_LONG).show();
				
				try {
					final Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(getFileStreamPath(MY_PIC)));
					imageView.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				}
				
			}
		});
		
	}

//ServiceConnection	
//Interface for monitoring the state of an application service. See Service and Context.bindService() for more information. 
//Like many callbacks from the system, the methods on this class are called from the main thread of your process
	
	private class MyServiceConnection implements ServiceConnection
	{
       //Called when a connection to the Service has been established,
		// with the IBinder of the communication channel to the Service.
		@Override
		public void onServiceConnected(ComponentName cn, IBinder binder) {
			
			downloadImageBinder = (DownloadImageBinder)binder;
			isBounded = true;
			button.setEnabled(true);
		}

		//Called when a connection to the Service has been lost.
		// This typically happens when the process hosting the service has crashed or been killed
		@Override
		public void onServiceDisconnected(ComponentName cn) {
			isBounded = false;
		}};

	
}
