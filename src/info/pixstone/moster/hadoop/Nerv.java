package info.pixstone.moster.hadoop;

import org.apache.hadoop.io.*;
import java.io.*;

public  class Nerv implements Writable{

	private  IntWritable InputCount;
	private  IntWritable OutputCount;
	private  IntWritable HiddenCount;

	private  Text WeightMartix;
	
	private Text data;
	
	public Nerv(){
		InputCount=new IntWritable();
		OutputCount=new IntWritable();
		HiddenCount=new IntWritable();
		WeightMartix=new Text();
		data=new Text();
	}
	
	public IntWritable getInputCount(){
		return InputCount;
	}
	public IntWritable getOutputCount(){
		return OutputCount;
	}
	public IntWritable getHiddenCount(){
		return HiddenCount;
	}

	public Text getWeightMartix(){
		return WeightMartix;
	}
	public Text getData(){
		return data;
	}
	public void setData(Text data){
		this.data=data;
	}
	public void setInputCount(IntWritable InputCount){
		this.InputCount=InputCount;
	}
	public void setOutputCount(IntWritable OutputCount){
		this.OutputCount=OutputCount;
	}
	public void setHiddenCount(IntWritable HiddenCount){
		this.HiddenCount=HiddenCount;
	}
	public void setWeightMartix(Text WeightMartix){
		this.WeightMartix=WeightMartix;
	}	
	public void write(DataOutput out) throws IOException {
         this.InputCount.write(out);
         this.OutputCount.write(out);
         this.HiddenCount.write(out);
         this.WeightMartix.write(out);
         this.data.write(out);
    }
       
    public void readFields(DataInput in) throws IOException {
        this.InputCount.readFields(in);
        this.OutputCount.readFields(in);
        this.HiddenCount.readFields(in);
        this.WeightMartix.readFields(in);
        this.data.readFields(in);
    }
	
    public static Nerv read(DataInput in) throws IOException {
         Nerv N = new Nerv();
         N.readFields(in);
         return N;
    }
}