package info.pixstone.moster.hadoop.mapper;

import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.filecache.DistributedCache;

import java.io.*;

import info.pixstone.moster.tool.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SpatialFilterMapper extends MapReduceBase implements Mapper<LongWritable,Text,LongWritable,Text>{
	private final static Log logger= LogFactory.getLog(SpatialFilterMapper.class);
	private Path[] localFiles;
	private Path maskFile;
	private int SpatialDataPos;
	private String maskFileName;
	private String mask;
	private int TargetSpatialRelationship;
	private String delimiters;
	//计数器
	enum SpatialFilterCounter{
		Match_SUCCECC,
		Match_FAIL
	}
	@Override
	public void configure(JobConf job){


		SpatialDataPos=job.getInt("SpatialDataPos",0);
		maskFileName=job.get("MaskFileName");
		delimiters=job.get("delimiters");
		TargetSpatialRelationship=job.getInt("TargetSpatialRelationship",0);
		try{
			localFiles=DistributedCache.getLocalCacheFiles(job);
		}catch(IOException e){
			logger.error(e.getStackTrace());
		}
		for(Path temp:localFiles){
			if(temp.getName().equals(maskFileName)){
				maskFile=temp;
				break;
			}
		}
		String masks=FileUtil.ReadFile(maskFile.toString());
		mask=SpatialTool.WKTMerge(masks);
	}
	@Override
	public void map(LongWritable Key,Text value,OutputCollector<LongWritable,Text> output,Reporter reporter)throws IOException{
			String line=value.toString();
			String[] content =line.split(delimiters);
		try{
			logger.debug("maskFile"+maskFile.toString());
			logger.debug("SpatialData:"+content[SpatialDataPos]);
			int SpatialRelationship=SpatialTool.getSpatialRelationship(content[SpatialDataPos],mask);
			if(SpatialRelationship>=TargetSpatialRelationship){
				reporter.incrCounter(SpatialFilterCounter.Match_SUCCECC,1);
				output.collect(Key,value);
			}else{
				reporter.incrCounter(SpatialFilterCounter.Match_FAIL,1);
			}
		}catch(java.lang.ArrayIndexOutOfBoundsException e){
			logger.error("错误记录:"+line);
			logger.error("错误参数,SpatialDataPos:"+SpatialDataPos);
		}
	}
}