package com.example.live_data;

import android.content.Context;
import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import android.support.v4.content.AsyncTaskLoader;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.util.Log;

public class LiveDataLoader extends AsyncTaskLoader<ArrayList<Golfcourse>> {
	
	private String localUrl = null;
	
public LiveDataLoader(Context ctx, String url){
	 super(ctx);
	 localUrl = url;
	}

@Override
public ArrayList<Golfcourse> loadInBackground() {
	ArrayList<Golfcourse> courses = new ArrayList<Golfcourse>();
	Log.v("myapp", "Loader, loadInBackground callback started");
	courses = getLiveDataFromUrl(localUrl);
	return courses;
}

public ArrayList<Golfcourse> getLiveDataFromUrl(String url)
{
	String htmlGet = "";
	String coursesJson = "";
	ArrayList<Golfcourse> coursesArray = null;
	
	// Read live data fromt from server and save to rawString

	try {
		  URL myServer = new URL(url);
		  HttpURLConnection con = (HttpURLConnection) myServer.openConnection();
		  htmlGet = readStream(con.getInputStream());
		  } 
	catch (Exception e) {
		  e.printStackTrace();
		}

    // Strip the html tags (special case when getting json from html server
    coursesJson = htmlGet.replaceAll(".*?<body>(.*?)</body>.*", "$1");
	// Parse the JSON, which contains an array of Golfcourse objects
    Type collectionType = new TypeToken<ArrayList<Golfcourse>>(){}.getType();
    coursesArray = new Gson().fromJson(coursesJson, collectionType);
	return coursesArray; 
}

private String readStream(InputStream in) {
	  BufferedReader reader = null;
	  String htmlGet = "";
	  try {
	    reader = new BufferedReader(new InputStreamReader(in));
	    String line = "";
	    while ((line = reader.readLine()) != null) {
	      htmlGet += line;
	    }
	  } 
	  catch (IOException e) {
	    e.printStackTrace();
	  } 
	  finally {
	    if (reader != null) {
	      try {
	        reader.close();
	      } 
	      catch (IOException e) {
	        e.printStackTrace();
	        }
	    }
	  }
	  return htmlGet;
	}
}