
The Research of GIS Distributed Compute Based on Hadoop
Taking Typhoon Precipitation Model for Example 

Student:WANG Xiao-lei    Supervisor:ZHANG Ming-feng

Geographical Information System, School of Geographical Sciences

  Abstract��In recent years, with the maturity and popularity of the distributed parallel computing technology,  computing intensive scientific research with new ideas, that the use of low-cost, scalable, distributed cluster instead of the traditional high-performance servers . The purpose of this paper is to you typhoon precipitation forecast, for example, research the possibility of space objects based on Hadoop distributed computing and effectiveness.In order to better evaluate the value of Hadoop in GIS research, the case study using parallel artificial neural network - genetic algorithm to simulate a sufficient density calculation.
   Keywords��Typhoon  Precipitation  Hadoop  Genetic BP-ANN


=============================================
	�ļ��нṹ
=============================================

bulid ������
conf �����ļ�
data ��Ŀ����
dist ���а�Ŀ¼
lib �������
logs ������־�ļ�
relib ���а����
src Դ��

============================================
	��Ŀ˵��
============================================

1����Ŀ��ʹ��ANT 1.9 ������
2��������Ŀ��ʱ��Ҫ����Build.xml����������Ϣ
3����Ŀ���õ���ANT��SSH��������ʹ��ԭ��ANT��Ҫ�������SSH���
4����Ŀ���������Ϊ MosterDriver�������װ����Ŀ�õ���MapReduce�����һЩʵ�֣�ʹ�ò���Build.xml�ļ��е�DataJoin�ű�

============================================
	����ʵ�ֵ�MapReduce
--------------------------------------------
MapReduceʵ����	ʵ����			����
--------------------------------------------

"DataFilter"	DataFilterDriver.class	"A Moster-DataFilter for Filter CSV" ���ݹ���������ȡָ���е�����; 
"DateMerge"	DateMergeDriver.class	"A Moster-DateMerge for Merge Some Column in CSV");���ݺϲ�����ָ�������ݺϲ���һ��
"PointBuffer"	PointBufferDriver.class	"A Moster-PointBuffer for Point Buffer ");XY��Ļ���������
"BigJoinSmall"  BigJoinSmallDriver.class	"A Moster-PointBuffer for Data Join(A Small Size Data and  A bigData ) ");�����ݼ���С���ݼ�������
"JoinData"	JoinDataDriver.class	"A Moster-DataJoin for Data Join ");  ��С��ȵ����ݼ����� Reduce������
"JoinDataByDate"	JoinDataByDateDriver.class	"A Moster-DataJoin for Data Join By Date "); ʹ��������Ϊ���������
"GBK"	GBKDriver.class	 "A Moster for GBK Support");	����ת��֧�� GBKתUTF-8
"ClimateDriver"	ClimateDriver.class	"A Moster for ClimateDriver case"); �������ݴ���
"XY2Point"	XY2PointDriver.class	"A Moster Tool: (X,Y) convert to WKT Format .e.g: Point(X Y)"); ��XY����ת����Point��
"Typhoon"	TyphoonDriver.class	"A Moster TyhoonData Case"); ̨�绺��������
"GABP"	BPAnnDriver.class	"A Moster GABP Case");	GABP�㷨ʵ��
"DateAvg"	DateAvgDriver.class	"A Moster calculate Avg by Date");��ƽ��ֵ

______________________________________
| E-Mail��kocio@vip.qq.com
| Author:��С��
| Ver��0.42
| 2013-06-05 
--------------------------------------