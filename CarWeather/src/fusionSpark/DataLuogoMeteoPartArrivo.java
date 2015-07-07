package fusionSpark;

import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import parser.Parser;
import scala.Tuple2;
import takeMeteo.ilMeteo;

/***
 * Data, Luogo e Condizioni Meteo sia di partenza sia di arrivo
 * @author lorenzomartucci
 *
 */
public class DataLuogoMeteoPartArrivo {
	
	@SuppressWarnings({ "resource", "serial" })
	public static void main(String[] args) {
		long start = System.nanoTime();
		Logger.getLogger("org").setLevel(Level.OFF);
		String logFile = "NewDatasetMobility6.txt"; // Settare il path del file di input
		SparkConf conf = new SparkConf().setAppName("Word Count Application").setMaster("local[*]");
		JavaSparkContext spark = new JavaSparkContext(conf);
		JavaRDD<String> textFile = spark.textFile(logFile);

		JavaRDD<String> words = textFile.flatMap(new FlatMapFunction<String, String>() {
			int i=0;
			public Iterable<String> call(String line) { 
				i++;
				System.out.println("LAP "+i);
				String[] arrayLine = Parser.oneLineToArray(line);
				String[] datiMeteoPartenza = ilMeteo.findMeteo(arrayLine[4], arrayLine[1]);
				
				if (datiMeteoPartenza == null || datiMeteoPartenza.equals("")) {
					datiMeteoPartenza = new String[1];
					datiMeteoPartenza[0] = "NienteMeteoPartenza";
				}
				String[] datiMeteoArrivo = ilMeteo.findMeteo(arrayLine[9], arrayLine[6]);
				
				if (datiMeteoArrivo == null || datiMeteoArrivo.equals("") ) {
					datiMeteoArrivo = new String[1];
					datiMeteoArrivo[0] = "NienteMeteoArrivo";
				}
				
				String dataPartenza = arrayLine[1].substring(0, 4)+"-"+arrayLine[1].substring(4, 6)+"-"+arrayLine[1].substring(6, 8);
				String dataArrivo = arrayLine[6].substring(0, 4)+"-"+arrayLine[6].substring(4, 6)+"-"+arrayLine[6].substring(6, 8);
				String key = dataPartenza +" "+arrayLine[4]+" "+datiMeteoPartenza[0]+" "+dataArrivo+" "+arrayLine[9]+" "+datiMeteoArrivo[0];
				
				ArrayList<String> al = new ArrayList<String>();
				al.add(key);
				return al;
			}
		});
		System.out.println("STAMPA UNO: "+words.first());
		
		

				
		
		JavaPairRDD<String, Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
			public Tuple2<String, Integer> call(String s) { 
				return new Tuple2<String, Integer>(s, 1); }
		});
		
		
		
		System.out.println("STAMPA DUE: "+pairs.first().toString());
		JavaPairRDD<String, Integer> counts = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
			public Integer call(Integer a, Integer b) { return a + b; } 
		});
		System.out.println("STAMPA TRE: "+counts.first().toString());
		//counts.saveAsObjectFile("outputProva");
		FileSystem fs;
		try {
			fs = FileSystem.get(new Configuration());
			if(fs.exists(new Path("output"))){
				fs.delete(new Path("output"),true);
				System.out.println("ELIMINATA");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ERRORE");
			e.printStackTrace();
			System.out.println("ERRORE");
		}
		
		long end = System.nanoTime();
		long microseconds = (end - start) / 1000;
		double seconds = (double) microseconds / 1000000;
		double minutes = (double) seconds / 60;
		System.out.println("microsecondi: "+microseconds);
		System.out.println("secondi: "+seconds);
		System.out.println("minuti: "+minutes);
		
		
		counts.saveAsTextFile("output"); // Settare il path dei file di output
	}

}
