package takeMeteo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ilMeteo {
	
	public static String query(String citta,String data){
		citta = citta.replaceAll(" ", "+");
		String data_mese = data.substring(4, 6); 
		String giorno = data.substring(6, 8);
		String mese;
		switch (Integer.parseInt(data_mese)) {
		case 03:
			mese = "Marzo";
			break;
		case 04:
			mese = "Aprile";
			break;
		case 05:
			mese = "Maggio";
			break;
		default:
			mese = "";
		}
		String html = "";
		try{
			URL url = new URL("http://www.ilmeteo.it/portale/archivio-meteo/"+citta+"/2015/"+mese+"/"+giorno);
			System.out.println(url.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("User-agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.124 Safari/537.36");
			BufferedReader read = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = read.readLine();	
			while (line!=null) {
				html += line;
				line = read.readLine();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println("html "+html);
		return html;
	}
	
	//Inutile
	public static String takeTable(String citta,String data){
		String html = ilMeteo.query(citta,data);
		String regex = "<table.*?>(.*?)</table>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(html);
		while(matcher.find()){
			String table = matcher.group();
			System.out.println("table "+table);
		}
		return null;
	}
	
	public static String[] findMeteo(String citta,String data){
		String condizioniMeteo = "";
		String[] fenomeni = new String[]{"Nessuno","Nessuno"};
		String html = ilMeteo.query(citta,data);
		String regex_table = "<table.*?>(.*)</table>";
		Pattern pattern_table = Pattern.compile(regex_table);
		String regex_riga = "<tr.*?>(.*?)</tr>";
		Pattern pattern_riga = Pattern.compile(regex_riga);
		String regex_colonna_fenomeni = "<td.*?>Fenomeni</td>";
		Pattern pattern_colonna_fenomeni = Pattern.compile(regex_colonna_fenomeni);
		String regex_colonna_condizioneMeteo = "<td.*?>Condizione Meteo</td>";
		Pattern pattern_colonna_condizioneMeteo = Pattern.compile(regex_colonna_condizioneMeteo);
		String regex_colonna = "<td.*?>(.*?)</td>";
		Pattern pattern_colonna = Pattern.compile(regex_colonna);
		String regex_image_alt = "<img.*?alt=(.*?) style.*?>";
		Pattern pattern_image_alt = Pattern.compile(regex_image_alt);
		
		Matcher matcher_table = pattern_table.matcher(html);
		while(matcher_table.find()){
			String table = matcher_table.group(1);
			//System.out.println("table "+table);
			Matcher matcher_riga = pattern_riga.matcher(table);
			String riga = "";
			while (matcher_riga.find()) {
				riga = matcher_riga.group(1);

				//Fenomeni
				Matcher matcher_colonna_fenomeni = pattern_colonna_fenomeni.matcher(riga);
				if (matcher_colonna_fenomeni.find()){
					Matcher matcher_colonna = pattern_colonna.matcher(riga);
					if(matcher_colonna.find()) {
						matcher_colonna.group(1);
						matcher_colonna.find();
						String fenomeni_html = matcher_colonna.group(1);						
						Matcher matcher_image_alt = pattern_image_alt.matcher(fenomeni_html);
						if(matcher_image_alt.find())
							fenomeni[0] = matcher_image_alt.group(1).replaceAll("\"", "");
						if(matcher_image_alt.find())
							fenomeni[1] = matcher_image_alt.group(1).replaceAll("\"", "");
					}
				}
				
				
				//Condizione Meteo
				Matcher matcher_colonna_condizioneMeteo = pattern_colonna_condizioneMeteo.matcher(riga);
				if (matcher_colonna_condizioneMeteo.find()){
					Matcher matcher_colonna = pattern_colonna.matcher(riga);
					if(matcher_colonna.find()) {
						matcher_colonna.group(1);
						matcher_colonna.find();
						condizioniMeteo = matcher_colonna.group(1);
					}
				}
				
			}
		}
		System.out.println("Fenomeni = "+fenomeni[0]+" "+fenomeni[1]);
		System.out.println("Condizioni meteo = "+condizioniMeteo);
		System.out.println();
		return fenomeni;
		
	}
	
	public static void main(String[] args) {
		//ilMeteo.takeTable("Roma", "2015031584598");
		
		String[] risposta = ilMeteo.findMeteo("Milano", "20150304");
		System.out.println("risposta = "+risposta[0]+","+risposta[1]);
		
		//String risposta = ilMeteo.findMeteo("Roma", "2015031584598");
		//System.out.println("risposta = "+risposta);
	}


}
