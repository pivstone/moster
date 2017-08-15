package info.pixstone.moster.tool;

import java.io.IOException;

import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import org.geotools.geometry.jts.JTS;
import org.opengis.referencing.operation.TransformException;
import org.geotools.referencing.CRS;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import com.vividsolutions.jts.io.ParseException;
import org.apache.log4j.Logger;
import com.vividsolutions.jts.geom.Envelope;
import org.geotools.geometry.jts.GeometryClipper;

public class SpatialTool {
	public final static Logger logger=Logger.getLogger(SpatialTool.class);
	public final static String Buffer4WGS84(String WKTString,double distance){
		Geometry result=null;
		try{
				CRSAuthorityFactory   factory = CRS.getAuthorityFactory(true);
				CoordinateReferenceSystem WGS = factory.createCoordinateReferenceSystem("EPSG:4326");
				CoordinateReferenceSystem lambert = factory.createCoordinateReferenceSystem("EPSG:3395");	
				Envelope envelope=new Envelope(-20037508.3428d, 20037508.3428d, -15496570.7397d,18764656.2314d);
				GeometryClipper clipper=new GeometryClipper(envelope);
				MathTransform wgs2lmb = CRS.findMathTransform(WGS, lambert, false);
				MathTransform lmb2wgs = CRS.findMathTransform(lambert, WGS, false);	
				WKTReader reader=new WKTReader();
				Geometry geo=	reader.read(WKTString);
				Geometry target =JTS.transform(geo, wgs2lmb);
				Geometry bufferArea=target.buffer(distance);
				Geometry buffer=clipper.clip(bufferArea,false);
				result=JTS.transform(buffer, lmb2wgs);		
			}
			catch(TransformException e)
			{
				logger.warn("空间参考坐标转换失败!"+e.getStackTrace());
			}
			catch(NoSuchAuthorityCodeException e)
			{
				logger.warn("空间参考坐标解码失败，没有改类空间参考坐标系统编码!"+e.getStackTrace());
			}	
			catch(FactoryException e)
			{
				logger.warn("空间参考坐标转换矩阵获取失败!"+e.getStackTrace());
			}catch(ParseException e)
			{
				logger.warn("WKT字符串解析失败，原字符串为："+WKTString+e.getStackTrace());
			}
		if(result!=null)
			return result.toText();
		else
			return "";
			}		
	public final static int getSpatialRelationship(String WKTLeft,String WKTRight){
		int result=0;
		try{
			WKTReader reader=new WKTReader();
			Geometry left=	reader.read(WKTLeft);
			Geometry right=	reader.read(WKTRight);
			
			if(left.contains(right)) result=SpatialRelationShip.CONTAINS;
			
			if(left.coveredBy(right))result=SpatialRelationShip.COVERED;
			
			if(left.covers(right))result=SpatialRelationShip.COVERS;
			
			if(left.crosses(right))result=SpatialRelationShip.CROSSES;
			
			if(left.disjoint(right))result=SpatialRelationShip.DISJOINT;
			
			if(left.intersects(right))result=SpatialRelationShip.INTERSECTS;
			
			if(left.overlaps(right))result=SpatialRelationShip.OVERLAPS;
			
			if(left.touches(right))result=SpatialRelationShip.TOUCHES;
			
			if(left.within(right))result=SpatialRelationShip.WITHIN;
			
		}catch(ParseException e){
				logger.warn("WKT字符串解析失败"+e.getStackTrace());
		}
		return result;
	}
	
	public final static String WKTMerge(String WKTs)	{
		String result="";
		Geometry[] geos=null;
		try{
			String[] content=WKTs.split("\r\n");
			GeometryFactory gf=new GeometryFactory();
			geos=new Geometry[content.length];
			WKTReader reader=new WKTReader();
			for(int i=0;i<content.length;i++){
				geos[i]=reader.read(content[i]);
			}
			GeometryCollection gc=gf.createGeometryCollection(geos);
			result=gc.buffer(0).toText();
		}catch(ParseException e){
				logger.warn("WKT字符串解析失败，原字符串为："+WKTs+e.getStackTrace());
				}
		return result;
	}
	
	public static void main(String[] args) throws IOException{
				String masks=FileUtil.ReadFile("E:\\moster\\data\\xian_fjzq_Project.csv");
				String  left="POINT(119.46 27.32)";
				String mask=WKTMerge(masks);
				String taget=Buffer4WGS84(left,380000);
				
				System.out.println(taget);
				System.out.println(left);
				System.out.println(getSpatialRelationship(taget,mask));
	}
}