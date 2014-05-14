package com.example.boundedserviceexample;

//בשביל קריאה חזרה למאזין שגמרנו להוריד את התמונה והיא נשמרה
//המתודה מקבלת מחרוזת
//של כתובת התמונה איפה ששמרנו אותה
public interface DownloadListener {

	public void onDownloadCompleted(String absPath);
	
}
