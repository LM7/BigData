package hadoop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import parser.Parser;
import takeMeteo.ilMeteo;


public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	
	private static final IntWritable one = new IntWritable(1);
	private Text word = new Text();
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		StringTokenizer itr1 = new StringTokenizer(value.toString(), "\n");
		while (itr1.hasMoreTokens()) {
			String line = itr1.nextToken();
			String[] arrayLine = Parser.oneLineToArray(line);
			String[] datiMeteo = ilMeteo.findMeteo(arrayLine[9], arrayLine[6]);
			if (datiMeteo == null || datiMeteo[0].equals("") ) {
				datiMeteo = new String[1];
				datiMeteo[0] = "NienteMeteo";
			}
			String data = arrayLine[6].substring(0, 4)+"-"+arrayLine[6].substring(4, 6)+"-"+arrayLine[6].substring(6, 8);
			String wordString = data +" "+arrayLine[9]+" "+datiMeteo[0];
			word = new Text(wordString);
			context.write(word, one);
		}
	}

}
