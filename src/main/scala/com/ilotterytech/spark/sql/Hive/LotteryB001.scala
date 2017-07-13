package com.ilotterytech.spark.sql.Hive

/**
  * Created by liush on 17-7-3.
  */
import java.sql.Date
import org.apache.spark.sql.{Row, SQLContext, SaveMode}
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}
import util.CombineAlgorithm
import scala.collection.mutable.ArrayBuffer
object LotteryB001 {
  //基本票
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
    import hiveContext.implicits._
    import hiveContext.sql
    def _sqlContext: SQLContext = hiveContext
    sql(s"USE test")
    /*
    *单式
    * 列子0001010710112023~11^0001021112152629~09
    *([\S]?)取^
    * ([\d]{2})玩法
    * ([\d]{2})数量
    * ([\d]{12})红球
    * (\S{1})取~
    * ([\d]{2})蓝球
    */
    val p4 =
    """([\S]?)([00]{2})([\d]{2})([\d]{12})(\~{1})([\d]{2})""".r
    /*
    *红色复式
    *1001*04051112152531~05^
    *([\S]?)取^
    * ([\d]{2})玩法
    * ([\d]{2})注数
    * (\S{1}) 取*
    * ([\d]{12})红球
    * (\S{1})取~
    * ([\d]{2})蓝球
     */
    val f4 =
      """([\S]?)([10]{2})([\d]{2})(\S{1})([\d]{14})(\~{1})([\d]{2})""".r
    /*
    *全复式
    *3001*03040810212729~070813^
    *([\S]?)取^
    * ([\d]{2})玩法
    * ([\d]{2})注数
    * (\S{1}) 取*
    * ([\d]{14})红球
    * (\S{1})取~
    * ([\d]{6})蓝球
    */
    val qf4 =
    """([\S]?)([30]{2})([\d]{2})(\S{1})([\d]{14})(\~{1})([\d]{6})""".r
    /*
     *胆拖
     *4001111528*1213142223~10^
     *([\S]?)取^
     * ([\d]{2})玩法
     * ([\d]{2})胆拖
     * ([\d]{6})红球胆
     * (\S{1}) 取*
     * ([\d]{10})红球拖
     * (\S{1})取~
     * ([\d]{2})蓝球
    */
    val td =
    """([\S]?)^([40,50]{2})([\d]{2})([\d]+)(\*{1})([\d]+)(\~{1})([\d]+)""".r
    /*
     *蓝色复式
     *2001*060708262729~060708^
     *([\S]?)取^
     * ([\d]{2})玩法
     * ([\d]{2})注数
     * (\S{1}) 取*
     * ([\d]{12})红球
     * (\S{1})取~
     * ([\d]{2})蓝球
     */
    val f4ls =
    """([\S]?)([20]{2})([\d]{2})(\*{1})([\d]{12})(\~{1})([\d]{6})""".r
    def extractKey(rec: Ticket, content: String): Ticket = {
      //val ticketId=System.currentTimeMillis()
      println("==rec==" + rec + "==" + content)
      //正测表达式
      p4.findAllMatchIn(content).foreach(x=> {
        val consumeId = System.currentTimeMillis()
        //select_mode 自选/机选，0/1
        val select_mode = 1
        //玩法 play_mode
        /**
          * 1：单式
          * 2：红色复式
          * 3：蓝色复式
          * 4：全复式
          * 5：胆拖
          */
        val play_mode = 1
        val wf = x.group(2)
        val sl = x.group(3)
        //红球
        val hq = x.group(4)
        //蓝球
        val lq = x.group(6)
        //金额total_cost
        val total_cost = sl.toFloat * 2
        val consume = Consume(consumeId, rec.id, select_mode, play_mode, hq, lq, sl.toInt, total_cost)
        println("单式==取^=1==" + x.group(1) + "==玩法2==" + x.group(2) + "==数量3==" + x.group(3) + "==红球4==" + x.group(4) + "==取~5==" + x.group(5) + "==蓝球6==" + x.group(6))
        rec.total_cost=total_cost.toFloat
        rec.cons += consume
      })
      f4.findAllMatchIn(content).foreach(x => {
        val consumeId = System.currentTimeMillis()
        val wf = x.group(2)
        val zs = x.group(3)
        val hq = x.group(5)
        val lq = x.group(7)
        val zhongzhusum = CombineAlgorithm.C(7, 6).intValue() * zs.toInt
        val total_cost = zhongzhusum * 2
        val select_mode = 2
        //玩法 play_mode
        /**
          * 1：单式
          * 2：红色复式
          * 3：蓝色复式
          * 4：全复式
          * 5：胆拖
          */
        val play_mode = 2
        val consume = Consume(consumeId, rec.id, select_mode, play_mode, hq, lq, zs.toInt, total_cost)
        println("红色复式==取^=1==" + x.group(1) + "==玩法2==" + x.group(2) + "==注数3==" + x.group(3) + "==取*4==" + x.group(4) + "==红球5==" + x.group(5) + "==取~6==" + x.group(6) + "==蓝球7==" + x.group(7))
        rec.cons += consume
        rec.total_cost=total_cost.toFloat
      })
      td.findAllMatchIn(content).foreach(x => {
        val consumeId = System.currentTimeMillis()
        val wf = x.group(2)
        //胆拖注
        val td = x.group(3)
        //红球胆
        val dq = x.group(4)
        //红球拖
        val hq = x.group(6)
        //蓝球
        //val lq = "08"
        val lq = x.group(8)
        val d = (6 - dq.length / 2)
        val e = hq.length / 2
        val zhongzhusum = td.toInt * CombineAlgorithm.C(e, d).intValue() * 1
        val total_cost = zhongzhusum * 2
        val select_mode = 5
        //玩法标志位50（多蓝球胆拖)
        //玩法标志位40
        //玩法 play_mode
        /**
          * 1：单式
          * 2：红色复式
          * 3：蓝色复式
          * 4：全复式
          * 5：胆拖
          */
        val play_mode = 5
        val consume = Consume(consumeId, rec.id, select_mode, play_mode, hq, lq, zhongzhusum.toInt, total_cost.toFloat)
        println("胆拖==取^=1==" + x.group(1) + "==玩法2==" + x.group(2) + "==胆拖3==" + x.group(3) + "==红球胆4==" + x.group(4) + "==取*5==" + x.group(5) + "==红球拖6==" + x.group(6) + "==取~7==" + x.group(7) + "==蓝球8==" + x.group(8))
        rec.cons += consume
        rec.total_cost=total_cost.toFloat
      })
      f4ls.findAllMatchIn(content).foreach(x => {
        val consumeId = System.currentTimeMillis()
        val wf = x.group(2)
        val zs = x.group(3)
        val hq = x.group(5)
        val lq = x.group(7)
        val total_cost = zs * 2 * 3
        val select_mode = 1
        //玩法标志位50（多蓝球胆拖)
        //玩法标志位40
        //玩法 play_mode
        /**
          * 1：单式
          * 2：红色复式
          * 3：蓝色复式
          * 4：全复式
          * 5：胆拖
          */
        val play_mode = 3
        val consume = Consume(consumeId, rec.id, select_mode, play_mode, hq, lq, zs.toInt, total_cost.toFloat)
        println("蓝色复式==取^=1==" + x.group(1) + "==玩法2==" + x.group(2) + "==注数3==" + x.group(3) + "==取*4==" + x.group(4) + "==红球5==" + x.group(5) + "==取~6==" + x.group(6) + "==蓝球7==" + x.group(7))
        rec.cons += consume
        rec.total_cost=total_cost.toFloat
      })
      qf4.findAllMatchIn(content).foreach(x => {
        val consumeId = System.currentTimeMillis()
        val wf = x.group(2)
        val zs = x.group(3)
        val hq = x.group(5)
        val lq = x.group(7)
        //3个蓝球
        val zhongzhusum = CombineAlgorithm.C(7, 6).intValue() * 3 * zs.toInt
        val total_cost = zhongzhusum * 2
        val select_mode = 1
        //玩法标志位50（多蓝球胆拖)
        //玩法标志位40
        //玩法 play_mode
        /**
          * 1：单式
          * 2：红色复式
          * 3：蓝色复式
          * 4：全复式
          * 5：胆拖
          */
        val play_mode = 4
        val consume = Consume(consumeId, rec.id, select_mode, play_mode, hq, lq, zs.toInt, total_cost.toFloat)
        rec.total_cost=total_cost
        println("全复式==取^=1==" + x.group(1) + "==玩法2==" + x.group(2) + "==注数3==" + x.group(3) + "==取*4==" + x.group(4) + "==红球5==" + x.group(5) + "==取~6==" + x.group(6) + "==蓝球7==" + x.group(7))
        rec.cons += consume
      })
      rec
    }
    val test = sql(" SELECT a.serial,station,issue1,issue2,st_order,ordertime,content,order_num FROM test.b001_selldata a where  a.ordertime>='2016-10-01' and   a.ordertime<='2016-10-5'")
   val rdd = test.map {
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
     //case (row : Row)
     case Row(serial: String, station: String, issue1: String, issue2: String, st_order: Int, ordertime: String, content: String, order_num: Int) => {
        val ticketId = System.currentTimeMillis()
        val res = Ticket(ticketId, serial,null,station.toInt, null,
          ordertime,issue1, issue2, null, 1,
          null,null,null,0f,new ArrayBuffer[Consume]())
        extractKey(res, content)
      }
    }
    rdd.toDF().show(false)
    rdd.toDF().registerTempTable("records")
    val ticketdf = sql(
      """
        |select a.id,a.ticket_code,a.mainframe_id,a.station_id,a.machine_id,a.sold_time,a.sold_issue,a.valid_issue,
        |a.lottery_time,a.game_id,a.operator_id,a.shop_serial,a.issue_serial,a.total_cost from records a
      """.stripMargin)
    ticketdf.show(false)
    //rdd.toDF().registerTempTable("ticketdf")
   ticketdf.write.mode(SaveMode.Append).insertInto("shuangse_ticket")
    val consumetdf = sql(
      """
        |select ints.id,ints.ticketId,ints.selectMode as select_mode,ints.playMode as play_mode,
        |ints.prefixNumber,ints.surfixNumber,
        |ints.times,ints.totalCost from records LATERAL VIEW explode(cons) a AS ints
      """.stripMargin)
    consumetdf.show(false)
    consumetdf.write.mode(SaveMode.Append).insertInto("shuangse_consume")
    sc.stop()
  }
}
