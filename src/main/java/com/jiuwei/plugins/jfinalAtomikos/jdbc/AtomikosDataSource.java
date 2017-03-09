package com.jiuwei.plugins.jfinalAtomikos.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.atomikos.jdbc.AtomikosDataSourceBean;

public class AtomikosDataSource implements DataSource {
	
	private AtomikosDataSourceBean atomikosDataSourceBean;
	
	public AtomikosDataSourceBean getAtomikosDataSourceBean() {
		return atomikosDataSourceBean;
	}

	public void setAtomikosDataSourceBean(
			AtomikosDataSourceBean atomikosDataSourceBean) {
		this.atomikosDataSourceBean = atomikosDataSourceBean;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return atomikosDataSourceBean.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		atomikosDataSourceBean.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		atomikosDataSourceBean.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return atomikosDataSourceBean.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return atomikosDataSourceBean.getParentLogger();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return atomikosDataSourceBean.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return atomikosDataSourceBean.isWrapperFor(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return atomikosDataSourceBean.getConnection();
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		return atomikosDataSourceBean.getConnection(username, password);
	}



}
