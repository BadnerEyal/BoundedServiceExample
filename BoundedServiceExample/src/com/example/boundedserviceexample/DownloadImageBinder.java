package com.example.boundedserviceexample;


//Interface 
//�� �� ����� Bind
//���� ����� ��������� ��� �����
public interface DownloadImageBinder 
{
	public void downloadImage(String urlPath, String target, DownloadListener listener); 
	
}
