package kimjoohe.myboard.mapper;

import kimjoohe.myboard.domain.userVO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    public void userJoin(userVO user) throws Exception;
    public int idChk(String userID) throws Exception;
    public userVO userLogin(String userID) throws Exception;
}
