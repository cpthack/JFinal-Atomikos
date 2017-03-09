package com.jiuwei.plugins.jfinalAtomikos.intercetpor;


import java.sql.SQLException;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.plugin.activerecord.IAtom;
import com.jiuwei.plugins.jfinalAtomikos.jdbc.DbExt;

public class Xa implements Interceptor {

	@Override
	public void intercept(final ActionInvocation ai) {
		TxConfig txConfig = ai.getMethod().getAnnotation(TxConfig.class);
		if (txConfig == null)
			txConfig = ai.getController().getClass()
					.getAnnotation(TxConfig.class);
		String[] configNames=txConfig.values();
		DbExt.use(configNames).tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				ai.invoke();
				return true;
			}
		});
	}

}
