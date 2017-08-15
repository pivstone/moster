package info.pixstone.moster.hadoop.mapper;

import java.io.*;
import java.util.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

import info.pixstone.moster.hadoop.*;
import info.pixstone.moster.tool.*;
import info.pixstone.moster.common.algorithm.genetic.*;
import info.pixstone.moster.common.algorithm.ann.*;

public class BpAnnMapper extends MapReduceBase implements Mapper<LongWritable,Text,Text,Nerv>{

	private int KeyPos;
	private int[] outputPos;
	private int[] inputPos;
	private  String delimiters;
	private int PopulationsSize;
	private int Epochs;
	public float length_rate=0f;
	@Override
	public void configure(JobConf job){
		KeyPos=job.getInt("KeyPos",0);
		String outputParam=job.get("output");
		delimiters=job.get("delimiters");
		String inputParam=job.get("input");
		PopulationsSize=job.getInt("PopulationsSize",100);
		Epochs=job.getInt("Epochs",100);
		length_rate=job.getFloat("LengthRate",0.01f);
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
	@Override
	public void map(LongWritable key,Text value,OutputCollector<Text,Nerv> outputer,Reporter report)throws IOException{
		String line=value.toString();
		String[] content=line.split(delimiters);
		String groupkey=content[KeyPos];
		double[] output=new double[outputPos.length];
		double[] input=new double[inputPos.length];
		for(int i=0;i<outputPos.length;i++){
			output[i]=StringUtil.RainDrop(content[outputPos[i]]);
		}
		for(int i=0;i<inputPos.length;i++){
			input[i]=StringUtil.String2double(content[inputPos[i]]);
		}
		int hidden=(int)((outputPos.length+inputPos.length)*1.5);
		int[] layer=new int[]{inputPos.length,hidden,outputPos.length};
				BPGa Bpga=new BPGa(Epochs,PopulationsSize,layer);
		Bpga.evolution(input,output);
		BPAnn bp=Bpga.getTop();
		bp.setLearning_effic((float)length_rate);
		Nerv nerv=BPAnn2Nerv(bp);
		nerv.setData(value);
		outputer.collect(new Text(""),nerv);
	}
}