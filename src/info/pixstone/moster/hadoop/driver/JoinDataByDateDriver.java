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
import org.apache.hadoop.contrib.utils.join.*;
import org.apache.hadoop.util.GenericOptionsParser;

import org.apache.log4j.Logger;

public class JoinDataByDateDriver extends Configured implements Tool {

private static Logger logger  =  Logger.getLogger(JoinDataByDateDriver.class);
	
public int run(String[] args) throws Exception {
	Configuration conf = getConf();
	String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	logger.info("otherArgs_length:"+otherArgs.length);
	if (otherArgs.length !=10) {
		  System.err.println("Usage: JoinData <in> <out><-f:FileList><-g:GroupKeyPostion><-d:DataFormat e.g. d:Day y:Year>");
		  System.exit(2);
	}
	 
	JobConf job = new JobConf(getConf(), JoinDataByDateDriver.class);
	job.setJobName("JoinData");
	
	String GroupKeyPos="";
	String FileList="";
	String DateFormat="";
	String DateType="";
	String delimiters="";
	for(int i=0;i<args.length;i++){
		if(args[i].equals("-g"))	GroupKeyPos=(args[i+1]);
		if(args[i].equals("-f"))	FileList=(args[i+1]);
		if(args[i].equals("-d"))	DateType=(args[i+1]);
		if(args[i].equals("-dm"))	delimiters=(args[i+1]);
		}
		
	if(GroupKeyPos.split(",").length!=FileList.split(",").length){
		logger.error("GroupKeyPos' length NOT equal FileList's length!");
		return -1;
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
	
	job.set("GroupKeyPos",GroupKeyPos);
	job.set("DateFormat",DateFormat);
	job.set("delimiters",delimiters);
	job.set("FileList",FileList);
	job.setLong("datajoin.maxNumOfValuesPerGroup",Long.MAX_VALUE);
	
	job.setJarByClass(JoinDataByDateDriver.class);
	job.setMapperClass(JoinDataByDateMapper.class);
	job.setReducerClass(JoinDataReduce.class);
	job.setOutputKeyClass(Text.class);
	job.setOutputValueClass(Text.class);

	job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(TaggedWritable.class);
	
	logger.info("InputFile:"+otherArgs[0]);
	logger.info("OutputFile:"+otherArgs[1]);
	FileInputFormat.setInputPaths(job, new Path(otherArgs[0]));
	FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
	JobClient.runJob(job);
	return 0;
  }
 public static void main(String[] args) throws Exception {
	int res = ToolRunner.run(new Configuration(), new JoinDataByDateDriver(), args);
	System.exit(res);
	}
}