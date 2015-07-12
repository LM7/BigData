package hadoop;



import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import parser.Parser;
import takeMeteo.ilMeteo;
import workData.Data;

public class WordCountLuogoMeteoOrariPartArrivo {
	
	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable>{


		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();


		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			StringTokenizer itr1 = new StringTokenizer(value.toString(), "\n");
			while (itr1.hasMoreTokens()) {
				String line = itr1.nextToken();
				
				int orarioPartenza;
				int orarioArrivo;
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
				orarioPartenza = Data.fasciaOraria(arrayLine[1]);
				orarioArrivo = Data.fasciaOraria(arrayLine[6]);
				
				String wordString = arrayLine[4]+" "+datiMeteoPartenza[0]+" Fascia Oraria: "+orarioPartenza+" "+arrayLine[9]+" "+datiMeteoArrivo[0]+" Fascia Oraria: "+orarioArrivo;
				
				word = new Text(wordString);
				context.write(word, one);
			}
		}
	}

	public static class IntSumReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}

	public static void main(String[] args) throws Exception {
		long start = System.nanoTime();
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		Job job = new Job(conf, "word count");
		job.setJarByClass(WordCountDataLuogoMeteoArrivo.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path("NewDatasetMobility.txt"));
		FileSystem fs;
		try {
			fs = FileSystem.get(new Configuration());
			if(fs.exists(new Path("resultsHADOOP"))){
				fs.delete(new Path("resultsHADOOP"),true);
				System.out.println("ELIMINATA");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ERRORE");
			e.printStackTrace();
			System.out.println("ERRORE");
		}
		FileOutputFormat.setOutputPath(job, new Path("resultsHADOOP"));
		if (job.waitForCompletion(true)) {
			System.out.println("DONE");
			long end = System.nanoTime();
			long microseconds = (end - start) / 1000;
			double seconds = (double) microseconds / 1000000;
			double minutes = (double) seconds / 60;
			System.out.println("microsecondi: "+microseconds+" ");
			System.out.println("secondi: "+seconds);
			System.out.println("minuti: "+minutes);
			System.exit(0);
		}
		else {
			System.exit(1);
		}
	}

}
