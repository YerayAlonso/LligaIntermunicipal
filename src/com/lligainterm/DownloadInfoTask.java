package com.lligainterm;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class DownloadInfoTask extends AsyncTask<String, Integer, String> {

	private Context cont;
	private String jornada;
	private Fase fase;
	
	// Do the long-running work in here
	protected String doInBackground(String... urls) {
		String myLine = "";
		try {
			myLine = readFromURL(urls[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return myLine;
	}
	
	public void setJornada(String jornada) {
		this.jornada = jornada;
	}
	
	public void setFase(Fase fase) {
		this.fase = fase;
	}

	private String readFromURL(String URLString) throws IOException { 
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(URLString);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("id_temporada", "223"));
		nameValuePairs.add(new BasicNameValuePair("id_esport", "4"));
		nameValuePairs.add(new BasicNameValuePair("id_categoria", "13"));
		nameValuePairs.add(new BasicNameValuePair("grup", fase.grup));
		nameValuePairs.add(new BasicNameValuePair("jornada", jornada));

		post.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] result = null;
		StatusLine statusLine = response.getStatusLine();
        if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
            result = EntityUtils.toByteArray(response.getEntity());
        }
        
        PdfReader reader = new PdfReader(result);
		return PdfTextExtractor.getTextFromPage(reader, 1);
	}
	
	protected void onPreExecute() {
		
    }
	
	// This is called each time you call publishProgress()
	protected void onProgressUpdate(Integer... progress) {

	}

	// This is called when doInBackground() is finished
	protected void onPostExecute(Long result) {
	}

	public Context getContext() {
		return cont;
	}
	
	public void setContext(Context c) {
		cont = c;
	}

}
