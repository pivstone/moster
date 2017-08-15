package info.pixstone.moster.hadoop.driver;

import info.pixstone.moster.hadoop.mapper.*;
import info.pixstone.moster.hadoop.reduce.*;

import org.apache.hadoop.mapred.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.*;
import org.apache.hadoop.io.*;

import org.apache.log4j.Logger;

public class DateAvgDriver extends Configured implements Tool{

	private static Logger logger  =  Logger.getLogger(DateAvgDriver.class);
	public int run(String[] args) throws Exception {
	
		Configuration conf = getConf();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		logger.info("otherArgs_length:"+otherArgs.length);
		if (otherArgs.length !=12) {
		  System.err.println("Usage: DateAvgDriver <in> <out><-t：targetComlun><-k：keyPos><-dp：DatePos><-dm：delimiters><-d:DateType>");
		  System.exit(2);
		}
		 
		JobConf job = new JobConf(getConf(), GBKDriver.class);
		job.setJobName("DateAvgDriver");
		String delimiters="";
		String AvgPosParam="";
		String DateType="";
		String DateFormat="";
		int KeyPos=0;
		int DatePos=0;
		for(int i=0;i<otherArgs.length;i++){
			if(otherArgs[i].equals("-t"))	AvgPosParam=otherArgs[i+1];
			if(otherArgs[i].equals("-k"))	KeyPos=Integer.parseInt(otherArgs[i+1]);
			if(otherArgs[i].equals("-dp"))	DatePos=Integer.parseInt(otherArgs[i+1]);
			if(otherArgs[i].equals("-dm"))	delimiters=(otherArgs[i+1]);
			if(otherArgs[i].equals("-d"))	DateType=(otherArgs[i+1]);
			}
		
		if(DateType.equals("s")) DateFormat="yyyy-MM-dd HH:mm:ss";
		if(DateType.equals("m")) DateFormat="yyyy-MM-dd HH:mm";
		if(DateType.equals("h")) DateFormat="yyyy-MM-dd HH";
		if(DateType.equals("d")) DateFormat="yyyy-MM-dd";
		if(DateType.equals("m")) DateFormat="yyyy-MM";
		if(DateType.equals("y")) DateFormat="yyyy";
		else if(DateFormat.equals("")){
			logger.error("DateFormat Error! DateFormat is:"+DateType);
			return -1;
		}
		job.setInt("KeyPos",KeyPos);
		job.setInt("DatePos",DatePos);
		job.set("delimiters",delimiters);
		job.set("AvgPosParam",AvgPosParam);
		job.set("DateFormat",DateFormat);
		
		job.setJarByClass(DateAvgDriver.class);
		job.setMapperClass(DateAvgMapper.class);
		job.setReducerClass(DateAvgReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		logger.info("InputFile:"+otherArgs[0]);
		logger.info("OutputFile:"+otherArgs[1]);
		FileInputFormat.setInputPaths(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
   
		JobClient.runJob(job);
		return 0;
		
	}
	public static void main(String[] args) throws Exception {
	int res = ToolRunner.run(new Configuration(), new DateAvgDriver(), args);
	System.exit(res);
	}
}