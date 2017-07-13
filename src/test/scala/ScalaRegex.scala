import java.util
import java.util.UUID

import com.ilotterytech.spark.sql.Hive.LotteryB001.{Consume, Ticket}

import scala.beans.BeanProperty
import scala.collection.mutable.ArrayBuffer
/**
  * Created by liush on 17-7-4.
  */
object ScalaRegex {
  def main(args: Array[String]): Unit = {

    //例子一整个字符串匹配模式
    println("51".matches("""\d+"""))//true

    //例子二查询是否包含复合正则的模式
    println("""\d+""".r.findAllIn("foo456bar").length!=0)//true

    //例子三返回第一个匹配正则的字符串
    var numbers="""\d+""".r
    var str="foo 123 bar 456"
    println(numbers.findFirstIn(str))//Some(123)

    //例子四迭代所有匹配到的复合模式的字符串
    numbers.findAllMatchIn(str).foreach(println)// 123  456

    //例子五返回所有正则匹配作为一个List
    println(numbers.findAllMatchIn(str).toList)//List(123, 456)

    //例子六使用正则查询和替换
    var letters="""[a-zA-Z]+""".r
    var str2="foo123bar"
    println(letters.replaceAllIn(str2,"spark"))//spark123spark

    //例子七使用正则查询和替换使用一个函数
    println(letters.replaceAllIn(str,m=>m.toString().toUpperCase()))//FOO 123 BAR 456

    //例子八使用正则查询替换字符
    var exp="""##(\d+)##""".r
    var str8="foo##123##bar"
    //group 0 返回所有捕获，其他1...n分别返回第一个捕获，或第二个捕获，至第n个捕获
    println(exp.replaceAllIn(str8,m=>(m.group(0)).toString))//foo##123##bar
    println(exp.replaceAllIn(str8,m=>(m.group(1).toInt *2).toString))//foo246bar

    //例子九使用正则提取值进入一个变量里面
    val pattern="""(\d{4})-([0-9]{2})""".r

    val myString="2016-02"

    val pattern(year,month)=myString

    println(year)//2016
    println(month)//02

    //例子十在case match匹配中使用 正则
    val dataNoDay="2016-08"
    val dateWithDay="2016-08-20"

    val yearAndMonth = """(\d{4})-([01][0-9])""".r
    val yearMonthAndDay = """(\d{4})-([01][0-9])-([012][0-9])""".r

    dateWithDay match{
      case yearAndMonth(year,month) => println("no day provided")
      case yearMonthAndDay(year,month,day) => println(s"day provided: it is $day")
    }
    //day provided: it is 20

    //例子十一正则匹配忽略大小写

    val caseSensitivePattern = """foo\d+"""
    //println("Foo123".matches(caseSensitivePattern))//false
    val caseInsensitivePattern = """(?i)foo\d+"""
   // println("Foo123".matches(caseInsensitivePattern))//true
    //0->2  04  0
    //2->4  05  1
    //6->8  15  2
    val hq="1522252804050708"
    val ticketId=System.currentTimeMillis()
    case class FastOperator(output: ArrayBuffer[Consume],a:String)
    var a=FastOperator(new ArrayBuffer[Consume](),"122121")
  //  val res=Ticket(ticketId,"212121","111","222","333",444,"555","666",12,null)
    val b=ArrayBuffer[Consume]()
    for(x<-1 to 10){
      val cl=Consume(x,12,12,12,"12","122112",21,12)
      println("==="+cl)
     // FastOperator(cl :: Nil,"ddsd") :: Nil

     // a.output(cl :: Nil) :: Nil//
      a.output+=cl
      //res.cont :: Nil
     // println(res.cont)


    }
   // res.cont.++:(a)
    println("===="+a.output)
   // println("===="+a)


    def stringSplit(x:String)={
      val stringBuilder = new StringBuilder
      val list=new ArrayBuffer[String]()
      for(a<-0 until  (x.length,2)){
        val tem=x.substring(a,a+2)
        list+=tem
      }
     list.sorted.mkString(",")
    }
   // println(stringSplit(hq))

    //val arry=Array("15","04","05","07","28","22")
   // println(arry.sorted.mkString(","))
    //注意使用正则字符串三个双引号，不需要转义
    println(UUID.randomUUID().toString.replace('-', '_'))

  }

  val base_lottery_station =
    """
      |INSERT OVERWRITE TABLE base_lottery_station SELECT a.station AS one,null,null,null,null,
      |concat("站点_",a.station) as key,
      |null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
      |null,null,null,null,null,null
      |FROM test.k520_selldata a GROUP BY a.station order by a.station""".stripMargin

  println(base_lottery_station)


}
