package kimjoohe.myboard.controller;

import kimjoohe.myboard.domain.userVO;
import kimjoohe.myboard.mapper.BoardMapper;
import kimjoohe.myboard.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/join")
    public String join(){
        return "join";
    }

    @PostMapping("/JoinAction")
    public String joinAction(JoinForm form) throws Exception{
        userVO user = new userVO();
        user.setUserID(form.getUserID());
        user.setUserPassword(form.getUserPassword());
        user.setUserEmail(form.getUserEmail());
        userMapper.userJoin(user);
        return "redirect:/board";
    }

    @ResponseBody
    @RequestMapping(value = "/idChk", method = RequestMethod.POST)
    public int idChk(HttpServletRequest req) throws Exception{
        String userID = req.getParameter("id");
        int result = userMapper.idChk(userID);
        return result;
    }
}
