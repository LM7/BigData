package meteoCitta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

import takeMeteo.ilMeteo;

public class MeteoCitta {
	
	public static void main(String[] args) throws Exception {
		//BufferedReader reader = new BufferedReader(new FileReader("DatasetMobility.txt"));
		
		File path = new File("DatasetMobility.txt");
		BufferedReader reader = new BufferedReader(
				   new InputStreamReader(
		                      new FileInputStream(path), "UTF8"));
		String line = reader.readLine();
		int i = 0;
		while (line!=null){
			String[] splits = line.split(",");		
			String[] res = ilMeteo.findMeteo(splits[11],splits[8]);
			System.out.println("Num: "+i);
			System.out.println("TEMPO "+res[0]+" CITTA "+splits[11]);
			i++;
			line = reader.readLine();
		}
		
		reader.close();

		System.out.println("DONE");	
	}
	
	/*
	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader("DatasetMobility.txt"));
		String line = reader.readLine();
		int i = 0;
		while (line!=null){
			String[] splits = line.split(",");
			if(Prova.in(splits[11])) {
				String[] res = ilMeteo.findMeteo(splits[11],splits[8]);
				System.out.println("CITTA "+splits[11]+"  TEMPO "+res[0]+" ; numero "+i);
			}
			else {
				System.out.println("CITTA "+splits[11]+" non nuova; numero "+i);
			}
			i++;
			line = reader.readLine();
		}
		
		reader.close();

		System.out.println("DONE");	
	}
	*/

	/*
	public static void main(String[] args) throws Exception {

		BufferedReader reader = new BufferedReader(new FileReader("DatasetMobility.txt"));
		String line = reader.readLine();
		int i = 0;
		while (line!=null){
			String[] splits = line.split(",");
			try{
				String fileName = "citta/"+splits[11]+".txt";
				FileReader fileReader = new FileReader(fileName);
				
				if(Prova.in(splits[11])) {
					throw new Exception();
				}
				System.out.println("CITTA "+splits[11]+" gia c'e'; numero "+i);
			}
			catch(Exception e){
				Prova.add(splits[11]);
				String[] res = ilMeteo.findMeteo(splits[11],splits[8]);
				System.out.println("CITTA "+splits[11]+"  TEMPO "+res[0]+" ; numero "+i);
			}
			i++;
			line = reader.readLine();
		}
		
		reader.close();

		System.out.println("DONE");	
	}
	*/

	private static void add(String string){
		try {
			if (! MeteoCitta.in(string)) {		
				FileWriter fileWriter = new FileWriter("NUOVElorenzo.txt",true);

				BufferedWriter bufferedWriter =new BufferedWriter(fileWriter);

				bufferedWriter.newLine();
				bufferedWriter.write(string);

				bufferedWriter.close();
			}
		}
		catch(Exception ex) {
				System.out.println(ex.getMessage());
		}
		
	}

	private static boolean in(String string) throws Exception {
		FileReader fileReader = new FileReader("NUOVElorenzo.txt");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line = null;
		while((line = bufferedReader.readLine()) != null) {
			if (line.equals(string)){
				bufferedReader.close();
				return true;
			}
		} 
		bufferedReader.close(); 
		return false;
	}

}
