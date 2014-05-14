package com.example.boundedserviceexample;


//Interface 
//כל מי שיעשה Bind
//יוכל לקרוא לפונקציות אלו מתוכו
public interface DownloadImageBinder 
{
	public void downloadImage(String urlPath, String target, DownloadListener listener); 
	
}
