package info.pixstone.moster.hadoop;
import java.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.contrib.utils.join.*;

public  class TaggedWritable extends TaggedMapOutput{
	private Text data;
	
	public  TaggedWritable(){
		this.data=new Text("");
		this.data=new Text("");
	}
	
	public  TaggedWritable(Text data){
		this.tag=new Text("");
		this.data=data;
	}
	public Writable getData(){
		return data;
	}
	
	public void write(DataOutput out) throws IOException{
		this.tag.write(out);
		this.data.write(out);
	}
	
	public void readFields(DataInput in)throws IOException{
		this.tag.readFields(in);
		this.data.readFields(in);
	}

}