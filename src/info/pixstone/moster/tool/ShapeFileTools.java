package info.pixstone.moster.tool;

import java.lang.String;
import java.lang.StringBuffer;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.shapefile.shp.ShapefileHeader;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.FeatureSource;

import org.opengis.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;



import java.nio.channels.FileChannel;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.io.ParseException;
import org.geotools.geometry.jts.WKTReader2;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTFileReader;

import java.nio.charset.Charset;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.net.URI;
import java.util.List;
import org.apache.hadoop.fs.Path;

import org.apache.log4j.Logger;

public  final class ShapeFileTools{
	
		private final static  Logger logger  =  Logger.getLogger(ShapeFileTools.class);
		public  static  void main( String args[] ) throws Exception 
		{
		//	Path p=new Path("E:\\hadoop-1.0.4\\hadoop-1.0.4\\docs\\api\\index.html");
		//	logger.warn("Test Messag:"+p.getName());
			ShpFeature2CSV("C:\\Users\\Administrator\\Desktop\\xian_fjzq\\xian_fjzq_Project.shp","E:\\");
			//WKTMerage("E:\\xian_fjzq_Project.csv");
		}
		
		public final static void ShpFeature2CSV(String ShpFile,String CSVPath)
		{
			ShapefileReader r =null;
			try{
				int lastIndex=ShpFile.indexOf(".");
				int firstIndex=ShpFile.lastIndexOf("\\");
				String filename=ShpFile.substring(firstIndex+1,lastIndex);
			
				File csvFile=	FileUtil.CreatFile(CSVPath+filename+"_shp.csv");
				GeometryFactory gf=new	GeometryFactory() ;
				ShpFiles shp=new ShpFiles(ShpFile);
				r = new ShapefileReader( shp,true,true,gf);
					while (r.hasNext()) {
						Geometry shape = (Geometry) r.nextRecord().shape();
						FileUtil.WriteFile(csvFile,shape.toText()+"\r\n",true);	
					} 
			}catch(IOException e){
				logger.warn("shp文件："+ShpFile+"读取异常"+e.getStackTrace());
				e.printStackTrace();
			}finally{
					try{
						if(r!=null)
							r.close();
					}catch(IOException e){
						logger.warn("记录指针关闭异常"+e.getStackTrace());
						e.printStackTrace();
				}		
			}
		}
		public final static void Dbf2CSV(String DbfFile,String CSVPath,String token)
		{
			DbaseFileReader r=null;
			Charset defaultCharset=java.nio.charset.Charset.defaultCharset();
			try{

				FileChannel in = new FileInputStream(DbfFile).getChannel();
				logger.info("读取Dbf文件："+DbfFile);
				
				int lastIndex=DbfFile.indexOf(".");
				int firstIndex=DbfFile.lastIndexOf("\\");
				String filename=DbfFile.substring(firstIndex+1,lastIndex);	
				File metaFile=	FileUtil.CreatFile(CSVPath+"Meta_"+filename+".csv");
				File csvFile=	FileUtil.CreatFile(CSVPath+filename+"_dbf.csv");
				
				r = new DbaseFileReader(in,true,defaultCharset);
				DbaseFileHeader h=r.getHeader();		
				FileUtil.WriteFile(metaFile,h.toString());		
				int NumFields=r.getHeader().getNumFields();		
					while (r.hasNext()) {
						Object[] fields = new Object[NumFields];					
						fields=r.readEntry();
						StringBuffer record=new StringBuffer();
						for(Object obj:fields){
							if(obj.toString()!=""&&obj.toString()!=null){
								record.append(obj+token);
							}
							else	{	
								record.append("NULL,");
							}
						}
						record.append("\r\n");
						FileUtil.WriteFile(csvFile,record.toString(),true);	
					} 
			}catch(IOException e){
						logger.warn("记录指针关闭异常"+e.getStackTrace());
						e.printStackTrace();
					}finally{
							try{
								if(r!=null)
									r.close();
							}catch(IOException e){
								logger.warn("记录指针关闭异常"+e.getStackTrace());
								e.printStackTrace();
							}		
				}
		}
		public final static void Shp2CSV(String ShpFile,String CSVPath)
		{
			try{
				URL url=new File(ShpFile).toURI().toURL();
				ShapefileDataStore sds=new ShapefileDataStore(url);
				String TypeName=sds.getTypeNames()[0];
				FeatureSource fs=sds.getFeatureSource(TypeName);
				FeatureCollection fc=fs.getFeatures();
				FeatureIterator fi=fc.features();
					while(fi.hasNext())	{
						Feature feature=fi.next();
				//		logger2.debug(feature.getProperties().toArray()[2]);
					}
				}catch(IOException e){
					logger.warn("Shapefile文件："+ShpFile+"读取异常"+e.getStackTrace());
					e.printStackTrace();
				}
				
		}
		public final static void WKTMerge(String WKTFile)
		{
			File file=new File(WKTFile);
			String feature=FileUtil.ReadFile(WKTFile);
			String data=SpatialTool.WKTMerge(feature);
			File result= FileUtil.CreatFile("Merge_"+file.getName());
			logger.info("要素合并完成，开始写入文件！");
			FileUtil.WriteFile(result,	data);		
		}		
}