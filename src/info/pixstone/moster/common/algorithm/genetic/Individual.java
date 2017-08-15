package info.pixstone.moster.common.algorithm.genetic;


/**
*	������
*
*/
import java.io.*;
import java.util.*;


public abstract class Individual implements Comparator<Individual>,Cloneable{

	private Object phenotype; //������
	private String genes;//����
	private static CodeConverter converter;//ת�빤��
	private boolean CodeState;//�������״̬
	
	public Individual(Object phenotype,CodeConverter converter,boolean CodeState){
		this.phenotype=phenotype;
		this.converter=converter;
		this.CodeState=CodeState;
		if(CodeState)	this.genes=this.converter.encode(phenotype.toString());
		this.genes=phenotype.toString();
	}
	public Individual(Object phenotype){
		CodeConverter converter=new GrayCodeConverterImpl();
		this.phenotype=phenotype;
		this.converter=converter;
		this.CodeState=true;
		if(CodeState)	this.genes=this.converter.encode(phenotype.toString());
		this.genes=phenotype.toString();
	}
	
	public Individual(Object phenotype,boolean CodeState){
		CodeConverter converter=new GrayCodeConverterImpl();
		this.phenotype=phenotype;
		this.converter=converter;
		this.CodeState=CodeState;
		if(CodeState)	this.genes=this.converter.encode(phenotype.toString());
		this.genes=phenotype.toString();
	}
	
	public String getGenes(){
		return this.genes;
	}
	public Object getPhenotype(){
		return this.phenotype;
	}
	
	public CodeConverter getCodeConverter(){
		return this.converter;
	}
	
	public boolean getCodeState(){
		return this.CodeState;
	}
	
	public void setGenes(String genes,Object phenotype){
		this.genes=genes;
		this.phenotype=phenotype;
	}
	
	public void setPhenotype(String genes,Object phenotype){
		this.genes=genes;
		this.phenotype=phenotype;
	}
	
	public abstract int compare(Individual o1, Individual o2) ;
	public abstract boolean equals(Individual o1) ;
	
	@Override
	public Object clone(){
	   Individual o = null;  
        try {  
            o = (Individual) super.clone();  

              }  
         catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
                return o;  
    }  
}