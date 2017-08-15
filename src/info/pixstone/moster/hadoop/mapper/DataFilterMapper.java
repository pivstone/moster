package info.pixstone.moster.hadoop.mapper;
import java.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;


public class DataFilterMapper extends MapReduceBase implements Mapper<LongWritable, Text, NullWritable, Text>
	{
		 private  String KeepColumn;	 
		 private  String delimiters;	 
		 public void configure(JobConf job) {
				 KeepColumn =job.get("KeepColumn");
				 delimiters =job.get("delimiters");
			}
			
		@Override
		public void map(LongWritable key,Text value,OutputCollector<NullWritable, Text> output, Reporter reporter) 
		throws IOException{
			StringBuffer sb=new StringBuffer();
			String line=value.toString();
			String[] content=line.split(delimiters);
			String[] ColumnNum=KeepColumn.split(",");
			int[] Column=new int[ColumnNum.length];
			for(int i=0;i<ColumnNum.length;i++)
			{	
				
				Column[i]=Integer.parseInt(ColumnNum[i]);
				sb.append(content[Column[i]]+delimiters);
			}
			output.collect(NullWritable.get(),new Text(sb.toString()));
		} 
	}