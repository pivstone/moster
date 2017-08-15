package info.pixstone.moster.hadoop.reduce;

import info.pixstone.moster.hadoop.*;

import java.io.*;

import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.contrib.utils.join.*;
	
public  class JoinDataReduce extends DataJoinReducerBase {
	private  String delimiters;	 
	@Override
	 public void configure(JobConf job) {
				super.configure(job);
				 delimiters =job.get("delimiters");
			}
	protected TaggedMapOutput combine(Object[] tags,Object [] values){
		if(tags.length<2) return null;
		StringBuffer joinedStr=new StringBuffer();
		for(int i=0;i<values.length;i++){
			if(i>0) joinedStr.append(delimiters);
			TaggedWritable tw=(TaggedWritable)values[i];
			String line=((Text)tw.getData()).toString();
			joinedStr.append(line);
		}
		TaggedWritable tw=new TaggedWritable(new Text(joinedStr.toString()));
		tw.setTag((Text)tags[0]);
		return tw;
	}
}