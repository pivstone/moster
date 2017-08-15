package info.pixstone.moster.common.algorithm.genetic;

import java.math.BigInteger;
/**
 * 
 * 格雷码转码器
 * @author Administrator
 *
 */


public final class GrayCodeConverterImpl implements CodeConverter {

	/**
	 * @param code 待编码的二进制字符串
	 * @return 格雷码 
	 */
	@Override
	public final String encode(String code) {
		BigInteger bi=new BigInteger(code);
		String binaryString=bi.toString(2);
		
		char[] codeCharArray=binaryString.toCharArray();
		char[] result=new char[codeCharArray.length];
		result[0]=codeCharArray[0];
		for(int i=1;i<codeCharArray.length;i++)
		{
			result[i]=mutiplus(codeCharArray[i-1],codeCharArray[i]);
		}
		return new String(result);
	}
	/**
	 * @param code 待解码的二进制字符串
	 * @return 格雷码 
	 */
	@Override
	public final String decode(String code) {
		
		char[] codeCharArray=code.toCharArray();
		char[] result=new char[codeCharArray.length];
		result[0]=codeCharArray[0];
		for(int i=1;i<codeCharArray.length;i++)
		{
			result[i]=mutiplus(result[i-1],codeCharArray[i]);
		}
		String BinaryString=new String(result);
		BigInteger bi=new BigInteger(BinaryString,2);
		return bi.toString();
	}
	
	/**
	 * 格雷码计算  1X0=1、0X1=1、0X0=0、1X1=0；
	 * @param left 左数字
	 * @param right 右数字
	 * @return 运算结果
	 */
	private char mutiplus(char left,char right)
	{
		char a='1';
		char b='0';
		if(left==right)
			return b;
		else
			return a;
	}
	

}
