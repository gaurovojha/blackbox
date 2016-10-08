package com.blackbox.ids.services.impl.common;

import java.io.IOException;

import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.common.constant.OTPStatus;
import com.blackbox.ids.dto.OTPStatusDTO;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class OTPStatusTypeAdapter<T> extends TypeAdapter<T> {

	@Override
	@Transactional
	public void write(JsonWriter out, T value) throws IOException {
		if (value == null) {
			out.nullValue();
			return;
		}
		OTPStatusDTO status = (OTPStatusDTO) value;
		// Here write what you want to the JsonWriter.
		out.beginObject();
		out.name("code");
		out.value(status.getCode());
		out.name("message");
		out.value(status.getMessage());
		out.endObject();
	}

	@Override
	@Transactional
	public T read(JsonReader in) throws IOException {
		// Properly deserialize the input (if you use deserialization)
		return null;
	}
}