package com.ilotterytech.sqoop.dataimport;

import org.apache.sqoop.client.SqoopClient;
import org.apache.sqoop.model.MConnector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author eva_shi
 * 
 */
public class SqoopUtil {

	private volatile static SqoopUtil sqoopUtil = null;
	private static HashMap<String, Long> connectorMap = null;
	private static String url = "http://name-node2:12000/sqoop/";
	private static SqoopClient client = null;
	
	private SqoopUtil() throws IOException {
		
		client = new SqoopClient(url);
		System.out.println("url:" +url);
		connectorMap = new HashMap<String, Long>();
		readConnectorMap();
		System.out.println("connectorMap size---------"+ connectorMap.size());
		
	}
	
	private static void readConnectorMap() {
		
		System.out.println("reading");
		ArrayList<MConnector> connectorList = new ArrayList<MConnector>(client
				.getConnectors());
 
		for (MConnector con : connectorList) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			System.out.println("unique name: " + con.getUniqueName());
			System.out.println("persistenced id: " + con.getPersistenceId());
			String conName = con.getUniqueName();
			long conId = con.getPersistenceId();
			connectorMap.put(conName.trim(), conId);
		}
		
	}
	
	private static SqoopUtil getSqoopUtil() throws IOException {
		
		if(sqoopUtil == null) {
			synchronized(SqoopUtil.class) {
				if(sqoopUtil == null) {
					long start = System.currentTimeMillis();
					sqoopUtil = new SqoopUtil();
					long end = System.currentTimeMillis();
					System.out.println("construct sqoop util last "+ (end-start));
				}
			}
		}
		
		return sqoopUtil;
	}
	
	public static long getConnectorId(String name) throws IOException {
		
		if(getSqoopUtil().connectorMap.containsKey(name)) 
			return  connectorMap.get(name);
		else
			return -1;
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		System.out.println("starting//////"+ getSqoopUtil().connectorMap.size());
		for (String conName : connectorMap.keySet()) {
			
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			System.out.println("unique name: " + conName);
			System.out.println("persistenced id: " + connectorMap.get(conName));
			
		}
		System.out.println("ending........"+ getConnectorId("generic-jdbc-connector"));

		/*
		 * HashMap<String, String> map= new HashMap<String, String>();
		 * System.out.println(map.put("a", "A"));
		 * System.out.println(map.put("b", "B"));
		 * System.out.println(map.put("b", "BB"));
		 * System.out.println(map.values());
		 */
	}



}
