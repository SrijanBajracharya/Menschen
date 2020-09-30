package com.achiever.menschenfahren.entities.response;

import java.io.Serializable;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DataResponse<T> implements Serializable {

	private static final long serialVersionUID = -7039640254132599178L;

	@NonNull
	private T data;

}
