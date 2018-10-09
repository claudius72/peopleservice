/**
 * 
 */
package com.velox.cloud.ms.rest.PeopleService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

/**
 * @author claudius28
 *
 */
@Controller
@RequestMapping("api/person")
public class PersonController {
	private DBCollection peopleColl;
	
	public PersonController() {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = mongoClient.getDB("local");
		peopleColl = db.getCollection("people");
	}
	
    @GetMapping(produces = "application/json")
	public ResponseEntity<String> doGet() {
		Gson gson = new Gson();
		List<Person> peopleList = new ArrayList<>();
		try {
			DBCursor curs = peopleColl.find();
			curs.forEach(p -> {			
				Person person = gson.fromJson(p.toString(), Person.class);
				System.out.println(person);
				peopleList.add(person);
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(gson.toJson(peopleList));
	}

	@GetMapping(path = "/{name}", produces = "application/json")
	public ResponseEntity<String> doGetByName(@PathVariable("name") String n) {
		Gson gson = new Gson();
		Person person = new Person();
		try {
			DBObject p = peopleColl.findOne(new BasicDBObject("name", n));
			
			person = gson.fromJson(p.toString(), Person.class);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(gson.toJson(person));
	}

	@PostMapping(consumes = {"text/plain", "application/json"})
	public ResponseEntity<String> doPost(@RequestBody java.lang.String entity) {	
		System.out.println("POST argument is = " + entity);
		Gson gson = new Gson();
		List<Person> peopleList = new ArrayList<>();
		try {
			DBObject person = BasicDBObject.parse(entity);
					
			System.out.println("Person = " + person.toString());
			peopleColl.insert(person);
			
			DBCursor curs = peopleColl.find();
			curs.forEach(p -> {		
				Person pers = gson.fromJson(p.toString(), Person.class);
				peopleList.add(pers);
				System.out.println(pers);
			});
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);		
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		return ResponseEntity.ok(gson.toJson(peopleList));
	}
}
