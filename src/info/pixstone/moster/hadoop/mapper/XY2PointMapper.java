package info.pixstone.moster.hadoop.mapper;
import java.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;


public class XY2PointMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text>{
	private int X;
	private int Y;
	private String delimiters;
	@Override
	public void configure(JobConf job){
		X=job.getInt("X",0);
		Y=job.getInt("Y",0);
		delimiters=job.get("delimiters");
	}

	public void map(LongWritable Key,Text value,OutputCollector<Text,Text> output,Reporter reporter)throws IOException{
		String line=value.toString();
		String[] content=line.split(delimiters);
		String Point="Point("+content[X]+" "+content[Y]+")";
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<content.length;i++){
			if(i>1) sb.append(",");
			if(i==X||i==Y) continue;
			sb.append(content[i]);
		}
		sb.append(",");
		sb.append(Point);
		output.collect(new Text(""),new Text(sb.toString()));
	}
}