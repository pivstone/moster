package info.pixstone.moster.hadoop.driver;

import info.pixstone.moster.hadoop.mapper.*;
import info.pixstone.moster.hadoop.reduce.*;
import info.pixstone.moster.hadoop.*;

import org.apache.hadoop.mapred.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.lib.IdentityReducer;

import org.apache.log4j.Logger;

public class BPAnnDriver extends Configured implements Tool{

	private static  Logger logger  =  Logger.getLogger(BPAnnDriver.class);
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		logger.info("otherArgs_length:"+otherArgs.length);
		if (otherArgs.length !=22) {
		  System.err.println("Usage: BPAnnDriver <in> <out>");
		  System.exit(2);
		}
		 
		int KeyPos=0;
		int Epochs=0;
		int PopulationsSize=0;
		int MapNum=1;
		int ReduceNum=1;
		int trianNum=10;
		float LengthRate=0.01f;
		String output="";
		String input="";
		String KeyPosParam="";
		String delimiters="";
		for(int i=0;i<otherArgs.length;i++){
			if(otherArgs[i].equals("-d")){
				delimiters=otherArgs[i+1];
			}if(otherArgs[i].equals("-i")){
				input=otherArgs[i+1];
			}if(otherArgs[i].equals("-t")){
				output=otherArgs[i+1];
			}if(otherArgs[i].equals("-k")){
				KeyPosParam=otherArgs[i+1];
			}if(otherArgs[i].equals("-p")){
				PopulationsSize=Integer.parseInt(otherArgs[i+1]);
			}if(otherArgs[i].equals("-e")){
				Epochs=Integer.parseInt(otherArgs[i+1]);
			}if(otherArgs[i].equals("-m")){
				MapNum=Integer.parseInt(otherArgs[i+1]);
			}if(otherArgs[i].equals("-r")){
				ReduceNum=Integer.parseInt(otherArgs[i+1]);
			}if(otherArgs[i].equals("-tr")){
				trianNum=Integer.parseInt(otherArgs[i+1]);
			}if(otherArgs[i].equals("-lr")){
				LengthRate=Float.parseFloat(otherArgs[i+1]);
				}
		}
		KeyPos=Integer.parseInt(KeyPosParam);
		JobConf job = new JobConf(getConf(), GBKDriver.class);
		job.setJobName("GABP");
		job.setInt("KeyPos",KeyPos);
		job.setInt("Epochs",Epochs);
		job.setInt("trianNum",trianNum);
		job.setInt("PopulationsSize",PopulationsSize);
		job.setFloat("LengthRate",LengthRate);
		job.set("input",input);	
		job.set("output",output);		
		job.set("delimiters",delimiters);		
		job.setJarByClass(BPAnnDriver.class);
		job.setMapperClass(BpAnnMapper.class);
		job.setReducerClass(BPAnnReduce.class);
		job.setCombinerClass(BPAnnCombine.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Nerv.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setNumMapTasks(MapNum);
		job.setNumReduceTasks(ReduceNum);
		//job.setOutputFormat(GBKTextOutputFormat.class);
		logger.info("InputFile:"+otherArgs[0]);
		logger.info("OutputFile:"+otherArgs[1]);
		FileInputFormat.setInputPaths(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
   
		JobClient.runJob(job);
		return 0;
		
	}
	public static void main(String[] args) throws Exception {
	
	int res = ToolRunner.run(new Configuration(), new BPAnnDriver(), args);
	System.exit(res);
	}
}