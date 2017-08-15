package info.pixstone.moster.hadoop.reduce;

import java.io.*;
import java.util.*;

import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;

import info.pixstone.moster.tool.*;
import info.pixstone.moster.common.algorithm.ann.*;
import info.pixstone.moster.hadoop.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public  class BPAnnReduce extends MapReduceBase implements Reducer<Text,Nerv,Text,Text>{
	private final static  Log logger  =  LogFactory.getLog(BPAnnReduce.class);		
	public int[] outputPos;
	public int[] inputPos;
	public int trainNum;

	private String  delimiters;
		
	@Override
	public void configure(JobConf job){
		String outputParam=job.get("output");
		String inputParam=job.get("input");
		delimiters=job.get("delimiters");
		trainNum=job.getInt("trainNum",10);

		outputPos=StringUtil.String2IntArray(outputParam,",");
		inputPos=StringUtil.String2IntArray(inputParam,",");
	}
	
	private Nerv BPAnn2Nerv(BPAnn bp){
		Nerv nerv=new Nerv();
		nerv.setInputCount(new IntWritable(bp.getInputCount()));
		nerv.setOutputCount(new IntWritable(bp.getOutputCount()));
		nerv.setHiddenCount(new IntWritable(bp.getHiddenCount()));
		nerv.setWeightMartix(new Text(bp.toString()));
		return nerv;
	}
	
	private BPAnn Nerv2BP(Nerv nerv){
		int[] layer={nerv.getInputCount().get(),nerv.getHiddenCount().get(),nerv.getOutputCount().get()};
		BPAnn bp=new BPAnn(layer);
	//	logger.info(nerv.getWeightMartix().toString());
		bp.read(nerv.getWeightMartix().toString());
		return bp;
	}
	
	
	
	public  int sortUtil(int head,int tail,BPAnn[] array){

		double keyValue=array[head].getErr();
		BPAnn key=array[head];
		while(tail>head){
			while(array[tail].getErr()>=keyValue&&tail>head)
				tail--;
			BPAnn obj=array[head];
			array[head]=array[tail];
			array[tail]=obj;
			while(array[head].getErr()<=keyValue&&tail>head)
				head++;
			BPAnn obj1=array[head];
			array[head]=array[tail];
			array[tail]=obj1;		
		}
		array[tail] = key;
		return head;
	}

	public  void sort(int head,int tail,BPAnn[] array){
		if(head>=tail) {
			return ;
		}
	/* 	int key=(int)(Math.random() *(tail-head))+head;
		int temp=array[head];
		array[head]=array[key];
		array[key]=temp; */
		
		int index= sortUtil(0,array.length-1,array);
		sort(head,index-1,array);
		sort(index+1,tail-1,array);
	}

	public  void quickSort(BPAnn[] array){
		sort(0,array.length-1,array);
	}
	
	
	@Override
	public void reduce(Text key,Iterator<Nerv> value,OutputCollector<Text,Text> outputer,Reporter  report)throws  IOException{
		ArrayList<Text> datalist=new ArrayList<Text>();
		ArrayList<BPAnn> annlist=new ArrayList<BPAnn>();
		while(value.hasNext()){
//		double temp={27.05d,118.32d,116.1d,21.0d,15.0,35.0,970.0,350.0,130.0,12.0,315.0};
			Nerv nerv=value.next();
			BPAnn bp=Nerv2BP(nerv);
	//		bp.netOut();
			annlist.add(bp);
			Text data=nerv.getData();
			datalist.add(data);
		}
		for(int i=0;i<trainNum;i++){
			for(int j=0;j<annlist.size();j++){
				BPAnn bp=annlist.get(j);
				for(int k=0;k<datalist.size();k++){
					Text data=datalist.get(k);
					String line=data.toString();
					String[] content=line.split("\r\n");
					for(String temp:content){
						String[] tempdata=temp.split(delimiters);
						double[] output=new double[outputPos.length];
						double[] input=new double[inputPos.length];
						for(int z=0;z<outputPos.length;z++){
							output[z]=StringUtil.RainDrop(tempdata[outputPos[z]]);
						}
						for(int z=0;z<inputPos.length;z++){
							input[z]=StringUtil.String2double(tempdata[inputPos[z]]);
						}
					//logger.info(bp.check());
					
						bp=bp.trian(input,output);
					}
				}
			}
		}
		BPAnn[] template=new BPAnn[0];
		BPAnn[] array=annlist.toArray(template);
		this.quickSort(array);
		BPAnn best=array[array.length-1];
		//Nerv nv=BPAnn2Nerv(best);
		outputer.collect(key,new Text(best.toString()));
	}
}