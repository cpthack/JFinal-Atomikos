package com.jiuwei.plugins.jfinalAtomikos.jdbc;

import com.jfinal.plugin.activerecord.Db;

public class DbExt extends Db {
	
	public static DbProExt use(String... configNames){
		return new DbProExt().setConfigNames(configNames);
	}
}
