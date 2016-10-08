package com.blackbox.ids.abbyy.api.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.blackbox.ids.abbyy.api.response.Document;

@Path("/")
public interface AbbyyResource {

	@POST
	@Path("/extract")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	Document extract(@FormParam("path") String path);
	
}
