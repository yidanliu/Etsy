import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class parseShopList {
	public static void main(String[] args) throws IOException {
		// shopid, shopdhref, shopname, shopowner
		int page = 1;
		int shopid = 0;
		String str = "e:\\data\\etsy\\shoppage\\";
		FileWriter fw = new FileWriter("e:\\data\\etsy\\shoppage\\shoplist.txt", true);
		for (; page <= 1250; page++) {
			String src = str + page + ".html";
			File toparse = new File(src);
			Document doc = Jsoup.parse(toparse, null);
			Elements shops = doc.select("span.shopname.wrap");
			for (Element shop: shops) {
				Element info = shop.getElementsByTag("a").first();
				String href = info.attr("href");
				String name = info.text();
				fw.write(shopid + "\t" + href + "\t" + name + System.getProperty( "line.separator" ));
			}
		}
		fw.close();
	}
}