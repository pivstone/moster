package info.pixstone.moster.hadoop.mapper;

import java.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import info.pixstone.moster.tool.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public  class PointBufferMapper extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, Text>{
	enum PointBufferCounter{
		Buffer_Success,
		Buffer_fail
	}
	 private final static  Log logger  =  LogFactory.getLog(PointBufferMapper.class);		
	 private  int LngPos;
	 private  int LatPos;
	 private  float distance;
	 private String delimiters;
	 public void configure(JobConf job) {
			 LngPos =job.getInt("LngPos",0);
			 LatPos =job.getInt("LatPos",0);
			 distance =job.getFloat("distance",0f);
			 delimiters =job.get("delimiters");
		}
		
	@Override
	public void map(LongWritable key,Text value,OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException{
		String line=value.toString();
		String[] content=line.split(delimiters);
		String lng =content[LngPos];
		String lat =content[LatPos];
		String WKT="Point("+lng+" "+lat+")";
		String result= SpatialTool.Buffer4WGS84(WKT,distance);
		if(result.equals("")){
			reporter.incrCounter(PointBufferCounter.Buffer_fail,1);
			logger.error("缓存分析无结果,WKT为:"+WKT);
		}else{
		
			logger.debug("result"+result);
			output.collect(key,new Text(line+result));
			reporter.incrCounter(PointBufferCounter.Buffer_Success,1);
		}
	} 
}