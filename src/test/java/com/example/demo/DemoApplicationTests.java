package com.example.demo;

import com.example.demo.domain.Todo;
import com.example.demo.dto.request.ReqTodoDTO;
import com.example.demo.dto.response.ResTodoDTO;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.TodoRepository;
import com.example.demo.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	TodoService todoService;

	private static Long todoId;
	private final String prePath = "/api/todos";


	@Container
	private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.33")
			.withDatabaseName("testdb")
			.withUsername("testuser")
			.withPassword("testpass");

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		mysql.start();
		registry.add("spring.datasource.url", mysql::getJdbcUrl);
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
		registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
	}

	@BeforeEach
	void initData() throws Exception {
		ReqTodoDTO request = new ReqTodoDTO("새로운 할 일");
		MvcResult result = mockMvc.perform(post(prePath)
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(request)))
				.andExpect(status().isOk())
				.andReturn();

		ResTodoDTO response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), ResTodoDTO.class);
		todoId = response.getId();
	}

	@Test
	@Order(1)
	@DisplayName("ToDo 추가 API 테스트 - 성공")
	void insertTodo_Success() throws Exception {
		ReqTodoDTO request = new ReqTodoDTO("새로운 할 일");
		mockMvc.perform(post(prePath)
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.task").value("새로운 할 일"));
	}

	@Test
	@Order(2)
	@DisplayName("ToDo 추가 API 테스트 - 실패 (task 값 없음)")
	void insertTodo_Fail_EmptyTask() throws Exception {
		ReqTodoDTO request = new ReqTodoDTO("");
		mockMvc.perform(post(prePath)
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(request)))
				.andExpect(status().isBadRequest());
	}

	@Test
	@Order(3)
	@DisplayName("ToDo 목록 조회 API 테스트 - 성공")
	void getTodoList_Success() throws Exception {
		mockMvc.perform(get(prePath))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").isNumber());
	}

	@Test
	@Order(4)
	@DisplayName("ToDo 단일 조회 API 테스트 - 성공")
	void getTodo_Success() throws Exception {
		mockMvc.perform(get(prePath + "/{id}", todoId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(todoId));
	}

	@Test
	@Order(5)
	@DisplayName("ToDo 단일 조회 API 테스트 - 실패 (존재하지 않는 ID)")
	void getTodo_Fail_NotFound() throws Exception {
		Long invalidId = 999L;
		mockMvc.perform(get(prePath + "/{id}", invalidId))
				.andExpect(status().isNotFound());
	}

	@Test
	@Order(6)
	@DisplayName("ToDo 완료 여부 수정 API 테스트 - 성공")
	void updateCompleteYn_Success() throws Exception {
		mockMvc.perform(put("/api/todos/{id}/completeYn", todoId)
						.param("ynType", "Y"))
				.andExpect(status().isOk());

		ResTodoDTO resTodoDTO = todoService.getTodo(todoId);
		assertEquals(resTodoDTO.isCompleteYn(), true);
	}

	@Test
	@Order(7)
	@DisplayName("ToDo 삭제 여부 수정 API 테스트 - 성공")
	void updateDeleteYn_Success() throws Exception {
		mockMvc.perform(put(prePath + "/{id}/deleteYn", todoId)
						.param("ynType", "Y"))
				.andExpect(status().isOk());

		ResTodoDTO resTodoDTO = todoService.getTodo(todoId);
		assertEquals(resTodoDTO.isDeleteYn(), true);
	}

	@Test
	@Order(8)
	@DisplayName("ToDo 수정 API 테스트 - 성공")
	void updateTodo_Success() throws Exception {
		ReqTodoDTO request = new ReqTodoDTO("수정된 할 일");
		mockMvc.perform(put(prePath + "/{id}", todoId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request)))
				.andExpect(status().isOk());

		// DB에서 수정된 값 확인
		ResTodoDTO resTodoDTO = todoService.getTodo(todoId);

		// 수정된 값이 기대하는 값과 동일한지 검증
		assertEquals(request.getTask(), resTodoDTO.getTask());
	}

	@Test
	@Order(9)
	@DisplayName("ToDo 수정 API 테스트 - 실패 (존재하지 않는 ID)")
	void updateTodo_Fail_NotFound() throws Exception {
		Long invalidId = 999L;
		ReqTodoDTO request = new ReqTodoDTO("수정된 할 일");

		mockMvc.perform(put(prePath + "/{id}", invalidId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request)))
				.andExpect(status().isNotFound());
	}

	@Test
	@Order(10)
	@DisplayName("ToDo 수정 API 테스트 - 실패 (task 값 없음)")
	void updateTodo_Fail_EmptyTask() throws Exception {
		ReqTodoDTO request = new ReqTodoDTO("");

		mockMvc.perform(put(prePath + "/{id}", todoId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request)))
				.andExpect(status().isBadRequest());
	}

	@Test
	@Order(11)
	@DisplayName("ToDo 삭제 API 테스트 - 성공")
	void deleteTodo_Success() throws Exception {
		mockMvc.perform(delete(prePath + "/{id}", todoId))
				.andExpect(status().isOk());

		assertThatThrownBy(() -> todoService.getTodo(todoId)).isInstanceOf(CustomException.class);
	}

	@Test
	@Order(12)
	@DisplayName("ToDo 삭제 API 테스트 - 실패 (존재하지 않는 ID)")
	void deleteTodo_Fail_NotFound() throws Exception {
		Long invalidId = 999L;

		mockMvc.perform(delete(prePath + "/{id}", invalidId))
				.andExpect(status().isNotFound());
	}

	@Test
	@Order(13)
	@DisplayName("3일 이상 지난 완료된 ToDo 자동 삭제 테스트")
	void deleteOldTodos_Success() {
		// given
		ReqTodoDTO reqTodoDTO1 = new ReqTodoDTO("완료처리된 오래된 할 일");
		Todo todo1 = new Todo(reqTodoDTO1);
		todo1.setCompleteYn(true);
		todo1.setCompletedDate(LocalDateTime.now().minusDays(3)); // 4일 전 완료

		ReqTodoDTO reqTodoDTO2 = new ReqTodoDTO("삭제처리된 오래된 할 일");
		Todo todo2 = new Todo(reqTodoDTO2);
		todo2.setDeleteYn(true);
		todo2.setDeletedDate(LocalDateTime.now().minusDays(1));  // 1일 전 완료
		todoRepository.save(todo1);
		todoRepository.save(todo2);

		// when
		todoService.deleteOldTodos(); // 3일 이상 지난 완료된 ToDo 삭제 메서드 호출

		// then
		List<Todo> todos = todoRepository.findAll();
		assertThat(todos).contains(todo2); // 최신 TODO는 삭제되지 않아야 함
		assertThat(todos).doesNotContain(todo1); // 7일 이상 지난 TODO는 삭제되어야 함
	}
}

