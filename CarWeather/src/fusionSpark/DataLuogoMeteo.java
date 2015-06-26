package fusionSpark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import parser.Parser;
import takeMeteo.ilMeteo;

/***
 * Data d'arrivo, luogo d'arrivo, condizioni meteo d'arrivo
 * @author lorenzomartucci
 *
 */

public class DataLuogoMeteo {
	
	public static void datePlaceWeather(File dataset) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(dataset));
		String line = reader.readLine();
		String[] dati = new String[15];
		String[] datiMeteo = new String[2];
		int lap = 0;
		while (line!=null) {
			lap++;
			System.out.println("LAP "+ lap);
			dati = Parser.oneLineToArray(line);
			datiMeteo = ilMeteo.findMeteo(dati[9], dati[6]); //prendo l'array con i 2elementi del meteo
			if (datiMeteo != null) {
				dati[13] = datiMeteo[0]; //metto le condizioni meteo
				dati[14] = datiMeteo[1]; //metto i fenomeni
				//Roba di SPARK
			    /*for (int i = 0; i < dati.length; i++) {
			    	if (i == 14) {
			    		System.out.print(dati[i]);
			    		System.out.println();
			    	}
			    	else {
			    		System.out.print(dati[i]+",");
			    	}
			    }*/
			}
			line = reader.readLine();
		}
		reader.close();
		System.out.println("DONE");
	}
	
	public static void main(String[] args) throws IOException {
		File prova = new File("input.txt");
		DataLuogoMeteo.datePlaceWeather(prova);
	}

}
