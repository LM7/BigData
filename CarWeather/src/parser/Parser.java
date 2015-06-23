package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Parser {
	
	public static void fileToProv(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file)); //titoli appena ottenuti
		String line = reader.readLine();
		PrintWriter tutteProv = new PrintWriter("tutteprovince.txt", "UTF-8");
		String andProv = "";
		String ritProv = "";
		ArrayList<String> listaProv = new ArrayList<String>();
		int k = 1;
		while (line!=null) {
			System.out.println("LAP "+k);
			String[] splits = line.split(",");
			/*for (int i = 0; i<splits.length; i++) {
				System.out.println(splits[i]);
			}*/
			andProv = splits[5];
		    ritProv = splits[11];
		    if ( ! (listaProv.contains(andProv)) ) {
		    	listaProv.add(andProv);
		    	tutteProv.println(andProv);
		    }
		    if ( ! (listaProv.contains(ritProv)) ) {
		    	listaProv.add(ritProv);
		    	tutteProv.println(ritProv);
		    }
		    line = reader.readLine();
		    k++;
		}
		reader.close();
		tutteProv.close();
		System.out.println("DONE");
		System.out.println("NUMERO PROVINCE "+listaProv.size());
	}
	
	public static void main(String[] args) throws IOException {
		//File file = new File("province.txt");
		File file = new File("DatasetMobility.txt");
		Parser.fileToProv(file);
	}

}
