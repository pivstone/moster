package info.pixstone.moster.common.algorithm.ann;


public class Sigmoid extends AnnUtil {

	public  double activationFun(double x) {
		
		return 1/(1+Math.exp(-x));
	}

	@Override
	public double difactivationFun(double x) {
		
		return x*(1-x);
	}
	
	public  double defun(double x){
		return Math.log(-1*(x/(x-1)));
	}
}
