package com.moisse.others;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class UploadImage extends AsyncTask<String, Void, Void>{

	@Override
	protected Void doInBackground(String... params) {
		String pathImage = params[0];
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"upload_images_parqueo.php");
		File file_image = new File(pathImage);
		try{
			MultipartEntity mpEntity = new MultipartEntity();
			ContentBody cBody = new FileBody(file_image, "image/jpeg");
			mpEntity.addPart("imageUp",cBody);
			httpPost.setEntity(mpEntity);
			HttpResponse response = httpClient.execute(httpPost);
			httpClient.getConnectionManager().shutdown();
			HttpEntity entity = response.getEntity();
			@SuppressWarnings("unused")
			String verify = EntityUtils.toString(entity);
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}

}
