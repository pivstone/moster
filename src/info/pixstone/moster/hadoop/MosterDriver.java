package info.pixstone.moster.hadoop;

import org.apache.hadoop.util.ProgramDriver;
import info.pixstone.moster.hadoop.driver.*;

public  class MosterDriver {
  
	public static void main(String argv[]){
		int exitCode = -1;
		ProgramDriver pgd = new ProgramDriver();
		try {
					pgd.addClass("DataFilter", DataFilterDriver.class, 
					   "A Moster-DataFilter for Filter CSV"); 
					pgd.addClass("DateMerge", DateMergeDriver.class, 
					   "A Moster-DateMerge for Merge Some Column in CSV"); 
					pgd.addClass("PointBuffer", PointBufferDriver.class, 
					   "A Moster-PointBuffer for Point Buffer ");
					pgd.addClass("BigJoinSmall", BigJoinSmallDriver.class, 
					   "A Moster-PointBuffer for Data Join(A Small Size Data and  A bigData ) ");
					pgd.addClass("JoinData", JoinDataDriver.class, 
					   "A Moster-DataJoin for Data Join ");
					pgd.addClass("JoinDataByDate", JoinDataByDateDriver.class, 
					   "A Moster-DataJoin for Data Join By Date ");
					pgd.addClass("GBK", GBKDriver.class, 
					   "A Moster for GBK Support");
					pgd.addClass("ClimateDriver", ClimateDriver.class, 
					   "A Moster for ClimateDriver case");
					pgd.addClass("XY2Point", XY2PointDriver.class, 
					   "A Moster Tool: (X,Y) convert to WKT Format .e.g: Point(X Y)");
					pgd.addClass("Typhoon", TyphoonDriver.class, 
					   "A Moster TyhoonData Case");
					pgd.addClass("GABP", BPAnnDriver.class, 
					   "A Moster GABP Case");
				   pgd.addClass("DateAvg", DateAvgDriver.class, 
					   "A Moster calculate Avg by Date");
				pgd.driver(argv);					   
				exitCode = 0;
		}
		catch(Throwable e){
		  e.printStackTrace();
		}
		System.exit(exitCode);
	}
}