package com.ilotterytech.sqoop.dataimport;

import org.apache.sqoop.client.SqoopClient;
import org.apache.sqoop.model.*;
import org.apache.sqoop.submission.counter.Counter;
import org.apache.sqoop.submission.counter.CounterGroup;
import org.apache.sqoop.submission.counter.Counters;
import org.apache.sqoop.validation.Status;

import java.io.IOException;

/**
 * @author eva_shi
 * 
 */
public class MySqoopJob {

	private String url = "http://name-node2:12000/sqoop/";
	private SqoopClient client = new SqoopClient(url);
	private String DB_NAME = "jdbc:mysql://192.168.200.2/azkaban?characterEncoding=utf8";
	private String JDBCDRIVER="com.mysql.jdbc.Driver";
	private String DB_USER = "azkaban";
	private String DB_PWD = "czkjAzkaban1234";
	
	//private String HDFS_URI = "hdfs://namenode1:8020";
    private String HDFS_URI ="hdfs://name-node1:8020/";
	private String HDFS_CONF_DIR = "/user/sqoop2/execution_flows";
	private String DBBASE_NAME="azkaban";
	private int SQOOP_EXEC_NUM = 10;
	private int SQOOP_LOADER_NUM = 5;
	private long fromLinkId;
	private long toLinkId;
	private long jobId;

	public MySqoopJob () throws IOException{

	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		
		/*String tableName=args[0];
		String partitionCol=args[1];
		String outputDir=args[2];*/
        String tableName="execution_flows";
        String partitionCol="exec_id";
        String outputDir="/user/sqoop2/execution_flows";

		MySqoopJob m = new MySqoopJob();
		m.jobId = m.saveJob(tableName, partitionCol, outputDir);

		System.out.println("save finished!");
		m.executeJob(m.jobId);
        System.out.println("executeJob finished!");
		//m.clean();
	}

	/**
	 * JDBC link
	 * @return JDBC link
	 * @throws IOException
	 */
	public long initJDBCLink() throws IOException {
		// create a placeholder for link
		long connectorId = SqoopUtil.getConnectorId("generic-jdbc-connector");
		System.out.println("jdbc connector id is:"+ connectorId);
		
		MLink link = client.createLink(connectorId);
		link.setCreationUser("evashi");
		MLinkConfig linkConfig = link.getConnectorLinkConfig();
		System.out.println("db:"+DB_NAME+"\tDB_USER"+DB_USER +"\tDB_PWD"+DB_PWD);
		
		linkConfig.getStringInput("linkConfig.connectionString")
			.setValue(DB_NAME);
		linkConfig.getStringInput("linkConfig.jdbcDriver").setValue(JDBCDRIVER);
		linkConfig.getStringInput("linkConfig.username").setValue(DB_USER);
		linkConfig.getStringInput("linkConfig.password").setValue(DB_PWD);
		
		// save the link object that was filled
		Status status = client.saveLink(link);

		System.out.println("jdbc link id is:"+ link.getPersistenceId());
		if (status.canProceed()) {
			System.out.println("Created Link with Link Id : "
					+ link.getPersistenceId());
		} else {
			System.out.println("Something went wrong creating the link");
		}

		return link.getPersistenceId();
	}

	/**
	 * HDFS link
	 * @return HDFS linkId
	 * @throws IOException
	 */
	public long initHDFSlink() throws IOException {
		long connectorId = SqoopUtil.getConnectorId("hdfs-connector");		
		MLink link = client.createLink(connectorId);
		link.setCreationUser("evashi");
		MLinkConfig linkConfig = link.getConnectorLinkConfig();

		linkConfig.getStringInput("linkConfig.uri").setValue(HDFS_URI);
		//linkConfig.getStringInput("linkConfig.confDir").setValue(HDFS_CONF_DIR);
		
		// save the link object that was filled
		Status status = client.saveLink(link);

		if (status.canProceed()) {
			System.out.println("Created Link with Link Id : "
					+ link.getPersistenceId());
		} else {
			System.out.println("Something went wrong creating the HDFS link");
		}

		return link.getPersistenceId();
	}

	/**
	 * saveJob
	 * @param tableName
	 * @param partitionCol
	 * @param outputDir hdfs
	 * @return jobId
	 * @throws IOException
	 */
	public long saveJob(String tableName, String partitionCol, String outputDir) throws IOException {
		// Creating dummy job object
		fromLinkId = initJDBCLink();
		toLinkId = initHDFSlink();

		System.out.println("fromlinkId:" + fromLinkId + "	toLinkId:" + toLinkId);
		MJob job = client.createJob(fromLinkId, toLinkId);

		MFromConfig fromJobConfig = job.getFromJobConfig();
		fromJobConfig.getStringInput("fromJobConfig.schemaName").setValue(DBBASE_NAME);
		fromJobConfig.getStringInput("fromJobConfig.tableName").setValue(tableName);
		fromJobConfig.getStringInput("fromJobConfig.partitionColumn").setValue(partitionCol);
		
		// set the "TO" link job config values
		MToConfig toJobConfig = job.getToJobConfig();
		toJobConfig.getStringInput("toJobConfig.outputDirectory").setValue(outputDir);
		
		// set the driver config values
		MDriverConfig driverConfig = job.getDriverConfig();
		driverConfig.getIntegerInput("throttlingConfig.numExtractors").setValue(SQOOP_EXEC_NUM);
		driverConfig.getIntegerInput("throttlingConfig.numLoaders").setValue(SQOOP_LOADER_NUM);

		Status status = client.saveJob(job);
		if (status.canProceed()) {
			System.out.println("[" + status.name() + "!] Created Job " +
					"with Job Id: " + job.getPersistenceId());
		} else {
			System.out.println("Something went wrong creating the job"
					+ status.name());
		}

		return job.getPersistenceId();
	}

	/**
	 *
	 * @param jobId 
	 */
	public void executeJob(long jobId) {
		
		MSubmission submission = client.startJob(jobId);
		System.out.println("Job Submission Status : " + submission.getStatus());
		
		if (submission.getStatus().isRunning()
				&& submission.getProgress() != -1) {
			System.out.println("Progress : " + submission.getProgress() * 100
					+ "%");
		}
		
		System.out.println("Hadoop job id :" + submission.getExternalJobId());
		System.out.println("Job link : " + submission.getExternalLink());
        Counters counters = submission.getCounters();
		if (counters != null) {
			System.out.println("Counters:");
			for (CounterGroup group : counters) {
				System.out.print("\t");
				System.out.println(group.getName());
				for (Counter counter : group) {
					System.out.print("\t\t");
					System.out.print(counter.getName());
					System.out.print(": ");
					System.out.println(counter.getValue());
				}
			}
		}
		//System.out.println(submission.getError());
		if (submission.getError() != null) {
			System.out.println("Exception info : " + submission.getError());
		}

        //System.out.println(submission.getStatus().isRunning()+"==="+submission.getProgress());

		if (submission.getStatus().isRunning()
				&& submission.getProgress() != -1) {
			System.out.println("Progress : "
					+ String.format("%.2f %%", submission.getProgress() * 100));
		}
        //Stop a running job
        //submission.stopJob(jobId);

	}


	public void clean() {

		System.out.println("begin cleaning>>>>>>>>>>>>>>>>>>");
		client.deleteJob(jobId);
		client.deleteLink(fromLinkId);
		client.deleteLink(toLinkId);
		System.out.println("end cleaning<<<<<<<<<<<<<<<<<<<<");

	}
}
