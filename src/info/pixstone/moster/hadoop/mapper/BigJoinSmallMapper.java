package info.pixstone.moster.hadoop.mapper;

import java.io.*;
import java.util.*;
	
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.filecache.DistributedCache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public  class BigJoinSmallMapper extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, Text>{
	private Path[] localFiles;
	private Path smallFile;
	private final static  Log logger  =  LogFactory.getLog(BigJoinSmallMapper.class);		
	private int SmallFileGroupKeyPos;
	private int BigFileGroupKeyPos;
	private String SmallFileName;
	private String delimiters;
	private Hashtable<String,String> SmallData=new Hashtable<String,String>();
	
	enum BigJoinSmallCounter{
		JOIN_SUCCECC,
		JOIN_FAIL
	}
	public void configure(JobConf job)  {
		String line="";
		SmallFileGroupKeyPos =job.getInt("SmallFileGroupKeyPos",0);
		BigFileGroupKeyPos =job.getInt("BigFileGroupKeyPos",0);
		SmallFileName =job.get("SmallFileName");	
		delimiters =job.get("delimiters");	
		try{
				localFiles = DistributedCache.getLocalCacheFiles(job);
				for(Path temp:localFiles)
					{
						if(temp.getName().equals(SmallFileName))
						{
							smallFile=temp;
							break;
						}
				}
				BufferedReader reader=new BufferedReader(
															new FileReader(smallFile.toString()));
				while((line=reader.readLine())!=null){
					String[] value=line.split(delimiters);
					String key=value[SmallFileGroupKeyPos];
					SmallData.put(key,line);
				}
				reader.close();
		}catch(IOException e){
					logger.error("读取分布式缓存文件失败！"+e.getStackTrace());	
		}
	}
		
	@Override
	public void map(LongWritable key,Text value,OutputCollector<LongWritable, Text> output, Reporter reporter) 
	throws IOException{
		String line=value.toString();
		String[] content=line.split(delimiters);
		String groupkey=content[BigFileGroupKeyPos];
		String joinData="";

		if(SmallData.containsKey(groupkey))
			{	
				joinData=SmallData.get(groupkey);
				output.collect(key,new Text(line+joinData));
				reporter.incrCounter(BigJoinSmallCounter.JOIN_SUCCECC,1);

		}else{	
				output.collect(key,value);
				reporter.incrCounter(BigJoinSmallCounter.JOIN_FAIL,1);
				logger.error("标签关联失败"+groupkey);	
		}
	} 
}
