import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class validUserDate {
	// 54991 user-list records, 27348 user records. Average 2 lists for each user
	public static void getDateForValidUser () throws IOException {
		BufferedReader br0 = new BufferedReader(new FileReader("/users/liuyidan/Documents/paper/project/map_ulDate.txt"));
		BufferedReader br1 = new BufferedReader(new FileReader("/users/liuyidan/Documents/paper/project/map_validUser.txt"));
		FileWriter fw0 = new FileWriter("/users/liuyidan/Documents/paper/project/map_validUserDate.txt");
		FileWriter fw1 = new FileWriter("/users/liuyidan/Documents/paper/project/map_validulDate.txt");		
		
		Set<Integer> users = new HashSet<Integer>();
		Map<Integer, ArrayList<Date>> map = new HashMap<Integer, ArrayList<Date>>();
		String tmp = null;
		while((tmp = br1.readLine()) != null) {
			String[] tmps = tmp.split("\t");
			Integer user = Integer.valueOf(tmps[0]);
			users.add(user);
		}
		while((tmp = br0.readLine()) != null) {
			String[] tmps = tmp.split("\t");
			Integer user = Integer.valueOf(tmps[0]);
			if (!users.contains(user))
				continue;
			fw1.write(tmp + System.getProperty("line.separator"));
			String[] dates = tmps[2].split(",");
			ArrayList<Date> dd;
			if (!map.containsKey(user)) {
				dd = new ArrayList<Date>();
			}
			else {
				dd = map.get(user);
			}
			for (String s: dates) {
				dd.add(Date.valueOf(s));
			}
			java.util.Collections.sort(dd);
			map.put(user, dd);
		}
		
		for (Integer user: users) {
			ArrayList<Date> dd = map.get(user);
			fw0.write(user + "\t");
			for (int i = 0; i < dd.size(); i++) {
				fw0.write(dd.get(i).toString());
				if (i != dd.size()-1)
					fw0.write(",");
			}
			fw0.write(System.getProperty("line.separator"));
		}
		fw0.close();
		fw1.close();
		br0.close();
		br1.close();
	}
	
	public static void main(String[] args) throws IOException {
		getDateForValidUser();
	}
}