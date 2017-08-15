package info.pixstone.moster.hadoop.driver;

import info.pixstone.moster.hadoop.mapper.*;
import info.pixstone.moster.hadoop.reduce.*;
import info.pixstone.moster.tool.*;

import java.net.URI;
import java.io.*;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.mapred.lib.IdentityReducer;

import org.apache.log4j.Logger;

public  class PointBufferDriver extends Configured implements Tool {

	private static  Logger logger  =  Logger.getLogger(PointBufferDriver.class);
		
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length < 8) {
		  System.err.println("Usage: PointBuffer <in> <out><-d :distance> <-lat :lat Row><-lnt :lnt Row>");
		  System.exit(2);
		}
		 
		JobConf job = new JobConf(getConf(), PointBufferDriver.class);
		job.setJobName("PointBuffer");
		
		int LngPos=0;
		int LatPos=0;
		float distance=0f;
		String delimiters="";
		for(int i=0;i<otherArgs.length;i++){
			if(otherArgs[i].equals("-lat")){
				LatPos=Integer.parseInt(args[i+1]);
				}
			if(otherArgs[i].equals("-lng")){
				LngPos=Integer.parseInt(args[i+1]);
				}
			if(otherArgs[i].equals("-d")){
				distance=Float.parseFloat(args[i+1]);
				}
			if(otherArgs[i].equals("-dm")){
				delimiters=(args[i+1]);
				}
		}
		
	//	JarSender.send(otherArgs,job);
		job.setInt("LatPos", LatPos);
		job.setInt("LngPos", LngPos);
		job.setFloat("distance", distance);
		job.set("delimiters", delimiters);
		
		job.setJarByClass(PointBufferDriver.class);
		job.setMapperClass(PointBufferMapper.class);
		job.setReducerClass(IdentityReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
	   
	   JobClient.runJob(job);
		return 0;
	  }
	  
	 public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new PointBufferDriver(), args);
		System.exit(res);
   }
}