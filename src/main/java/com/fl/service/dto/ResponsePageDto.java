package com.fl.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponsePageDto<T> {

	List<T> content;
}
