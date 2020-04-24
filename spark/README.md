# Spark Project
## Table of Contents
* [Introduction](#introduction)
* [Spark Architecture](#spark-architecture)
* [Spark RDD Project](#spark-rdd-project)
* [Spark DataFrame Project](#spark-dataframe-df-project)

## Introduction
Apache Spark is a unified analytics engine for big data processing. It has built-in modules for streaming, SQL, machine learning and graph processing.
The focus of this project is to get familiarized with the concepts and usage of features of Spark by solving a series of queries. These queries are related to Resilient Distributed Data (RDD), SparkSQL and Sparks Structured APIs (Dataframes and Datasets). This project also helped me understanding Spark Architecture that is described in the next section. 
Besides Spark, this project also introduced me to multi paradigm programming language SCALA. It is a pure object oriented language, in the sense that every value is an object and functional language, in the sense that every function in Scala is a value. 


## Spark Architecture

![Spark Architecture](./assets/Spark_Architecture.png)


Spark Architecture consist of following components: 
__Spark Driver__ - Spark Driver is the JVM process running on the master node. It is the starting point of spark architecture. The spark application that has the main method is known as driver process. When Spark program is executed, the driver will be launched in the master node. This driver internally launches a spark service called SparkContext. 
The driver is responsible for creating SparkContext, executing code, creating RDDS, applying transformations or actions on RDDs.
SparkContext is the heart of spark application. Every spark application will have a SparkContext created at the start. It is treated as a gateway between driver and the cluster. 
Driver uses __SparkContext__ to communicate with the cluster manager (YARN, MESOS, Spark StandAlone) to acquire resources for executors. If SparkContext is killed, spark application will also be killed or stopped.

__Spark Executors__ - Spark executor is a place where all spark tasks run. TaskScheduler that is hosted by the driver will assign the tasks to the executors. If there is any failure in tasks during the execution, that task will be reattempted by another executor. If there is an executor failure, the driver will request the cluster manager to provide a resource for launching a new executor. 
Executors run till the application is completed. The number of executors is configurable via properties. Each executor is assigned required resources like RAM, CPU, cores, etc. Once the Spark application is completed, executors will be shut down and resources will be assigned back to the cluster manager.

__Cluster Manager__ - Cluster Manager is a platform where Spark application is running. It provides resources to all worker nodes as per need. In Spark, there are three types of cluster managers: 
* Standalone Cluster Manager
* Hadoop YARN
* Apache Mesos

__Spark Session__ - Spark is written in Scala, so translation is needed if code is written in other language. This is where SparkSession comes in picture. The code that is accepted by the Spark Application JVM will be translated by Spark session into lower-level instructions which is understood and executed by Spark executors.


## Spark RDD Project

### Resilient Distributed Dataset (RDD)
Resilient Distributed Dataset, also known as RDD, is a fundamental data structure of Spark. Each dataset in RDD is divided into logical partitions., which may be computed on different nodes of the cluster. RDDs may contain Python, Java, Scala objects. RDDs represent immutable, partitioned collection of elements that can be operated on in parallel. They are highly resilient, i.e., they are able to recover quickly from any issue as same chunks of data are replicated across multiple executor nodes. So, even if one executor node fails, another will still process the data.

### Project Details
RDD project helps in understanding the concept and usage of RDD by solving business questions related to data. It introduces with the concepts of transformations and actions.

### RDD Project Snapshot

![RDD Project](./assets/RDD_Project_Aayushi.png)


## Spark DataFrame (DF) Project

### Structured API
Structured APIs in Spark are set of Datasets, Dataframes and SQL. These help in manipulating either raw, semi-structured or highly structured data. Datasets and Dataframes are collections that are similar to real tables. They have well-defined rows and columns. The major difference between datasets and dataframes is the former checks for the conformation of data at compile time while the latter does it at runtime. SQL can be used to query datasets or dataframes using SparkQL or Spark Query Language. Spark translates these structured APIs into physical plan that can be read by the driver.

### Why Structured APIs are preferred over RDDs?
Sparks Structured APIs are preferred over RDDs because they are better optimized by catalysts. It is easier to work with them since they have columns and can execute fragments of SQL code directly. Also, there are less parsing issues encountered while working with Structured APIs as compared to RDDs.

### Project Details
This project serves as an introduction to Spark Structured APIs. It helps in getting familiarize with syntax and features of Structured APIs. More advanced business questions related to data were solved using Structured APIs in this project. In last few queries, graphs are plotted, instigating us with Zeppelins graph feature.

### DataFrame Project Snapshot

![DF Project](./assets/DF_Project_Aayushi.png)
