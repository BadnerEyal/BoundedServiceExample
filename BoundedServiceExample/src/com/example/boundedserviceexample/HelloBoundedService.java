package com.example.boundedserviceexample;



import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class HelloBoundedService extends Service {


	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("HelloIntentService", "onCreate");
	}

	//���� ���� ��� ��� ����� �����
	private class MyBinder extends Binder implements DownloadImageBinder
	{
       
		//implements DownloadImageBinder
		//���� �� ����� ���� ������� ����� �����
		@Override
		public void downloadImage(String urlPath, String target,
				DownloadListener listener) {
			
			_downloadImage(urlPath, target, listener) ;
		}
		
	}
	
	//�� ������ ���� ���� ����� ���� ����� �������
	@Override
	public IBinder onBind(Intent arg0) 
	{
		Binder binder = new MyBinder();
		
		return binder;
	}
	
	/**
	 * The IntentService calls this method from the default worker thread with
	 * the intent that started the service. When this method returns,
	 * IntentService stops the service, as appropriate.
	 */
	
	
	public void _downloadImage(final String urlPath, final String target, final DownloadListener listener) 
	{
		// Normally we would do some work here, like download a file.
		// For our sample, we just sleep for 5 seconds.
		Log.d("HelloIntentService", "onHandleIntent: start");
        //���� ��� ��� ������ ������ �����
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				URL url;
				URLConnection urlConnection;
				InputStream urlInputStream;

				try 
				{
					//����� ����� ������ ����� �����
					url = new URL(urlPath);
					urlConnection = url.openConnection();
					urlInputStream = urlConnection.getInputStream();
					final Bitmap bmp = BitmapFactory.decodeStream(urlInputStream);

					File file = getFileStreamPath(target);
					
					FileOutputStream fOut = new FileOutputStream(file);
					
					bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
					
					fOut.flush();
					fOut.close();
					
					//����� ����� ������ �� ����� ����� ���� ������
					//������ �� ������ �� ������ ������
					//���� ����� �� 
					//���� ������ onDownloadCompleted
					listener.onDownloadCompleted(file.getAbsolutePath());
					
				} 
				catch (Exception e) 
				{
					Log.e(getClass().getName(), "Fail sending message", e);
					e.printStackTrace();
				}
				
				Log.d("HelloIntentService", "onHandleIntent: end");

			}
		}).start();
		
		
		
	}

	@Override
	public void onDestroy() {
		Log.d("HelloIntentService", "onDestroy");
		super.onDestroy();
	}

}