package com.murasame.smarthrm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SkillDTO implements Serializable {
	private Integer skillId;
    private String skillName;
}
