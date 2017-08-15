package info.pixstone.moster.hadoop.driver;

import info.pixstone.moster.hadoop.mapper.*;
import info.pixstone.moster.hadoop.reduce.*;

import org.apache.hadoop.mapred.lib.IdentityReducer;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.*;
import org.apache.hadoop.io.*;

import org.apache.log4j.Logger;


public class XY2PointDriver extends Configured implements Tool{

	private static  Logger logger  =  Logger.getLogger(XY2PointDriver.class);
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		logger.info("otherArgs_length:"+otherArgs.length);
		if (otherArgs.length !=6) {
		  System.err.println("Usage: XY2PointDriver <in> <out> <-x:X pos><-y:Y pos>");
		  System.exit(2);
		}
		 
		int X=0;
		int Y=0;
		String delimiters="";
		for(int i=0;i<otherArgs.length;i++){
			logger.info(otherArgs[i]);
			
			if(otherArgs[i].equals("-x"))
			X=Integer.parseInt(otherArgs[i+1]);
			if(otherArgs[i].equals("-y"))
			Y=Integer.parseInt(otherArgs[i+1]);	
			if(otherArgs[i].equals("-d"))
			delimiters=otherArgs[i+1];
		} 
		JobConf job = new JobConf(getConf(), XY2PointDriver.class);
		job.setJobName("XY2PointDriver");
			
		job.setInt("X",X);
		job.setInt("Y",Y);
		job.set("delimiters",delimiters);
		job.setJarByClass(XY2PointDriver.class);
		job.setMapperClass(XY2PointMapper.class);
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
	int res = ToolRunner.run(new Configuration(), new XY2PointDriver(), args);
	System.exit(res);
	}
}