package kimjoohe.myboard;

import kimjoohe.myboard.domain.BoardVO;
import kimjoohe.myboard.mapper.BoardMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;


@SpringBootTest
public class MyBoardApplicationTests {
	@Autowired
	private BoardMapper mapper;

	@Test
	void contextLoads() {
	}
	@Test
	public  void testMapper() throws Exception{
		BoardVO vo = new BoardVO();
		vo.setSubject("2제목입니다.");
		vo.setContent("2내용입니다.");
		vo.setWriter("2작성자입니다.");
		mapper.boardInsert(vo);
	}


}
