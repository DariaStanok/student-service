package telran.java52.student.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import telran.java52.student.dao.StudentRepository;
import telran.java52.student.dto.ScoreDto;
import telran.java52.student.dto.StudentAddDto;
import telran.java52.student.dto.StudentDto;
import telran.java52.student.dto.StudentUpdateDto;
import telran.java52.student.dto.exeptions.StudentNotFoundExeption;
import telran.java52.student.model.Student;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
	
	final ModelMapper modelMapper;
	final StudentRepository studentRepository;

	@Override
	public Boolean addStudent(StudentAddDto studentAddDto) {
		if(studentRepository.existsById(studentAddDto.getId())) {
			return false;
		}
	//	Student student = new Student(studentAddDto.getId(), studentAddDto.getName(), studentAddDto.getPassword());
		Student student = modelMapper.map(studentAddDto, Student.class);
		studentRepository.save(student);
		return true;
	}

	@Override
	public StudentDto findStudent(Long id) {
		Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundExeption::new);
		//return new StudentDto(id, student.getName(), student.getScores());
		return modelMapper.map(student, StudentDto.class);
	}

	@Override
	public StudentDto removeStudent(Long id) {
		Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundExeption::new);
		studentRepository.deleteById(id);
		//return new StudentDto(id, student.getName(), student.getScores());
		return modelMapper.map(student, StudentDto.class);
	}

	@Override
	public StudentAddDto updateStudent(Long id, StudentUpdateDto studentUpdateDto) {
		Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundExeption::new);
			student.setName(studentUpdateDto.getName());
			student.setPassword(studentUpdateDto.getPassword());
			studentRepository.save(student);
		//return new StudentAddDto(student.getId(), student.getName(), student.getPassword());
			return modelMapper.map(student, StudentAddDto.class);
	}

	@Override
	public Boolean addScore(Long id, ScoreDto scoreDto) {
		Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundExeption::new);
		boolean res = student.addScore(scoreDto.getExamName(), scoreDto.getScore());
		studentRepository.save(student);
		return res;
	}

	@Override
	public List<StudentDto> findStudentsByName(String name) {
	    return studentRepository.findByNameIgnoreCase(name)
	            .map(student -> modelMapper.map(student, StudentDto.class))
	            .toList();
	           // .collect(Collectors.toList());
	}

//	public Long getStudentsNamesQuantity(Set<String> names) {
//		 return studentRepository.getAllBy()
//						.map(Student::getName)
//						.filter(names::contains)
//						.distinct()
//						.count();
//		
//	}			
	
//	public Long getStudentsNamesQuantity(Set<String> names) {
//		 return studentRepository.findDistinctByNameIn(names)
//									.distinct()
//									.count();
//					
//	}

	@Override
	public Long getStudentsNamesQuantity(Set<String> names) {
		 return studentRepository.countDistinctNamesInSet(names);
					
	}

	@Override
	public List<StudentDto> getStudentsByExamMinScore(String exam, Integer minScore) {
		
		 return studentRepository.findByExamAmdScoreGreaterThanStream(exam, minScore)
		            .filter(student -> student.getScores().containsKey(exam) && student.getScores().get(exam) >= minScore)
		            .map(student ->  modelMapper.map(student, StudentDto.class))
		            .collect(Collectors.toList());
	}

}
