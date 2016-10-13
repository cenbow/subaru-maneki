package com.subaru.maneki.vo;

import java.util.List;
import java.util.Map;

import com.subaru.maneki.model.Image;

/**
 * @author lee
 * 商品详情页面，将ProductVO的信息转换为模板需要的数据
 */
public class ProductPageVO implements BaseVO{
	
	int 		productId;
	
	boolean		isInWishlist;
	
	double		price;
	
	/**
	 * 计算打折多少
	 */
	int			off;
	
	List<Image>	imageList;
	
	String 		skuPropJson;
	
	/**
	 * sku的json结构
	 * 这是对skuPropJson的解析
	 * 生成的结果表示为：
	 * {'size': ['S', 'L', 'XL',...]}
	 */
	Map<String, List<String>> 		skuMap;
	
	String		name;
	
	int 		soldNum;
	
	String 		score;
	
//	String 		spuPropJson;
	/**
	 * spu属性的描述
	 */
	List<Description> spuDescriptionList;
	
	boolean		isPublished;
	
	/**
	 * 表示该商品所属的品类列表
	 */
	List<Integer> 		categoryIdList;
		
	String 		productNo;
	
	boolean		isAvailable;
	
	String 		shippingTime;
	
	int 		ratingNum;
	
	String 		currency;
	
	int 		freeShippingPrice;
	
	double 		shippingFee;
	
	//String 		cDetails;
	List<PlatformPriceDetail> platformPriceDetailList;
	
	//String 		comment;
	List<Comment>     		commentList;
	
	/**
	 * 这是把所有sku的具体值列表，比如
	 * {1:{size: 'S', color : 'red'}, 2:{size: 'S', color : 'blue'}...}
	 */
	Map<String, List<String>> 	skuValueMap;
	
	String 						skuValueJson;
	
	/**
	 * 购物车的商品数量
	 */
	int 						cartNum;
	
	String 						gaData;
	
	public class Description{
		String 	key;
		String  value;
		
		public Description() {
			// TODO Auto-generated constructor stub
		}
		
		public Description(String key, String value){
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
		
		
	}
	
	/**
	 * @author lee
	 * 对每一个comment的解析
	 */
	public class Comment{
		String 					buyer;
		double 					rating;
		
		List<Map<String, String>>  imageMapList ;
		
		List<String> 			bigImageList;
		
		String  				feedback;
		
		String 				time;
		
		public Comment(){}
		
		public Comment(String buyer, double rating, List<Map<String, String>>  imageMapList, List<String> bigImageList, String feedback, String time){
			this.buyer = buyer;
			this.rating = rating;
			this.imageMapList = imageMapList;
			this.bigImageList = bigImageList;
			this.feedback = feedback;
			this.time = time;
		}

		public String getBuyer() {
			return buyer;
		}

		public void setBuyer(String buyer) {
			this.buyer = buyer;
		}

		public double getRating() {
			return rating;
		}

		public void setRating(double rating) {
			this.rating = rating;
		}

		public List<Map<String, String>> getImageMapList() {
			return imageMapList;
		}

		public void setImageMapList(List<Map<String, String>> imageMapList) {
			this.imageMapList = imageMapList;
		}

		public String getFeedback() {
			return feedback;
		}

		public void setFeedback(String feedback) {
			this.feedback = feedback;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public List<String> getBigImageList() {
			return bigImageList;
		}

		public void setBigImageList(List<String> bigImageList) {
			this.bigImageList = bigImageList;
		}
		
	}
	
	/**
	 * @author lee
	 * 对弹窗中每一行信息的解析
	 */
	public class PlatformPriceDetail{
		
		double basePrice;
		
		double shippingFee;
		
		double totalPrice;
		
		String url;
		
		String platform;
		
		public PlatformPriceDetail(){
			
		}
		
		public PlatformPriceDetail(double basePrice, double shippingFee, double totalPrice, String url, String platform){
			this.basePrice = basePrice;
			this.shippingFee = shippingFee;
			this.totalPrice = totalPrice;
			this.url = url;
			this.platform = platform;
		}

		public double getBasePrice() {
			return basePrice;
		}

		public void setBasePrice(double basePrice) {
			this.basePrice = basePrice;
		}

		public double getShippingFee() {
			return shippingFee;
		}

		public void setShippingFee(double shippingFee) {
			this.shippingFee = shippingFee;
		}

		public double getTotalPrice() {
			return totalPrice;
		}

		public void setTotalPrice(double totalPrice) {
			this.totalPrice = totalPrice;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getPlatform() {
			return platform;
		}

		public void setPlatform(String platform) {
			this.platform = platform;
		}
	}
	
	public void transferPrice(double rate){
		if (Double.compare(rate, 0.0) == 0){
			return;
		}
		this.price = this.price * rate;
		this.shippingFee = this.shippingFee * rate;
		for(PlatformPriceDetail platformPriceDetail: platformPriceDetailList){
			platformPriceDetail.basePrice = platformPriceDetail.basePrice * rate;
			platformPriceDetail.shippingFee = platformPriceDetail.shippingFee * rate;
			platformPriceDetail.totalPrice = platformPriceDetail.totalPrice * rate;
		}
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public boolean getIsInWishlist() {
		return isInWishlist;
	}

	public void setIsInWishlist(boolean isInWishlist) {
		this.isInWishlist = isInWishlist;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getOff() {
		return off;
	}

	public void setOff(int off) {
		this.off = off;
	}

	public List<Image> getImageList() {
		return imageList;
	}

	public void setImageList(List<Image> imageList) {
		this.imageList = imageList;
	}

	public String getSkuPropJson() {
		return skuPropJson;
	}

	public void setSkuPropJson(String skuPropJson) {
		this.skuPropJson = skuPropJson;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSoldNum() {
		return soldNum;
	}

	public void setSoldNum(int soldNum) {
		this.soldNum = soldNum;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public boolean getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(boolean isPublished) {
		this.isPublished = isPublished;
	}

//	public int getCategoryId() {
//		return categoryId;
//	}
//
//	public void setCategoryId(int categoryId) {
//		this.categoryId = categoryId;
//	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String getShippingTime() {
		return shippingTime;
	}

	public void setShippingTime(String shippingTime) {
		this.shippingTime = shippingTime;
	}

	public int getRatingNum() {
		return ratingNum;
	}

	public void setRatingNum(int ratingNum) {
		this.ratingNum = ratingNum;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getFreeShippingPrice() {
		return freeShippingPrice;
	}

	public void setFreeShippingPrice(int freeShippingPrice) {
		this.freeShippingPrice = freeShippingPrice;
	}

	public double getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(double shippingFee) {
		this.shippingFee = shippingFee;
	}

	public List<PlatformPriceDetail> getPlatformPriceDetailList() {
		return platformPriceDetailList;
	}

	public void setPlatformPriceDetailList(List<PlatformPriceDetail> platformPriceDetailList) {
		this.platformPriceDetailList = platformPriceDetailList;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public Map<String, List<String>> getSkuMap() {
		return skuMap;
	}

	public void setSkuMap(Map<String, List<String>> skuMap) {
		this.skuMap = skuMap;
	}

	public List<Description> getSpuDescriptionList() {
		return spuDescriptionList;
	}

	public void setSpuDescriptionList(List<Description> spuDescriptionList) {
		this.spuDescriptionList = spuDescriptionList;
	}

	public List<Integer> getCategoryIdList() {
		return categoryIdList;
	}

	public void setCategoryIdList(List<Integer> categoryIdList) {
		this.categoryIdList = categoryIdList;
	}

	public Map<String, List<String>> getSkuValueMap() {
		return skuValueMap;
	}

	public void setSkuValueMap(Map<String, List<String>> skuValueMap) {
		this.skuValueMap = skuValueMap;
	}

	public String getSkuValueJson() {
		return skuValueJson;
	}

	public void setSkuValueJson(String skuValueJson) {
		this.skuValueJson = skuValueJson;
	}

	public String getGaData() {
		return gaData;
	}

	public void setGaData(String gaData) {
		this.gaData = gaData;
	}

	public int getCartNum() {
		return cartNum;
	}

	public void setCartNum(int cartNum) {
		this.cartNum = cartNum;
	}
	
}
