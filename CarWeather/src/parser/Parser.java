package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Parser {
	
	/***
	 * Array di 15 elementi, ha subito i primi 13 elementi, gli altri due saranno quelli relativi al meteo,
	 * da aggiungere in seguito.
	 * @param linea
	 * @return
	 */
	
	public static String[] oneLineToArray(String linea) {
		String[] elementiUtili = new String[13];
		String[] splits = linea.split(",");
		int i;
		int j = 0;
		for(i = 0; i < splits.length; i++) {
			if (i != 1 && i != 7 && i != 13 && i != 16 && i != 17) {
				elementiUtili[j] = splits[i];
				j++;
			}
		}
		return elementiUtili;
	}
	
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
	
	/***
	 * Una lista di array di stringhe ArrayList<String[]>
	 * Ogni elemento della lista corrisponde a una frase, elementi della frase:
	 * 
	 * 0 = Tipo di Veicolo
	 * 1 = Data di Partenza
	 * 2 = Tipo di Strada
	 * 3 = CAP di Partenza
	 * 4 = Città di Partenza
	 * 5 = Provincia di Partenza
	 * 6 = Data di Arrivo
	 * 7 = Tipo di Strada
	 * 8 = CAP di Arrivo
	 * 9 = Città di Arrivo
	 * 10 = Provincia di Arrivo
	 * 11 = Distanza
	 * 12 = Velocità Media
	 * 
	 * Si crea anche un file con i 13 dati che vogliamo (invece dei 18 iniziali)
	 *
	 * @param file
	 * @throws IOException
	 */
	
	public static void fileToRight(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file)); //titoli appena ottenuti
		String line = reader.readLine();
		PrintWriter datiGiusti = new PrintWriter("datiFinaliGiusti.txt", "UTF-8");
		String[] appoggio;
		ArrayList<String[]> datiFinali = new ArrayList<String[]>(); //potrebbe servire
		int i;
		int j;
		while (line!=null) {
			appoggio = new String[13];
			String[] splits = line.split(",");
			j = 0;
			for(i = 0; i < splits.length; i++) {
				if (i != 1 && i != 7 && i != 13 && i != 16 && i != 17) {
					if (i ==  15 ) {
						datiGiusti.print(splits[i]);
						datiGiusti.println();
						appoggio[j] = splits[i];
					}
					else {
						datiGiusti.print(splits[i]+",");
						appoggio[j] = splits[i];
					}
					j++;
				}
			}
			/*for (int k = 0; k < 13; k++) {
				System.out.println(appoggio[k] + " "+k);
			}*/
			datiFinali.add(appoggio);
			line = reader.readLine();
		}
		datiGiusti.close();
		reader.close();
		
		System.out.println("DONE");
		
		/*for (String[] frasi: datiFinali) {
			for (int k = 0; k < 13; k++) {
				System.out.println(frasi[k]);
			}
		}*/
		
	}
	
	
	public static void newDataset(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file)); //titoli appena ottenuti
		String line = reader.readLine();
		PrintWriter datiGiusti = new PrintWriter("NewDatasetMobility.txt", "UTF-8");
		PrintWriter lineeBrutte = new PrintWriter("LineeBrutte.txt", "UTF-8");
		int lap = 0;
		while (line != null) {
			lap++;
			System.out.println("LAP "+lap);
			String[] splits = line.split(",");
			if ( splits[2] == null || splits[2].equals(" ") || splits[2].equals("") ||
					splits[5] == null || splits[5].equals(" ") || splits[5].equals("") ||
					splits[8] == null || splits[8].equals(" ") || splits[8].equals("") ||
					splits[11] == null || splits[11].equals(" ") || splits[11].equals("") ) {
				lineeBrutte.println(lap);
				lineeBrutte.println(line);
			}
			else {
				datiGiusti.println(line);
			}
			line = reader.readLine();
		}
		lineeBrutte.close();
		datiGiusti.close();
		reader.close();
	}
	
	public static void tagliaDataset(File file) throws IOException {
		final int COSTANTE_TAGLIO = 6000000; //12000000 e dopo 6000000
		BufferedReader reader = new BufferedReader(new FileReader(file)); //titoli appena ottenuti
		String line = reader.readLine();
		PrintWriter datiTagliati = new PrintWriter("NewDatasetMobility6.txt", "UTF-8");
		int lap = 1;
		while ( (line != null) && (lap <= COSTANTE_TAGLIO) ) {
			System.out.println("LAP "+lap);
			if (lap <= COSTANTE_TAGLIO) {
				datiTagliati.println(line);
			}
			line = reader.readLine();
			lap++;
		}
		datiTagliati.close();
		reader.close();
	}
	
	public static void main(String[] args) throws IOException {
		//File file = new File("province.txt");
		//File file = new File("DatasetMobility.txt");
		//Parser.fileToProv(file);
		//Parser.fileToRight(file);
		/*String[] parole;
		String text = "3,1,20150302220013,U,00169,Roma,RM,3,2015032221157,U,00043,Ciampino,RM,3,11500,16,13,3";
		parole = Parser.oneLineToArray(text);
		for (int i=0; i <parole.length; i++) {
			System.out.println(i+": "+parole[i]);
		}*/
		File file = new File("NewDatasetMobility.txt");
		Parser.tagliaDataset(file);
	}

}
