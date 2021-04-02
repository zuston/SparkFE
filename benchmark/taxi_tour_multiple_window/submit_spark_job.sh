#!/bin/bash

set -x -x

time $SPARK_HOME/bin/spark-submit \
     --master local[*] \
     ./multiple_window.py
