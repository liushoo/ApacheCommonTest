/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// scalastyle:off println
package com.ilotterytech.spark.sql.Hive
import java.io.File

import org.apache.spark.sql._
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.hive.test.TestHive.sql
import org.apache.spark.{SparkConf, SparkContext}
object HiveFromSpark {
  case class Consumet(id: Long,ticketId:Long)
  case class Record(key: Int, value: String,cont:Seq[Consumet] )
  //获得resources目录下kv1.txt
  val kv1Stream = HiveFromSpark.getClass.getResourceAsStream("/kv1.txt")
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("HiveFromSpark").setMaster("local")
    //sparkConf.set("spark.driver.extraClassPath", "/opt/cloudera/parcels/CDH/lib/hive/lib/*,/etc/hive/conf/hive-site.xml")
    sparkConf.set("spark.executor.extraClassPath", "/opt/cloudera/parcels/CDH/lib/hive/lib/*")
    sparkConf.set("spark.hadoop.user.name", "hive")
    val sc = new SparkContext(sparkConf)
    /**
      """
      CREATE DATABASE IF NOT EXISTS testdb;
      CREATE TABLE testdb.createdtable like default.src;
      INSERT INTO TABLE testdb.createdtable SELECT * FROM default.src;
      SELECT * FROM testdb.createdtable;
      DROP DATABASE IF EXISTS testdb CASCADE
      */
    /**sc.getConf.getExecutorEnv.foreach{case (key:String, value:String) =>
      println("++++"+key+"==="+value+"++++")
    }
    sc.getConf.getAll.foreach{case (key:String, value:String) =>
      println("++==++"+key+"==="+value+"++==++")
    }
    val hadoopconfig=sc.hadoopConfiguration.iterator()
    while (hadoopconfig.hasNext){
      val map=hadoopconfig.next()
      println("==="+map.getKey+"=="+map.getValue+"===")
    }*/
    val hiveContext = new HiveContext(sc)
    import hiveContext.sql
    import hiveContext.implicits._

    def _sqlContext: SQLContext = hiveContext
    val sqlContext = _sqlContext
  /*  sql("SHOW TABLES").toString
    sql("SELECT * FROM src").toString
    sql("drop TABLE  src")*/
   // sql("USE default")
    //sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING)")
    //LOCA本地数据位置,去LOCAL是hdfs位置
    //sql("LOAD DATA LOCAL INPATH 'examples/src/main/resources/kv1.txt' INTO TABLE src")
    //切换数据库

    // Queries are expressed in HiveQL
    val rddjava=sql(" select * from test.testa")
      .map(r => {
        // sql("select * from test.b001_selldata").show(false)
        val c = Consumet(1, 222)

        val b = Consumet(2, 333)
        Record(1, s"$r", List(c, b))
//        val person = new Person()
//        person.setAge(r.getAs[String]("stringfield").toInt)
//        person.setName(s"${r.getAs("intfield")}")
//        person
      })



    //java bean 创建DataFrame方式
    //val schemaPeople = hiveContext.createDataFrame(rddjava, classOf[Person])
    //schemaPeople.toDF().show(false)
/*
    val rdd=sql(" SELECT a.serial,a.station,a.issue1,a.issue2,a.st_order,a.ordertime,a.content,a.order_num FROM test.b001_selldata a where  a.ordertime>='2016-01-01' and   a.ordertime<='2016-01-2'")
      .map(r=>{
        // sql("select * from test.b001_selldata").show(false)
        val c=Consumet(1,222)

        val b=Consumet(2,333)
        Record(1, s"$r",  List(c,b))
      })

    rdd.toDF().registerTempTable("records")

    val Ticket= sql(
      """
        |select ints.id,ints.ticketId from records
        |
        |
      """.stripMargin)
  //  Ticket.write.insertInto("ticket");

    val consumet= sql(
      """
        |select ints.id,ints.ticketId from records LATERAL VIEW explode(cont) a AS ints
        |
        |
      """.stripMargin)*/
   // consumet.write.insertInto("consumet");




   /* println("Result of 'SELECT *': ")
    println("TimeMillis1:"+System.currentTimeMillis())
    sql("""SELECT CURRENT_TIMESTAMP().getTimestamp().getTime()""").show(false)
    println("===="+sql("""SELECT CURRENT_TIMESTAMP().getTimestamp().getTime()""").collect().head.getTimestamp(
      0).getTime)
    sql("""SELECT CURRENT_DATE(),CURRENT_TIMESTAMP()""").show(false)
    println("TimeMillis2:"+System.currentTimeMillis())*/
/*    //sql("drop TABLE  src")
    sql("SHOW DATABASES").collect().foreach(x=>print("===SHOW DATABASES==="+x))
    println("=======show tables=========")
    sql("SHOW TABLES").collect().foreach(x=>println("=====SHOW TABLES========"+x))
    println("=======end show tables=========")
   // Aggregation queries are also supported.
    //聚集查询也支持
    val count = sql("SELECT COUNT(*) FROM src").collect().head.getLong(0)
    println(s"COUNT(*): $count")
    val rddFromSql = sql("SELECT key, value FROM src WHERE key < 10 ORDER BY key")
       println("Result of RDD.map:")
       val rddAsStrings = rddFromSql.map {
         case Row(key: Int, value: String) => s"Key: $key, Value: $value"
       }
       rddAsStrings.collect().foreach(println)
       // You can also register RDDs as temporary tables within a HiveContext.
       val rdd = sc.parallelize((1 to 100).map(i => Record(i, s"val_$i")))
       rdd.toDF().registerTempTable("records")
       // Queries can then join RDD data with data stored in Hive.
       //查询可以加入RDD数据与存储在Hive数据
       println("Result of SELECT *:")
       sql("SELECT * FROM records r JOIN src s ON r.key = s.key").collect().foreach(println)*/
    sc.stop()
  }
}
// scalastyle:on println
