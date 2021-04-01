
<div align=center>![logo](./images/NativeSpark.png)</div>

* [**Slack Channel**](https://hybridsql-ws.slack.com/archives/C01R7L5SXPW)
* [**Discussions**](https://github.com/4paradigm/NativeSpark/discussions)
* [**README中文**](./README-CN.md)

## 介绍

NativeSpark是基于LLVM优化的Spark兼容的高性能原生执行引擎，解决Spark在AI场景落地的性能和一致性等问题。

![Architecture](./images/native_spark_architecture.png)

## 背景

为什么需要NativeSpark？

* Spark性能优化已经达到瓶颈，基于C++和LLVM开发原生执行引擎可以更好地利用CPU和GPU等硬件加速，实现数倍的性能提升。
* Spark专注成为通用的离线计算框架，对机器学习的特征抽取方法和特征上线支持不足，NativeSpark能够弥补缺陷让Spark在生产环境更好地落地。
* 对比其他原生执行引擎，如Intel OAP和Nvidia spark-rapids等，NativeSpark重写SQL优化和基于LLVM即时编译代码，在性能和灵活性上都有更好的表现。

NativeSpark受众是谁？

* Spark用户，NativeSpark兼容大部分SparkSQL语法，只要是Spark用户就可以使用NativeSpark加速，并且不需要修改应用代码。
* Spark开发者，熟悉Spark源码或LLVM JIT的开发者，都可以参与NativeSpark开源项目，实现更多Spark的性能加速和功能拓展。
* 使用Spark做机器学习的用户，NativeSpark提供针对AI场景落地的语法拓展和高性能特征抽取实现，能够解决离线特征上线时的一致性问题。

NativeSpark为什么快？

* NativeSpark基于C++和LLVM开发，能针对不同硬件进行编译优化和二进制码生成，重写了SQL编译和表达式优化过程，对Spark不支持CodeGen的物理节点也在NativeSpark中实现了，比较创新地在Spark上实现了多窗口并行计算以及窗口数据倾斜优化，还有更高效的内存管理以及避免内存垃圾回收等优化。

NativeSpark有什么特点？

* **高性能**

    基于LLVM优化，在相同硬件下部分场景性能比Spark快6倍以上，用户享受更低的运行时间和TCO成本消耗。
    
* **零迁移成本**

    无论是Scala、Java、Python还是R语言实现的SparkSQL应用，都不需要修改源码或重新编译，替换SPARK_HOME即可享受原生执行引擎加速。
    
* **针对机器学习优化**
  
    提供机器学习场景常用的特殊拼表操作以及定制化UDF/UDAF支持，基本满足生产环境下机器学习特征抽取的应用需求。

* **离线在线一致性**
  
    结合HybridSE和FEDB，使用NativeSpark开发的机器学习应用支持一键上线，保证离线在线一致性，大大降低机器学习场景落地成本。

* **Upstream同步** 
  
    兼容Spark 3.0及后续版本，保证Spark基础功能与社区主干同步，特殊场景也可以回滚享受Spark本身的优化和加速。

## 快速开始

### 使用Docker镜像

运行官方的NativeSpark容器镜像。

```bash
docker run -it ghcr.io/4paradigm/nativespark bash
```

直接执行Spark命令即可，默认使用NativeSpark进行SQL优化加速。

```bash
$SPARK_HOME/bin/spark-submit \
  --master local \
  --class org.apache.spark.examples.sql.SparkSQLExample \
  $SPARK_HOME/examples/jars/spark-examples*.jar
```

### 使用ativeSpark发行版

从[Releases页面](https://github.com/4paradigm/NativeSpark/releases)下载预编译包，解压后可执行Spark命令。

```bash
tar xzvf ./native-spark-3.0.0-bin-hadoop2.7.tar.gz

export SPARK_HOME=`pwd`/native-spark-3.0.0-bin-hadoop2.7/

$SPARK_HOME/bin/spark-submit \
  --master local \
  --class org.apache.spark.examples.sql.SparkSQLExample \
  $SPARK_HOME/examples/jars/spark-examples*.jar
```

## 性能测试

NativeSpark在AI计算场景上比开源Spark有明显的性能提升，性能测试结果如下。

![Benchmark](./images/native_spark_benchmark.jpeg)

本地可进行性能测试复现结果，步骤如下。

```bash
docker run -it ghcr.io/4paradigm/nativespark bash

git clone https://github.com/4paradigm/NativeSpark.git 
cd ./NativeSpark/benchmark/taxi_tour_multiple_window/

wget http://103.3.60.66:8001/nativespark_resources/taxi_tour_parquet.tar.gz
tar xzvf ./taxi_tour_parquet.tar.gz

export SPARK_HOME=/spark-3.0.0-bin-hadoop2.7/
./submit_spark_job.sh

export SPARK_HOME=/spark-3.0.0-bin-nativespark/
./submit_spark_job.sh
```

## 项目贡献

从源码编译native-spark模块。

| 操作系统 | 编译命令 |
| ------- | ------- |
| Linux	  | mvn clean package|
| MacOS   | mvn clean package -Pmacos |
| All in one | mvn clean package -Pallinone |

从源码编译NativeSpark发行版。

```bash
git clone --recurse-submodules git@github.com:4paradigm/NativeSpark.git

cd ./spark/

./dev/make-distribution.sh --name nativespark --pip --tgz -Phadoop-2.7 -Pyarn
```

详细介绍参考[NativeSpark官方文档](https://docs.fedb.io/nativespark)。

## 未来规划

### SQL兼容

NativeSpark兼容大部分SparkSQL应用，未来将逐步增强对ANSI SQL语法的兼容性，从而降低Spark用户的迁移成本。

* [2021H1&H2]完善Window的标准语法，支持Where, Group By, Join等操作
* [2021H1&H2]针对AI场景扩展特有的语法特性和UDAF函数

### 性能优化

NativeSpark基于C++和LLVM优化性能比其他JVM实现高很多，未来将进一步优化减少跨语言调用开销和支持异构计算硬件。

* [2021H1]多编码格式支持兼容Spark UnsafeRow内存格式
* [2021H1]自动优化拼表数据倾斜和窗口数据倾斜场景
* [2021H1]针对机器学习场景的Native LastJoin优化Pass集成
* [2021H2]全流程列式存储格式支持，降低OLAP系统读写文件开销以及支持CPU向量化计算
* [2021H2]异构计算硬件支持

### 生态集成

NativeSpark目前兼容Spark应用生态，未来将与更多开源系统对接集成，满足客户的应用落地需求。

* [2021H2]Spark多版本集成和预编译包下载

## 许可证

[Apache License 2.0](./LICENSE)
