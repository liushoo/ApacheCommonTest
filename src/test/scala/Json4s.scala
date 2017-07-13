



import scala.concurrent.duration._
import scala.io.Source
import scala.language.postfixOps
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
/**
  * Created by liush on 17-6-30.
  */
object Json4s {
  case class Recordb(serial: String,station:String,issue1:String,issue2:String,st_order:Int,ordertime:String,content:String,order_num:Int)

  def main(args: Array[String]): Unit = {
    /*implicit val formats = Serialization.formats(ShortTypeHints(List()))
    val str: JsonAST.JObject =
      ("property_name" -> "三聚氰胺(mg/kg)") ~  ("property_result" -> "阴性")
    println(compact(render(str)))

    implicit val formats = org.json4s.DefaultFormats
    implicit val formats = Serialization.formats(ShortTypeHints(List()))*/
   /* val str: List[JsonAST.JObject] = List(
      ("property_name" -> "三聚氰胺(mg/kg)") ,
        ("property_result" -> "阴性"),
      ("property_name" -> "铬(mg/kg)") ,
        ("property_result" -> "0.022"))*/
    //println(compact(render(str)))

   val shuangse_ticket ="INSERT OVERWRITE TABLE shuangse_ticket SELECT %d,%s,%d,%s,%s,%s,%s,%s,%d,%s,%s,%s,%s,%.2f"

    val rec=Recordb("272fc63f360f981bf110","61010020","101004","101004",1,"2016-01-01 09:35:13","0000010401060708^0000010402060708^000001050102060708^",3)
    val ticketId=System.currentTimeMillis()
    val pf=shuangse_ticket.format(ticketId,rec.serial,100010,rec.station,rec.ordertime,rec.issue1,rec.issue2,null,1,null,null,null,null,2f)

    val tset="0001060812182831~09^"
    val shuangse_consume= "INSERT OVERWRITE TABLE shuangse_consume SELECT %d,%s,%d,%s,%s,%s,%d,%.2f"
    val tdd="""([\S]?)^([40,50]{2})([\d]{2})([\d]+)(\*{1})([\d]+)(\S{1})([\d]+)""".r
    tdd.findFirstIn(tset) match {
      //apacheLogRege正测表达式提取分组
      //ip地址,user 记录用户的身份信息,dateTime 记录访问时间与时区
      //query记录请求的URL与http协议,status状态码,由服务器端发送回客户端
      //bytes表示服务器向客户端发送了多少的字节,referer记录的是客户在提出请求时所在的目录或URL
      //ua 记录客户端浏览器的相关信息

      case Some(tdd(a, b, c, d, f, g,h)) =>
        val consumeId=System.currentTimeMillis()
      /*  val wf=x.group(2)
        val sl=x.group(3)
        val hq=x.group(4)
        val lq=x.group(6)
        val total_cost=sl*2*/
        // val shuangse_consume= "INSERT OVERWRITE TABLE shuangse_consume SELECT %d,%s,%d,%d,%s,%s,%d,%.2f"
        println("单式==取^=1=="+a+"==玩法2=="+b+"==数量3=="+c+"==红球4=="+d+"==取~5=="+f+"==蓝球6=="+g+"=="+h)
        // val shuangse_consume= "INSERT OVERWRITE TABLE shuangse_consume SELECT %d,%s,%d,%d,%s,%s,%d,%.2f"
        //val pf=shuangse_consume.format(consumeId,ticketId,0,1,hq,lq,total_cost)
       // println("==="+pf)

        //(10.10.10.10,"FRED",GET http://images.com/2013/Generic.jpg HTTP/1.1)
        //println("a=="+a+"==s=="+s+"==d=="+d+"=e="+e+"=f="+f+"=g=="+g+"=h=="+h+"==q="+q)
    }

    println("==="+pf)
  }
}
