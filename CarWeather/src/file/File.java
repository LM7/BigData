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
				String[] riga = line.split(",");
				if (riga[0].equals(data)){
					bufferedReader.close();
					return new String[]{riga[1],riga[2]};
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
			bufferedWriter.write(data+","+meteo[0]+","+meteo[1]);

			bufferedWriter.close();
		}
		catch(Exception ex) {
			System.out.println("Error writing to file '"+ fileName + "'");
		}
	}

	private static void deleteLine(String citta,String data){
		try {
			java.io.File temp = java.io.File.createTempFile("preferiti",".txt");
			temp.deleteOnExit();

			BufferedWriter out = new BufferedWriter(new FileWriter(temp,true));
			//PrintWriter pw = new PrintWriter(out,true);
			BufferedReader in = new BufferedReader(new FileReader("citta/"+citta+".txt"));

			//scandisco le righe del file e non considero la riga che voglio eliminare
			String linea = in.readLine();
			while (linea != null) {
				String[] riga = linea.split(",");
				if (!riga[0].equals(data)){
					out.newLine();
					out.write(linea);
				}
				linea = in.readLine();
			}

			in.close();
			out.close();
			//pw.close();


			//java.io.File file = temp;
			//FileOutputStream fos=new FileOutputStream(file);
			java.io.File origine= new java.io.File("citta/"+citta+".txt");
			origine.delete();
			temp.renameTo(origine);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		/*
		String[] results = File.takeFile("Roma", "0315");
		if (results != null){
			System.out.println("Condizioni meteo = "+results[0]);
			System.out.println("Fenomeni = "+results[1]);			
		}
		else {
			File.createFile("Roma", "0315", new String[]{"Bello","BUnoo ok"});
			System.out.println("creato??");
		}
		*/
		File.deleteLine("PROVAPROVA", "0406");
	}

}
