package com.kamalan.chapter10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import cam.kamalan.adapters.CategoryListAdapter;
import cam.kamalan.classes.Category;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	
	private Button btnGetList;
	private ProgressBar progressBar;
	private ListView listView;
	
	private List<Category> categoryList;
	private CategoryListAdapter adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.i(TAG, "Try to create Activity...");
        
        setContentView(R.layout.activity_main);
        
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        
        listView = (ListView) findViewById(R.id.listView);
        listView.setVisibility(View.INVISIBLE);
        
        btnGetList = (Button) findViewById(R.id.btnGetList);
        btnGetList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Hide Button
				btnGetList.setVisibility(View.INVISIBLE);
				
				// Show List
				listView.setVisibility(View.VISIBLE);
				
				// Get Data
				getDataFromInternet();
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	
	
	private void getDataFromInternet() {
		if(getConnectionStatus()) {
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				new MyAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
			else
				new MyAsyncTask().execute();
		}
	}
    
    private boolean getConnectionStatus() {
    	boolean found = false;
    	
    	ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {		    	
	    	found = true;
	    }
	    
	    return found;
    }
    
    /*-----------------------------------------------------------------------------------
	 * This method is responsible for creating another thread in parallel with
	 * main UI thread in order to connect to server and get data. After receiving 
	 * data and doing extraction (from XML or json), it should be displayed in the screen.
	 * -----------------------------------------------------------------------------------*/
	public class MyAsyncTask extends AsyncTask<Void, Void, Boolean> {
        
		@Override
	    protected void onPreExecute() {
			Log.i(TAG, "myAsyncTask is about to start...");
			
			progressBar.setVisibility(View.VISIBLE);
	    }
		
		@Override
		protected Boolean doInBackground(Void... params) {
			boolean status = false;	
			
			categoryList = getLatestKategoriItems();
			// Check categoryList content
			for(Category str: categoryList)
				Log.i(TAG, str.toString());
			
			if(categoryList != null || categoryList.size() <= 0)
				status = true;
				
			return status;
		}

		@Override
	    protected void onPostExecute(Boolean result) {
			Log.i(TAG, "myAsyncTask finished its task.");
			
			progressBar.setVisibility(View.INVISIBLE);
			
			if(result)
				displayData();
	    }
	}

	private List<Category> getLatestKategoriItems() {
		List<Category> list = new ArrayList<Category>();
		String connection = Category.URL;
		
		try {
			URL url = new URL(connection);
			Log.i(TAG, "Try to open: " + connection);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					
			int responseCode = conn.getResponseCode();
			Log.i(TAG, "Response code is: " + responseCode);
			
			if(responseCode != HttpURLConnection.HTTP_OK) {
				Log.e(TAG, "Couldn't open connection in getLatestKategoriItems()");
				return null;
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			if (in != null) {
				StringBuilder strBuilder = new StringBuilder();
				// Read character by character    			
				int ch = 0;
				while ((ch = in.read()) != -1)
					strBuilder.append((char) ch);
				    			
				// get returned message and show it
				String response = strBuilder.toString();
				Log.i("JSON returned by server:", response);
				    			
				// Parse JSON String
				JSONObject jObject = new JSONObject(response);
				JSONArray jObjects = jObject.getJSONArray("kategoriContents");
				for(int i=0; i<jObjects.length(); i++){
					Category category = new Category();
					
					category.setFeatured(jObjects.getJSONObject(i).getString("featured"));
					category.setCategoryDesc(jObjects.getJSONObject(i).getString("kategoriDesc"));
					category.setCategoryId(jObjects.getJSONObject(i).getString("kategoriId"));
					category.setCategoryLogoURL(Category.ROOT + jObjects.getJSONObject(i).getString("kategoriLogoURL"));
					category.setCategoryName(jObjects.getJSONObject(i).getString("kategoriName"));
					
					list.add(category);
				}
			}
			    
			in.close();
						
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return list;
	}
    
    private void displayData() {
    	Log.i(TAG, "Inside display data...");
    	
    	adapter = new CategoryListAdapter(getApplicationContext(), categoryList);
    	listView.setAdapter(adapter);
    }
}
