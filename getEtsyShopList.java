import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class getEtsyShopList {
	public static void main(String[] args) throws IOException, InterruptedException {
		String start = "https://www.etsy.com/search/shops?page=";
		int page = 1;
		for (; page <= 1250; page++) {
			String src = start + page;
			String des = "";
			getXml ge = new getXml();
			ge.getsource(src, des);
			TimeUnit.SECONDS.sleep(5);
		}
		return;
	}
}