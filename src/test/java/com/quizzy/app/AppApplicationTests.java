package com.quizzy.app;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.header.Header;
import org.springframework.test.context.junit4.SpringRunner;

import com.quizzy.app.model.Group;
import com.quizzy.app.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppApplicationTests {
	@Autowired
	private TestRestTemplate template;
	
	private static String COURSE_ENDPOINT = "http://localhost:8080/api/courses";
	private static String USER_ENDPOINT = "http://localhost:8080/api/users";
	private static String GROUP_ENDPOINT = "http://localhost:8080/api/groups";
	
	@Test
	@Ignore
	public void contextLoads() {
		
	}
	
	@Test
	@Ignore
	public void test_Create_Users() {
		User user = new User();
		user.setName("Fadejimi");
		user.setEmail("adegbulugbefadejimi@gmail.com");
		user.setPassword("password");
		user.setToken("939394848444");
		
		template.postForEntity(USER_ENDPOINT, user, User.class);
		
		Group group = new Group();
		group.setAdmin(user);
		group.setName("New Group");

		template.postForEntity(USER_ENDPOINT + "/1/groups", group, Group.class);
		
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("content-type", "text/uri-list");
		HttpEntity<String> userHttpEntity = 
				new HttpEntity<>(USER_ENDPOINT + "/1");
	}

}
