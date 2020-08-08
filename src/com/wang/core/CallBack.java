package com.wang.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface CallBack {

	public Object doExecute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet);

}
