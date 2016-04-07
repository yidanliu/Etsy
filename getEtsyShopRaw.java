import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class getEtsyShopRaw {
	public static void getAdmirerPage(String shopname) throws IOException, InterruptedException{
		// https://www.etsy.com/shop/luckykaerufabric/favoriters?page=2
		String href = "https://www.etsy.com/shop/" + shopname + "/favoriters";
		File f = new File("" + shopname + "/favoriters");
		f.mkdirs();
		String save = "" + shopname + "/favoriters/favoriters_0.html";
		getXml ge = new getXml();
		ge.getsource(href, save);
		TimeUnit.SECONDS.sleep(4);
		
		// parse the file
		Document doc = Jsoup.parse(save, null);
		Element pager = doc.getElementById("pager-wrapper");
		if (pager == null)
			return;
		Elements pages = pager.getElementsByTag("li");
		int page_num = pages.size();		// including the current page
		for (int i = 1; i < page_num; i++) {
			String savemore = "" + shopname + "/favoriters/favoriters_" + i + ".html";
			String hrefmore = "https://www.etsy.com/shop/" + shopname + "/favoriters?page=" + Integer.toString(i+1);
			ge.getsource(hrefmore, savemore);
			TimeUnit.SECONDS.sleep(4);
		}
		return;		
	
	}
	public static void getReviewPage(String shopname) throws IOException, InterruptedException {
		// https://www.etsy.com/shop/luckykaerufabric/reviews?page=2
		String href = "https://www.etsy.com/shop/" + shopname + "/reviews";
		File f = new File("" + shopname + "/reviews");
		f.mkdirs();
		String save = "" + shopname + "/reviews/reviews_0.html";
		getXml ge = new getXml();
		ge.getsource(href, save);
		TimeUnit.SECONDS.sleep(4);
		
		// parse the file
		Document doc = Jsoup.parse(save, null);
		Element pager = doc.getElementById("pager-wrapper");
		if (pager == null)
			return;
		Elements pages = pager.getElementsByTag("li");
		int page_num = pages.size();		// including the current page
		for (int i = 1; i < page_num; i++) {
			String savemore = "" + shopname + "/reviews/reviews_" + i + ".html";
			String hrefmore = "https://www.etsy.com/shop/" + shopname + "/reviews?page=" + Integer.toString(i+1);
			ge.getsource(hrefmore, savemore);
			TimeUnit.SECONDS.sleep(4);
		}
		return;
		
	}
	public static void getItemPage(String shopname, String item_href) throws IOException, InterruptedException {
		// first get the first page, then parse it to see how many more pages to go
		// https://www.etsy.com/shop/luckykaerufabric?section_id=5128554&ref=shopsection_leftnav_2
		int num = Integer.valueOf(item_href.charAt(item_href.length()-1));
		String save = "" + num + "_0.html";
		getXml ge = new getXml();
		ge.getsource(item_href, save);
		TimeUnit.SECONDS.sleep(4);
		
		// parse the file 
		Document doc = Jsoup.parse(save, null);
		Element pager = doc.getElementById("pager-wrapper");
		if (pager == null)
			return;
		Elements pages = pager.getElementsByTag("li");
		int page_num = pages.size();		// including the current page
		for (int i = 1; i < page_num; i++) {
			String savemore = "" + num + "_" + i + ".html";
			String hrefmore = item_href + "&page=" + Integer.toString(i+1);
			ge.getsource(hrefmore, savemore);
			TimeUnit.SECONDS.sleep(4);
		}
		return;
	}
	public static void getShopProfile(String shopname) throws IOException, InterruptedException {
		// a shop: id/name
		// items
		// admirers
		// reviews
		
		// https://www.etsy.com/shop/luckykaerufabric
		String href = "https://www.etsy.com/shop/" + shopname;
		String save = "" + shopname + ".html";
		getXml ge = new getXml();
		ge.getsource(href, save);
		TimeUnit.SECONDS.sleep(4);
		
		// parse the html file
		Document doc = Jsoup.parse(save, null);
		
		// id = shop-sections
		Elements section = doc.getElementById("shop-sections").getElementsByTag("li");
		if (section.size() == 1) {
			// if only have one shop-home section
			String item_href = section.get(0).getElementsByTag("a").first().attr("href");
			getItemPage(shopname, item_href);
		}
		else {
			// else, create a new file for sections
			FileWriter fw = new FileWriter("" + "sections.txt");
			for (int i = 1; i < section.size(); i++) {
				Element one = section.get(i);
				String item_href = one.getElementsByTag("a").first().attr("href");
				getItemPage(shopname, item_href);
				fw.write(one.getElementsByTag("a").first().text());
			}
			fw.close();
		}
		
		// review: https://www.etsy.com/shop/luckykaerufabric/reviews
		getReviewPage(shopname);
		
		// admirer: https://www.etsy.com/shop/luckykaerufabric/favoriters
		getAdmirerPage(shopname);

		 
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		BufferedReader br = new BufferedReader(new FileReader(""));
		String tmp;
		while ((tmp=br.readLine()) != null) {
			String shopname = tmp.split("\t")[0];
			getShopProfile(shopname);
		}
		br.close();
	}
}