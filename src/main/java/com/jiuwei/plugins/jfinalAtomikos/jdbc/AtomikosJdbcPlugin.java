package com.jiuwei.plugins.jfinalAtomikos.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import javax.sql.DataSource;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;

public class AtomikosJdbcPlugin implements IPlugin, IDataSourceProvider {

	private String jdbcUrl;
	private String user;
	private String password;
	private String driverClass = "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource";
	private int maxPoolSize = 100;
	private int minPoolSize = 10;
	private int initialPoolSize = 10;
	private int maxIdleTime = 20;
	private int acquireIncrement = 2;

	private AtomikosDataSource dataSource;
	private AtomikosDataSourceBean atomikosDataSourceBean;

	public AtomikosJdbcPlugin setDriverClass(String driverClass) {
		if (StrKit.isBlank(driverClass))
			throw new IllegalArgumentException("driverClass can not be blank.");
		this.driverClass = driverClass;
		return this;
	}

	public AtomikosJdbcPlugin setMaxPoolSize(int maxPoolSize) {
		if (maxPoolSize < 1)
			throw new IllegalArgumentException("maxPoolSize must more than 0.");
		this.maxPoolSize = maxPoolSize;
		return this;
	}

	public AtomikosJdbcPlugin setMinPoolSize(int minPoolSize) {
		if (minPoolSize < 1)
			throw new IllegalArgumentException("minPoolSize must more than 0.");
		this.minPoolSize = minPoolSize;
		return this;
	}

	public AtomikosJdbcPlugin setInitialPoolSize(int initialPoolSize) {
		if (initialPoolSize < 1)
			throw new IllegalArgumentException(
					"initialPoolSize must more than 0.");
		this.initialPoolSize = initialPoolSize;
		return this;
	}

	public AtomikosJdbcPlugin setMaxIdleTime(int maxIdleTime) {
		if (maxIdleTime < 1)
			throw new IllegalArgumentException("maxIdleTime must more than 0.");
		this.maxIdleTime = maxIdleTime;
		return this;
	}

	public AtomikosJdbcPlugin setAcquireIncrement(int acquireIncrement) {
		if (acquireIncrement < 1)
			throw new IllegalArgumentException(
					"acquireIncrement must more than 0.");
		this.acquireIncrement = acquireIncrement;
		return this;
	}

	public AtomikosJdbcPlugin(String jdbcUrl, String user, String password) {
		this.jdbcUrl = jdbcUrl;
		this.user = user;
		this.password = password;
	}

	public AtomikosJdbcPlugin(String jdbcUrl, String user, String password,
			String driverClass) {
		this.jdbcUrl = jdbcUrl;
		this.user = user;
		this.password = password;
		this.driverClass = driverClass != null ? driverClass : this.driverClass;
	}

	public AtomikosJdbcPlugin(String jdbcUrl, String user, String password,
			String driverClass, Integer maxPoolSize, Integer minPoolSize,
			Integer initialPoolSize, Integer maxIdleTime,
			Integer acquireIncrement) {
		initAtomikosJdbcProperties(jdbcUrl, user, password, driverClass,
				maxPoolSize, minPoolSize, initialPoolSize, maxIdleTime,
				acquireIncrement);
	}

	private void initAtomikosJdbcProperties(String jdbcUrl, String user,
			String password, String driverClass, Integer maxPoolSize,
			Integer minPoolSize, Integer initialPoolSize, Integer maxIdleTime,
			Integer acquireIncrement) {
		this.jdbcUrl = jdbcUrl;
		this.user = user;
		this.password = password;
		this.driverClass = driverClass != null ? driverClass : this.driverClass;
		this.maxPoolSize = maxPoolSize != null ? maxPoolSize : this.maxPoolSize;
		this.minPoolSize = minPoolSize != null ? minPoolSize : this.minPoolSize;
		this.initialPoolSize = initialPoolSize != null ? initialPoolSize
				: this.initialPoolSize;
		this.maxIdleTime = maxIdleTime != null ? maxIdleTime : this.maxIdleTime;
		this.acquireIncrement = acquireIncrement != null ? acquireIncrement
				: this.acquireIncrement;
	}

	public AtomikosJdbcPlugin(File propertyfile) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(propertyfile);
			Properties ps = new Properties();
			ps.load(fis);

			initAtomikosJdbcProperties(ps.getProperty("jdbcUrl"),
					ps.getProperty("user"), ps.getProperty("password"),
					ps.getProperty("driverClass"),
					toInt(ps.getProperty("maxPoolSize")),
					toInt(ps.getProperty("minPoolSize")),
					toInt(ps.getProperty("initialPoolSize")),
					toInt(ps.getProperty("maxIdleTime")),
					toInt(ps.getProperty("acquireIncrement")));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public AtomikosJdbcPlugin(Properties properties) {
		Properties ps = properties;
		initAtomikosJdbcProperties(ps.getProperty("jdbcUrl"),
				ps.getProperty("user"), ps.getProperty("password"),
				ps.getProperty("driverClass"),
				toInt(ps.getProperty("maxPoolSize")),
				toInt(ps.getProperty("minPoolSize")),
				toInt(ps.getProperty("initialPoolSize")),
				toInt(ps.getProperty("maxIdleTime")),
				toInt(ps.getProperty("acquireIncrement")));
	}

	public boolean start() {

		atomikosDataSourceBean = new AtomikosDataSourceBean();
		atomikosDataSourceBean.setUniqueResourceName(UUID.randomUUID()
				.toString());// 设置资源池唯一名称
		try {
			atomikosDataSourceBean.setXaDataSourceClassName(driverClass);
		} catch (Exception e) {
			atomikosDataSourceBean = null;
			System.err.println("AtomikosJdbcPlugin start error");
			throw new RuntimeException(e);
		}
		Properties p = new Properties();
		p.setProperty("user", user);
		p.setProperty("password", password);
		p.setProperty("URL", jdbcUrl);
		atomikosDataSourceBean.setXaProperties(p);
		atomikosDataSourceBean.setMaxPoolSize(maxPoolSize);
		atomikosDataSourceBean.setMinPoolSize(minPoolSize);
		atomikosDataSourceBean.setMaxIdleTime(maxIdleTime);
		atomikosDataSourceBean.setPoolSize(initialPoolSize);
		atomikosDataSourceBean.setMaintenanceInterval(acquireIncrement);

		dataSource = new AtomikosDataSource();
		dataSource.setAtomikosDataSourceBean(atomikosDataSourceBean);
		return true;
	}

	private Integer toInt(String str) {
		return Integer.parseInt(str);
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public boolean stop() {
		if (atomikosDataSourceBean != null)
			atomikosDataSourceBean.close();
		return true;
	}
}
