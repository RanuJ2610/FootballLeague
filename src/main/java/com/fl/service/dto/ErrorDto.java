package com.fl.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDto {

	private String correlationId;
	private String code;
	private String message;
	private String data;

	public ErrorDto(String correlationId, String code, String message){
		this.correlationId = correlationId;
		this.code = code;
		this.message = message;
	}
}
