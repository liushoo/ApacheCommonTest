import java.sql.DriverManager
import java.text.{DateFormat, SimpleDateFormat}

import com.czkj1010.utils.{HiveUtil, JDBCUtil}
import data.pricelevel.PriceLevel
import data.time_offset.TimeOffset
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import util.PlayMode

import scala.collection.mutable.HashMap

/**
  * Created by SJ on 2017-6-13.
  */
object Main {

  val DELIMITER = "\t"

  val stationMap: HashMap[String, String] = HashMap[String, String]()
  val machineMap: HashMap[String, String] = HashMap[String, String]()
  val dateMap: HashMap[String, String] = HashMap[String, String]()
  val timeMap: HashMap[String, String] = HashMap[String, String]()

  val df: DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    conf.setAppName("test").setMaster("yarn-cluster")
    val sc =  new SparkContext(conf)
    val partitionNum = 200
    val flatTicketTable: RDD[(String, String)] = flatTicket(sc, partitionNum)
    val flatConsumeTable: RDD[(String, String)] = flatConsume(sc, partitionNum)

    // consumeTable
    // ticketId -> cs_id  cp_fk cs_price  cs_total_price  cs_mutiple  cs_frequency  cs_pl_fk

    // ticketTable
    // ticketId => issue_fk ticketid  ticketprice date_fk time_fk ticket_pl_fk  s_fk  time_offset_fk

    val joinedTable = flatTicketTable.join(flatConsumeTable).map(line => {
      val ticketId = line._1
      val ticketInfo = line._2._1
      val consumeInfo = line._2._2
      val buf = new StringBuffer()

      val ticketSplits = ticketInfo.split(DELIMITER, -1)
      val consumeSplits = consumeInfo.split(DELIMITER, -1)

      val ticketPrice = if(consumeSplits(0)=="1") ticketSplits(2).toDouble.toInt else 0
      val ticketMark = if(consumeSplits(0)=="1") 1 else 0

      buf.append(ticketSplits(0)).append(",").append(ticketSplits(1)).append(",")
        .append(ticketMark).append(",").append(ticketPrice).append(",")
        .append(consumeSplits(0)).append(",").append(ticketSplits(3)).append(",")
        .append(ticketSplits(4)).append(",").append(consumeSplits(1)).append(",")
        .append(ticketSplits(6)).append(",").append(consumeSplits(2)).append(",")
        .append(consumeSplits(3)).append(",").append(consumeSplits(4)).append(",")
        .append(consumeSplits(5)).append(",").append(consumeSplits(6)).append(",")
        .append(ticketSplits(5)).append(",").append(ticketSplits(7))
      buf.toString
    })
    joinedTable.saveAsTextFile("/user/hdfs/d2/shuangse/temp")

//    flatTicketTable.join(flatConsumeTable).saveAsTextFile("/user/hdfs/d2/shuangse/temp")
    sc.stop()
  }

  // ticketId => issue_fk ticketid  ticketprice date_fk time_fk ticket_pl_fk s_fk
  def flatTicket (sc: SparkContext, partitionNum: Int): RDD[(String, String)] = {
    val mysqlUrl = "jdbc:mysql://192.168.200.2:3306/stats?user=qgk&password=lottery2017"
    val hiveUrl = "jdbc:hive2://192.168.200.81:10000/db_20170430"
    val conn = DriverManager.getConnection(mysqlUrl)
    val stmt = conn.createStatement()

    val stationSql = "select * from base_lottery_station"
    val machineSql = "select * from base_betting_machine"
    val stationResultSet = stmt.executeQuery(stationSql)

    while(stationResultSet.next()) {
      stationMap += ( stationResultSet.getString("id") -> stationResultSet.getString("name"))
      //  resultSet.get
    }

    val machineResultSet = stmt.executeQuery(machineSql)

    while(machineResultSet.next()) {
      machineMap += ( machineResultSet.getString("id") -> machineResultSet.getString("code"))
    }

    stmt.close()
    conn.close()

    System.out.println(stationMap)
    System.out.println(machineMap)

    val consumeRdd = sc.textFile("/user/hdfs/d2/shuangse/consume/2015/01/06/*", partitionNum)
    val ticketRdd = sc.textFile("/user/hdfs/d2/shuangse/ticket/2015/01/06/*", partitionNum)

    val stationMapBroadCast = sc.broadcast(stationMap).value
    val machineMapBroadCast = sc.broadcast(machineMap).value

    //D3 维表读取
    val sMap: HashMap[String, String] = readSQLMap("select * from temp_server_d3", "s_station,s_machine", "s_pk")
    val dMap: HashMap[String, String] = readHiveMap("select * from d", "d_date", "d_pk")
    val tMap: HashMap[String, String] = readHiveMap("select * from t", "t_time", "t_pk2")
    val issueMap: HashMap[String, String] = readHiveMap("select * from issue where issue_playmode=" +
      "'乐透型-双色球'", "issue_name", "issue_pk")

    println("issueMap: " + issueMap)
    println(issueMap.get("第2015003期"))
    //pricelevel维表获取map
    val plMap: HashMap[String, String] = readHiveMap("select * from pricelevel", "pl_level2", "pl_pk")
    val timeOffsetMap: HashMap[String, String] = readHiveMap("select * from time_offset where to_domain_category='乐透型-双色球'",
      "to_offset","to_pk")

    println("time_offset_map:"+ timeOffsetMap)
    //对map作broadcast操作
    val plMapBroadCast = sc.broadcast(plMap).value
    val sMapBroadCast = sc.broadcast(sMap).value
    val dMapBroadCast = sc.broadcast(dMap).value
    val tMapBroadCast = sc.broadcast(tMap).value
    val issueMapBroadCast = sc.broadcast(issueMap).value
    val timeOffsetMapBroadCast = sc.broadcast(timeOffsetMap).value

    val replacedTicketRdd = ticketRdd.map(line => {
      val splits = line.split(DELIMITER, -1)
      val issue_fk = issueMapBroadCast.get("第" + splits(6) + "期").getOrElse(None)
      val ticketid = splits(1)
      val ticketprice = splits(13)
      val date_fk = dMapBroadCast.get(splits(5).split(" ", -1)(0)).getOrElse(None)
      val time_fk = tMapBroadCast.get(splits(5).split(" ", -1)(1).substring(0, 5)).getOrElse(None)

      splits(3) = stationMapBroadCast.getOrElse(splits(3), null)
      splits(4) = machineMapBroadCast.getOrElse(splits(4), null)

      val s_fk = sMapBroadCast.get(splits(3) + "\t" + splits(4)).getOrElse(None)
      val ticket_pl_fk = plMapBroadCast.get(PriceLevel.getKeyByPrice(ticketprice.toDouble.toInt))
        .getOrElse(None)//map映射得到

      //splits(5) 时间
      val to = new TimeOffset()
      val timeOffset = to.getTimeOffset(splits(5), PlayMode.DOUBLE_BALL)
      val time_offset_fk = timeOffsetMapBroadCast.getOrElse("第"
        + f"$timeOffset%02d" + "小时", None)

      (splits(0), issue_fk+DELIMITER+ticketid+DELIMITER+ticketprice+
        DELIMITER+date_fk + DELIMITER + time_fk + DELIMITER + ticket_pl_fk+
        DELIMITER + s_fk + DELIMITER + time_offset_fk)

    })

    replacedTicketRdd
  }

  /**
    *   ticketId -> cs_id  cp_fk cs_price  cs_total_price  cs_mutiple  cs_frequency  cs_pl_fk
    */
  def flatConsume (sc: SparkContext, partitionNum: Int): RDD[(String, String)] = {

    val consumeRdd = sc.textFile("/user/hdfs/d2/shuangse/consume/2015/01/06/*", partitionNum)

    //D2表Map获取
    //双色球玩法字典表获取map
    val wfMap: HashMap[String, String] = readSQLMap("select * from core_dictionary where " +
      "dict_code='SSQWF' and use_flag=1", "dict_code", "id")
    println("wfmap:" + wfMap)
    if(!wfMap.contains("SSQWF"))
      return null
    val wfId = wfMap.get("SSQWF").getOrElse(None)
    val shuangseWFMap: HashMap[String, String] = readSQLMap("select * from core_dictionary " +
      "where parent_id='"+ wfId +"' and use_flag=1", "value","name")
    /**
      *  SELECT a.*
        FROM core_dictionary a
        WHERE a.parent_id in (select id from core_dictionary b  WHERE dict_code='SSQWF' and use_flag=1)
      */

    //投注方式字典表获取map
    val selectModeCodeMap: HashMap[String, String] = readSQLMap("select * from core_dictionary " +
      "where dict_code='TZFS' and use_flag=1", "dict_code", "id")

    println("selectmodeCodeMap" + selectModeCodeMap)
    if(!selectModeCodeMap.contains("TZFS"))
      return null
    val smId = selectModeCodeMap.get("TZFS").getOrElse(None)
    val selectModeMap: HashMap[String, String] = readSQLMap("select * from core_dictionary where " +
      "parent_id='"+ smId +"' and use_flag=1", "value","name")
    /**
      *  SELECT a.*
        FROM core_dictionary a
        WHERE a.parent_id in (select id from core_dictionary b  WHERE dict_code='TZFS' and use_flag=1)
      */

    //D3 维表读取
    //pricelevel维表获取map
    val plMap: HashMap[String, String] = readHiveMap("select * from pricelevel", "pl_level2", "pl_pk")

    //cp维表获取映射关系(selectmode+玩法 => id)
    //cp_select_mode 人工,机选
    //cp_play_mode 单式下一注,单式下多注,单式下多注,蓝色球号码复式,全复式,胆拖
    //cp_pk:id
    val cpMap: HashMap[String, String] = readHiveMap("select * from cp where cp_catory='双色球'",
      "cp_select_mode,cp_play_mode", "cp_pk")

    println("shuangseWFMap:"+ shuangseWFMap)
    println("selectModeMap:"+ selectModeMap)

    println("plMap:"+ plMap)
    println("cpMap:"+ cpMap)
    //对map作broadcast操作
    val plMapBroadCast = sc.broadcast(plMap).value
    val shuangseWFMapBroadCast = sc.broadcast(shuangseWFMap).value
    val selectModeMapBroadCast = sc.broadcast(selectModeMap).value
    val cpMapBroadCast = sc.broadcast(cpMap).value

    val replacedConsumeRdd = consumeRdd.map(line => {

      val splits = line.split(DELIMITER, -1)
      val ticket_id = splits(1)

      val selectModeStr = selectModeMapBroadCast.getOrElse(splits(2), None)
      val playModeStr = shuangseWFMapBroadCast.getOrElse(splits(3), None)

      val cp_fk = cpMapBroadCast.getOrElse(selectModeStr + "\t" + playModeStr, None)//map映射得到

      val price = 2
      val total_price = splits(7)
      val cs_multiple = splits(6)
      val cs_frequency = 1
      val cs_pl_fk = plMapBroadCast.get(PriceLevel.getKeyByPrice(total_price.toDouble.toInt))
        .getOrElse(None)//map映射得到

      (ticket_id, Seq[String](cp_fk + DELIMITER + price + DELIMITER + total_price +
        DELIMITER + cs_multiple + DELIMITER + cs_frequency + DELIMITER + cs_pl_fk))

    }).reduceByKey((val1, val2)=> {
      val seq = val1.++ (val2)
      seq
    })
    replacedConsumeRdd.flatMap(s => {
      var i = 1
      var ans = Seq[String]()
      for(line <- s._2) {
        val newLine: String = s._1 + "\t" + i + "\t" + line
        i+=1
        ans = ans :+ newLine
      }
      ans
    }).map(line => {
      val splits = line.split("\t", -1)
      (splits(0), splits.drop(1).mkString("\t"))
    })


  }

  def readSQLMap(sql: String, key: String, value: String): HashMap[String, String] = {
    val util = JDBCUtil.getInstance()
    val conn = util.getConnection()
    val stmt = conn.createStatement()
    val resultSet = stmt.executeQuery(sql)

    val map = HashMap[String, String]()

    while(resultSet.next()) {
      val keyBuf = new StringBuffer()
      key.split(",", -1).foreach(p=> print(p+"->"+resultSet.getString(value) +";"))

      key.split(",", -1).filter(p => p != null && p.trim != "").map(p=> keyBuf.append(resultSet
        .getString(p)).append("\t"))

      val mapKey = keyBuf.substring(0, keyBuf.length()-1)
      val mapVal = resultSet.getString(value)
      map += (mapKey -> mapVal)
    }
    map
  }

  def readHiveMap(sql: String, key: String, value: String): HashMap[String, String] = {

    val util = HiveUtil.getInstance()
    val conn = util.getConnection()
    val stmt = conn.createStatement()
    val resultSet = stmt.executeQuery(sql)
    val map = HashMap[String, String]()

    while(resultSet.next()) {
      val keyBuf = new StringBuffer()

      key.split(",", -1).filter(p => p != null && p.trim != "").map(p=> keyBuf.append(resultSet
        .getString(p)).append("\t"))

      val mapKey = keyBuf.substring(0, keyBuf.length()-1)
      val mapVal = resultSet.getString(value)
      map += (mapKey -> mapVal)
    }
    map
  }
}