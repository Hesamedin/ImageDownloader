package cam.kamalan.adapters;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cam.kamalan.classes.Category;

import com.kamalan.chapter10.R;
import com.kamalan.utility.ImageDownloader;

public class CategoryListAdapter extends BaseAdapter {

	private final String TAG = "*** CategoryListAdapter ***";
	private final int[] colors = new int[] { 0xFFE5E5E5, 0xFFFFFFFF };
	
	private LayoutInflater myInflater;
	private List<Category> categoryList;
	private ImageDownloader idLogo;
	
	public CategoryListAdapter(Context context, List<Category> categoryList) {
		myInflater = LayoutInflater.from(context);
				
		this.categoryList = categoryList;
		
		// Get singleton instance of ImageLoader
		idLogo = new ImageDownloader(context);
				
		Log.i(TAG, "Adapter setuped successfully.");
	}	
	

	@Override
	public int getCount() {
		return categoryList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;        
		
		if (convertView == null) {
        	convertView = myInflater.inflate(R.layout.list_category, null);
        	holder 		= new ViewHolder();
        	
        	holder.rlContainer = (RelativeLayout) 	convertView.findViewById(R.id.rlContainer);
			holder.imageView   = (ImageView) 		convertView.findViewById(R.id.ivLogo);
			holder.tvtitle     = (TextView)  		convertView.findViewById(R.id.tvTitle);
			
			convertView.setTag(holder);
        } else {
        	holder = (ViewHolder) convertView.getTag();
        }
		
		int colorPos = position % colors.length;
		holder.rlContainer.setBackgroundColor(colors[colorPos]);
		
		holder.tvtitle.setText(categoryList.get(position).getCategoryName());

		idLogo.displayImage(holder.imageView, categoryList.get(position).getCategoryLogoURL(), null);
		
		return convertView;
	}

	
	static class ViewHolder {
		RelativeLayout rlContainer;
		ImageView imageView;
		TextView  tvtitle;
	}
}
