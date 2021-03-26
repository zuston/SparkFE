/*
 * Copyright 2021 4Paradigm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com._4paradigm.hybridse.spark.api

import com._4paradigm.hybridse.spark.SchemaUtil
import com._4paradigm.hybridse.spark.SchemaUtil
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.slf4j.LoggerFactory

case class NativeDataframe(nativeSession: NativeSession, sparkDf: DataFrame) {

  private val logger = LoggerFactory.getLogger(this.getClass)
  private var tableName: String = "table"

  /**
   * Register the dataframe with name which can be used for sql.
   *
   * @param name
   */
  def createOrReplaceTempView(name: String): Unit = {
    tableName = name
    // Register for Spark SQL
    sparkDf.createOrReplaceTempView(name)

    // Register for NativeSpark SQL
    nativeSession.registerTable(name, sparkDf)
  }

  def tiny(number: Long): NativeDataframe = {
    sparkDf.createOrReplaceTempView(tableName)
    val sqlCode = s"select * from ${tableName} limit ${number};"
    new NativeDataframe(nativeSession, nativeSession.sparksql(sqlCode).sparkDf)
  }

  /**
   * Save the dataframe to file with Spark API.
   *
   * @param path
   * @param format
   * @param mode
   * @param renameDuplicateColumns
   * @param partitionNum
   */
  def write(path: String,
           format: String = "parquet",
           mode: String = "overwrite",
           renameDuplicateColumns: Boolean = true,
           partitionNum: Int = -1): Unit = {

    var df = sparkDf
    if (renameDuplicateColumns) {
      df = SchemaUtil.renameDuplicateColumns(df)
    }

    if (partitionNum > 0) {
      df = df.repartition(partitionNum)
    }

    format.toLowerCase match {
      case "parquet" => df.write.mode(mode).parquet(path)
      case "csv" => df.write.mode(mode).csv(path)
      case "json" => df.write.mode(mode).json(path)
      case "text" => df.write.mode(mode).text(path)
      case "orc" => df.write.mode(mode).orc(path)
      case _ => Unit
    }
  }

  /**
   * Run Spark job without other operators.
   */
  def run(): Unit = {
    nativeSession.getSparkSession.sparkContext.runJob(sparkDf.rdd, { _: Iterator[_] => })
  }

  /**
   * Show with Spark API.
   */
  def show(): Unit = {
    sparkDf.show()
  }

  /**
   * Count with Spark API.
   *
   * @return
   */
  def count(): Long = {
    sparkDf.count()
  }

  /**
   * Sample with Spark API.
   *
   * @param fraction
   * @param seed
   * @return
   */
  def sample(fraction: Double, seed: Long): NativeDataframe = {
    new NativeDataframe(nativeSession, sparkDf.sample(fraction, seed))
  }

  /**
   * Sample with Spark API.
   *
   * @param fraction
   * @return
   */
  def sample(fraction: Double): NativeDataframe = {
    new NativeDataframe(nativeSession, sparkDf.sample(fraction))
  }

  /**
   * Describe with Spark API.
   *
   * @param cols
   * @return
   */
  def describe(cols: String*): NativeDataframe = {
    new NativeDataframe(nativeSession, sparkDf.describe(cols:_*))
  }

  /**
   * Print Spark plan with Spark API.
   *
   * @param extended
   */
  def explain(extended: Boolean = false): Unit = {
    sparkDf.explain(extended)
  }

  def summary(): NativeDataframe = {
    new NativeDataframe(nativeSession, sparkDf.summary())
  }

  /**
   * Cache the dataframe with Spark API.
   *
   * @return
   */
  def cache(): NativeDataframe = {
    new NativeDataframe(nativeSession, sparkDf.cache())
  }

  /**
   * Collect the dataframe with Spark API.
   *
   * @return
   */
  def collect(): Array[Row] = {
    sparkDf.collect()
  }

  /**
   * Return the string of Spark dataframe.
   *
   * @return
   */
  override def toString: String = {
    sparkDf.toString()
  }

  /**
   * Get Spark dataframe object.
   *
   * @return
   */
  def getSparkDf(): DataFrame = {
    sparkDf
  }

  /**
   * Get session object.
   *
   * @return
   */
  def getNativeSession(): NativeSession = {
    nativeSession
  }

  /**
   * Get Spark session object.
   *
   * @return
   */
  def getSparkSession(): SparkSession = {
    nativeSession.getSparkSession
  }

  /**
   * Get Spark dataframe scheme json string.
   *
   * @return
   */
  def schemaJson: String = {
    sparkDf.queryExecution.analyzed.schema.json
  }

  /**
   * Print Spark codegen string.
   */
  def printCodegen: Unit = {
    sparkDf.queryExecution.debug.codegen
  }

}
