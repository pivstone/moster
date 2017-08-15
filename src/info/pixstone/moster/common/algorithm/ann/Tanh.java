package info.pixstone.moster.common.algorithm.ann;

/**
 * Ë«ÇúÏßÕýÇÐ»»Êý
 * @author Administrator
 *
 */
public class Tanh extends AnnUtil {

	@Override
	public double activationFun(double x) {
		return (1-Math.exp(-x))/(1+Math.exp(-x));
	}

	@Override
	public double difactivationFun(double x) {
	
		return (1-x*x);
	}
	@Override
	public  double defun(double x){
		return Math.log(-x/(x-1)-1/(x-1));
	}
}
