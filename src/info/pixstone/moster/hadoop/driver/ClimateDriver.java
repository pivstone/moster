package info.pixstone.moster.hadoop.driver;

import info.pixstone.moster.hadoop.mapper.*;
import info.pixstone.moster.hadoop.reduce.*;

import org.apache.hadoop.mapred.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.lib.IdentityReducer;
import org.apache.hadoop.mapred.lib.ChainMapper;

import org.apache.log4j.Logger;

public class ClimateDriver extends Configured implements Tool{

	private static  Logger logger  =  Logger.getLogger(ClimateDriver.class);
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		logger.info("otherArgs_length:"+otherArgs.length);
		if (otherArgs.length !=2) {
		  System.err.println("Usage: ClimateDriver <in> <out>");
		  System.exit(2);
		}
		 
		JobConf job = new JobConf(getConf(), ClimateDriver.class);
		job.setJobName("ClimateDriver");
	
		job.setJarByClass(ClimateDriver.class);
		job.setMapperClass(GBKMapper.class);
		job.setReducerClass(IdentityReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		//job.setOutputFormat(GBKTextOutputFormat.class);
		logger.info("InputFile:"+otherArgs[0]);
		logger.info("OutputFile:"+otherArgs[1]);
		FileInputFormat.setInputPaths(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		
		int SmallFileGroupKeyPos=1;
		int BigFileGroupKeyPos=1;
		String SmallFileName="site_GBK.csv";
		String TargetColumn="2,3,4";
		String KeepColumn="5,25,26,29";
		String delimiters="/";
		
		job.setInt("SmallFileGroupKeyPos",SmallFileGroupKeyPos);
		job.setInt("BigFileGroupKeyPos",BigFileGroupKeyPos);
		job.set("SmallFileName",SmallFileName);
		job.set("delimiters",delimiters);
		job.set("TargetColumn",TargetColumn);
		job.set("KeepColumn",KeepColumn);
		
		JobConf GBKConf=new JobConf(false);
		ChainMapper.addMapper(job,
														GBKMapper.class,
														LongWritable.class,
														Text.class,
														LongWritable.class,
														Text.class,
														true,
														GBKConf);
														
		JobConf BigJoinSmallConf=new JobConf(false);
		ChainMapper.addMapper(job,	
													BigJoinSmallMapper.class,
													LongWritable.class,
													Text.class,
													LongWritable.class,
													Text.class,
													true,
													BigJoinSmallConf);
													
		JobConf DateMerageConf=new JobConf(false);

		ChainMapper.addMapper(job,	
													DateMergeMapper.class,
													LongWritable.class,
													Text.class,
													LongWritable.class,
													Text.class,
													true,
													DateMerageConf);
													
													
		JobConf DataFilterConf=new JobConf(false);

		ChainMapper.addMapper(job,	
													DataFilterMapper.class,
													LongWritable.class,
													Text.class,
													NullWritable.class,
													Text.class,
													true,
													DataFilterConf);
		JobClient.runJob(job);
		return 0;
		
	}
	public static void main(String[] args) throws Exception {
	int res = ToolRunner.run(new Configuration(), new ClimateDriver(), args);
	System.exit(res);
	}
}