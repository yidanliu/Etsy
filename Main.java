import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Main {
	
	private static String basePath = "/Users/liuyidan/Documents/paper/thesis/data/etsy";
	
	public static void preProcess() throws IOException {
		Map<String, Integer> mapUserNameUserId = new HashMap<String, Integer>();
		FileWriter shopUser = new FileWriter(basePath + "/shopId_userId.txt");
		// FileWriter 
		int shopNum = 2;
		PreProcess pr = new PreProcess();
		for (int i = 0; i < shopNum; i++) {			
			FileWriter shopItem = new FileWriter(basePath + "/" + Integer.toString(i) + "/itemId_itemTitle_itemLink.txt");
			FileWriter userItem = new FileWriter(basePath + "/" + Integer.toString(i) + "/userId_itemId.txt");
			
			// shop favoriters
			List<Integer> favoriterIds = new ArrayList<Integer>();
			List<String> favoriters = pr.getShopFavoriters(i);
			for (String name : favoriters) {
				if (!mapUserNameUserId.containsKey(name)) {
					mapUserNameUserId.put(name, mapUserNameUserId.size());
				}
				favoriterIds.add(mapUserNameUserId.get(name));
			}
			shopUser.write(Integer.toString(i));
			for (int fav : favoriterIds) {
				shopUser.write(" " + Integer.toString(fav));
			}
			shopUser.write(System.getProperty("line.separator"));
						
			// shop items
			List<ItemProfile> items = pr.getShopItems(i);
			for (ItemProfile item : items) {
				shopItem.write(item.itemId + "\t" + item.itemTitle + "\t" + item.itemLink + System.getProperty("line.separator"));
			}
			shopItem.close();
			
			// user items
			List<ReviewProfile> reviews = pr.getUserItemsReviews(i);
			for (ReviewProfile review : reviews)  {
				String userName = review.userName;
				if (!mapUserNameUserId.containsKey(userName)) {
					mapUserNameUserId.put(userName, mapUserNameUserId.size());
				}
				String userId = Integer.toString(mapUserNameUserId.get(userName));
				userItem.write(userId + "\t" + review.itemId + "\t" + review.itemTitle + "\t" + review.itemLink + System.getProperty("line.separator"));
			}
			userItem.close();
		}
		
		// user Id user Name
		FileWriter userIdUserName = new FileWriter(basePath + "/userName_userId.txt");
		for (String userName : mapUserNameUserId.keySet()) {
			userIdUserName.write(userName + "\t" + mapUserNameUserId.get(userName) + System.getProperty("line.separator"));
		}
		userIdUserName.close();
		shopUser.close();
	}
	
	
	public static void main(String[] args) throws IOException {
		preProcess();
	}
}