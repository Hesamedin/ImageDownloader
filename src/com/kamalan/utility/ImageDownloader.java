package com.kamalan.utility;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.URLConnectionImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;


public class ImageDownloader {

	private final String directoryName = "KAMALAN_COM";
	
	private File cacheDir;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ImageLoaderConfiguration config;
	
	public ImageDownloader(Context context) {
		
		cacheDir = StorageUtils.getOwnCacheDirectory(context, directoryName + "/Cache");
		
		// Get singleton instance of ImageLoader
		imageLoader = ImageLoader.getInstance();
		
		// Create configuration for ImageLoader (all options are optional)
		config = new ImageLoaderConfiguration.Builder(context)
//		            .memoryCacheExtraOptions((int) ScreenManager.getScreenWidth(), (int) ScreenManager.getScreenHeight())
//		            .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75) // Can slow ImageLoader, use it carefully (Better don't use it)
		            .threadPoolSize(5)
		            .threadPriority(Thread.MIN_PRIORITY + 2)
		            .denyCacheImageMultipleSizesInMemory()
		            .offOutOfMemoryHandling()
//		            .memoryCacheSize(4 * 1024 * 1024)
		            .memoryCache(new UsingFreqLimitedMemoryCache(50 * 1024 * 1024))
//		            .memoryCache(new WeakMemoryCache())
		            .discCache(new UnlimitedDiscCache(cacheDir))
//		            .discCacheFileCount(200)
		            .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
		            .imageDownloader(new URLConnectionImageDownloader(5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)
		            .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
		            .enableLogging()
		            .build();
		
		// Initialize ImageLoader with created configuration. Do it once.
		imageLoader.init(config);
		
		// Transform matrix
//		Matrix matrix = new Matrix();
//		matrix.postSkew(-0.2f, -0.2f);
		
		// Creates display image options for custom display task (all options are optional)
		options = new DisplayImageOptions.Builder()
//		           .showStubImage(R.drawable.icon_loading)
//		           .showImageForEmptyUri(R.drawable.icon_noimage)
		           .cacheInMemory()
		           .cacheOnDisc()
		           .imageScaleType(ImageScaleType.POWER_OF_2)
//		           .transform(matrix)
		           .build();
	}
	
	
	public void displayImage(ImageView imageView, String imageURI, final ProgressBar spinner) {
		// Load and display image
		imageLoader.displayImage(imageURI, imageView, options, new ImageLoadingListener() {
		    @Override
		    public void onLoadingStarted() {
		    	if(spinner != null)
		    		spinner.setVisibility(View.VISIBLE);
		    }
		    
		    @Override
		    public void onLoadingFailed(FailReason failReason) {
		    	if(spinner != null)
		    		spinner.setVisibility(View.INVISIBLE);
		    }
		    
		    @Override
		    public void onLoadingComplete(Bitmap loadedImage) {
		    	if(spinner != null)
		    		spinner.setVisibility(View.INVISIBLE);
		    }
		    
		    @Override
		    public void onLoadingCancelled() {
		        // Do nothing
		    }
		});
	}
}
