package info.pixstone.moster.tool;

import java.io.IOException;
import info.pixstone.moster.common.algorithm.ann.*;

import org.apache.log4j.Logger;

public class BPAnnVaildate{
	
	private static  Logger logger  =  Logger.getLogger(BPAnnVaildate.class);	
	public static void main(String[] args)throws IOException{

		 // System.err.println("Usage: BPAnnVaildate <-fa:annFile><-fd:datafile><-i:InputFactorPos><-o:OutputFactorPos><-d:delimiters> ");
		 // System.exit(2);
		String[] arg=args[0].split(" ");
		String annfile="";
		String datafile="";
		String inputParam="";
		String outputParam="";
		String delimiters="";
		for(int i=0;i<arg.length;i++){
			if(arg[i].equals("-fa")) annfile=arg[i+1];
			if(arg[i].equals("-fd")) datafile=arg[i+1];
			if(arg[i].equals("-i")) inputParam=arg[i+1];
			if(arg[i].equals("-o")) outputParam=arg[i+1];
			if(arg[i].equals("-d")) delimiters=arg[i+1];
		}
		int[] inputPos=StringUtil.String2IntArray(inputParam,",");
		int[] outputPos=StringUtil.String2IntArray(outputParam,",");
		
		String AnnWeghit=FileUtil.ReadFile(annfile);
		String data=FileUtil.ReadFile(datafile);
		String[] lines=data.split("\n");
		System.out.println(lines.length);
		int hidden=(int)((inputPos.length+outputPos.length)*1.5);
		int[] layer={inputPos.length,hidden,outputPos.length};
		BPAnn bp=new BPAnn(layer);
		bp.read(AnnWeghit);
		double totalavg=0.0d;
		int z=0;
		for(String line:lines){
			z++;
			String[] content=line.split("/");
			double[] input=new double[inputPos.length];
			double[] output=new double[outputPos.length];
			double avg=0d;
			for(int i=0;i<inputPos.length;i++){
				input[i]=StringUtil.String2double(content[inputPos[i]]);
			}
			double[] netOut=bp.netOut(input);
			if(netOut.length!=output.length){
				System.err.println("Output length != target length");
				System.exit(2);
			}
			for(int i=0;i<outputPos.length;i++){
				output[i]=StringUtil.RainDrop(content[outputPos[i]]);
				//logger.info(content[11]);
				if(output[i]!=0.0d)
					avg=Math.abs(output[i]-netOut[i]);
				logger.info("     netOut:"+(netOut[i])+"      target:"+output[i]+"       rate:"+Math.abs(netOut[i]-output[i]));	
				totalavg=totalavg+avg;
			}

		}
			totalavg/=z;
			logger.info("totalavg:"+totalavg);
	}
}