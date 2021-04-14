
<div align=center><img src="./images/sparkfe_logo.png"/></div>

* [**Slack频道**](https://hybridsql-ws.slack.com/archives/C01TMST8AE7)
* [**项目讨论**](https://github.com/4paradigm/SparkFE/discussions)
* [**README in English**](./README.md)

## 介绍

SparkFE是面向特征工程场景的基于LLVM优化的高性能Spark原生执行引擎。

Spark发展迅速逐渐成为大数据处理领域的事实标准，然而它并非为机器学习场景设计，在AI场景有越来越多的限制。SparkFE使用C++重写了底层执行引擎，在特征抽取上获得超过6倍的性能提升，并且能够保证离线在线一致性，让AI场景落地更加容易。想了解更多细节，请参考[SparkFE中文文档](https://docs.fedb.io/v/zh-hans/sparkfe)。

![Architecture](./images/sparkfe_architecture.png)

## 特性

* **高性能**

    基于LLVM优化，在相同硬件下部分场景性能比Spark快6倍以上，同一个应用的运行时间更短且TCO成本更低。
    
* **零迁移成本**

    Scala、Java、Python或R语言实现的SparkSQL应用，不需要修改源码和重新编译，替换SPARK_HOME即可享受原生执行引擎加速。
    
* **针对机器学习优化**
  
    提供机器学习场景常用的特殊拼表操作以及定制化UDF/UDAF支持，基本满足生产环境下机器学习场景特征工程的研发需求。

* **离线在线一致性**
  
    结合[FEDB](https://github.com/4paradigm/fedb)，使用SparkFE开发的机器学习应用支持一键上线，保证离线在线一致性，大大降低机器学习场景的落地成本。

* **Upstream同步** 
  
    兼容Spark 3.0及后续版本，保证Spark基础功能与社区上游同步，特殊场景也可以回退使用Spark本身的优化。

## 性能

SparkFE在AI计算场景上比开源Spark有明显的性能提升，部分性能测试结果如下。

![Benchmark](./images/sparkfe_benchmark.png)

本地可进行性能测试复现结果，步骤如下。

```bash
docker run -it 4pdosc/sparkfe bash

git clone https://github.com/4paradigm/SparkFE.git 
cd ./SparkFE/benchmark/taxi_tour_multiple_window/

wget http://103.3.60.66:8001/sparkfe_resources/taxi_tour_parquet.tar.gz
tar xzvf ./taxi_tour_parquet.tar.gz

export SPARK_HOME=/spark-3.0.0-bin-hadoop2.7/
./submit_spark_job.sh

export SPARK_HOME=/spark-3.0.0-bin-sparkfe/
./submit_spark_job.sh
```

## 快速开始

### 使用Docker镜像

运行官方的[SparkFE容器镜像](https://hub.docker.com/r/4pdosc/sparkfe)。

```bash
docker run -it 4pdosc/sparkfe bash
```

直接执行Spark命令即可，默认使用SparkFE进行SQL优化加速。

```bash
$SPARK_HOME/bin/spark-submit \
  --master local \
  --class org.apache.spark.examples.sql.SparkSQLExample \
  $SPARK_HOME/examples/jars/spark-examples*.jar
```

### 使用SparkFE发行版

从[Releases页面](https://github.com/4paradigm/SparkFE/releases)下载预编译包，解压后可执行Spark命令。

```bash
tar xzvf ./spark-3.0.0-bin-sparkfe.tgz

export SPARK_HOME=`pwd`/spark-3.0.0-bin-sparkfe/

$SPARK_HOME/bin/spark-submit \
  --master local \
  --class org.apache.spark.examples.sql.SparkSQLExample \
  $SPARK_HOME/examples/jars/spark-examples*.jar
```

## 参与贡献

使用官方镜像参与项目开发。

```
docker run -it 4pdosc/sparkfe bash

git clone --recurse-submodules git@github.com:4paradigm/SparkFE.git
cd ./SparkFE/sparkfe/
```

从源码编译sparkfe模块。

| 操作系统 | 编译命令 | 备注 |
| ------- | ------ | ---- |
| Linux	  | mvn clean package| 支持CentOS 6、Ubuntu等Linux发行版 |
| MacOS   | mvn clean package -Pmacos | 支持macOS Big Sur以及后续版本 |
| All in one | mvn clean package -Pallinone | 同时支持Linux、MacOS操作系统 |

从源码编译SparkFE发行版。

```bash
cd ../spark/

./dev/make-distribution.sh --name sparkfe --pip --tgz -Phadoop-2.7 -Pyarn
```

## 未来规划

### SQL兼容

SparkFE兼容大部分SparkSQL应用，未来将继续完善与ANSI SQL语法的兼容性，从而降低开发者的迁移成本。

* [2021 H1&H2] 完善多种Window语法的支持，支持带复杂表达式的Where、GroupBy等语法。
* [2021 H1&H2] 针对AI场景扩展特征工程所需要的语法特性和UDF/UDAF函数。

### 性能优化

SparkFE基于C++和LLVM优化后性能提升明显，未来将进一步优化减少跨语言调用开销和支持异构计算硬件。

* [2021 H1] 支持多种行编码格式，兼容Spark UnsafeRow内存布局。
* [2021 H1] 自动优化窗口数据倾斜和拼表数据倾斜场景。
* [2021 H1] 集成针对机器学习场景的Native LastJoin优化过程。
* [2021 H2] 全流程列式存储格式支持，降低OLAP系统读写文件开销以及支持CPU向量化计算。
* [2021 H2] 异构计算硬件支持。

### 生态集成

SparkFE目前兼容Spark应用生态，未来将与更多开源系统对接集成，满足真实场景落地需求。

* [2021 H2] Spark多版本集成和提供预编译包下载。

## 许可证

[Apache License 2.0](./LICENSE)
