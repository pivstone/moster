package info.pixstone.moster.hadoop.reduce;
/**
*
*按照日期 进行记录求均值
*
*/
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapred.*;
import java.io.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.MathContext;
import info.pixstone.moster.tool.StringUtil;

public class DateAvgReducer extends MapReduceBase implements Reducer<Text,Text,NullWritable,Text>{


	private String delimiters;

	private int KeyPos;
	private int[] AvgPos;
	
	
	@Override
	public void configure(JobConf job){
		delimiters=job.get("delimiters");
		String AvgPosParam=job.get("AvgPosParam");
		KeyPos=job.getInt("KeyPos",0);
		AvgPos=StringUtil.String2IntArray(AvgPosParam,",");
	}
	
	@Override
	public void reduce(Text key,Iterator<Text> values,OutputCollector<NullWritable,Text> output,Reporter report)throws IOException{
		BigDecimal[] total=new BigDecimal[AvgPos.length];
		int z=0;
		StringBuffer result=new StringBuffer();
		while(values.hasNext()){
			z++;
			Text value=values.next();
			String line=value.toString();
			String[] contents=line.split(delimiters);
			for(int i=0;i<AvgPos.length;i++){
				double temp=StringUtil.String2double(contents[AvgPos[i]]);
				if(total[i]==null)
					total[i]=new  BigDecimal(0d);
				total[i]=total[i].add(new BigDecimal(temp));
			}
		}
		for(int i=0;i<AvgPos.length;i++){
			total[i]=total[i].divide(new BigDecimal (z),MathContext.DECIMAL32);
			result.append(total[i].toString());
			result.append(delimiters);
		}
		result.append(key.toString());
		output.collect(NullWritable.get(),new Text(result.toString()));
	}
}