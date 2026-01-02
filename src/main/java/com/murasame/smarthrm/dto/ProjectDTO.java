package com.murasame.smarthrm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ProjectDTO implements Serializable {
	private Integer projId;
	private String projName;
}
