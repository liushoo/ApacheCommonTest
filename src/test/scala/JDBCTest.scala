
/**
  * Created by liush on 17-6-23.
  */
object JDBCTest {
      def main(args: Array[String]): Unit = {
        //val configFile = Utils.getContextOrSparkClassLoader.getResource("hive-site.xml")
      /*  println("==="+configFile)
        if (configFile != null) {
          sparkContext.hadoopConfiguration.addResource(configFile)
        }
*/
      val numSlaves = 4
       // println("local-cluster[%d, 1, 1024]".format(numSlaves)+"===")
       // println("=="+classOf[com.ilotterytech.spark.util.DateUtil])

     /* val path = Thread.currentThread.getContextClassLoader.getResource("./config/core-site.xml")
        println("path:"+path.toURI.toURL.getPath)
        val m = Map[String, Int]("a" -> 1, "b" -> 2, "c" -> 3)
       // m.foreach{case (key:String, value:Int) => println(">>> key=" + key + ", value=" + value)}

        m.foreach((e: (String, Int)) => println(e._1 + "=" + e._2))
        val sparkHome = System.getenv("SPARK_HOME")
        println("==="+sparkHome)*/
  /*      System.getenv().forEach(case (key:Stirng,value:String)=>{
          println(key+"==="+value)
        })*/

        val arr=Array("0001010710112023~11^0001021112152629~09^0001011214162330~02^0001071213222832~10^0001041316182529~01^",
          //2.1.3.1 单式
          "0002041416182026~07^",
          "000105071213262730^000103111618192627^",
          //2.1.3.5 胆拖2
          "40010406162324*05080911252627283032~10^",
          "500104131824*0726303133~0511^",
          //2.1.3.5 胆拖
          "4001111528*1213142223~10^",
          //2.1.3.2 红色复式
          "1001*04051112152531~05^",
          //2.1.3.3 蓝色复式
          "2001*060708262729~060708^",
          //2.1.3.4 全复式
          "3001*03040810212729~070813^",
          "000105071213262730^000103111618192627^"
        )

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
        val p4="""([\S]?)([00]{2})([\d]{2})([\d]{12})(\~{1})([\d]{2})""".r
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
        val f4="""([\S]?)([10]{2})([\d]{2})(\S{1})([\d]{14})(\~{1})([\d]{2})""".r
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
        val qf4="""([\S]?)([30]{2})([\d]{2})(\S{1})([\d]{14})(\~{1})([\d]{6})""".r

        /*
         *胆拖
         * 4001111528*1213142223~10^
         * 40010406162324*05080911252627283032~10^
         * 500104131824*0726303133~0511^
         *([\S]?)取^
         * ([\d]{2})玩法
         * ([\d]{2})胆拖
         * ([\d]{6})红球胆
         * (\S{1}) 取*
         * ([\d]{10})红球拖
         * (\S{1})取~
         * ([\d]{2})蓝球
        */
        val td="""([\S]?)^([40,50]{2})([\d]{2})([\d]+)(\*{1})([\d]+)(\~{1})([\d]+)""".r
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
        val f4ls="""([\S]?)([20]{2})([\d]{2})(\*{1})([\d]{12})(\~{1})([\d]{6})""".r
        val shuangse_consume= "INSERT OVERWRITE TABLE shuangse_consume SELECT %d,%s,%d,%d,%s,%s,%d,%.2f"
        val arr7L=Array("000105071213262730^000103111618192627^",
          //2.1.3.1 单式
          "000104060811172425^",
          //3.1.3.2 复式
          "1001*0203101821222728^",
          //2.1.3.5 胆拖
          "200109161727*0405070815222528^"
        )
        /**
          *  七乐彩
          * 000105071213262730^000103111618192627^
          *  前00为普通投注，03是数量3注（倍投），后续的01030809131623分别是01 03 08 09 13 16 23共7个投注号码
          *
          *
          */
        def extractKey7L(line: String): Unit = {//提取键值
          //apacheLogRege配置正测表达式
          p4.findAllMatchIn(line).foreach(x=>{
            //自增流水号
            val ticketId=System.currentTimeMillis()
            //ticket_id对
            val consumeId=System.currentTimeMillis()
            //select_mode 自选/机选，0/1
            val select_mode=1
            //玩法 play_mode
            val play_mode=1

            val wf=x.group(2)

            val sl=x.group(3)
            //红球
            val hq=x.group(4)
            //蓝球
            val lq=x.group(6)

            //金额total_cost
            val total_cost=sl.toFloat*2

            println("单式==取^=1=="+x.group(1)+"==玩法2=="+x.group(2)+"==数量3=="+x.group(3)+"==红球4=="+x.group(4)+"==取~5=="+x.group(5)+"==蓝球6=="+x.group(6))

            val pf=shuangse_consume.format(consumeId,ticketId,select_mode,play_mode,hq,lq,null,total_cost)
            println("==="+pf)

          })
          f4.findAllMatchIn(line).foreach(x=>{
            println("红色复式==取^=1=="+x.group(1)+"==玩法2=="+x.group(2)+"==注数3=="+x.group(3)+"==取*4=="+x.group(4)+"==红球5=="+x.group(5)+"==取~6=="+x.group(6)+"==蓝球7=="+x.group(7))
          })
          td.findAllMatchIn(line).foreach(x=>{
            println("胆拖==取^=1=="+x.group(1)+"==玩法2=="+x.group(2)+"==胆拖3=="+x.group(3)+"==红球胆4=="+x.group(4)+"==取*5=="+x.group(5)+"==红球拖6=="+x.group(6)+"==取~7=="+x.group(7)+"==蓝球8=="+x.group(8))
          })
          f4ls.findAllMatchIn(line).foreach(x=>{
            println("蓝色复式==取^=1=="+x.group(1)+"==玩法2=="+x.group(2)+"==注数3=="+x.group(3)+"==取*4=="+x.group(4)+"==红球5=="+x.group(5)+"==取~6=="+x.group(6)+"==蓝球7=="+x.group(7))
          })
          qf4.findAllMatchIn(line).foreach(x=>{
            println("全复式==取^=1=="+x.group(1)+"==玩法2=="+x.group(2)+"==注数3=="+x.group(3)+"==取*4=="+x.group(4)+"==红球5=="+x.group(5)+"==取~6=="+x.group(6)+"==蓝球7=="+x.group(7))
          })


        }

        //  println("==="+tdd.findFirstIn(d)
        arr7L.map(line => {
          println("==="+line)
          extractKey7L(line)
        })




        val arr3D=Array("0002030601^0201010306^",
          //4.1.3.1 直选单式(00)
          "0001070108^",
          //4.1.3.2 组三直选单式(01)
          "0101040407^",
          //4.1.3.3 组六直选单式(02)
          "0201070809^",
          //4.1.3.4 1D（百位）（03）
          "030105^",
          //4.1.3.5 1D（十位）（04）
          "040106^",
          //4.1.3.6 1D（个位）（05）
          "050109^",
          //4.1.3.7 2D（百位-十位）（06）
          "06010307^",
          //4.1.3.8 2D（百位-个位）（07）
          "07010809^",
          //4.1.3.9 2D（十位-个位）（08）
          "08020409^",
          //4.1.3.10 通选投注（09)
          "0901010409^",
          //4.1.3.11 猜1D（0:）
          "0:0109^",
          //4.1.3.12 猜2D（0<）
          "0<010809^",
          //4.1.3.13 直选和值（10）
          "100123^",
          //4.1.3.14 组三和值（11）
          "110123^",
          //4.1.3.15 组六和值（12）
          "120123^",
          //4.1.3.16 直选复式（20）
          "20010101^0109^1000010203040506070809^",
          //4.1.3.17 1D（百位）复式（21）
          "2101020008^",
          //4.1.3.18 1D（十位）复式（22）
          "2201020205^",
          //4.1.3.19 1D（个位）复式（23）
          "2301 020204^",
          //4.1.3.19 1D（个位）复式（23）
          "2301 020204^",
          //4.1.3.20 2D（百位-十位）复式（24）
          "2401 020006^03020508^",
          //4.1.3.21 2D（百位-个位）复式（25）
          "2501 020006^03020508^",
          //4.1.3.22 2D（十位-个位）复式（26）
        "2601 020006^03020508^",
          //4.1.3.23 通选复式（28）
          "2801 020005^020407^020109^",
          //4.1.3.24 组三包号（31）
          "3101020408^",
          //4.1.3.25 组六包号（32）
          "32010404060709^",
          //4.1.3.26 组六直选复式（34）
          "3401 03010408^"

        )






        def extractKey(line: String): Unit = {//提取键值
          //apacheLogRege配置正测表达式
          p4.findAllMatchIn(line).foreach(x=>{
            //自增流水号
            val ticketId=System.currentTimeMillis()
            //ticket_id对
            val consumeId=System.currentTimeMillis()
            //select_mode 自选/机选，0/1
            val select_mode=1
            //玩法 play_mode
            val play_mode=1

            val wf=x.group(2)

            val sl=x.group(3)
            //红球
            val hq=x.group(4)
            //蓝球
            val lq=x.group(6)

            //金额total_cost
            val total_cost=sl.toFloat*2

            println("单式==取^=1=="+x.group(1)+"==玩法2=="+x.group(2)+"==数量3=="+x.group(3)+"==红球4=="+x.group(4)+"==取~5=="+x.group(5)+"==蓝球6=="+x.group(6))

            val pf=shuangse_consume.format(consumeId,ticketId,select_mode,play_mode,hq,lq,null,total_cost)
            println("==="+pf)

          })
          f4.findAllMatchIn(line).foreach(x=>{
            println("红色复式==取^=1=="+x.group(1)+"==玩法2=="+x.group(2)+"==注数3=="+x.group(3)+"==取*4=="+x.group(4)+"==红球5=="+x.group(5)+"==取~6=="+x.group(6)+"==蓝球7=="+x.group(7))
          })
          td.findAllMatchIn(line).foreach(x=>{
            println("胆拖==取^=1=="+x.group(1)+"==玩法2=="+x.group(2)+"==胆拖3=="+x.group(3)+"==红球胆4=="+x.group(4)+"==取*5=="+x.group(5)+"==红球拖6=="+x.group(6)+"==取~7=="+x.group(7)+"==蓝球8=="+x.group(8))
          })
          f4ls.findAllMatchIn(line).foreach(x=>{
            println("蓝色复式==取^=1=="+x.group(1)+"==玩法2=="+x.group(2)+"==注数3=="+x.group(3)+"==取*4=="+x.group(4)+"==红球5=="+x.group(5)+"==取~6=="+x.group(6)+"==蓝球7=="+x.group(7))
          })
          qf4.findAllMatchIn(line).foreach(x=>{
            println("全复式==取^=1=="+x.group(1)+"==玩法2=="+x.group(2)+"==注数3=="+x.group(3)+"==取*4=="+x.group(4)+"==红球5=="+x.group(5)+"==取~6=="+x.group(6)+"==蓝球7=="+x.group(7))
          })


        }

        val d="4001111528*1213142223~10^"
        val tdd="""([\S]?)^([40,50]{2})([\d]{2})([\d]+)(\*{1})([\d]+)(\S{1})([\d]+)""".r
        tdd.findFirstIn(d) match {
          //apacheLogRege正测表达式提取分组
          //ip地址,user 记录用户的身份信息,dateTime 记录访问时间与时区
          //query记录请求的URL与http协议,status状态码,由服务器端发送回客户端
          //bytes表示服务器向客户端发送了多少的字节,referer记录的是客户在提出请求时所在的目录或URL
          //ua 记录客户端浏览器的相关信息
          case Some(tdd(a, s, d, e, f, g, h, q)) =>
            //(10.10.10.10,"FRED",GET http://images.com/2013/Generic.jpg HTTP/1.1)
           println("a=="+a+"==s=="+s+"==d=="+d+"=e="+e+"=f="+f+"=g=="+g+"=h=="+h+"==q="+q)
        }

      //  println("==="+tdd.findFirstIn(d)
       arr.map(line => {
          println("==="+line)
          extractKey(line)
        })
        /**println("\\d{16}(\\~){1}\\d{2}".r.findFirstIn(test))*/
      }

}
