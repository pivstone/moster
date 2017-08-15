package info.pixstone.moster.hadoop.driver;

import info.pixstone.moster.hadoop.mapper.*;
import info.pixstone.moster.hadoop.reduce.*;
import info.pixstone.moster.hadoop.*;
import info.pixstone.moster.tool.*;

import java.io.*;
import java.util.*;
import java.net.URI;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapred.lib.IdentityReducer;

import org.apache.log4j.Logger;

public  class BigJoinSmallDriver extends Configured implements Tool {

private static  Logger logger  =  Logger.getLogger(BigJoinSmallDriver.class);

public int run(String[] args) throws Exception {
	Configuration conf = getConf();
	String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	logger.info("otherArgs_length:"+otherArgs.length);
	if (otherArgs.length !=8) {
	  System.err.println("Usage: BigJoinSmall <in> <out><-sp :SmallFileGroupKeyPos> <-bp :BigFileGroupKeyPos><-sn :SmallFileName>");
	  System.exit(2);
	}
	 
	JobConf job = new JobConf(getConf(), BigJoinSmallDriver.class);
	job.setJobName("BigJoinSmall");
	
	 int SmallFileGroupKeyPos=0;
	 int BigFileGroupKeyPos=0;
	 String SmallFileName="";
	 String delimiters="";
	for(int i=0;i<args.length;i++){
	logger.info(args[i]);
			if(args[i].equals("-sn")){
				SmallFileName=	new Path(args[i+1]).getName();
				}
			if(args[i].equals("-sp")){
				SmallFileGroupKeyPos=Integer.parseInt(args[i+1]);
				}
			if(args[i].equals("-bp")){
				BigFileGroupKeyPos=Integer.parseInt(args[i+1]);
				}
			if(args[i].equals("-dm")){
				delimiters=(args[i+1]);
				}
		}
	logger.info("SmallFileName:"+SmallFileName);
	logger.info("BigFileGroupKeyPos:"+BigFileGroupKeyPos);
	logger.info("SmallFileGroupKeyPos:"+SmallFileGroupKeyPos);

	job.set("delimiters",delimiters);
	job.set("SmallFileName",SmallFileName);
	job.setInt("BigFileGroupKeyPos",BigFileGroupKeyPos);
	job.setInt("SmallFileGroupKeyPos",SmallFileGroupKeyPos);
	
	job.setJarByClass(BigJoinSmallDriver.class);
	job.setMapperClass(BigJoinSmallMapper.class);
	job.setReducerClass(IdentityReducer.class);
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
	int res = ToolRunner.run(new Configuration(), new BigJoinSmallDriver(), args);
	System.exit(res);
	}
}