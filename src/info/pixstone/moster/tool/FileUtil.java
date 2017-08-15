package info.pixstone.moster.tool;

import java.lang.String;
import java.lang.StringBuffer;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.charset.*;
import org.apache.log4j.Logger;

public  class FileUtil{

	private  static  Logger logger  =  Logger.getRootLogger();
	public final static File CreatFile(String FilePath)
		{
			File file=null;
			try{
				file=new File(FilePath);
				if(!file.exists())	{
					file.createNewFile();
					logger.info("创建文件"+FilePath);
					}else{
					logger.warn("文件已存在"+FilePath);
				}
			}catch(Exception e){
				 logger.error("文件创建失败!"+FilePath+e.getStackTrace());	
				 e.printStackTrace();
			  }			  
			  return file;
		}
	public final static void  WriteFile(File file,String content,boolean append)
	{
		 FileChannel fc = null;
		 FileOutputStream  os=null;
		 try {
			os = new FileOutputStream(file,append);
			fc = os.getChannel();
			Charset gbk = Charset.forName( "GBK" );
			CharsetDecoder decoder = gbk.newDecoder();
			CharsetEncoder encoder = gbk.newEncoder();
			ByteBuffer bb=ByteBuffer.wrap(content.getBytes());
			CharBuffer cb=decoder.decode(bb);
			ByteBuffer outputData = encoder.encode( cb );
			fc.write(outputData);
				} catch(Exception e){
				 logger.error("文件写入错误!"+e.getStackTrace());	
					e.printStackTrace();	
					}
					finally{
							try{
								if(fc!=null)
								fc.close();
						}catch(Exception e){
								logger.error("通道关闭错误!"+e.getStackTrace());	
								e.printStackTrace();	
							}finally{
									try{
									if(os!=null)
									os.close();
									}catch(Exception e){
										logger.error("文件关闭错误!"+e.getStackTrace());	
										e.printStackTrace();	
									}
								}		
						}		
	}
	public final static void  WriteFile(File file,String content)
	{
		boolean append=false;
		FileChannel fc = null;
		FileOutputStream   os =null;
		  try {
				os = new FileOutputStream(file,append);
				fc = os.getChannel();
				Charset gbk = Charset.forName( "GBK" );
				CharsetDecoder decoder = gbk.newDecoder();
				CharsetEncoder encoder = gbk.newEncoder();
				ByteBuffer bb=ByteBuffer.wrap(content.getBytes());
				CharBuffer cb=decoder.decode(bb);
				ByteBuffer outputData = encoder.encode( cb );
				fc.write(outputData);
		   } catch(Exception e){
				 logger.error("文件写入错误!"+e.getStackTrace());	
					e.printStackTrace();	
					}
					finally{
							try{
								if(fc!=null)
								fc.close();
						}catch(Exception e){
								logger.error("通道关闭错误!"+e.getStackTrace());	
								e.printStackTrace();	
							}finally{
									try{
									if(os!=null)
									os.close();
									}catch(Exception e){
										logger.error("文件关闭错误!"+e.getStackTrace());	
										e.printStackTrace();	
									}
								}		
						}		
	}
			
	public final static String ReadFile(String file)
	{
		String result="";
		File fileObj=null;
		FileInputStream fin=null;
		FileChannel fcin=null;
		try{
			fileObj=new File(file);
			fin=new FileInputStream(fileObj);
			fcin = fin.getChannel(); 
			MappedByteBuffer mbb=fcin.map(FileChannel.MapMode.READ_ONLY,0,fileObj.length());
			if(!mbb.isLoaded())
				mbb.load();
				byte[] value=new byte[(int)fileObj.length()-1];
				mbb.get(value);
				result= new String(value);
		}catch(IOException e){
			e.printStackTrace();			
			}finally{
					try{
						if(fcin!=null)
							fcin.close();
					}catch(IOException e){
						logger.error("文件关闭错误!"+e.getStackTrace());	
						e.printStackTrace();
						}finally{
								try{
									if(fin!=null)
										fin.close();
									}catch(IOException e){
										logger.error("文件关闭错误!"+e.getStackTrace());	
										e.printStackTrace();
									}
								
						}			
				}
			return result;
	}
}