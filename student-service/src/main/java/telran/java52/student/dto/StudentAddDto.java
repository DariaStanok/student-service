package telran.java52.student.dto;

import lombok.Getter;

@Getter
public class StudentAddDto {
    Long id;
	String name;
	String password;
	
	public StudentAddDto(Long id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}
	
	
}
