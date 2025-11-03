package com.kt.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.kt.domain.Gender;
import com.kt.domain.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository {
	private final JdbcTemplate jdbcTemplate;

	public void save(User user) {
		// 서비스에서 dto를 도메인(비즈니스 모델)으로 바꿔서 전달받음

		var sql = """
			INSERT INTO MEMBER (
													id,
													loginId, 
													password, 
													name,
													birthday,
													mobile,
													email,
													gender,
													createdAt,
													updatedAt     
													) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
			""";
			// "INSERT INTO MEMBER (loginId, password, name, birthday) VALUES (?, ?, ?, ?)";

		jdbcTemplate.update(
			sql,
			user.getId(),
			user.getLoginId(),
			user.getPassword(),
			user.getName(),
			user.getBirthday(),
			user.getMobile(),
			user.getEmail(),
			user.getGender(),
			user.getCreatedAt(),
			user.getUpdatedAt()
		);

	}

	public Long selectMaxId(){
		var sql = "SELECT MAX(id) FROM MEMBER";

		var maxId = jdbcTemplate.queryForObject(sql, Long.class);

		return maxId == null ? 0L : maxId;
	}

	// 아이디 중복 체크
	// 1. count 해서 0보다 큰지 체크, select 해서 결과가 있는데 체크 -> 별로임
	// 2. unique 제약조건 걸어서 예외 처리 -> (DateViolatuion Exception) 별로인듯
	// 3. exist로 존재 여부 체크 -> boolean으로 바로 알 수 있음

	 public boolean existByLoginId(String loginId) {
		 var sql = "SELECT EXIST (SELECT id FROM MEMBER WHERE loginId = ?)";

		 return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, loginId));
	 }

	public void updatePassword(Long id, String password) {
		var sql = "UPDATE MEMBER SET password = ? WHERE id = ?";

		jdbcTemplate.update(sql, password, id);
	}

	public boolean existById(Long id) {
		var sql = "SELECT EXIST (SELECT id FROM MEMBER WHERE id = ?)";

		return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
	}

	public Optional<User> selectById(Long id) {
		var sql = "SELECT * FROM WHERE id = ?";

		// return jdbcTemplate.queryForObject(sql, rowMapper(), id);
		var list = jdbcTemplate.query(sql, rowMapper(), id);

		return list.stream().findFirst();
	}

	private RowMapper<User> rowMapper(){
		return (rs, rowNum) -> mapToUser(rs);
	}

	private User mapToUser(ResultSet rs) throws SQLException {
		return new User(
			rs.getLong("id"),
			rs.getString("loginId"),
			rs.getString("password"),
			rs.getString("name"),
			rs.getString("email"),
			rs.getString("mobile"),
			Gender.valueOf(rs.getString("gender")),
			rs.getObject("birthday", LocalDate.class),
			rs.getObject("createdAt", LocalDateTime.class),
			rs.getObject("updatedAt", LocalDateTime.class)
		);
	}
}
