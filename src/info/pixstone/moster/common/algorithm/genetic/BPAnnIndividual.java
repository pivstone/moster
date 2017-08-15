package info.pixstone.moster.common.algorithm.genetic;

import info.pixstone.moster.common.algorithm.ann.*;
import java.math.BigDecimal;
public class BPAnnIndividual extends Individual implements Cloneable{

	private BigDecimal accAdaptivevalue=new BigDecimal(0.0d);
	public BPAnnIndividual(BPAnn bp){
		super(bp);
	}
	public BPAnnIndividual(BPAnn bp,boolean codeConverState){
		super(bp,codeConverState);
	}
	public BigDecimal getAccAdaptivevalue(){
		return accAdaptivevalue;
	}
	public void setAccAdaptivevalue(BigDecimal accAdaptivevalue){
		this.accAdaptivevalue=accAdaptivevalue;
	}
	public double evaluate(double[] input,double[] target){
		BPAnn Bp1=this.getPhenotype();
		return 1/Bp1.evaluate(input,target);
	}
	
	public double getAdaptivevalue(){
		Object obj2=super.getPhenotype();
		BPAnn Bp2=(BPAnn)obj2;
		return 1/Bp2.getErr();
	}
	
	public BPAnn getPhenotype(){
		Object obj=super.getPhenotype();
		BPAnn ann=(BPAnn)obj;
		return ann;
	}
	
		@Override
	public  int compare(Individual o1, Individual o2) {
		BPAnn Bp1=(BPAnn)o1.getPhenotype();
		BPAnn Bp2=(BPAnn)o2.getPhenotype();
		if(Bp1.getErr()==Bp2.getErr()) return 0;
		if(Bp1.getErr()>Bp2.getErr()) return 1;
		if(Bp1.getErr()<Bp2.getErr()) return -1;
		return 0;
	}
	@Override
	public  boolean equals(Individual o1) {
	 	BPAnn Bp1=(BPAnn)o1.getPhenotype();
		BPAnn Bp2=this.getPhenotype();
		return Bp1.toString().equals(Bp2.toString());
	}
		@Override
	 public Object clone() {  
        BPAnnIndividual o = null;  
            o = (BPAnnIndividual) super.clone();  
                return o;  
    }  
}