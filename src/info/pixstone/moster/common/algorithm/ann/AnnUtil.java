package info.pixstone.moster.common.algorithm.ann;
/**
*
*/
public abstract class AnnUtil {

	public abstract double activationFun(double x);

	public abstract double difactivationFun(double x);

	public abstract double defun(double x);
	
	public static double[][] Transpose(double[][] input) {
		double[][] result =new double[input[0].length][input.length];
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[0].length; j++) {
				result[j][i]=input[i][j];
			}
		}
		return result;
	}


}
