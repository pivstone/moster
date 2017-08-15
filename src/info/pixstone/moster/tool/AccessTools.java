package info.pixstone.moster.tool;

import java.lang.String;
import java.lang.StringBuffer;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.util.Properties;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.io.File;

import org.apache.log4j.Logger;

public final class AccessTools{
	
		private final static  Logger logger  =  Logger.getLogger(AccessTools.class);
		public  static  void main( String args[] ) throws Exception 	{
			AccessToCSV("E:\\STone\\test\\test.mdb","E:\\");
		}
		public final static void AccessToCSV(String MdbPath,String CSVPath){
			AccessToCSV(MdbPath,CSVPath,"Admin","","|");
		}
		public final static void AccessToCSV(String MdbPath,String CSVPath,String UserName,String passwd,String token){
			Statement st = null;
			ResultSetMetaData rsmd=null;
			String SQL="SELECT * FROM ";
			ArrayList<String> TableName=new ArrayList<String>();
			Connection con=getConnect(MdbPath,UserName,passwd);
				try{
					st = (Statement)con.createStatement();
					DatabaseMetaData  dbmd=con.getMetaData();  
					ResultSet  TableSet=dbmd.getTables(null,null,"%",new String[]{"TABLE"});  
					while(TableSet.next()){  
						TableName.add(TableSet.getString(3));
					}for(String Temp:TableName){
						logger.debug("表名："+Temp);
						File	MetaDataFile=FileUtil.CreatFile(CSVPath+"\\Meta_"+Temp+".csv");
						File	CSVFile=FileUtil.CreatFile(CSVPath+"\\"+Temp+".csv");
						ResultSet  rs=  st.executeQuery(SQL+Temp);		
						ResultSetMetaData data=rs.getMetaData(); 
						logger.info("开始写入元数据文件："+CSVPath+"\\Meta_"+Temp);
						writeMeta2File(MetaDataFile,data,false);
						logger.info("写入元数据完成，开始转换记录");
												
						while(rs.next()){ 
							StringBuffer record=new StringBuffer();
							for(int i = 1 ; i<= data.getColumnCount() ; i++){ 
							//获得指定列的列值 
								record.append(rs.getString(i)+token); 
							}
							record.append("\r\n"); 
							FileUtil.WriteFile(CSVFile,record.toString(),true);
						}
						
					}
				}catch(Exception e){
					logger.error("打开mdb数据库失败!"+e.getStackTrace());	
					e.printStackTrace();
				}				
	}
		public final static Connection getConnect(String MdbPath)	{
				
				return getConnect(MdbPath,"Admin","");
		}
		public final static Connection getConnect(String MdbPath,String UserName,String passwd)	{
				Connection con = null;
				Properties prop = new Properties();  
				prop.put("charSet", "gb2312");             
				prop.put("Id", UserName);
				prop.put("password", passwd);
				String  url="jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ="+MdbPath;  
				try{
					Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); 
					con=DriverManager.getConnection(url,prop);  
				} catch(Exception e)  {
					logger.error("连接mdb数据库失败!"+e.getStackTrace());	
					e.printStackTrace();					
				  }
				return con;
		}
		
		
		public final static void writeMeta2File(File file,ResultSetMetaData data,boolean append){
			StringBuffer Meta=new StringBuffer();
			try{
				for(int i = 1 ; i<= data.getColumnCount() ; i++){ 					
					Meta.append("\t第"+i+"列数\t");
					//获得指定列的列名 
					String columnName = data.getColumnName(i); 
					Meta.append("\t列名:\t"+columnName);
					
					//获得指定列的数据类型 
					int columnType=data.getColumnType(i); 
					Meta.append("\t数据类型:\t"+columnType);
					
					//获得指定列的数据类型名 
					String columnTypeName=data.getColumnTypeName(i); 
					Meta.append("\t数据类型名:\t"+columnTypeName);
					
					//对应数据类型的类 
					String columnClassName=data.getColumnClassName(i); 
					Meta.append("\t数据类型的类:\t"+columnClassName);
					
					//在数据库中类型的最大字符个数 
					int columnDisplaySize=data.getColumnDisplaySize(i); 
					Meta.append("\t类型最大字符个数:\t"+columnDisplaySize);
					
					//默认的列的标题 
					String columnLabel=data.getColumnLabel(i); 
					Meta.append("\t列标题:\t"+columnLabel);
					
					//某列类型的精确度(类型的长度) 
					int precision= data.getPrecision(i); 
					Meta.append("\t类型的长度:\t"+precision);
					
					//小数点后的位数 
					int scale=data.getScale(i); 
					Meta.append("\t类型精度:\t"+scale);
					
					
					// 是否自动递增 
					boolean isAutoInctement=data.isAutoIncrement(i); 
					Meta.append("\t自增:\t"+isAutoInctement);
					
					//在数据库中是否为货币型 
					boolean isCurrency=data.isCurrency(i); 
					Meta.append("\t货币型:\t"+isCurrency);
					
					//是否为空 
					int isNullable=data.isNullable(i); 
					Meta.append("\t为空:\t"+isNullable);
					
					//是否为只读 
					boolean isReadOnly=data.isReadOnly(i); 
					Meta.append("\t只读:\t"+isReadOnly);
					
					//能否出现在where中 
					boolean isSearchable=data.isSearchable(i);
					Meta.append("\t能否执行Where:\t"+isSearchable);
					Meta.append("\r\n");
				}
			}catch(Exception e){
					logger.error("写入元数据失败!"+e.getStackTrace());	
				 e.printStackTrace();
			}
			FileUtil.WriteFile(file,Meta.toString(),append);
		}			
}