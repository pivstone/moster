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

public  class SpatialFilterDriver extends Configured implements Tool {

private static  Logger logger  =  Logger.getLogger(SpatialFilterDriver.class);

public int run(String[] args) throws Exception {
	Configuration conf = getConf();
	String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	logger.info("otherArgs_length:"+otherArgs.length);
	if (otherArgs.length !=8) {
	  System.err.println("Usage: SpatialFilter <in> <out><-sp :SpatialPos> <-r :SpatialRelationShip><-m :MaskFileName>");
	  System.exit(2);
	}
	 
	JobConf job = new JobConf(getConf(), SpatialFilterDriver.class);
	job.setJobName("SpatialFilter");
	
	 int SpatialDataPos=0;
	 int SpatialRelationShip=0;
	 String delimiters="";
	 String MaskFileName="";
	for(int i=0;i<args.length;i++){
	logger.info(args[i]);
			if(args[i].equals("-m"))
			{
				MaskFileName=	new Path(args[i+1]).getName();
				}
			if(args[i].equals("-sp"))
			{
				SpatialDataPos=Integer.parseInt(args[i+1]);
				}
			if(args[i].equals("-r"))
			{
				SpatialRelationShip=Integer.parseInt(args[i+1]);
				}
			if(otherArgs[i].equals("-d"))
				delimiters=otherArgs[i+1];
		}
	logger.info("MaskFileName:"+MaskFileName);
	logger.info("TargetSpatialRelationship:"+SpatialRelationShip);
	logger.info("SpatialDataPos:"+SpatialDataPos);

	job.set("MaskFileName",MaskFileName);
	job.set("delimiters",delimiters);
	job.setInt("TargetSpatialRelationship",SpatialRelationShip);
	job.setInt("SpatialDataPos",SpatialDataPos);
	
	job.setJarByClass(SpatialFilterDriver.class);
	job.setMapperClass(SpatialFilterMapper.class);
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
	int res = ToolRunner.run(new Configuration(), new SpatialFilterDriver(), args);
	System.exit(res);
	}
}