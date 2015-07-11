package hadoop;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

public class WordCountDataLuogoMeteoArrivo {

	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable>{


		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();


		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
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
		FileInputFormat.addInputPath(job, new Path("NewDatasetMobility6.txt"));
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
