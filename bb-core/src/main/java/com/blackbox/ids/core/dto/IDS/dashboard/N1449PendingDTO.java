package com.blackbox.ids.core.dto.IDS.dashboard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.mysema.commons.lang.Pair;
import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.types.Expression;

public class N1449PendingDTO extends IDSDashBoardBaseDTO {
	
	private Map<String,Date> idsPending1449;
	private Date notifiedOn;
	
	@QueryProjection
	public N1449PendingDTO(Long dbId, String familyId, String jurisdiction, String applicationNo,Map<String, Calendar> calMap , Calendar notifiedOn) {
		super(dbId, familyId, jurisdiction, applicationNo);
		Map<String,Date> idsPending1449Dates = new LinkedHashMap<>();
		int i = 0;
		
 		System.out.println("INPUT");
		//calMap.entrySet().stream().forEach(e-> System.out.println(e.getValue().getTime()));
		System.out.println("SORTED");
		//calMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(e-> System.out.println(e.getValue().getTime()));
		
		/*for (Map.Entry<String, Calendar> entry : calMap.) {
			
		}
		*/
		/*idsPending1449Dates = calMap.entrySet().stream().sorted(Comparator.comparing(e -> e.getValue()))
				.collect(Collectors.toMap(e-> e.getKey(), e-> e.getValue() == null ? null : e.getValue().getTime()));*/
		
		 calMap.entrySet().stream().sorted(Comparator.comparing(e -> e.getValue())).forEachOrdered(e ->idsPending1449Dates.put(e.getKey(),e.getValue() == null? null:e.getValue().getTime()));
		
		this.idsPending1449 = idsPending1449Dates == null?null:idsPending1449Dates;
	
		
		this.notifiedOn = notifiedOn == null? null: notifiedOn.getTime();
	}


	public Map<String,Date> getIdsPending1449() {
		return idsPending1449;
	}


	public void setIdsPending1449(Map<String,Date> idsPending1449) {
		this.idsPending1449 = idsPending1449;
	}

	public Date getNotifiedOn() {
		return notifiedOn;
	}

	public void setNotifiedOn(Date notifiedOn) {
		this.notifiedOn = notifiedOn;
	}
}
