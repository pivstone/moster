package info.pixstone.moster.hadoop.driver;

import info.pixstone.moster.hadoop.mapper.*;
import info.pixstone.moster.hadoop.reduce.*;
import info.pixstone.moster.tool.SpatialRelationShip;

import org.apache.hadoop.mapred.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.lib.IdentityReducer;
import org.apache.hadoop.mapred.lib.ChainMapper;

import org.apache.log4j.Logger;

public class TyphoonDriver extends Configured implements Tool{

	private static  Logger logger  =  Logger.getLogger(TyphoonDriver.class);
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		logger.info("otherArgs_length:"+otherArgs.length);
		if (otherArgs.length !=2) {
		  System.err.println("Usage: TyphoonDriver <in> <out>");
		  System.exit(2);
		}
		 
		JobConf job = new JobConf(getConf(), TyphoonDriver.class);
		job.setJobName("TyphoonDriver");
				
		job.setJarByClass(TyphoonDriver.class);
		job.setMapperClass(GBKMapper.class);
		job.setReducerClass(IdentityReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		//job.setOutputFormat(GBKTextOutputFormat.class);
		logger.info("InputFile:"+otherArgs[0]);
		logger.info("OutputFile:"+otherArgs[1]);
		FileInputFormat.setInputPaths(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		
		int LngPos=5;
		int LatPos=6;
		int SpatialDataPos=15;
		int TargetSpatialRelationship=SpatialRelationShip.INTERSECTS;
		float distance=450000;
		String delimiters="/";
		String MaskFileName="xian_fjzq_Project.csv";
		String KeepColumn="0,5,6,7,8,9,10,11,12,13,14";
		
		job.setInt("LngPos",LngPos);
		job.setInt("LatPos",LatPos);
		job.setInt("SpatialDataPos",SpatialDataPos);
		job.setInt("TargetSpatialRelationship",TargetSpatialRelationship);
		job.setFloat("distance",distance);
		job.set("delimiters",delimiters);
		job.set("MaskFileName",MaskFileName);
		job.set("KeepColumn",KeepColumn);
		job.set("maperd.job.priority","VERY_HIGH");
		job.setNumMapTasks(52);
		job.setNumReduceTasks(1);
		
		JobConf GBKConf=new JobConf(false);
		ChainMapper.addMapper(job,
														GBKMapper.class,
														LongWritable.class,
														Text.class,
														LongWritable.class,
														Text.class,
														true,
														GBKConf);
														
		JobConf pointBufferConf=new JobConf(false);
		ChainMapper.addMapper(job,
														PointBufferMapper.class,
														LongWritable.class,
														Text.class,
														LongWritable.class,
														Text.class,
														true,
														pointBufferConf);
														
		JobConf  spatialFilterConf=new JobConf(false);
		ChainMapper.addMapper(job,
														SpatialFilterMapper.class,
														LongWritable.class,
														Text.class,
														LongWritable.class,
														Text.class,
														true,
														spatialFilterConf);

		JobConf  dataFilterConf=new JobConf(false);
		ChainMapper.addMapper(job,
														TyphoonFilterMapper.class,
														LongWritable.class,
														Text.class,
														NullWritable.class,
														Text.class,
														true,
														dataFilterConf);
		JobClient.runJob(job);
		return 0;
		
	}
	public static void main(String[] args) throws Exception {
	int res = ToolRunner.run(new Configuration(), new TyphoonDriver(), args);
	System.exit(res);
	}
}