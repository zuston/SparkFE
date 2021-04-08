# Taxi Tour Multiple Window

This uses the public taxi tour dataset and simple SQL query with multiple over window for benchmark.

Download and extract the test dataset.

```
wget http://103.3.60.66:8001/sparkfe_resources/taxi_tour_parquet.tar.gz
tar xzvf ./taxi_tour_parquet.tar.gz
```

Run the benchmark Spark application.

```
./submit_spark_job.sh
```

