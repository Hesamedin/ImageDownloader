package cam.kamalan.classes;


public class Category {

	private String featured;
	private String categoryDesc;
	private String categoryId;
	private String categoryLogoURL;
	private String categoryName;
	
	public static final String ROOT = "http://customer.appxtream.com/astro.apps.cms";
	public static final String URL = ROOT + "/jsonFeed.action?service=astroResepiService&action=grabJsonText&p1=KategoriList&mimeType=application/json";
	
	public String getFeatured() {
		return featured;
	}
	
	public String getCategoryDesc() {
		return categoryDesc;
	}
	
	public String getCategoryId() {
		return categoryId;
	}
	
	public String getCategoryLogoURL() {
		return categoryLogoURL;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	
	public void setFeatured(String featured) {
		this.featured = featured;
	}
	
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	public void setCategoryLogoURL(String categoryLogoURL) {
		this.categoryLogoURL = categoryLogoURL;
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return "Cat Name: " + categoryName + ", Id: " + categoryId;
	}
}
