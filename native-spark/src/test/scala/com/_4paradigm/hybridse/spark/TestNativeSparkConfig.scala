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

package com._4paradigm.hybridse.spark

import org.apache.spark.sql.SparkSession
import org.scalatest.FunSuite

class TestNativeSparkConfig extends FunSuite {

  test("test make config from spark") {
    val sess = SparkSession.builder()
        .config("spark.nativespark.group.partitions", 100)
        .config("spark.nativespark.test.print", value=true)
        .master("local[1]")
        .getOrCreate()
    val config = NativeSparkConfig.fromSparkSession(sess)
    assert(config.groupPartitions == 100)
    assert(config.print)
    sess.close()
  }

  test("test make config from dict") {
    val dict = Map(
      "nativespark.group.partitions" -> 100,
      "nativespark.test.print" -> true
    )
    val config = NativeSparkConfig.fromDict(dict)
    assert(config.groupPartitions == 100)
    assert(config.print)
  }

}
