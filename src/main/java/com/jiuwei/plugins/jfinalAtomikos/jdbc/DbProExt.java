package com.jiuwei.plugins.jfinalAtomikos.jdbc;

import java.sql.Connection;

import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.DbPro;
import com.jfinal.plugin.activerecord.IAtom;

public class DbProExt extends DbPro {
	private final Logger logger = Logger.getLogger(DbProExt.class);

	private String[] configNames;

	public String[] getConfigNames() {
		return configNames;
	}

	public DbProExt setConfigNames(String[] configNames) {
		this.configNames = configNames;
		return this;
	}

	public static UserTransaction getUserTransaction() {
		UserTransaction utx = new UserTransactionImp();
		return utx;
	}

	public boolean tx(IAtom atom) {
		UserTransaction utx = getUserTransaction();
		boolean result = false;
		Config config = null;
		Connection conn = null;
		try {
			utx.begin();
			for (String configName : configNames) {
				config = DbKit.getConfig(configName);
				conn = config.getConnection();
				conn.setAutoCommit(false);
				config.setThreadLocalConnection(conn);
			}
			result = atom.run();
			if (result)
				utx.commit();
			else
				utx.rollback();
		} catch (Exception e) {
			logger.error("XA Transaction Execption >>>", e);
		} finally {
			for (String configName : configNames) {
				config = DbKit.getConfig(configName);
				config.removeThreadLocalConnection();
			}
		}
		return result;
	}

}
