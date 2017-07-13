import java.sql.Timestamp


import scala.collection.mutable.ArrayBuffer
/**
  * Created by liush on 17-7-6.
  */
object QL370 {


  def main(args: Array[String]): Unit = {
    /**
      * 七乐彩
      * 000105071213262730^000103111618192627^
      * 前00为普通投注，03是数量3注（倍投），后续的01030809131623分别是01 03 08 09 13 16 23共7个投注号码
      *
      *
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
      case class Consume7lecai(id: Long,ticketId:Long,selectMode:Int,playMode:Int,prefixNumber:String,surfixNumber:String,times:Int,totalCost:Float)
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
      ql.findAllMatchIn(line).foreach(x => {
        //自增流水号
        val ticketId = System.currentTimeMillis()
        //ticket_id对
        val consumeId = System.currentTimeMillis()
        //select_mode 自选/机选，0/1
        val select_mode = 1
        //玩法 play_mode
        val play_mode = 1
        val wf = x.group(2)
        val sl = x.group(3)
        //红球
        val hq = x.group(4)
        /*for(a<-(hq.length/2)){

        }*/
        //蓝球
        //val lq = x.group(6)
        //金额total_cost
        val total_cost = sl.toFloat * 2
        val q=stringSplit(hq)
        println("单式==取^=1==" + x.group(1) + "==玩法2==" + x.group(2) + "==数量3==" + x.group(3) + "==球4==" + x.group(4)+"==q=="+q)

        //val pf = shuangse_consume.format(consumeId, ticketId, select_mode, play_mode, hq, lq, null, total_cost)
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
    val arr7L=Array("000105071213262730^000103111618192627^",
      //2.1.3.1 单式
      "000104060811172425^",
      //3.1.3.2 复式
      "1001*0203101821222728^1001*0203101821222729^",
      //2.1.3.5 胆拖
      "200109161727*0405070815222528^200109161727*0405070815222528^")
    //  println("==="+tdd.findFirstIn(d)
    arr7L.map(line => {
      println("===" + line)

      //extractKey7L(res)
      extractKey7L(line)
    })


  }
}
