## JFinal-Atomikos

基于JFinal、Atomikos Jdbc的XA分布式事务插件，特点：使用方式极简。

### 使用示例

#### 1、创建多个数据源

> Test.java代码引用

数据源1：

		String dbName="test1";
		String className = "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource";
		String jdbcUrl = "jdbc:mysql://192.168.1.117:3306/test?useUnicode=true&amp;characterEncoding=utf-8";
		String user = "jianzhimao";
		String pwssword = "jiuwei20130629";
			
		plugin = new AtomikosJdbcPlugin(jdbcUrl, user, pwssword, className);
		plugin.start();
		
		arp = new ActiveRecordPlugin(dbName, plugin);
		arp.start();
数据源2：

		dbName="test2";
		className = "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource";
		jdbcUrl = "jdbc:mysql://192.168.1.117:3306/test2?useUnicode=true&amp;characterEncoding=utf-8";
		user = "jianzhimao";
		pwssword = "jiuwei20130629";
			
		plugin = new AtomikosJdbcPlugin(jdbcUrl, user, pwssword, className);
		plugin.start();
		
		arp = new ActiveRecordPlugin(dbName, plugin);
		arp.start();
				
#### 2、执行跨库分布式事务

> Test.java代码引用

		DbExt.use("test1","test2").tx(new IAtom() {
			
			@Override
			public boolean run() throws SQLException {
				
				Db.use("test1").update("insert into test values(NULL,'2321')");
				
				Db.use("test2").update("update lm_bank_card set account='1father1' where id='03a2fb24-62f6-4ff7-85ba-74a51351b975'");
				
				return true;
			}
		});	

### 具体参考
	
* 测试类：[Test.java](https://github.com/cpthack/JFinal-Atomikos/blob/master/src/test/java/com/jiuwei/plugins/jfinalAtomikos/test/jdbc/Test.java)