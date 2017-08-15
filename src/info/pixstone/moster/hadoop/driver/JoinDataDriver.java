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

public class JoinDataDriver extends Configured implements Tool {

private static Logger logger  =  Logger.getLogger(JoinDataDriver.class);
	
public int run(String[] args) throws Exception {
	Configuration conf = getConf();
	String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	logger.info("otherArgs_length:"+otherArgs.length);
	if (otherArgs.length !=6) {
		  System.err.println("Usage: JoinData <in> <out><-f:FileList><-g:GroupKeyPostion>");
		  System.exit(2);
	}
	 
	JobConf job = new JobConf(getConf(), JoinDataDriver.class);
	job.setJobName("JoinData");
	
	String GroupKeyPos="";
	String FileList="";
	String delimiters="";
	for(int i=0;i<args.length;i++){
		if(args[i].equals("-g"))
			GroupKeyPos=(args[i+1]);
		if(args[i].equals("-f"))	
			FileList=(args[i+1]);
		if(args[i].equals("-d"))	
			delimiters=(args[i+1]);
	if(GroupKeyPos.split(",").length!=FileList.split(",").length){
		logger.error("GroupKeyPos' length NOT equal FileList's length!");
		return -1;
		}
	}
	job.set("GroupKeyPos",GroupKeyPos);
	job.set("FileList",FileList);
	job.set("delimiters",delimiters);
	job.setLong("datajoin.maxNumOfValuesPerGroup",Long.MAX_VALUE);
	
	job.setJarByClass(JoinDataDriver.class);
	job.setMapperClass(JoinDataMapper.class);
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
	int res = ToolRunner.run(new Configuration(), new JoinDataDriver(), args);
	System.exit(res);
	}
}