package com.example.live_data;

import com.example.live_data.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.content.Context;
import android.os.StrictMode;
import android.support.v4.content.Loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.util.Log;

public class GolfcourseListActivity extends FragmentActivity
        implements GolfcourseListFragment.Callbacks {

    private boolean mTwoPane;
    private DataModel dataModel;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Create data model and initialize with golf courses
        dataModel = new DataModel(this, "courses.txt");

     	FragmentManager fm = getSupportFragmentManager();

        setContentView(R.layout.activity_golfcourse_list);

        if (findViewById(R.id.golfcourse_detail_container) != null) {
            mTwoPane = true;
            
            GolfcourseDetailFragment df = (GolfcourseDetailFragment) fm.findFragmentByTag("Detail");
            if (df == null) {
	            // Initialize new detail fragment
	            Log.v("myApp", "List Activity: Initialize new detail view");
	            df = new GolfcourseDetailFragment();
	            Bundle args = new Bundle();
	            args.putParcelable("course", new Golfcourse("Welcome to Golf Droid"));
	            df.setArguments(args);
	            fm.beginTransaction().replace(R.id.golfcourse_detail_container, df, "Detail").commit();
            }  
        	else {
        		Log.v("myApp", "List Activity, Use existing Detail Fragment " + df);	
        	}
        }
        
        // Initialize and display the golfcourse list fragment
      
        	GolfcourseListFragment cf = (GolfcourseListFragment) fm.findFragmentByTag("List");
        	if ( cf == null) {
        		cf = new GolfcourseListFragment();
            	Bundle arguments = new Bundle();
            	arguments.putParcelableArrayList("courses", dataModel.getCourses());
            	cf.setArguments(arguments);           	
            	fm.beginTransaction().replace(R.id.golfcourse_list, cf, "List").commit();
        	}
        	else {
        		Log.v("myApp", "List Activity: Use existing List Fragment " + cf);
        	}
    }
   

    private String getId() {
	// TODO Auto-generated method stub
	return null;
}

	@Override
//    public void onItemSelected(String id) {
	public void onItemSelected(Golfcourse c) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("course", c);
            GolfcourseDetailFragment fragment = new GolfcourseDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.golfcourse_detail_container, fragment, "Detail")
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, GolfcourseDetailActivity.class);
            detailIntent.putExtra("course", c);
            startActivity(detailIntent);
        }
    }
		
}

 class DataModel {
	
    private ArrayList<Golfcourse> coursesArray = new ArrayList<Golfcourse>();
	
    // Initializer to read a text file into an array of golfcourse objects    
	public DataModel(Context ctx, String coursesFilename) {
        String line;
        BufferedReader br;
    	String coursesJson = ""; 
    	
        // REad json string from file
        try{
        	br = new BufferedReader(new InputStreamReader(ctx.getResources().getAssets().open("courses-json.txt")));
        	
        	while((line = br.readLine()) != null) {
        		coursesJson += line;
        	}	
        }
        catch (IOException e){
        	Log.v("myapp", e.getMessage());
        }
	
        // Parse the JSON, which contains an array of Golfcourse objects
        Type collectionType = new TypeToken<ArrayList<Golfcourse>>(){}.getType();
        coursesArray = new Gson().fromJson(coursesJson, collectionType);
	}
	
	// Getter to retrieve courses
    public ArrayList<Golfcourse> getCourses() {
    	return coursesArray;     
  }
    
    // Setter to update courses
    public ArrayList<Golfcourse> setCourses(ArrayList<Golfcourse> courses) {
    	this.coursesArray = courses;
    	return coursesArray;
    }
}
