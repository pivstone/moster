package info.pixstone.moster.common.algorithm.genetic;

/**
* 神经网络-遗传算法
*
*
*/
import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.MathContext;
import java.io.*;
import java.util.*;
import info.pixstone.moster.common.algorithm.ann.*;
import org.apache.log4j.Logger;

public class BPGa{
	private  int currentEpoch=0;
	private static int Epochs=300;
	private  int PopulationsSize=1000;
	private  BPAnnIndividual[] SweetPool;
	private  static int InputCount;
	private  static int OutCount;
	private  static int hiddenCount;
	private  BigDecimal totalAdaptivevalue=new BigDecimal(0.0d);
	private double crossOverate=0.05;
	private double mutationrate=0.01;
	private int genesSize=0;
	private  final static  Logger logger  =  Logger.getRootLogger();
	
	public BPGa(int Epochs,int PopulationsSize,int[] layer)	{
		this.Epochs=Epochs;
		this.PopulationsSize=PopulationsSize;
		this.InputCount=layer[0];
		this.OutCount=layer[2];
		this.hiddenCount=layer[1];
		SweetPool=new BPAnnIndividual[PopulationsSize];
		for(int i=0;i<PopulationsSize;i++){
			BPAnn bp=new BPAnn(layer);
			BPAnnIndividual bi=new BPAnnIndividual(bp,false);
			SweetPool[i]=bi;
		}
	}

	public BigDecimal getAdaptivevalue(){
		return this.totalAdaptivevalue;
	}
	/**
	*族群进化 
	*@Param input 神经网络输入参数
	*@Param target 神经网络目标参数
	*
	*/
	public void evolution(double[] input,double[] target){
		evaluate(input,target);
		for(int i=0;i<this.Epochs;i++){
			reproduction();
			crossOver();
			mutation();
			evaluate(input,target);
		}
	}
	public void evolution(double[] input,double[] target,int Epochs){
		evaluate(input,target);
		for(int i=0;i<Epochs;i++){
			reproduction();
			crossOver();
			mutation();
			evaluate(input,target);
				logger.debug("totalAdaptivevalue:"+totalAdaptivevalue);
		}
	}
	
	/**
	*族群评价 
	*@Param input 神经网络输入参数
	*@Param target 神经网络目标参数
	*
	*/
	public void evaluate(double[] input,double[] target){
		this.totalAdaptivevalue=new BigDecimal(0.0d);
		for(int i=0;i<PopulationsSize;i++){
					BPAnnIndividual bi=this.SweetPool[i];
					this.totalAdaptivevalue=this.totalAdaptivevalue.add(new BigDecimal(bi.evaluate(input,target)));
					bi.setAccAdaptivevalue(this.totalAdaptivevalue);
						logger.debug("e:"+bi.getAccAdaptivevalue());	
			}
				quickSort();							
	}
	

	
	/**
	*族群繁殖
	*
	*/
	public void reproduction(){
		BPAnnIndividual[]	newGeneration=new BPAnnIndividual[PopulationsSize];
		for(int i=0;i<PopulationsSize;i++){
			BPAnnIndividual bi=this.SweetPool[i];
			BigDecimal tempAdaptivevalue=bi.getAccAdaptivevalue();
						 	logger.debug("tempAdaptivevalue:"+tempAdaptivevalue);
			bi.setAccAdaptivevalue(tempAdaptivevalue.divide(this.totalAdaptivevalue,MathContext.DECIMAL32));			
								 	logger.debug("SSS:"+bi.getAccAdaptivevalue());	
		 }

		for(int i=0;i<PopulationsSize;i++){
			double ether=Math.random();
			int selected =choose(0,PopulationsSize-1,ether);
			Object obj=this.SweetPool[selected].clone();
			BPAnnIndividual bi=(BPAnnIndividual)obj;
			newGeneration[i]=bi;
		}
		
		this.SweetPool=newGeneration;
	}
	/**
	*基因交叉
	*
	*/
	public void crossOver(){
		int evolutionChamberSize=(int)(this.PopulationsSize*this.crossOverate);
		if(evolutionChamberSize%2!=0) evolutionChamberSize++;
		BPAnnIndividual[] EvolutionChamber=new BPAnnIndividual[evolutionChamberSize];
		for(int i=0;i<evolutionChamberSize;i++){	
			double ether=Math.random();
			int selected= choose(0,PopulationsSize-1,ether);
			EvolutionChamber[i]=this.SweetPool[selected];
		}
		for(int i=0;i<evolutionChamberSize;i=i+2){

			int pos=(int)(Math.random()*this.genesSize);
			
			BPAnnIndividual leftAnn=EvolutionChamber[i];
			BPAnnIndividual rightAnn=EvolutionChamber[i+1];
			StringBuffer left=new StringBuffer(leftAnn.getGenes());
			StringBuffer right=new StringBuffer(rightAnn.getGenes());

			if(pos==0) pos=1;
			
			
			String leftSwap=left.substring(pos-1,pos);
			String rightSwap=right.substring(pos-1,pos);
			left.replace(pos-1,pos,rightSwap);
			right.replace(pos-1,pos,leftSwap);
			
			String leftNewGenes=left.toString();
			String rightNewGenes=right.toString();
			
			BPAnn leftBPAnn=leftAnn.getPhenotype();
			BPAnn rightBPAnn=rightAnn.getPhenotype();
			leftBPAnn.read(leftNewGenes);
			rightBPAnn.read(rightNewGenes);
			
			leftAnn.setGenes(leftNewGenes,leftBPAnn);
			rightAnn.setGenes(rightNewGenes,rightBPAnn);
		}
	}
	/**
	*基因突变
	*
	*/
	public void mutation(){
		int mutationSize=(int)(this.PopulationsSize*this.genesSize*this.mutationrate);
		for(int i=0;i<mutationSize;i++){
			int selected=(int)(Math.random()*this.PopulationsSize);
			int pos=(int)(Math.random()*this.genesSize);
			int mud=(int)(Math.random()*10);
			BPAnnIndividual bai=this.SweetPool[selected];
			StringBuffer gense=new StringBuffer(bai.getGenes());
			BPAnn bp=bai.getPhenotype();
			if(pos==0) gense.replace(0,1,mud+"");
			gense.replace(pos-1,pos,mud+"");
			String newGense=gense.toString();
			bp.read(newGense);
			bai.setGenes(newGense,bp);
		}
	}
	/**
	*个体选择 二折法
	*
	*/
	private int choose(int head,int tail,double ether){
		while(head<tail){
			int center=(int)((head+tail)/2);
			if(Math.abs(head-tail)<2) return center;
			if(this.SweetPool[center].getAccAdaptivevalue().compareTo(new BigDecimal(ether))<0){
					head=center;
			}
			else if(this.SweetPool[center].getAccAdaptivevalue().compareTo(new BigDecimal(ether))==0){
				return center;
			}
			else if(this.SweetPool[center-1].getAccAdaptivevalue().compareTo(new BigDecimal(ether))<0){
				return center;
			}
			else {
				tail=center;
			}
		}

		return head;
	}
	
	public BPAnn getTop(){
		quickSort();
		return this.SweetPool[0].getPhenotype();
	}
		
	private  int sortUtil(int head,int tail,BPAnnIndividual[] array){

		BigDecimal keyValue=array[head].getAccAdaptivevalue();
		BPAnnIndividual keyObj=array[head];
		while(tail>head){
			while(array[tail].getAccAdaptivevalue().compareTo(keyValue)>=0&&tail>head)
				tail--;
			BPAnnIndividual obj=array[head];
			array[head]=array[tail];
			array[tail]=obj;
			while(array[head].getAccAdaptivevalue().compareTo(keyValue)<=0&&tail>head)
				head++;
			BPAnnIndividual obj1=array[head];
			array[head]=array[tail];
			array[tail]=obj1;		
		}
		array[tail] = keyObj;
		return head;
	}

	private  void sort(int head,int tail,BPAnnIndividual[] array){
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

	private  void quickSort(){
		sort(0,this.PopulationsSize-1,this.SweetPool);
	}

	
	 public static void main(String[] args) throws IOException {  
		int[] layer={32, 15, 4};
        BPGa bp = new BPGa(100,500,layer);  
  
        Random random = new Random();  
        List<Integer> list = new ArrayList<Integer>();  
        for (int i = 0; i !=500; i++) {  
            int value = random.nextInt();  
            list.add(value);  
        }  
  

            for (int value : list) {  
			        for (int i = 0; i != 1; i++) {  
                double[] real = new double[4];  
                if (value >= 0)  
                    if ((value & 1) == 1)  
                        real[0] = 1;  
                    else  
                        real[1] = 1;  
                else if ((value & 1) == 1)  
                    real[2] = 1;  
                else  
                    real[3] = 1;  
                double[] binary = new double[32];  
                int index = 31;  
                do {  
                    binary[index--] = (value & 1);  
                    value >>>= 1;  
                } while (value != 0);  
                bp.evolution(binary, real,60);  
						 System.out.println(value+"=:"+bp.getAdaptivevalue());		
				//bp.getTop();
            }  
			
        } 
 System.out.println("训练完毕，下面请输入一个任意数字，神经网络将自动判断它是正数还是复数，奇数还是偶数。");  
  
        while (true) {  
            byte[] input = new byte[10];  
            System.in.read(input);  
            Integer value = Integer.parseInt(new String(input).trim());  
            int rawVal = value;  
            double[] binary = new double[32];  
            int index = 31;  
            do {  
                binary[index--] = (value & 1);  
                value >>>= 1;  
            } while (value != 0);  
			
			BPAnn ann=bp.getTop();
            double[] result = ann.netOut(binary);  
  
            double max = -Integer.MIN_VALUE;  
            int idx = -1;  
  
            for (int i = 0; i != result.length; i++) {  
                if (result[i] > max) {  
                    max = result[i];  
                    idx = i;  
                }  
            }  
  
            switch (idx) {  
            case 0:  
                System.out.format("%d是一个正奇数\n", rawVal);  
                break;  
            case 1:  
                System.out.format("%d是一个正偶数\n", rawVal);  
                break;  
            case 2:  
                System.out.format("%d是一个负奇数\n", rawVal);  
                break;  
            case 3:  
                System.out.format("%d是一个负偶数\n", rawVal);  
                break;  
            }  
        }  
    }  
 }