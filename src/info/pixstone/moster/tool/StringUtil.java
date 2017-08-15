package info.pixstone.moster.tool;

import java.lang.String;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.lang.Double;
import java.util.regex.Matcher;
import java.text.ParseException;
import java.util.Calendar;
import org.apache.log4j.Logger;
public class StringUtil{
	private final static  Logger logger  =  Logger.getRootLogger();
	public final static double String2double(String value){
		double result=0d;
		Pattern p=Pattern.compile("\\d+\\.{0,1}\\d+");
		Matcher m=p.matcher(value);
		if(m.find()){
		  result= Double.parseDouble(value.substring(m.start(),m.end()));
		}
		return result;
	}
	
	public final static int String2Int(String value){
		int result=0;
		Pattern p=Pattern.compile("\\d+|");
		Matcher m=p.matcher(value);
		if(m.find()){
		  result= Integer.parseInt(value);
		}
		return result;
	}
	public final static double[] String2doubleArray(String value,String token){
		String[] content=value.split(token);
		double[] result=new double[content.length];
		for(int i=0;i<content.length;i++){
			result[i]=String2double(content[i]);
		}
		return result;
	}
	
	public final static int[] String2IntArray(String value,String token){
		String[] content=value.split(token);
		int[] result=new int[content.length];
		for(int i=0;i<content.length;i++){
			result[i]=String2Int(content[i]);
		}
		return result;
	}
	
	public  final static Calendar String2Date(String value){
		Pattern p=Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}");
		Matcher m=p.matcher(value);
		Calendar date=Calendar.getInstance();
		date.set(1900,1,1);
		if(m.find()){
			try{
			 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 date.setTime(sdf.parse(value));
			}catch(ParseException e){
				logger.debug(value+"转换Date类型失败,尝试新模板");
			}try{
			 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			 date.setTime(sdf.parse(value));
			}catch(ParseException e){
				logger.warn(value+"转换Date类型失败");
			}
		}
			return date;
	}
	public final static double Aspect2Degree(String aspect){
		double result=0.0d;
		if(aspect.equals("北")) result=0.0d;
		if(aspect.equals("北东北")) result=22.5d;
		if(aspect.equals("北北东")) result=22.5d;
		if(aspect.equals("东北")) result=45.0d;
		if(aspect.equals("东东北")) result=67.5d;
		if(aspect.equals("东")) result=90.0d;
		if(aspect.equals("东东南")) result=112.5d;
		if(aspect.equals("东南")) result=135.0d;
		if(aspect.equals("南")) result=180.0d;
		if(aspect.equals("南西南")) result=202.5d;
		if(aspect.equals("西南")) result=225.0d;
		if(aspect.equals("西西南")) result=247.5d;
		if(aspect.equals("西")) result=270.0d;
		if(aspect.equals("西西北")) result=292.5d;
		if(aspect.equals("西北偏西")) result=292.5d;
		if(aspect.equals("西北西")) result=292.5d;
		if(aspect.equals("西北")) result=315.0d;
		if(aspect.equals("北西")) result=315.0d;
		if(aspect.equals("北西北")) result=337.5d;
		if(aspect.equals("西北北")) result=337.5d;
		return result;
	}
	
	public final static double RainDrop(String rain){
		double result=String2double(rain);
		String data=rain;
		Pattern p1=Pattern.compile("(?<=\\b32)\\d{3}");
		Pattern p2=Pattern.compile("(?<=\\b31)\\d{3}");
		Pattern p3=Pattern.compile("(?<=\\b30)\\d{3}");
		Pattern p4=Pattern.compile("^32700");
		Pattern p5=Pattern.compile("^32766");
		Pattern p6=Pattern.compile("^32744");
		Matcher m=p4.matcher(data);
		if(m.find()){
					return 0d;
					}
		m=p5.matcher(data);
		if(m.find()){
					return 0d;
					}
		m=p6.matcher(data);
		if(m.find()){
					return 0d;
					}
		m=p1.matcher(data);
		if(m.find()){
					result=String2double(data.substring(m.start(),m.end()));
			}
		m=p2.matcher(data);
		if(m.find()){
					result=String2double(data.substring(m.start(),m.end()));
			}
		m=p3.matcher(data);
		if(m.find()){
					result=String2double(data.substring(m.start(),m.end()));
			}
			if(result>0&&result<=100)		 result=0.0d;
			if(result>100&&result<=250)	 result=0.15d;
			if(result>250&&result<=500)	result=0.3d;
			if(result>500&&result<=750)	result=0.45d;
			if(result>750&&result<=2000)	result=0.75d;
			if(result>2000)	result=0.9d;
		return result;
	}
	public static void  main(String args[]){
		logger.info("32000:"+RainDrop("31110.0"));
	//	logger.info("2009-10-5 8:00:00 =>"+String2Date("200910-5 8:00:00").getTime());
	//	logger.info("2009-10-5  =>"+String2Date("2009-10-5").getTime());
	}	
}