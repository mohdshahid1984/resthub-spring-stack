package org.resthub.core.util.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.Assert;

/**
 * Stores information related to a database and the jdbc driver used to access
 * the database.
 * 
 * @author vanackej
 * 
 */
public class DatabaseDescriptor {

	private String productName;

	private String productVersion;

	private int majorVersion;

	private int minorVersion;

	private String driverName;

	private String driverVersion;

	private int driverMajorVersion;

	private int driverMinorVersion;

	private LinkedHashSet<String> tables;

	public DatabaseDescriptor(Connection connection) throws SQLException {
		init(connection);
	}
	
	public DatabaseDescriptor(DataSource dataSource) throws SQLException {
		Assert.notNull(dataSource, "dataSource is required");
		Connection connection = DataSourceUtils.getConnection(dataSource);
		try {
			init(connection);
		} finally {
			DataSourceUtils.releaseConnection(connection, dataSource);
		}
	}
	
	private void init(Connection connection) throws SQLException {
		Assert.notNull(connection, "connection is required");
		DatabaseMetaData metatData = connection.getMetaData();
		setProductName(metatData.getDatabaseProductName()
				.toLowerCase().trim());
		setProductVersion(metatData.getDatabaseProductVersion()
				.toLowerCase().trim());
		setMajorVersion(metatData.getDatabaseMajorVersion());
		setMinorVersion(metatData.getDatabaseMinorVersion());
		setDriverName(metatData.getDriverName().trim());
		setDriverVersion(metatData.getDriverVersion().trim());
		setDriverMajorVersion(metatData.getDriverMajorVersion());
		setDriverMinorVersion(metatData.getDriverMinorVersion());
		
		this.tables = new LinkedHashSet<String>();
		
		ResultSet rs = metatData.getTables(null, null, "%", new String[]{"TABLE"});
		while (rs.next()) {
			tables.add(rs.getString(3));
		}
	}
	
	public Set<String> getTables() {
		return tables;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}

	public int getMajorVersion() {
		return majorVersion;
	}

	public void setMajorVersion(int majorVersion) {
		this.majorVersion = majorVersion;
	}

	public int getMinorVersion() {
		return minorVersion;
	}

	public void setMinorVersion(int minorVersion) {
		this.minorVersion = minorVersion;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverVersion() {
		return driverVersion;
	}

	public void setDriverVersion(String driverVersion) {
		this.driverVersion = driverVersion;
	}

	public int getDriverMajorVersion() {
		return driverMajorVersion;
	}

	public void setDriverMajorVersion(int driverMajorVersion) {
		this.driverMajorVersion = driverMajorVersion;
	}

	public int getDriverMinorVersion() {
		return driverMinorVersion;
	}

	public void setDriverMinorVersion(int driverMinorVersion) {
		this.driverMinorVersion = driverMinorVersion;
	}

	@Override
	public String toString() {
		return "DatabaseDescriptor [productName=" + productName
				+ ", productVersion=" + productVersion + ", majorVersion="
				+ majorVersion + ", minorVersion=" + minorVersion
				+ ", driverName=" + driverName + ", driverVersion="
				+ driverVersion + ", driverMajorVersion=" + driverMajorVersion
				+ ", driverMinorVersion=" + driverMinorVersion + "]";
	}

}
