# Hadoop and HIVE Project
## Table of contents
- [Introduction](#introduction)
- [Hadoop Cluster](#hadoop-cluster)
	- [Cluster Architecture Diagram](#cluster-architecture-diagram)
	- [Hadoop Framework](#hadoop-framework)
	- [HDFS](#hdfs)
	- [MapReduce](#mapreduce)
	- [YARN](#yarn)
	- [HIVE](#hive)
- [Hardware Specifications](#hardware-specifications)
- [Hive Project](#hive-project)
	- [Introduction to Hive Project](#introduction-to-hive-project)
	- [Zeppelin Notebook](#zeppelin-notebook)
- [Improvements](#improvements)

## Introduction
The purpose of this project is to thoroughly understand the concepts of processing of BigData and distributed computing. I built a big data platform utilizing Apache Hadoop and its components like HDFS, MapReduce and YARN. A Hadoop cluster is provisioned on Google Cloud Platform(GCP) with 1 master node and 2 worker nodes. These nodes are managed by YARN. HIVE is used to read and write from HDFS by connecting to HIVE server via CLI(beeline) through SSH or Zeppelin Notebook through browser. I have used Zeppelin Notebook as it can be easily imported as JSON file.
I have used World Development Indicators(WDI) data set available on Google BigQuery. 
This dataset being very large in size with 21,759,408 rows and 2.21 GB storage size makes it challenging to perform queries efficiently using conventional methods. The whole objective is to analyze and optimize the data to increase HiveQL queries performance.

## Hadoop Cluster

### Cluster Architecture Diagram

![Hadoop Cluster Architecture](./Assets/Hadoop_Architecture.png)

### Hadoop Framework
Apache Hadoop is an open source framework written in Java. It allows distributed processing of massive amounts of data across multiple clusters of computers. The Hadoop Framework application provides a software framework for distributed storage and processing using MapReduce programming model. The storage part of Hadoop is known as Hadoop Distributed File System (HDFS). In 2010, YARN was introduced that manages computing resources in clusters and utilizing them for scheduling users application. 

### HDFS
HDFS is a filesystem designed for storing very large files with streaming data access patterns that run on clusters of commodity hardware. It is built around the idea that most efficient data processing pattern is write-once and read-many times pattern. HDFS cluster has two types of nodes operating in a master-slave pattern, as follows:
- __NameNode__: Also known as Master node. The namenode manages the filesystem namespace. It maintains the filesystem tree and the metadata for all the files and directories in the tree. The name node also knows the datanodes on which all the blocks for a given file are located.
- __DataNode__: Also known as worker node. These are workhorses of the filesystem. They store and retrieve blocks when the are told to do so by the namenode. The report back to the namenode periodically, with list of blocks they are storing.
	
### MapReduce
MapReduce is a programming model that allows developers to process large amount of data. MapReduce job processes data in 3 phases: The Mapping phase, The Shuffle phase and the reduce phase. 
- __The Map Phase__:  This phase consists of mappers that are user defined functions. It takes a part of data and applies business logic to it. The input data is converted into key-value pairs based on the mappers. This part is performed by data nodes. 
- __The Shuffle Phase__:  In this phase, the data is passed to the Reducers from the mappers. 
- __The Reduce Phase__:  In this phase, data is received from the mappers. Aggregation, filtering, sorting or combination of operations are applied on the data based on the requirements. The output of this reducer that is located at the namenode will be the final result.

_Note: Some jobs do not perform Reduce phase, like the ones where only map-side filtering is required._
### YARN
Apache YARN (Yet Another Resource Negotiator) is Hadoops cluster resource management system. It was introduced to improve MapReduce implementation and support other distributed computing paradigms as well. 
YARN provides its core services via two long-running daemons: The Resource Manager and The Node Managers. 
- __The Resource Manager__:  Each cluster has one resource manager to manage the use of resources across the cluster and assign resources to different jobs. Resource manager runs on master node and keeps track of workers location and their hardware usage. 
- __Node Manager__:  It runs on all the worker nodes in the cluster to launch and monitor containers. It gathers the hardware usage information and send it to resource manager. 

In this project, YARN allowed HIVE to run its queries through TEZ, without HIVE having to worry about allocating cluster resources directly. TEZ is an expansion of MapReduce that can process data in single job rather using multiple MapReduce jobs. 

#### Node Health Check
The node manager runs services to determine the health of the node it is executing on. It performs checks on the disk as well as on user specified tests . If any health checks fails, the Node Manager marks the node unhealthy and communicates this to the Resource Manager. Resource Manager, in turn, stops assigning containers to unhealthy nodes. Communication of the node status is done as the part of heartbeat between the Node Manager and the Resource Manager. When the heartbeat takes place, the status of both checks is used to determine the health of the node.

_Note: Since resource manager is the single point of failure in YARN, Application Masters come into picture. __The Application Master__ is responsible for the execution of single application. It asks for containers from the Resource Manager and executes specific programs on the obtained containers. The Application Master knows the application logic and thus it is framework-specific. For example, the MapReduce framework provides its own implementation of an Application Master. So using Application Master, YARN is spreading the metadata related to the running applications over the cluster. This reduces load on Resource manager and make it fast recoverable._
### HIVE
Apache Hive is a framework for data warehousing on top of Hadoop. It facilitates reading, writing, and managing large datasets residing in the distributed storage using SQL. Hive provides easy access to data via SQL, enabling data warehousing tasks such as Extract-Transform-Load (ETL), reporting and data analysis.In this project, Hive has performed queries by converting them into TEZ jobs. HIVE has 2 components: Hcatalog and WebHCat.
- __Hcatalog__:  Hcatalog is a table and storage management layer for Hadoop that enables users with different data processing tools including Pig and MapReduce to more easily read and write data on the grid. HCatalog also maintains the cache of HiveClients to talk to metastore. This cache behaviour can be modified using few parameters as follows: `hcatalog.hive.client.cache.expiry.time` that allows user to specify the number of seconds until a cache entry expires.
`hcatalog.hive.client.cache.disabled` allows user to disable cache altogether.
- __WebHCat__:  WebHCat provides a service that a user canuse to run Hadoop MapReduce (or YARN), Pig, Hive jobs. Using this, one can also perform Hive metadata operations using an HTTP (REST style) interface.
- __Beeline CLI__:  Beeline is one of the ways to connect to Hive through CLI. It is installed alongwith Hive. To establish a Hive connection, enter the following command on the command prompt - beeline -u "jdbc:hive2://jarvis-bootcamp-m:10000" . We can start using Hive, once the connection is established. 
- __Zeppelin Notebook__:  Zeppelin Notebooks provide an easy way to execute code in a wb notebook. Zeppelin can be used to execute scala, SQL, ssh commands and even schedule a job with cron to run at regular intervals. 
Using Beeline CLI, it is difficult to manage, execute and present large number of queries. This is one of the advantages of using Zeppelin notebooks. It helps us manage and present queries in a systematic way as the queries are organized into pages and paragraphs.  
- __Zeppelin Configs__:  Zeppeling runs on master node to establish Hive connection. A customized Hive interpreter is configured based o default JDBC interpreter. 
- __Zeppelin Usage__:   Zeppelin notebook, in our case, is organized into different paragraphs, each paragraph performing different HiveQL queries. There are few where ssh commands are run after putting %sh at the top of the paragraph. Each paragraph on the page is executed individually and sequentially.



## Hardware Specifications	
Hadoop cluster containing 1 master node and 2 worker nodes is created. Each node has 2 cores of 13 GB each and disk space of 100 GB is assigned.


## Hive Project
We have used WORLD DEVELOPMENT INDEX dataset which is dated from 1960 TO 2016. It is available on Google BigQuery and occupies 2.21GB storage space with 21,759,408 rows. 
The Hive project introduces us how to the execution of Hive queries and Zeppelin notebook and its features. 

### Introduction to Hive Project
We can divide Hive project in various parts as follows:
1. Loading data into HDFS
2. Perform Query optimizations.
3. Copy data between tables.  
4. Using Serializer/Deserialize (SerDe) to read data from a tabke and write it back to the HDFS in any custom format. SerDe interface allows user to instruct Hive on how a record should be processed.
5. Partitioning tables into related parts based on the values of columns.
6. Performance comparison between SparkQL and HiveQL.
	 
### Zeppelin Notebook
Below is the screenshot of Zeppelin Notebook explaining tasks likes Reading data from Google BigQuery, copying data between tables, loading data into HDFS, creating partitions and comparing for efficiency and various other performance comparisions.

![Zeppelin Notebook Screenshot](./Assets/ZeppelinNotebook_Aayushi.png)


## Improvements
* While comparing SparkSQL and HiveQL, we have noticed that SparkSQL has better performance than HiveQL and faster query execution. Hence, exploring Spark for large datasets can improve our performance.
* We can add more worker nodes for better performance.
* We can also consider using bucketing where Hive partitions are further subdivided into buckets, which gives the fine structure to Hive tables while performing queries on large datasets. It also helps user to maintain parts that are more manageable. User can set the size of buckets.
