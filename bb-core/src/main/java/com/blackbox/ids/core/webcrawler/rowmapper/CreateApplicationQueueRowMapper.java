package com.blackbox.ids.core.webcrawler.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.springframework.jdbc.core.RowMapper;

import com.blackbox.ids.core.model.webcrawler.CreateApplicationQueue;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;

public class CreateApplicationQueueRowMapper implements RowMapper<CreateApplicationQueue> {
	
	@Override
	public CreateApplicationQueue mapRow(ResultSet rs, int rownumber)
			throws SQLException {
		Long id = rs.getLong(1);
		String applicationNumber = rs.getString(2);
		String customerNumber = rs.getString(3);
		String jurisdiction = rs.getString(4);
		java.util.Date filingDate = rs.getDate(5);
		String correspondenceLink = rs.getString(6);
		int retryCount = rs.getInt(7);
		Long baseApplicationId = rs.getLong(8);
		String appNumberRaw = rs.getString(9);
		baseApplicationId = baseApplicationId == 0 ? null : baseApplicationId;
		Calendar filingDateCal = BlackboxDateUtil.toCalendar(filingDate);
		CreateApplicationQueue app = new CreateApplicationQueue(id, applicationNumber, customerNumber, jurisdiction, filingDateCal);
		app.setCorrespondenceLink(correspondenceLink);
		app.setRetryCount(retryCount);
		app.setBaseCaseApplicationQueueId(baseApplicationId);
		app.setAppNumberRaw(appNumberRaw);
		return app;
	}
}
