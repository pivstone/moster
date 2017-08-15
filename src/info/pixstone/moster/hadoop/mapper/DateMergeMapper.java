package info.pixstone.moster.hadoop.mapper;
import java.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;


public class DateMergeMapper extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, Text>{
		 private  String targetColumn;	 
		 private  String delimiters;	 
		 public void configure(JobConf job) {
				 targetColumn =job.get("TargetColumn");
				 delimiters =job.get("delimiters");
			}
			
		@Override
		public void map(LongWritable key,Text value,OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException{
			StringBuffer sb=new StringBuffer();
			String line=value.toString();
			String[] content=line.split(delimiters);
			String[] ColumnNum=targetColumn.split(",");
			int[] Column=new int[ColumnNum.length];
			for(int i=0;i<ColumnNum.length;i++){
				if(i>0)sb.append("-");
				Column[i]=Integer.parseInt(ColumnNum[i]);
				sb.append(content[Column[i]]);
			}
			output.collect(key,new Text(line+sb.toString()));
		} 
	}