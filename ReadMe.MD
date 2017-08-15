
### The Research of GIS Distributed Compute Based on Hadoop

Taking Typhoon Precipitation Model for Example 

Geographical Information System, School of Geographical Sciences

Abstract：In recent years, with the maturity and popularity of the distributed parallel computing technology,  computing intensive scientific research with new ideas, that the use of low-cost, scalable, distributed cluster instead of the traditional high-performance servers . The purpose of this paper is to you typhoon precipitation forecast, for example, research the possibility of space objects based on Hadoop distributed computing and effectiveness.In order to better evaluate the value of Hadoop in GIS research, the case study using parallel artificial neural network - genetic algorithm to simulate a sufficient density calculation.
Keywords：Typhoon  Precipitation  Hadoop  Genetic BP-ANN


### Something Important

The project is completed at my student age. So I didn't use maven to manage the dependencies and build process(I didn't know maven at that time). I was use ANT to compile code and publish jobs to the Hadoop Cluster.

And the project `NOT contained` the data. The data isn't open data and I have not been authorized to publish the data :(

### Special Thanks

- My Supervisor: Doctor.Zhang

- nchc.org.tw: Thanks for their Hadoop Cluster and help me to delete the huge out of controller log :P 

### Project Structure

- build 编译结果
- conf 配置文件
- data 项目数据
- dist 发行版目录
- lib 开发类库
- logs 开发日志文件
- relib 发行版类库
- src 源码

### Project Info 项目说明

- 项目是使用ANT 1.9 构建的
- 运行项目的时候要配置Build.xml里面的相关信息
- 项目中用到了ANT的SSH插件，如果使用原生ANT需要另外添加SSH插件
- 项目主函数入口为 MosterDriver，里面封装了项目用到的MapReduce计算的一些实现，使用参照Build.xml文件中的DataJoin脚本


###	几个实现的MapReduce

MapReduce实现名| 	实现类 | 描述
------- | -------| -------
DataFilter	|DataFilterDriver.class|	A Moster-DataFilter for Filter CSV 数据过滤器，提取指导列的数据; 
DateMerge|	DateMergeDriver.class|	A Moster-DateMerge for Merge Some Column in CSV;数据合并，将指定列数据合并成一列
PointBuffer|PointBufferDriver.class|	A Moster-PointBuffer for Point Buffer;XY点的缓冲区分析
BigJoinSmall|  BigJoinSmallDriver.class|	A Moster-PointBuffer for Data Join(A Small Size Data and  A bigData ) ;大数据集和小数据集的连接
JoinData|	JoinDataDriver.class|	A Moster-DataJoin for Data Join;  大小相等的数据集连接 Reduce侧连接
JoinDataByDate|	JoinDataByDateDriver.class|	A Moster-DataJoin for Data Join By Date; 使用日期作为联结键连接
GBK|	GBKDriver.class| A Moster for GBK Support;中文转码支持 GBK转UTF-8
ClimateDriver|	ClimateDriver.class| A Moster for ClimateDriver case; 气象数据处理
XY2Point|	XY2PointDriver.class| A Moster Tool: (X,Y) convert to WKT Format .e.g: Point(X Y); 将XY坐标转换成Point点
Typhoon|	TyphoonDriver.class|	A Moster TyhoonData Case; 台风缓冲区处理
GABP|	BPAnnDriver.class	|A Moster GABP Case; GABP算法实现
DateAvg|	DateAvgDriver.class|A Moster calculate Avg by Date; 求平均值



