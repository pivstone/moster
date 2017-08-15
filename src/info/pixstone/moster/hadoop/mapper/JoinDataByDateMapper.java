package info.pixstone.moster.hadoop.mapper;

import info.pixstone.moster.hadoop.*;
import info.pixstone.moster.tool.*;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.*;
import org.apache.hadoop.contrib.utils.join.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public  class JoinDataByDateMapper extends DataJoinMapperBase{	

	private String DateFormat;
	private String delimiters;
	private final static Log logger= LogFactory.getLog(JoinDataByDateMapper.class);
	private Hashtable<String,Integer> GroupKeyData=new Hashtable<String,Integer>();
	public void configure(JobConf job)  {
		super.configure(job);
		String GroupKeyPos =job.get("GroupKeyPos");
		String FileList=job.get("FileList");
		delimiters=job.get("delimiters");
		DateFormat=job.get("DateFormat");
		
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
		logger.debug("line:"+line.toString());
		String[] tokens=line.split(delimiters);
		String Tag=record.getTag().toString();

		int groupkeyPos=GroupKeyData.get(Tag);
				logger.debug("groupkeyPos:"+groupkeyPos);
				String groupKey="";
		try{		
			 groupKey=tokens[groupkeyPos];
		}catch(ArrayIndexOutOfBoundsException e){
							logger.info("Tag:"+Tag.toString());
							logger.info("line:"+line.toString());
							}
		SimpleDateFormat sdf=new SimpleDateFormat(DateFormat);
		Calendar calendar=StringUtil.String2Date(groupKey);
		String reslut=sdf.format(calendar.getTime());
						logger.debug("reslut:"+reslut);
		return new Text(reslut);
	}
	protected TaggedMapOutput generateTaggedMapOutput(Object  value){
		TaggedWritable tagw=new TaggedWritable((Text)value);
		tagw.setTag(this.inputTag);
		return tagw;
	}
}