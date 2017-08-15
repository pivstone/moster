package info.pixstone.moster.hadoop.mapper;

import info.pixstone.moster.hadoop.*;

import java.io.*;
import java.util.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.*;
import org.apache.hadoop.contrib.utils.join.*;

public  class JoinDataMapper extends DataJoinMapperBase{	

	private Hashtable<String,Integer> GroupKeyData=new Hashtable<String,Integer>();
	private String delimiters;
	
	public void configure(JobConf job)  {
		super.configure(job);
		String GroupKeyPos =job.get("GroupKeyPos");
		String FileList=job.get("FileList");
		String delimiters=job.get("delimiters");
		String[] GroupKey=GroupKeyPos.split(",");
		String[] FileNames=FileList.split(",");
		for(int i=0;i<GroupKey.length;i++){
			int groupkey=Integer.parseInt(GroupKey[i]);
			GroupKeyData.put(FileNames[i],groupkey);
		}
	}
	protected Text generateInputTag(String inputFiles){
		Path path=new Path(inputFiles);
		return new Text(path.getName());
	}
	
	protected Text generateGroupKey(TaggedMapOutput record)	{
		String line=((Text)record.getData()).toString();
		String[] tokens=line.split(delimiters);
		String Tag=record.getTag().toString();
		int groupkeyPos=GroupKeyData.get(Tag);
		String groupKey=tokens[groupkeyPos];
		return new Text(groupKey);
	}
	protected TaggedMapOutput generateTaggedMapOutput(Object  value){
		TaggedWritable tagw=new TaggedWritable((Text)value);
		tagw.setTag(this.inputTag);
		return tagw;
	}
}