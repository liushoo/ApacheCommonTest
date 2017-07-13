package com.ilotterytech.spark.sql

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by liush on 17-7-3.
  */
object TestObject {
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("HiveFromSpark").setMaster("local")
    val sc = new SparkContext(sparkConf)
  }
}
