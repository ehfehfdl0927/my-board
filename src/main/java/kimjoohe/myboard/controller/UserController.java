package kimjoohe.myboard.controller;

import kimjoohe.myboard.domain.userVO;
import kimjoohe.myboard.mapper.BoardMapper;
import kimjoohe.myboard.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.Normalizer;

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
        return "login";
    }

    @ResponseBody
    @RequestMapping(value = "/idChk", method = RequestMethod.POST)
    public int idChk(HttpServletRequest req) throws Exception{
        String userID = req.getParameter("id");
        int result = userMapper.idChk(userID);
        return result;
    }

    @GetMapping("/login")
    public String login(){ return "login"; }

    @PostMapping("/LoginAction")
    public String loginAction(Model model, JoinForm form, HttpServletRequest request)throws Exception {
        String userID = form.getUserID();
        String userPassword = form.getUserPassword();

        userVO user = userMapper.userLogin(userID);
        if(user != null){
            if(user.getUserPassword().equals(userPassword)){
                HttpSession session = request.getSession();
                session.setAttribute("id",userID);
                return "ff";
            }
            model.addAttribute("msg","비밀번호가 틀립니다.");
            model.addAttribute("url","login");
            return "alert";
        }

        model.addAttribute("msg","존재하지 않는 아이디입니다.");
        model.addAttribute("url","login");
        return "alert";
    }

    @PostMapping("/LogoutAction")
    public String logoutAction(HttpSession session){
        session.invalidate();
        return "login";
    }
    @GetMapping("/Logout")
    public String logout(HttpSession session){
        return "ff";
    }
}
