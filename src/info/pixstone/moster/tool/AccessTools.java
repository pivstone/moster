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
						logger.debug("������"+Temp);
						File	MetaDataFile=FileUtil.CreatFile(CSVPath+"\\Meta_"+Temp+".csv");
						File	CSVFile=FileUtil.CreatFile(CSVPath+"\\"+Temp+".csv");
						ResultSet  rs=  st.executeQuery(SQL+Temp);		
						ResultSetMetaData data=rs.getMetaData(); 
						logger.info("��ʼд��Ԫ�����ļ���"+CSVPath+"\\Meta_"+Temp);
						writeMeta2File(MetaDataFile,data,false);
						logger.info("д��Ԫ������ɣ���ʼת����¼");
												
						while(rs.next()){ 
							StringBuffer record=new StringBuffer();
							for(int i = 1 ; i<= data.getColumnCount() ; i++){ 
							//���ָ���е���ֵ 
								record.append(rs.getString(i)+token); 
							}
							record.append("\r\n"); 
							FileUtil.WriteFile(CSVFile,record.toString(),true);
						}
						
					}
				}catch(Exception e){
					logger.error("��mdb���ݿ�ʧ��!"+e.getStackTrace());	
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
					logger.error("����mdb���ݿ�ʧ��!"+e.getStackTrace());	
					e.printStackTrace();					
				  }
				return con;
		}
		
		
		public final static void writeMeta2File(File file,ResultSetMetaData data,boolean append){
			StringBuffer Meta=new StringBuffer();
			try{
				for(int i = 1 ; i<= data.getColumnCount() ; i++){ 					
					Meta.append("\t��"+i+"����\t");
					//���ָ���е����� 
					String columnName = data.getColumnName(i); 
					Meta.append("\t����:\t"+columnName);
					
					//���ָ���е��������� 
					int columnType=data.getColumnType(i); 
					Meta.append("\t��������:\t"+columnType);
					
					//���ָ���е����������� 
					String columnTypeName=data.getColumnTypeName(i); 
					Meta.append("\t����������:\t"+columnTypeName);
					
					//��Ӧ�������͵��� 
					String columnClassName=data.getColumnClassName(i); 
					Meta.append("\t�������͵���:\t"+columnClassName);
					
					//�����ݿ������͵�����ַ����� 
					int columnDisplaySize=data.getColumnDisplaySize(i); 
					Meta.append("\t��������ַ�����:\t"+columnDisplaySize);
					
					//Ĭ�ϵ��еı��� 
					String columnLabel=data.getColumnLabel(i); 
					Meta.append("\t�б���:\t"+columnLabel);
					
					//ĳ�����͵ľ�ȷ��(���͵ĳ���) 
					int precision= data.getPrecision(i); 
					Meta.append("\t���͵ĳ���:\t"+precision);
					
					//С������λ�� 
					int scale=data.getScale(i); 
					Meta.append("\t���;���:\t"+scale);
					
					
					// �Ƿ��Զ����� 
					boolean isAutoInctement=data.isAutoIncrement(i); 
					Meta.append("\t����:\t"+isAutoInctement);
					
					//�����ݿ����Ƿ�Ϊ������ 
					boolean isCurrency=data.isCurrency(i); 
					Meta.append("\t������:\t"+isCurrency);
					
					//�Ƿ�Ϊ�� 
					int isNullable=data.isNullable(i); 
					Meta.append("\tΪ��:\t"+isNullable);
					
					//�Ƿ�Ϊֻ�� 
					boolean isReadOnly=data.isReadOnly(i); 
					Meta.append("\tֻ��:\t"+isReadOnly);
					
					//�ܷ������where�� 
					boolean isSearchable=data.isSearchable(i);
					Meta.append("\t�ܷ�ִ��Where:\t"+isSearchable);
					Meta.append("\r\n");
				}
			}catch(Exception e){
					logger.error("д��Ԫ����ʧ��!"+e.getStackTrace());	
				 e.printStackTrace();
			}
			FileUtil.WriteFile(file,Meta.toString(),append);
		}			
}