package kimjoohe.myboard.mapper;

import kimjoohe.myboard.controller.SearchForm;
import kimjoohe.myboard.domain.BoardVO;
import kimjoohe.myboard.domain.userVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardMapper {
    public  void boardInsert(BoardVO board) throws Exception;
    public List<BoardVO> boardList() throws Exception;
    public BoardVO boardView(int bno) throws Exception;
    public void boardUpdate(BoardVO board) throws Exception;
    public void boardDelete(int bno) throws Exception;
    public List<BoardVO> boardSearch(@Param("search_option") String search_option, @Param("keyword") String keyword) throws Exception;
    //public void userJoin(userVO user) throws Exception;
    //public int idChk(String userID) throws Exception;
}
