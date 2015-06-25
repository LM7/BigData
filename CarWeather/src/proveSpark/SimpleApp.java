package proveSpark;

import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;

public class SimpleApp {
	@SuppressWarnings({ "serial", "resource" })
	public static void main(String[] args) {
		String logFile = "/home/sis/Spark/README.md"; // Settare il path del file di input
		SparkConf conf = new SparkConf().setAppName("Simple Application").setMaster("local[*]");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> logData = sc.textFile(logFile).cache();
		long numAs = logData.filter(new Function<String, Boolean>() {
			public Boolean call(String s) { return s.contains("a"); }
		}).count();
		long numBs = logData.filter(new Function<String, Boolean>() {
			public Boolean call(String s) { return s.contains("b"); }
		}).count();
		long numCs = logData.filter(new Function<String, Boolean>() {
			public Boolean call(String s) { return s.contains("c"); }
		}).count();
		long numDs = logData.filter(new Function<String, Boolean>() {
			public Boolean call(String s) { return s.contains("d"); }
		}).count();
		long numEs = logData.filter(new Function<String, Boolean>() {
			public Boolean call(String s) { return s.contains("e"); }
		}).count();
		System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs + ", lines with c: " + numCs + ", lines with d: " + numDs + ", lines with e: " + numEs);
	}
}
