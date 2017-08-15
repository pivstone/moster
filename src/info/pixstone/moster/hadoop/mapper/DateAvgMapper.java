package info.pixstone.moster.hadoop.mapper;


import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.conf.*;

import java.text.SimpleDateFormat;

import java.io.*;
import java.util.*;
import info.pixstone.moster.tool.StringUtil;
public class DateAvgMapper extends MapReduceBase implements Mapper<LongWritable,Text,Text,Text>{

	private String DateFormat;
	private String delimiters;
	private int KeyPos;
	private int DatePos;

	enum DateAvgMapperCounter{
		ArrayIndexOutOfBounds
	}
	
	@Override
	public void configure(JobConf job)  {
		KeyPos =job.getInt("KeyPos",0);
		DatePos =job.getInt("DatePos",0);
		delimiters=job.get("delimiters");
		DateFormat=job.get("DateFormat");
	}
	
	@Override
	public void map(LongWritable key,Text value,OutputCollector<Text,Text> ouput,Reporter reporter)throws IOException{
				String line=value.toString();
				String[] content=line.split(delimiters);
				String date="";
				try{
						date=content[DatePos];
				}catch(ArrayIndexOutOfBoundsException e){
					reporter.incrCounter(DateAvgMapperCounter.ArrayIndexOutOfBounds,1);
				}
				SimpleDateFormat sdf=new SimpleDateFormat(DateFormat);
				Calendar calendar=StringUtil.String2Date(date);
				String reslut=sdf.format(calendar.getTime());
				String  keyResult=content[KeyPos]+delimiters+reslut;
				ouput.collect(new Text(keyResult),value);
		}

}