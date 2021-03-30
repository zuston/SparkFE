FROM maven:3.6-jdk-8

# Install dependencies
RUN apt update -y
RUN apt install -y python3 python-pip vim

# Install PySpark
RUN pip install pyspark==3.0.0

# Install Spark
RUN wget https://archive.apache.org/dist/spark/spark-3.0.0/spark-3.0.0-bin-hadoop2.7.tgz
RUN tar xzf ./spark-3.0.0-bin-hadoop2.7.tgz &&  rm ./spark-3.0.0-bin-hadoop2.7.tgz

# Install NativeSpark, 
# TODO: Change to downlaod NativeSpark distribution
COPY spark-3.0.0-bin-nativespark.tgz /
RUN tar xzf ./spark-3.0.0-bin-nativespark.tgz &&  rm ./spark-3.0.0-bin-nativespark.tgz

# Setup environment
ENV SPARK_HOME /spark-3.0.0-bin-nativespark/
ENV PATH $PATH:$SPARK_HOME/bin/

WORKDIR /spark-3.0.0-bin-nativespark/

CMD bash
