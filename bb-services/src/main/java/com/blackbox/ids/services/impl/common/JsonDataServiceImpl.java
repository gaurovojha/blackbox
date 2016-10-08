package com.blackbox.ids.services.impl.common;

import java.lang.reflect.Type;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.services.common.JsonDataService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service("jsonDataService")
public class JsonDataServiceImpl implements JsonDataService{

	@Override
	@Transactional
	public String parseLongList(List<Long> ids) {
		Gson gson = new Gson();
		Type idList = new TypeToken<List<Long>>(){}.getType();		
		return gson.toJson(ids,idList); 		
	}

}
