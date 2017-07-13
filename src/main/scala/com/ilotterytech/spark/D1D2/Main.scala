package com.ilotterytech.spark.D1D2

/**
  * Created by ZhangXuan on 17-7-7
  */

import org.apache.spark.sql.Row
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

object Main {
    val DELIMITER = "\t"

    def main(args: Array[String]) {
        val sparkConf = new SparkConf()
        sparkConf.setAppName("D1-2-D2").setMaster("yarn-cluster")
        val sc = new SparkContext(sparkConf)

        val hiveContext = new HiveContext(sc)
        import hiveContext.sql

        val gameId = args(0)
        val region: String = if (args.length <= 1 || args(1) == null) "test" else args(1)
        val partNum: Int = if (args.length <= 2 || args(2) == null) 1 else args(2).toInt

        println("Spark will load " + region + "'s" + gameId + " game data into D1 in " + partNum + " number files")

   /*     val factory: GameFactory = SiLeGameFactoryFactory.getInstance.getFactory(gameId)
        val cateFactory: GameCategoryFactory = factory.getCategoryFactory
        val consume : LotteryConsume = new LotteryConsume*/

        //val fetchSql = "SELECT a.serial,a.station,a.issue1,a.issue2,a.st_order,a.ordertime,a.content,a.order_num FROM shanxi.b001_selldata a where a.ordertime>='2016-01-01' and a.ordertime<='2016-01-10'"
        val fetchSql = "SELECT a.serial,a.station,a.issue1,a.issue2,a.st_order,a.ordertime,a.flag5,a.content,a.order_num FROM " + region + "." + gameId.toLowerCase + "_selldata a"
        val rdd = sql(fetchSql).map(p => p).zipWithUniqueId().map {
            //case (Row(serial: String, station: String, issue1: String, issue2: String, st_order: Int, ordertime: String, flag5: Int, content: String, order_num: Int), uid : Long) => {
            case (row : Row, uid : Long) => {
                //生成票数据
            //    val id : Long = IdGeneratorUtils.genTimestampBasedId(uid)
             //   val ticket : String = generateTicketString(row, id, factory.getGameId)

                //生成消费注数据
                val content : String = row.getAs[String]("content")
                val flag5 : Int = row.getAs[Int]("flag5")
           //     val me : String = generateConsumeString(content, id, flag5, consume, cateFactory)

                //返回元组数据
            //    (ticket, me)
            }
        }

        rdd.cache()

        //rdd.map(p => p._1).coalesce(partNum).saveAsTextFile("/user/hdfs/temp/d1/" + gameId + "/ticket")
        //rdd.map(p => p._2).coalesce(partNum).saveAsTextFile("/user/hdfs/temp/d1/" + gameId + "/consume")

        sc.stop()
    }

    def generateTicketString(row : Row, id : Long, gameId : Int): String ={
        //(serial: String, station: String, issue1: String, issue2: String, st_order: Int, ordertime: String, content: String, order_num: Int)
        val buf : StringBuffer = new StringBuffer()
        buf.append(id)                                      //主键ID
            .append(DELIMITER)
            .append(row.getAs[String]("serial"))            //彩票原始编码号
            .append(DELIMITER)
            .append(61001)                                  //出票主机表Id
            .append(DELIMITER)
            .append(row.getAs[String]("station"))           //出票站点Id
            .append(DELIMITER)
            .append(row.getAs[String]("station") + "01")    //出票投注机Id
            .append(DELIMITER)
            .append(row.getAs[String]("ordertime"))         //出票时间
            .append(DELIMITER)
            .append(row.getAs[String]("issue1"))            //销售期号
            .append(DELIMITER)
            .append(row.getAs[String]("issue2"))            //有效期号
            .append(DELIMITER)
            .append("NULL")                                 //开奖时间
            .append(DELIMITER)
            .append(gameId)                                 //游戏Id
            .append(DELIMITER)
            .append("NULL")                                 //操作员Id
            .append(DELIMITER)
            .append(row.getAs[Int]("st_order"))             //店流水号
            .append(DELIMITER)
            .append("NULL")                                 //期流水号
            .append(DELIMITER)
            .append(row.getAs[Int]("order_num") * 2)        //总金额

        buf.toString
    }

    def generateConsumeString(content : String, id : Long, flag5 : Int) = {
       // val cont : Array[String] = factory.decodeContent(content)
      /* var sum: Int = 0
     var s : String = null
     val buf : StringBuffer = new StringBuffer()
     for (i <- 0 until cont.length) {
         //循环处理每一个消费注
     val prefix : String = factory.decodeCategoryPrefix(s)
         val decoder : GameCategoryDecoder = factory.getCategoryDecoder(prefix)
         if (decoder == null){
             println("can not found " + prefix + " decoder")
         }
         else {
             try {
                // decoder.decode(s, consume)
             }
             catch {
                 case e : Exception => println("decode error : " + e.getMessage)
             }

           if (consume == null || consume.getTotalNum == null){
                 println("decode [" + prefix + "] error and ora content is " + content)
             }
             sum += consume.getTotalNum
             val cid: Long = IdGeneratorUtils.genUniqueBasedId(id, i)
             buf.append(cid)                                 //消费注ID
                 .append(DELIMITER)
                 .append(id)                                 //基本票表ID
                 .append(DELIMITER)
                 .append(TicketUtils.getSelectMode(flag5))   //选择方式
                 .append(DELIMITER)
                 .append(decoder.getCategoryId)              //玩法
                 .append(DELIMITER)
                 .append(consume.getPrefixNumber)            //红球或胆拖码（投注信息）
                 .append(DELIMITER)
                 .append(consume.getSuffixNumber)            //蓝球
                 .append(DELIMITER)
                 .append(consume.getTimes)                   //倍数
                 .append(DELIMITER)
                 .append(consume.getTotalNum)                //注数
                 .append(DELIMITER)
                 .append(consume.getTotalPrice)              //金额
             if (i < cont.length - 1) {
                 buf.append("\n")
             }
             //println("deocde for [" + s + "]")
         }
        }**/


    }
}
