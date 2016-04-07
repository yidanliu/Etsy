import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class getXml {
	public static void getsource (String surl, String desti) throws IOException {
		URL url;
		int responsecode;
		HttpURLConnection urlConnection;
		BufferedReader reader;
		String line;
		long num_line = 1;
		FileWriter fw;
		//FileWriter fw2 = new FileWriter("f:\\workspace\\list\\test_getsource.html");
		try{
			url = new URL(surl);
			urlConnection = (HttpURLConnection)url.openConnection();
			responsecode = urlConnection.getResponseCode();
			fw = new FileWriter(desti);
			if(responsecode == 200) {
				reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));			
				while((line=reader.readLine()) != null) {
					fw.write(line + "\r\n");
					//fw2.write(num_line + "\r\n");
					num_line++;
					//System.out.println(num_line++);
				}
				//fw2.close();
			}
			else{
                System.out.println("Cannot get the source code, responsecode is:£º"+responsecode);
            }
			fw.close();
		}
		catch(Exception e) {
			System.out.println("fail to get the webpage source code: " + e);
		}
		
		
	}
}