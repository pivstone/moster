package info.pixstone.moster.hadoop.driver;

import info.pixstone.moster.hadoop.mapper.*;
import info.pixstone.moster.hadoop.reduce.*;

import org.apache.hadoop.mapred.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.lib.IdentityReducer;

import org.apache.log4j.Logger;

public class GBKDriver extends Configured implements Tool{

	private static  Logger logger  =  Logger.getLogger(GBKDriver.class);
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		logger.info("otherArgs_length:"+otherArgs.length);
		if (otherArgs.length !=2) {
		  System.err.println("Usage: GBKDriver <in> <out>");
		  System.exit(2);
		}
		 
		JobConf job = new JobConf(getConf(), GBKDriver.class);
		job.setJobName("GBKDriver");
				
		job.setJarByClass(GBKDriver.class);
		job.setMapperClass(GBKMapper.class);
		job.setReducerClass(IdentityReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		//job.setOutputFormat(GBKTextOutputFormat.class);
		logger.info("InputFile:"+otherArgs[0]);
		logger.info("OutputFile:"+otherArgs[1]);
		FileInputFormat.setInputPaths(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
   
		JobClient.runJob(job);
		return 0;
		
	}
	public static void main(String[] args) throws Exception {
	int res = ToolRunner.run(new Configuration(), new GBKDriver(), args);
	System.exit(res);
	}
}