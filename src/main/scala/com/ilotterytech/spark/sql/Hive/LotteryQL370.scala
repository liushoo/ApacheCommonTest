package com.ilotterytech.spark.sql.Hive

import java.sql.Timestamp

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.Row
import org.apache.spark.sql.hive.HiveContext


import scala.collection.mutable.ArrayBuffer

/**
  * Created by liush on 17-7-6.
  */
object LotteryQL370 {

  /**
    * 2,解析数据到票shuangse_ticket表
    * ID            自增(取系统当前时间戳)
    * ticket_code   serial票号
    * station_id    station
    * machine_id
    * sold_time     ordertime 投注时间
    * sold_issue    issue1
    * valid_issue   issue2
    * Idgame_id     双色球：0，快三：1，3D：2
    * shop_serial   st_order
    * total_cost    order_num*2
    */
  case class Ticket(
                     id: Long, ticket_code: String,mainframe_id:Integer, station_id: Int,machine_id:Integer,
                     sold_time:String,sold_issue:String,valid_issue:String, lottery_time:String,game_id:Integer,
                     operator_id:Integer,shop_serial:String,issue_serial:String,var total_cost:Float,cons:ArrayBuffer[Consume])
  /**
    * 2,解析content数据到shuangse_consume消费注表
    * ID            自增(取系统当前时间戳)
    * ticket_id     shuangse_ticket.id
    * select_mode    自选/机选，0/1
    * play_mode
    * prefix_number(红球或胆拖码)
    * surfix_number(蓝球)
    * times(倍数)
    * total_cost(金额)
    */
  case class Consume(id: Long, ticketId: Long, selectMode: Int, playMode: Int, prefixNumber: String, surfixNumber: String, times: Int, totalCost: Float)
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("HiveFromSpark").setMaster("local")
    //sparkConf.set("spark.driver.extraClassPath", "/opt/cloudera/parcels/CDH/lib/hive/lib/*,/etc/hive/conf/hive-site.xml")
    sparkConf.set("spark.executor.extraClassPath", "/opt/cloudera/parcels/CDH/lib/hive/lib/*")
    sparkConf.set("spark.hadoop.user.name", "hive")
    val sc = new SparkContext(sparkConf)

    val base_lottery_station =
      """
        |INSERT OVERWRITE TABLE base_lottery_station SELECT a.station AS one,null,null,null,null,
        |concat("站点_",a.station) as key,
        |null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        |null,null,null,null,null,null
        |FROM test.k520_selldata a GROUP BY a.station order by a.station""".stripMargin

    val delete_base_lottery_station =
      """
        |insert overwrite table base_lottery_station select * from base_lottery_station where 1=0
      """.stripMargin

    val hiveContext = new HiveContext(sc)
    import hiveContext.sql
    sql(s"USE test")
    //    sql(delete_base_lottery_station)
    //sql(base_lottery_station)
    /**
      * 1,初始化站点数据
      * 从selldata.以station站点分组插入到base_lottery_station
      * 站点Id=station
      * 站点名称name=站点_station
      * SELECT station  FROM k520_selldata  group by station order by station
      */
    // sql("CREATE TABLE dest1(key INT, value STRING) STORED AS TEXTFILE")
    /**
      * 七乐彩
      * 000105071213262730^000103111618192627^
      * 前00为普通投注，03是数量3注（倍投），后续的01030809131623分别是01 03 08 09 13 16 23共7个投注号码
      */
    def stringSplit(x:String)={
      val stringBuilder = new StringBuilder
      val list=new ArrayBuffer[String]()
      for(a<-0 until  (x.length,2)){
        val tem=x.substring(a,a+2)
        list+=tem
      }
      list.sorted.mkString(",")
    }
    def extractKey7L(line: String): Unit = {

      /*
      *单式
      *000104060811172425^
      *([\S]?)取^
      * ([\d]{2})玩法
      * ([\d]{2})注数
      * ([\d])   球
      */

      /**
        * 7lecai_ticket
        * id(Int)           主键
        * ticket_code(String) 原始彩票ID
        * mainframe_id(Int)  主机信息表id
        * station_id(Int)    站点ID
        * machine_id(Int)    投注机ID
        * sold_time(timestamp)     出票时间
        * sold_issue(string)    销售期号
        * valid_issue(string)   销售期号
        * lottery_time(timestamp)  开奖时间
        * game_id(int)       游戏Id(3)七乐彩游戏
        * operator_id(int)   操作员Id
        * shop_serial(String)   店流水号
        * issue_serial(String)  期流水号
        * total_cost(float)    金额
        */
      //基本票
      case class Ticket7lecai(id:Long,ticket_code: String,mainframe_id:Int,station_id:Int,sold_time:Timestamp,
                              sold_issue:String,valid_issue:String,lottery_time:Timestamp,game_id:Int,operator_id:Int,
                              shop_serial:String,issue_serial:String,total_cost:Float)
      //消费注
      case class Consume7lecai(id: Long,ticketId:Long,selectMode:Int,playMode:Int,detail:String,times:Int,totalCost:Float)
      //票shuangse_ticket表
      val shuangse_ticket ="INSERT OVERWRITE TABLE test.7lecai_ticket SELECT %d,%s,%d,%d,%d,%s,%s,%s,%s,%d,%d,%s,%s,%.2f"
      /**
        * 7lecai_consume
        * id (bigint)           主键
        * ticket_id (bigint)    主键
        * select_mode (tinyint) 自选/机选，0/1
        * play_mode (tinyint)   play_mode （1） 单式投注 （2） 复式投注,（3） 胆拖投注
        * detail (string)       详情
        * times (int)           倍数
        * total_cost (decimal(10,0))消费注消费总金额
        */
      //消费注表
      val shuangse_consume= "INSERT OVERWRITE TABLE tset.7lecai_consume SELECT %d,%d,%d,%d,%s,%d,%.2f"
      val ql="""([\^]?)([00]{2})([\d]{2})([\d]{14})""".r
      //自增流水号
      val ticketId = System.currentTimeMillis()
      ql.findAllMatchIn(line).foreach(x => {
        //ticket_id对
        val consumeId = System.currentTimeMillis()
        //select_mode 自选/机选，0/1
        val select_mode = 1
        val wf = x.group(2)
        val sl = x.group(3)
        //红球
        val hq = x.group(4)
        val selectMode=1
        val play_mode=1
        val detail=stringSplit(hq)
        //金额total_cost
        val total_cost = sl.toFloat * 2
        val c7=Consume7lecai(consumeId,ticketId,selectMode,play_mode,detail,sl.toInt,total_cost)
        println("单式==取^=1==" + x.group(1) + "==玩法2==" + x.group(2) + "==数量3==" + x.group(3) + "==球4==" + x.group(4)+"==q=="+detail)
        //val pf = shuangse_consume.format(c7.id, c7.ticketId, c7.selectMode, c7.playMode, c7.hq, lq, null, total_cost)
        // println("===" + pf)

      })
      /*
        *复式
        *1001*0203101821222728^
        *([\S]?)取^
        * ([\d]{2})玩法
        * ([\d]{2})注数
        * (\S{1}) 取*
        * ([\d]{12})红球
        * (\S{1})取~
        * ([\d]{2})蓝球
        */
      val f4ls="""([\^]?)([10]{2})([\d]{2})(\*{1})([\d]+)""".r
      f4ls.findAllMatchIn(line).foreach(x => {
        val qq= x.group(5)
        val q=stringSplit(qq)
        println("复式==取^=1==" + x.group(1) + "==玩法2==" + x.group(2) + "==注数3==" + x.group(3) + "==取*4==" + x.group(4) + "==红球5==" +qq+"==q=="+q)
      })
      /*
      *复式
      *200109161727*0405070815222528^
      *([\S]?)取^
      * ([\d]{2})玩法
      * ([\d]{2})注数
      * (\S{1}) 取*
      * ([\d]{12})红球
      * (\S{1})取~
      * ([\d]{2})蓝球
      */
      val dt="""([\^]?)([20]{2})([\d]{2})([\d]+)(\*{1})([\d]+)""".r
      dt.findAllMatchIn(line).foreach(x => {
        val qq= x.group(6)
        println("qq:"+qq)
        val q=stringSplit(qq)
        println("胆拖==取^=1==" + x.group(1) + "==玩法2==" + x.group(2) + "==注数3==" + x.group(3) + "==取*4==" + x.group(4) + "==胆5==" + x.group(5)  + "==拖6==" + x.group(6))
      })

    }

    sc.stop()
  }

}
