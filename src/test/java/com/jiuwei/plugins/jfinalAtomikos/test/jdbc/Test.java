package com.jiuwei.plugins.jfinalAtomikos.test.jdbc;
import java.sql.SQLException;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jiuwei.plugins.jfinalAtomikos.jdbc.AtomikosJdbcPlugin;
import com.jiuwei.plugins.jfinalAtomikos.jdbc.DbExt;

public class Test {

	public static void main(String[] arg) {
		ActiveRecordPlugin arp = null;
		AtomikosJdbcPlugin plugin = null;
		String dbName="test1";
		String className = "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource";
		String jdbcUrl = "jdbc:mysql://192.168.1.117:3306/test?useUnicode=true&amp;characterEncoding=utf-8";
		String user = "jianzhimao";
		String pwssword = "jiuwei20130629";
			
		plugin = new AtomikosJdbcPlugin(jdbcUrl, user, pwssword, className);
		plugin.start();
		
		arp = new ActiveRecordPlugin(dbName, plugin);
		arp.start();
		
		
		dbName="test2";
		className = "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource";
		jdbcUrl = "jdbc:mysql://192.168.1.117:3306/test2?useUnicode=true&amp;characterEncoding=utf-8";
		user = "jianzhimao";
		pwssword = "jiuwei20130629";
			
		plugin = new AtomikosJdbcPlugin(jdbcUrl, user, pwssword, className);
		plugin.start();
		
		arp = new ActiveRecordPlugin(dbName, plugin);
		arp.start();
		
		
		DbExt.use("test1","test2").tx(new IAtom() {
			
			@Override
			public boolean run() throws SQLException {
				
				Db.use("test1").update("insert into test values(NULL,'2321')");
				
				Db.use("test2").update("update lm_bank_card set account='1father1' where id='03a2fb24-62f6-4ff7-85ba-74a51351b975'");
				
				return true;
			}
		});
	}
}
