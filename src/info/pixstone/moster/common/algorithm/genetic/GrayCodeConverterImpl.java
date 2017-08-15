package info.pixstone.moster.common.algorithm.genetic;

import java.math.BigInteger;
/**
 * 
 * ������ת����
 * @author Administrator
 *
 */


public final class GrayCodeConverterImpl implements CodeConverter {

	/**
	 * @param code ������Ķ������ַ���
	 * @return ������ 
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
	 * @param code ������Ķ������ַ���
	 * @return ������ 
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
	 * ���������  1X0=1��0X1=1��0X0=0��1X1=0��
	 * @param left ������
	 * @param right ������
	 * @return ������
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
