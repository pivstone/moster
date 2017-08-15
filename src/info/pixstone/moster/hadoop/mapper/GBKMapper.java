package info.pixstone.moster.hadoop.mapper;

import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.conf.*;

import java.io.*;

public class GBKMapper extends MapReduceBase implements Mapper<LongWritable,Text,LongWritable,Text>{

	@Override
	public void map(LongWritable key,Text value,OutputCollector<LongWritable,Text> ouput,Reporter reporter)throws IOException{
				Text text=transformTextToUTF8(value,"GBK");
				ouput.collect(key,text);
		}
	public static Text transformTextToUTF8(Text text, String encoding)  {
          try {
               text.set(new String(text.getBytes(), 0, text.getLength(), encoding));
               
          } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
          }
		  return text;
     }
}