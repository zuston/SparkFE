FROM maven:3.6-jdk-8

# Install dependencies
RUN apt update -y
RUN apt install -y python3 python3-pip vim

# Install PySpark
RUN pip3 install pyspark==3.0.0

# Install Spark
RUN wget https://archive.apache.org/dist/spark/spark-3.0.0/spark-3.0.0-bin-hadoop2.7.tgz
RUN tar xzf ./spark-3.0.0-bin-hadoop2.7.tgz && rm ./spark-3.0.0-bin-hadoop2.7.tgz

# Install SparkFE
# TODO: Change to downlaod SparkFE distribution
COPY spark-3.0.0-bin-sparkfe.tgz /
RUN tar xzf ./spark-3.0.0-bin-sparkfe.tgz && rm ./spark-3.0.0-bin-sparkfe.tgz

# Setup environment
ENV SPARK_HOME /spark-3.0.0-bin-sparkfe/
ENV PATH $PATH:$SPARK_HOME/bin/

WORKDIR /spark-3.0.0-bin-sparkfe/

CMD bash
