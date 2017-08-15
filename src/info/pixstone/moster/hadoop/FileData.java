package info.pixstone.moster.hadoop;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.ipc.Client;
import org.apache.log4j.Logger;
import org.apache.hadoop.util.Progressable;


public class FileData {

	private  final static  Logger logger  =  Logger.getRootLogger();
	private static long size=0L;
	public static void  CopyFromLocal(String localFile,String RemoteFile,Configuration conf) throws IOException
	{
		InputStream in=new BufferedInputStream(new FileInputStream(localFile));
		
		FileSystem fs=FileSystem.get(URI.create(RemoteFile),conf);
		logger.info("本地文件："+localFile);
		logger.info("远程文件："+RemoteFile);
		OutputStream out=fs.create(new Path(RemoteFile),new Progressable(){
			public void progress()
			{
				size=+64;
				logger.debug(size+"KB");
			}
		});
		
		IOUtils.copyBytes(in, out, 4096,true);
		logger.info("文件传输完成:"+RemoteFile);
	}

}
