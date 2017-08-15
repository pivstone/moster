package info.pixstone.moster.hadoop.driver;

import info.pixstone.moster.hadoop.mapper.*;
import info.pixstone.moster.hadoop.reduce.*;

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
import org.apache.hadoop.util.GenericOptionsParser;

import org.apache.log4j.Logger;

public  class DataFilterDriver extends Configured implements Tool {

	private static  Logger logger  =  Logger.getLogger(DataFilterDriver.class);
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length < 2) {
		  System.err.println("Usage: DataFilter <in> <out><-t :KeepColumn>");
		  System.exit(2);
		}
		 
		JobConf job = new JobConf(getConf(), DataFilterDriver.class);
		job.setJobName("DataFilter");
		
		String KeepColumn="";
		String delimiters="";
		for(int i=0;i<otherArgs.length;i++){
			if(args[i].equals("-t")){
				KeepColumn=args[i+1];
			}
			if(args[i].equals("-d")){
				delimiters=args[i+1];
			}
		}
		job.set("KeepColumn", KeepColumn);
		job.set("delimiters", delimiters);
		job.setJarByClass(DataFilterDriver.class);
		job.setMapperClass(DataFilterMapper.class);
		job.setReducerClass(IdentityReducer.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
	   
	   JobClient.runJob(job);
		return 0;
	  }
	  
	 public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new DataFilterDriver(), args);
		System.exit(res);
   }
}