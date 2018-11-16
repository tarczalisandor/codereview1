package it.usi.xframe.xas.bfimpl;

import it.usi.xframe.xas.bfutil.ConstantsSms;
import it.usi.xframe.xas.bfutil.XASException;
import it.usi.xframe.xas.util.json.XConstants;
import it.usi.xframe.xas.wsutil.ENUM_STATUS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unicredit.xframe.slf.SmartLog;

/**
 * Handle the XAS tables.
 */
public class TableManager {
	private static Logger logger = LoggerFactory.getLogger(XasSendsmsServiceFacade.class);

	private static TableManager instance;
    
    // jdbc:oracle:thin:@(description=(address=(host=HOSTNAME)(protocol=tcp)(port=PORT))(connect_data=(service_name=SERVICENAME)(server=SHARED)))
	public static void insertDRrequest(String myUUID, Connection connection, String uuid, String phoneNumber, String[] smsId, String xasUser, Date validity) throws SQLException {
		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I)
		.logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasSendsmsServiceFacade.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
		.logIt(SmartLog.K_METHOD, "insert").preset("default"); 

		logger.debug(sl.getLogRow(true));

		Timestamp tsExpire = new Timestamp(validity.getTime());

		PreparedStatement psInsert = connection.prepareStatement("insert into XAS_DELIVERY_REPORT(XAS_UUID, XAS_PHONE_NUMBER, XAS_XASUSER, XAS_TS_EXPIRE) values (?, ?, ?, ?)");
		try {

			psInsert.setString(1, uuid);
			psInsert.setString(2, phoneNumber);
			psInsert.setString(3, xasUser);
			psInsert.setTimestamp(4, tsExpire);
			psInsert.executeUpdate();  
		} finally {
			psInsert.close();
		}

		PreparedStatement psInsertStatus = connection.prepareStatement("insert into XAS_DELIVERY_REPORT_STATUS(XAS_UUID, XAS_SMS_ID, XAS_STATUS) values (?, ?, ?)");

		try {
			for (int i = 0; i < smsId.length; i++) {
				psInsertStatus.setString(1, uuid);
				psInsertStatus.setString(2, smsId[i]);
				psInsertStatus.setString(3, STATUS_SENT);
				psInsertStatus.executeUpdate();  
			}
		} finally {
			psInsertStatus.close();
		}
     }

	public final static String STATUS_SENT = "XAS_SENT";
	private final static String LIST_SEP = "#";

	public static final String XAS_TYPE_MOBILE_TERMINATED = "MT";
	public static final String XAS_TYPE_MOBILE_ORIGINATED = "MO";
	public static final String XAS_TYPE_DELIVERY_REPORT = "DR";
	/**
	 * Fetch the xasUser for the Delivery Report.
	 * @param uuid
	 * @return xasUserName from the DB
	 * @throws XASException 
	 * @throws SQLException 
	 * @throws NamingException 
	 */
	public static TableInfo fetchDeliveryReport(String myUUID, Connection connection, String msgUuid, String smsId, String status) throws SQLException {
		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I)
			.logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasSendsmsServiceFacade.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
			.logIt(SmartLog.K_METHOD, "fetchDeliveryReport").preset("default"); 

//		Avoid SQL Injection using prepareStatement with placeholder
//		
//		String lockString = " select * " 
//	      + " from XAS_DELIVERY_REPORT "
//	      + " where XAS_UUID = '" + msgUuid + "' "
//	      + " for update wait 45 "  // ForUpdate
//	      ;
//		PreparedStatement stmtLock = connection.prepareStatement(lockString); // ForUpdate
//		ResultSet rsLock = stmtLock.executeQuery();

		PreparedStatement stmtLock = connection.prepareStatement("select * from XAS_DELIVERY_REPORT where XAS_UUID = ? for update wait 45"); // ForUpdate
		stmtLock.setString(1, msgUuid);
		ResultSet rsLock = stmtLock.executeQuery();


		
		String xasUserName = null, sqlString = null;
		String[] listId = null;
		// Fetch smsId for the corresponding uuid
		
//		Avoid SQL Injection using prepareStatement with placeholder
//		
		String fieldList = "A.XAS_UUID, A.XAS_PHONE_NUMBER, A.XAS_TS_EXPIRE, A.XAS_XASUSER";
		sqlString = " select " + fieldList 
				  + "  , listagg(trim(XAS_SMS_ID), '" + LIST_SEP + "') within group (order by XAS_SMS_ID) as SMS_LIST"
				  + "  , sum(case B.XAS_STATUS when '" + ENUM_STATUS._DELIVERED_TO_DEV + "' then 1 else 0 end) as COUNT_DEL " 
				  + "  , sum(case B.XAS_STATUS when '" + STATUS_SENT + "' then 1 else 0 end) as COUNT_SENT " 
				  + "  , sum(1) as COUNT_TOT"
			      + " from XAS_DELIVERY_REPORT A, XAS_DELIVERY_REPORT_STATUS B "
			      + " where A.XAS_UUID = B.XAS_UUID "
			      + "   and B.XAS_UUID = ? "
//*1			      + "   and B.XAS_UUID = '" + msgUuid + "' "
//			      + "   and B.XAS_UUID = (select XAS_UUID  from XAS_DELIVERY_REPORT_STATUS  where XAS_SMS_ID = '" + smsId + "') "
//			      + "   and B.XAS_SMS_ID <> '" + smsId + "' "
			      + " group by " + fieldList
			      ;
//*1		Statement sqlStmt = connection.createStatement(); 
//*1		ResultSet rsDeliveryReport = sqlStmt.executeQuery(sqlString);
		
		PreparedStatement sqlStmt = connection.prepareStatement(sqlString);
		try  { // sqlStmt
			sqlStmt.setString(1, msgUuid);

			ResultSet rsDeliveryReport = sqlStmt.executeQuery();
			try { // rsDeliveryReport
		        while (rsDeliveryReport.next()) {
		           xasUserName = rsDeliveryReport.getString("XAS_XASUSER");
		           int countDelivered = rsDeliveryReport.getInt("COUNT_DEL");
		           int countSent = rsDeliveryReport.getInt("COUNT_SENT");
		           int countTotal = rsDeliveryReport.getInt("COUNT_TOT");
		           listId = rsDeliveryReport.getString("SMS_LIST").split(LIST_SEP);
		           logger.debug(sl.logIt("a_countDelivered", Integer.toString(countDelivered)
						               , "a_countSent", Integer.toString(countSent)
						               , "a_countTotal", Integer.toString(countTotal)
						               , "a_status", status
		        		               ).getLogRow());
		           if (((countSent == 1) && (countDelivered == countTotal - 1)) || !ENUM_STATUS._DELIVERED_TO_DEV.equals(status)) { 
		        	   // All preceding multipart sms has been received or received a NOT delivered sms
		                if (xasUserName !=  null) {
		                	xasUserName = xasUserName.trim(); // Deliver to the application 
		                }
		           } else { // Wait for one more delivery report 
		        	   xasUserName = null;
		           }
		        }
			} finally {
		        rsDeliveryReport.close();
			}
		} finally {
	        sqlStmt.close(); 
		}

		TableInfo returnValue = TableManager.getInstance().new TableInfo();
		returnValue.setXasUserName(xasUserName);
//			  , lastStatus	 // IX_STATUS
		returnValue.setListId(listId);
		returnValue.setStmtLock(stmtLock);
		returnValue.setRsLock(rsLock);
			        
        return returnValue;
    }

	/**
	 * Fetch the xasUser for the Delivery Report.
	 * @param uuid
	 * @return xasUserName from the DB
	 * @throws XASException 
	 * @throws SQLException 
	 * @throws NamingException 
	 */
	public static void updateDeliveryReport(String myUUID, Connection connection, String uuid, String smsId, String status, boolean delete) throws SQLException {
		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I)
			.logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasSendsmsServiceFacade.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
			.logIt(SmartLog.K_METHOD, "updateDeliveryReport").preset("default"); 
//*2		String sqlString;
		// Identify smsReceived
//*2		String whereClause = " where XAS_UUID = '" + uuid + "'";

		logger.debug(sl.logIt("a_delete", Boolean.toString(delete)
	               , "a_msgUuid", uuid
	               , "a_smsId", smsId
	               , "a_status", status
	               ).getLogRow());

		if (!delete) { // Remove received multipart
	 	    // Update removing received smsId
			PreparedStatement stUpdate = null;
			try {
				// Need COALESCE to avoid return NULL if empty string
//*2				sqlString = "UPDATE XAS_DELIVERY_REPORT_STATUS SET XAS_STATUS = '" + status + "' "  + whereClause + " and XAS_SMS_ID = '" + smsId + "'";
//*2				stUpdate.executeUpdate(sqlString);

				stUpdate = connection.prepareStatement("UPDATE XAS_DELIVERY_REPORT_STATUS SET XAS_STATUS = ? where XAS_UUID = ? and XAS_SMS_ID = ?"); 
				stUpdate.setString(1, status);
				stUpdate.setString(2, uuid);
				stUpdate.setString(3, smsId);
				stUpdate.executeUpdate();

			} finally { 
				stUpdate.close();
			}
		} else { // All multipart sms delivered, delete the row
			PreparedStatement stDelete = null;
			try {
//*2				sqlString = "delete from XAS_DELIVERY_REPORT " + whereClause;
//*2				stDelete.executeUpdate(sqlString);  
				stDelete = connection.prepareStatement("delete from XAS_DELIVERY_REPORT where XAS_UUID = ?"); 
				stDelete.setString(1, uuid);
				stDelete.executeUpdate();
			} finally {
				stDelete.close();
			}

			try {
//*2				stDelete = connection.createStatement();
//*2				sqlString = "delete from XAS_DELIVERY_REPORT_STATUS " + whereClause;
//*2				stDelete.executeUpdate(sqlString);  
				stDelete = connection.prepareStatement("delete from XAS_DELIVERY_REPORT_STATUS where XAS_UUID = ?"); 
				stDelete.setString(1, uuid);
				stDelete.executeUpdate();
			} finally {
				stDelete.close();
			}
		}
    }

	/**
	 * Cleanup the expired Delivery Report .
	 * @param uuid
	 * @throws SQLException 
	 */
	public static void cleanupDeliveryReport(Connection connection, long drRetentionPeriod) throws SQLException {
		String sqlString;
//		Statement stDelete = connection.createStatement();
		PreparedStatement stDelete = null;
		try {
//			sqlString = "delete from XAS_DELIVERY_REPORT_STATUS "
//						+ " where exists (select * from XAS_DELIVERY_REPORT " 
//											+ " where XAS_DELIVERY_REPORT.XAS_UUID = XAS_DELIVERY_REPORT_STATUS.XAS_UUID and CURRENT_DATE - cast(XAS_DELIVERY_REPORT.XAS_TS_EXPIRE as DATE) > " + drRetentionPeriod + ") ";
//			stDelete.executeUpdate(sqlString);  

			sqlString = "delete from XAS_DELIVERY_REPORT_STATUS "
				+ " where exists (select * from XAS_DELIVERY_REPORT " 
									+ " where XAS_DELIVERY_REPORT.XAS_UUID = XAS_DELIVERY_REPORT_STATUS.XAS_UUID and CURRENT_DATE - cast(XAS_DELIVERY_REPORT.XAS_TS_EXPIRE as DATE) > ?) ";
			stDelete = connection.prepareStatement(sqlString); 
			stDelete.setLong(1, drRetentionPeriod);
			stDelete.executeUpdate();  
			
//			sqlString = "delete from XAS_DELIVERY_REPORT where CURRENT_DATE - cast(XAS_TS_EXPIRE as DATE) > " + drRetentionPeriod;
//			stDelete.executeUpdate(sqlString);

			sqlString = "delete from XAS_DELIVERY_REPORT where CURRENT_DATE - cast(XAS_TS_EXPIRE as DATE) > ? " ;
			stDelete = connection.prepareStatement(sqlString); 
			stDelete.setLong(1, drRetentionPeriod);
			stDelete.executeUpdate();  
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stDelete.close();
		}
    }

	/**
	 * Updates statistics record for sms sent for the provider\range\prefix.
	 * @param provider used provider
	 * @param range period to update
	 * @param prefix international recognized prefix
	 * @param smsSent number of sms sents
	 * @throws SQLException
	 * @throws NamingException 
	 */
	public static void updateStats(String myUUID, Connection connection, String type, String provider, String xasUserName, String range, String prefix, int smsSent) throws SQLException {
		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I)
		.logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasSendsmsServiceFacade.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
		.logIt(SmartLog.K_METHOD, "updateStats", "a_type", type, "a_provider", provider, "a_xasUser", xasUserName, "a_range", range, "a_prefix", prefix).preset("default"); 

		logger.debug(sl.getLogRow(true));
	 	   // Update removing received smsId
		PreparedStatement stUpdate = null;
//		Avoid SQL Injection using prepareStatement with placeholder
//		
//*3		Statement stUpdate = connection.createStatement();
		try {
			// Need COALESCE to avoid return NULL if empty string
//*3			String sqlUpdate = "UPDATE XAS_SMS_STATS SET XAS_SMS_SENT = XAS_SMS_SENT + " + smsSent 
//*3							 + " WHERE XAS_TYPE = '" + type + "' "
//*3							   + " and XAS_PROVIDER = '" + provider + "' "
//*3							   + " and XAS_XASUSER = '" + xasUserName + "' " 
//*3							   + " and XAS_RANGE = '" + range + "' "
//*3							   + " and XAS_PREFIX = '" + prefix + "'";
//*3			int rowUpdated = stUpdate.executeUpdate(sqlUpdate);

			
			// Better use ((OraclePreparedStatement)ps).setFixedCHAR(1, "A10");   instead of RTRIMing the field
			String sqlUpdate = "UPDATE XAS_SMS_STATS SET XAS_SMS_SENT = XAS_SMS_SENT + ? "  
			 + " WHERE RTRIM(XAS_TYPE) = ? "
			   + " and RTRIM(XAS_PROVIDER) = ? "
			   + " and RTRIM(XAS_XASUSER) = ? " 
			   + " and RTRIM(XAS_RANGE) = ? "
			   + " and RTRIM(XAS_PREFIX) = ? ";

			stUpdate = connection.prepareStatement(sqlUpdate); 
			stUpdate.setInt(1, smsSent);
			stUpdate.setString(2, type);
			stUpdate.setString(3, provider);
			stUpdate.setString(4, xasUserName);
			stUpdate.setString(5, range);
			stUpdate.setString(6, prefix);
			int rowUpdated = stUpdate.executeUpdate();
			
			if (rowUpdated == 0) { // if no row updated insert the new row with 0 and update again!
				PreparedStatement psInsert = connection.prepareStatement("insert into XAS_SMS_STATS(XAS_TYPE, XAS_PROVIDER, XAS_XASUSER, XAS_RANGE, XAS_PREFIX, XAS_SMS_SENT) values (?, ?, ?, ?, ?, ?)");
				try {
					psInsert.setString(1, type);
					psInsert.setString(2, provider);
					psInsert.setString(3, xasUserName);
					psInsert.setString(4, range);
					psInsert.setString(5, prefix);
					psInsert.setInt(6, 0);
					try { // ignore duplicate key insertion, someone else is inserting with smsSent = 0;
						psInsert.executeUpdate();  
					} catch (com.ibm.websphere.ce.cm.DuplicateKeyException e) {
						// Ignore duplicate insertion.
					}
				} finally {
					psInsert.close();
				}
				
				stUpdate = connection.prepareStatement(sqlUpdate); 
				stUpdate.setInt(1, smsSent);
				stUpdate.setString(2, type);
				stUpdate.setString(3, provider);
				stUpdate.setString(4, xasUserName);
				stUpdate.setString(5, range);
				stUpdate.setString(6, prefix);
				rowUpdated = stUpdate.executeUpdate();
			}
		} finally {
			if (stUpdate != null) stUpdate.close();
		}
    }

	public class TableInfo {
		
		public TableInfo() {
	        super();
        }
		public String getXasUserName() {
        	return xasUserName;
        }
		public void setXasUserName(String xasUserName) {
        	this.xasUserName = xasUserName;
        }
		public String[] getListId() {
        	return listId;
        }
		public void setListId(String[] listId) {
        	this.listId = listId;
        }
		public PreparedStatement getStmtLock() {
        	return stmtLock;
        }
		public void setStmtLock(PreparedStatement stmtLock) {
        	this.stmtLock = stmtLock;
        }
		public ResultSet getRsLock() {
        	return rsLock;
        }
		public void setRsLock(ResultSet rsLock) {
        	this.rsLock = rsLock;
        }
		private String xasUserName;
		private String [] listId;
		private PreparedStatement stmtLock;
		private ResultSet rsLock;
	}
 	/**
	 * Singleton getInstance.
	 * @return the instance.
	 * @throws XASException
	 */
 	public static synchronized TableManager getInstance() {
		if (instance == null) instance = new TableManager();
		return instance;
	}
 	
 	/**
 	 * Singleton private constructor.
 	 * @throws XASException
 	 */
 	private TableManager() {
	}
	public static String fetchXasUsers(String myUUID, Connection connection) throws SQLException {
//		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I)
//			.logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasSendsmsServiceFacade.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
//			.logIt(SmartLog.K_METHOD, "fetchXasUsers").preset("default"); 

		String sqlString = 
//			" SELECT " 
//			+ "XMLElement(\"" + Configuration.TAG_XASUSER + "\""
//			  +", XMLAttributes(trim(U.XAS_NAME) AS \""+ Configuration.ATT_XU_NAME +"\", trim(U.XAS_INTERFACE) AS \"" + Configuration.ATT_XU_INTERFACE + "\", trim(U.XAS_ORIGINATOR) AS \"" + Configuration.ATT_XU_ORIGINATOR + "\")) " 
//				   + "|| "
//				   + "'<" + Configuration.TAG_XASUSER + " " + Configuration.ATT_XU_NAME + "=\"' || trim(U.XAS_NAME) || '\" " 
//			       + Configuration.ATT_XU_INTERFACE + "=\"' || trim(U.XAS_INTERFACE) || '\" " 
//			       + Configuration.ATT_XU_ORIGINATOR + "=\"' || trim(U.XAS_ORIGINATOR) || '\" ' || O.XAS_PARAMS || '/>' AS XASUSER "
//			+ " FROM ("
//			+
			"SELECT XAS_NAME, " 
			  + " XMLELEMENT(NAME, XMLATTRIBUTES(XAS_VALUE AS EVALNAME, trim(XAS_KEY)) AS XASUSER" 
			        + " FROM (" + 
			        " SELECT O.XAS_NAME AS XAS_NAME, O.XAS_KEY AS XAS_KEY, O.XAS_VALUE AS XAS_VALUE, CA.XAS_ORDER AS XAS_ORDER FROM XAS_XASUSER_OPTIONS O, XAS_CONFIG_OPTIONS CA WHERE O.XAS_KEY=CA.XAS_KEY ORDER BY CA.XAS_ORDER" +
			        		") " 
//			        + " GROUP BY XAS_NAME"
//			        + ") O "
//			             + " RIGHT OUTER JOIN XAS_XASUSERS U "
//			             + " ON U.XAS_NAME = O.XAS_NAME "
//			             + "ORDER BY 1";
			        ;
		
		Statement sqlStmt = connection.createStatement(); 
		ResultSet rsDeliveryReport = sqlStmt.executeQuery(sqlString);
		String xasUsers = "";
		try {
	        while (rsDeliveryReport.next()) {
	           xasUsers += rsDeliveryReport.getString("XASUSER");
	        }
	        //  Close the Result Set and the Sql Statement 
		} finally {
	        rsDeliveryReport.close();
	        sqlStmt.close(); 
		}
			        
        return xasUsers;
    }
	
	
	public static String[][] fetchStats(String myUUID, Connection connection, String[] filters) throws SQLException {
		SmartLog sl = new SmartLog(SmartLog.COUPLING_LOOSE_I)
			.logItCompact(ConstantsSms.MY_APPL_ID, ConstantsSms.MY_LOG_VER, XasSendsmsServiceFacade.class.getName(), myUUID, SmartLog.V_SCOPE_DEBUG)
			.logIt(SmartLog.K_METHOD, "fetchDeliveryReport").preset("default"); 

		logger.debug(sl.reload("default").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_ENTER, SmartLog.K_PARAMS, XConstants.XSTREAMER.toXML(filters)).getLogRow(true)); // Debug and keep row

//		Avoid SQL Injection using prepareStatement with placeholder
//		
//		String lockString = " select * " 
//	      + " from XAS_DELIVERY_REPORT "
//	      + " where XAS_UUID = '" + msgUuid + "' "
//	      + " for update wait 45 "  // ForUpdate
//	      ;
//		PreparedStatement stmtLock = connection.prepareStatement(lockString); // ForUpdate
//		ResultSet rsLock = stmtLock.executeQuery();
		
		PreparedStatement stmtQuery = connection.prepareStatement("select XAS_TYPE, XAS_PROVIDER, XAS_XASUSER, XAS_RANGE, XAS_PREFIX, XAS_SMS_SENT from XAS_SMS_STATS where XAS_TYPE like ? AND XAS_PROVIDER like ? and XAS_XASUSER like ? and XAS_RANGE like ? and XAS_PREFIX like ? ");
		String filter;
		for (int i = 0; i < 5; i++) {
			filter = filters != null && filters.length > i ? filters[i] : ""; 
			stmtQuery.setString(i+1, filter + "%");
		}
		ResultSet rsQuery = stmtQuery.executeQuery();
		logger.debug(sl.reload("default").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_LOOP_INIT).getLogRow(true)); // Debug and keep row

		ArrayList al = new ArrayList();
 		try { // rsQuery
	        while (rsQuery.next()) {
	        	int colNum = 6;
	    	    String[] row = new String[colNum];
		   		for (int i = 0; i < colNum; i++) {
			           row[i] = rsQuery.getString(i + 1);
				}
		   		al.add(row);
	        }
		} finally {
			rsQuery.close();
		}
		logger.debug(sl.reload("default").logIt(SmartLog.K_PHASE, SmartLog.V_PHASE_LOOP_END).getLogRow(true)); // Debug and keep row
		String[][] returnValue = (String [][]) al.toArray(new String[al.size()][]);
			        
        return returnValue;
    }
	
}
