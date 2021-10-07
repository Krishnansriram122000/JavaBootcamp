
package com.example.demo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.id.Configurable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Configuration
@ComponentScan(basePackages = "com.example.demo")
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@RestController
public class DemoApplication {

	@Autowired
	private static DbOperations db;

	public static void main(String[] args) {

		ApplicationContext con = new ClassPathXmlApplicationContext("spring.xml");
		db= (DbOperations) con.getBean("DbOperations");
		//db.createAccount();
		//db.Transact();
		SpringApplication.run(DemoApplication.class, args);

	}
	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}
	@GetMapping("/create")
	public Account create(@RequestParam(value="fname")String fname,
						 @RequestParam(value="lname")String lname,
						 @RequestParam(value="ifsc")String ifsc,
						 @RequestParam(value="pan")String pan,
						 @RequestParam(value="city")String city,
						 @RequestParam(value="accno")long accno,
						 @RequestParam(value="aadhar")long aadhar,
						 @RequestParam(value="pno")long pno
						 ){
		return db.createAccount(fname,lname,ifsc,pan,city,accno,aadhar,pno);
	}

	@GetMapping("/transact")
	public Transaction transact(@RequestParam(value="dorw")String dorw,
						   @RequestParam(value="accno")long accno,
						   @RequestParam(value="amount")long amount
							){
			return db.Transact(dorw,accno,amount);
	}


	@RequestMapping(method = RequestMethod.POST,value="/jsonca")
	public Account jsonca(@RequestBody Account accdetai){
		return db.createjsn(accdetai);
	}

	@RequestMapping(method = RequestMethod.POST,value="/jsontr")
	public Transaction jsontr(@RequestBody Transaction t){
		return db.Transact(t.getTransaction_type(),t.getAccount_number(),t.getAmount());
	}

	@RequestMapping(method = RequestMethod.POST,value="/jsonpassbook")
	public List<Transaction> jsonpassbook(@RequestBody Transaction t){
		return db.passbook(t.getAccount_number());
	}

}
            