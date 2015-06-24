package file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class File {

	public static String[] takeFile(String citta,String data){
		String fileName = "citta/"+citta+".txt";
		String line = null;
		try{
		FileReader fileReader = new FileReader(fileName);

		// Always wrap FileReader in BufferedReader.
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		while((line = bufferedReader.readLine()) != null) {
			String[] riga = line.split(" ");
			if (riga[0].equals(data)){
				bufferedReader.close(); 
				return new String[]{riga[1],riga[2],riga[3]};
			}
		}    

		// Always close files.
		bufferedReader.close(); 
		}catch(Exception e){
			e.getMessage();
			return null;
		}
		return null;
	}
	
	public static void createFile(String citta,String data,String[] meteo){
		String fileName = "citta/"+citta+".txt";

        try {
            FileWriter fileWriter = new FileWriter(fileName,true);
            
            BufferedWriter bufferedWriter =new BufferedWriter(fileWriter);

            bufferedWriter.newLine();
            bufferedWriter.write(data+" "+meteo[0]+" "+meteo[1]+" "+meteo[2]);

            bufferedWriter.close();
        }
        catch(Exception ex) {
            System.out.println("Error writing to file '"+ fileName + "'");
        }
	}
	
	public static void main(String[] args) {
		String[] results = File.takeFile("Roma", "0315");
		if (results != null){
			System.out.println("Fenomeni = "+results[0]+" "+results[1]);
			System.out.println("Condizioni meteo = "+results[2]);
		}
		else {
			File.createFile("Roma", "0315", new String[]{"Bello","BUnoo","ok"});
			System.out.println("creato??");
		}
	}

}
