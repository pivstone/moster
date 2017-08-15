package info.pixstone.moster.common.algorithm.ann;

import java.text.DecimalFormat;
import java.util.Random;
import java.lang.StringBuffer;
import java.math.BigInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BPAnn {

	private final static  Log logger  =  LogFactory.getLog(BPAnn.class);		
	private int InputFactorCount = 3;// 输入要素个数 默认为3
	private int[] NervLayer = { 3, 5, 1 };// 神经元层 默认为3,1
	private int OutputCount = 1;// 输出要素个数 默认为1
	private double InputFactor[];// 输入要素
	private double Target[];// 输出要素
	private double Weight[][][];// 权重矩阵
	private double NetOut[][];// 各层输出
	private double OutDetla[];// 输出层误差信号因子
	private double HideDetla[];// 隐藏层误差信号因子
	private AnnUtil annUtil = new Sigmoid();// 转移函数
	private double Learning_effic = 0.05d;// 学习率
	private double HidErrSum = 0.0d; // 隐藏层误差和
	private double OutErrSum = 0.0d;// 输出误差和
	private double alfa = 0.5d;
	private double PreWeight[][][];// 上次隐藏层误差信号因子
	public void setAlfa(double alfa){
		this.alfa=alfa;
	}
	public void setLearning_effic(double Learning_effic){
		this.Learning_effic=Learning_effic;
	}
	public void setAnnUtil(AnnUtil annUtil){
		this.annUtil=annUtil;
	}
	public double getAlfa(){
		return alfa;
	}
	public double getLearning_effic(){
		return Learning_effic;
	}
	public AnnUtil getAnnUtil(){
		return annUtil;
	}
	public int getInputCount(){
		return this.NervLayer[0];
	}
	public int getHiddenCount(){
		return this.NervLayer[1];
	}
	public int getOutputCount(){
		return this.NervLayer[2];
	}
	
	public BPAnn(int[] iNervLayer) {
		this.NervLayer = iNervLayer;
		this.InputFactorCount = this.NervLayer[0];
		this.OutputCount = this.NervLayer[this.NervLayer.length - 1];
		this.InputFactor = new double[InputFactorCount];
		this.Target = new double[OutputCount];
		this.OutDetla = new double[this.OutputCount];
		this.HideDetla = new double[1];

		// 第一维为神经元层数
		// 第二维为神经元收到的权重系数
		// 第三维为神经元个数
		this.Weight = new double[2][][];
		this.PreWeight = new double[2][][];
		this.NetOut = new double[3][];
		for (int i = 1; i < 3; i++) {
			this.Weight[i - 1] = new double[this.NervLayer[i - 1]][this.NervLayer[i]];
			this.PreWeight[i - 1] = new double[this.NervLayer[i - 1]][this.NervLayer[i]];
		}
		for (int i = 1; i < 3; i++) {
			for (int j = 0; j < this.NervLayer[i - 1]; j++) {
				for (int k = 0; k < this.NervLayer[i]; k++) {
					Random rm = new Random();
					double a = rm.nextDouble();
					this.Weight[i - 1][j][k]= rm.nextDouble()>0.5?a:-a;

				}
			}
		}
	}

	public void read(String content){
		int size=content.length()/16;
		int z=0;
		for (int i = 1; i < 3; i++) {
			for (int j = 0; j < this.NervLayer[i - 1]; j++) {
				for (int k = 0; k < this.NervLayer[i]; k++) {
					String temp=content.substring(z*16,(z+1)*16);
					String Token=temp.substring(0,1);
					String value=temp.substring(1,16);
					double doubleValue=Double.parseDouble("0."+value);
					if(Token.equals("0")) doubleValue=doubleValue*-1;
					this.Weight[i - 1][j][k]=doubleValue;
					z++;
				}
			}
		}
	}

	@Override
	/**
	 *遍历输出权重
	 */
	public String toString() {
		StringBuffer result =new StringBuffer();
		DecimalFormat df = new DecimalFormat("0.0000000000000000");
		for (int i = 1; i < 3; i++) {
			for (int j = 0; j < this.NervLayer[i - 1]; j++) {
				for (int k = 0; k < this.NervLayer[i]; k++) {
						if(this.Weight[i - 1][j][k]>=0) {
							result.append("1") ;
							}
						if(this.Weight[i - 1][j][k]<0) {
							result.append("0");
							}
							String  data=df.format(this.Weight[i - 1][j][k]);
							int  start=data.indexOf(".");
							result.append(data.substring(start+1,data.length()));
					}
				}
			}
			return result.toString();
		}

	/**
	 * 获取神经网络 前行计算结果
	 * 
	 * @param input
	 *            输入因素
	 * @return 神经网络计算结果
	 */
	public double[] netOut(double[] input) {
		this.InputFactor = input;
		this.NetOut[0]=input;
		
		for (int i = 0; i < this.Weight.length; i++) {
			this.NetOut[i+1] = this.mutiply(this.NetOut[i], this.Weight[i]);
		}
		return this.NetOut[2];
	}

	/**
	*
	*
	*/
	public double evaluate(double[] input,double[] target){
		this.InputFactor =input;
		this.Target =target;
		this.netOut(this.InputFactor);
		this.outErr();
		return getErr();
	}	
	/**
	*
	*
	*/
	public double getErr(){	
		double errSum = 0;
		for (int i = 0; i < this.OutDetla.length; i++) {
			double netOut = this.NetOut[2][i];
			errSum += Math.abs(this.Target[i] - netOut);
		}
		return errSum;
	}
	/**
	 * 训练函数 训练神经网络
	 * 
	 * @param input
	 *            输入因子
	 * @param target
	 *            目标输出
	 */
	public void trian2(double[] input, double[] target) {
		this.InputFactor =input;
		this.Target= target;
		this.netOut(this.InputFactor);
		this.outErr();
		this.hideErr();
		// 输出层调整
		this.adjustWeight(this.OutDetla, this.NetOut[1],
				this.Weight[1],
				this.PreWeight[1]);
		// 隐藏层调整

			this.adjustWeight(this.HideDetla, this.NetOut[0],
					this.Weight[0], this.PreWeight[0]);
	}

		/**
	 * 训练函数 训练神经网络
	 * 
	 * @param input
	 *            输入因子
	 * @param target
	 *            目标输出
	 */
	public BPAnn trian(double[] input, double[] target) {
		this.InputFactor =input;
		this.Target= target;
		this.netOut(this.InputFactor);
		this.outErr();
		this.hideErr();
		// 输出层调整
		this.adjustWeight(this.OutDetla, this.NetOut[1],
				this.Weight[1],
				this.PreWeight[1]);
		// 隐藏层调整

			this.adjustWeight(this.HideDetla, this.NetOut[0],
					this.Weight[0], this.PreWeight[0]);
					return this;
	}
	/**
	 * 信号输入
	 * 
	 * @param netIn
	 *            神经输入
	 * @param weight
	 *            权重矩阵
	 * @return netOut 神经输出
	 */
	public double[] mutiply(double[] netIn, double[][] weight) {
		double[] netOut = null;
		if (netIn.length != weight.length){
		logger.error("netIn.length != weight.length");
			return netOut;
			}
		else {
			netOut = new double[weight[0].length];
			for (int i = 0; i < netOut.length; i++) {
				for (int k = 0; k < netIn.length; k++) {
					netOut[i] += netIn[k] * weight[k][i];
				}
				netOut[i] = annUtil.activationFun(netOut[i]);
			}
			return netOut;
		}
	}

	/**
	 * 计算输出层误差
	 */
	private void outErr() {
		double errSum = 0;
		for (int i = 0; i < this.OutDetla.length; i++) {
			double netOut = this.NetOut[2][i];
			this.OutDetla[i] = this.annUtil.difactivationFun(netOut)
					* (this.Target[i] - netOut);
			errSum += Math.abs(this.OutDetla[i]);
		}
		this.OutErrSum = errSum;
	}

	/**
	 * 计算隐藏层误差
	 */
	private void hideErr() {
		double errSum = 0;
			for (int j = 0; j < this.HideDetla.length; j++) {
				double netOut = this.NetOut[1][j];
				double sum = 0;
				for (int k = 0; k < this.OutDetla.length; k++) {
					sum += this.Weight[1][j][k] * this.OutDetla[k];
				}
				this.HideDetla[j] = this.annUtil.difactivationFun(netOut)
						* sum;
				errSum += Math.abs(this.HideDetla[j]);
		}
		this.HidErrSum = errSum;
	}

	/**
	 * 误差调整
	 * 
	 * @param delta
	 *            误差信号因子
	 * @param netOut
	 *            单层神经元输出值
	 * @param weight
	 *            权重矩阵
	 */
	 private void adjustWeight(double[] delta, double[] layer,  
	            double[][] weight, double[][] prevWeight) {   
	        for (int i = 0;i< delta.length; i++) {  
	            for (int j = 0; j<layer.length; j++) {  
	                double newVal = this.alfa * prevWeight[j][i] + this.Learning_effic * delta[i]  
	                        * layer[j];  
	                weight[j][i] += newVal;  
	                prevWeight[j][i] = newVal;  
	            }  
	        }  
	    }  
	public String check(){
		StringBuffer  result=new StringBuffer();
		result.append("this.Weight.length：");
		result.append(this.Weight.length);
		result.append("\r\n");

		result.append("this.Weight1.length：");
		result.append(this.Weight[0].length);
		result.append("\r\n");
		
		result.append("this.Weight2.length：");
		result.append(this.Weight[1].length);
		result.append("\r\n");
		
		result.append("InputCount：");
		result.append(getInputCount());
		result.append("\r\n");
		
		result.append("HiddenCount:");
		result.append(getHiddenCount());
		result.append("\r\n");
		
		result.append("OutputCount：");
		result.append(getOutputCount());
		result.append("\r\n");
		
		return result.toString();
	}
	
	public double[] Normalized(double[] input){
		double[] result=new double[input.length];
		for(int i=0;i<input.length;i++){
			result[i]=annUtil.activationFun(input[i]);
		}
		return result;
	}
	public static void main(String[] args){
		int[] layer =new int[]{3,600,1};
		BPAnn ann=new BPAnn(layer);
		for(int i=0;i<100;i++){
			double[] input=new double[]{25d,75d,34d};
			double[] output=new double[]{241d};
			System.out.println(ann.netOut(input)[0]);
			ann.trian(input,output);
		}
		System.out.println(ann.netOut(new double[]{11,30,20})[0]);
	}
}
