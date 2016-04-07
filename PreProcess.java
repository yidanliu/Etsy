import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class ItemProfile {
	String itemId, itemTitle, itemLink;
	ItemProfile(String id, String title, String link) {
		itemId = id;
		itemTitle = title;
		itemLink = link;
	}
}

class ReviewProfile {
	String userName;
	String itemId, itemTitle, itemLink;
	ReviewProfile (String uid, String tid, String title, String link) {
		userName = uid;
		itemId = tid;
		itemTitle = title;
		itemLink = link;
	}
}

public class PreProcess {
	
	private String basePath = "/Users/liuyidan/Documents/paper/thesis/data/etsy";
	
	/**
	 * @throws IOException 
	 * 
	 */
	public List<String> getShopFavoriters(int shopId) throws IOException {
		List<String> favoriters = new ArrayList<String>();
		String path = basePath + "/" + Integer.toString(shopId) + "/favoriters/";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			File favoriterPage = listOfFiles[i];
			if (favoriterPage.isFile()) {
				// parse file
				Document doc = Jsoup.parse(favoriterPage, null);
				Elements favs = doc.getElementsByClass("user-name");
				for (Element fav : favs) {
					if (!fav.getElementsByTag("a").isEmpty()) {
						String userLink = fav.getElementsByTag("a").get(0).attr("href")
								.replace("\t","").replace("\n", "").replace("\r", "");
						String[] ss = userLink.split("/");
						// add user name
						favoriters.add(ss[ss.length-1]);
					}
				}
			}
		}
		return favoriters;
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	public List<ItemProfile> getShopItems(int shopId) throws IOException {
		List<ItemProfile> items = new ArrayList<ItemProfile>();
		String path = basePath + "/" + Integer.toString(shopId);
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			File itemPage = listOfFiles[i];
			if (itemPage.isFile() && itemPage.getName().endsWith("html")) {

				// parse file
				Document doc = Jsoup.parse(itemPage, null);
				Elements listings = doc.getElementsByClass("listing-thumb");
				for (Element listing : listings) {
					String itemLink = listing.attr("href");
					String itemTitle = listing.attr("title");
					String[] ss = itemLink.split("/");
					String itemId = ss[2];
					
					items.add(new ItemProfile(itemId, itemTitle, itemLink));
				}
			}
		}
		return items;
	}
	
	/**
	 * 
	 */
	public List<ReviewProfile> getUserItemsReviews(int shopId) throws IOException {
		List<ReviewProfile> reviews = new ArrayList<ReviewProfile>();
		String path = basePath + "/" + Integer.toString(shopId) + "/reviews/";
		Map<String, HashSet<String>> dedupUserItem = new HashMap<String, HashSet<String>>();

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			File itemPage = listOfFiles[i];
			if (itemPage.isFile() && itemPage.getName().endsWith("html")) {
				// parse file
				Document doc = Jsoup.parse(itemPage, null);
				Elements sales = doc.getElementsByClass("receipt-review");
				for (Element sale : sales) {
					if (sale.getElementsByClass("reviewer-info").first().getElementsByTag("a").isEmpty()) {		// anonymous reviewer
						continue;
					}
					String userLink = sale.getElementsByClass("reviewer-info").first().getElementsByTag("a").first().attr("href");
					String[] ss = userLink.split("/");
					String userId = ss[2];
					
					HashSet<String> set = dedupUserItem.containsKey(userId) ? dedupUserItem.get(userId) : new HashSet<String>();
					
					Elements transactions = sale.getElementsByClass("transaction-title");
					for (Element saleInfo : transactions) {
						String itemLink = saleInfo.getElementsByTag("a").first().attr("href").replace("?ref=shop_review","");
						
						if (set.contains(itemLink)) {
							continue;
						}
						set.add(itemLink);
						
						ss = itemLink.split("/");
						String itemId = ss[2];
						if (itemId.equals("236699788")) {
							System.out.println(itemPage.getName());
							System.out.println(userLink + "," + itemLink);
						}
						String itemTitle = saleInfo.getElementsByTag("a").first().text().replace("\"", "").trim();
						reviews.add(new ReviewProfile(userId, itemId, itemTitle, itemLink));
						
					}
					
					dedupUserItem.put(userId, set);

				}
			}
		}
		return reviews;
	}
	
}