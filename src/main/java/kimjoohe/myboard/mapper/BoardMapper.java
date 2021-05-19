package kimjoohe.myboard.mapper;

import kimjoohe.myboard.domain.BoardVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardMapper {
    public  void boardInsert(BoardVO board) throws Exception;
    public List<BoardVO> boardList() throws Exception;
    public BoardVO boardView(int bno) throws Exception;
    public void boardUpdate(BoardVO board) throws Exception;
    public void boardDelete(int bno) throws Exception;

}
