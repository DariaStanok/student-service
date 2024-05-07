package telran.java52.student.dao;


import java.util.Set;
import java.util.stream.Stream;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import telran.java52.student.model.Student;



public interface StudentRepository extends MongoRepository<Student, Long> {
	Stream<Student> getAllBy();
	
	Stream<Student> findByNameIgnoreCase(String name);
	
	//Stream<Student> findDistinctByNameIn(Set<String> names);
	
	@Query(value="{ 'name' : { '$in' : ?0 } }", count=true)//no needed
	long countDistinctNamesInSet(Set<String> names);
	
	@Query ("{'scores.?0': {$gt: ?1}}")
	Stream<Student> findByExamAmdScoreGreaterThanStream (String exam, int score);
}
