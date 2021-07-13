package kimjoohe.myboard.controller;

import kimjoohe.myboard.domain.BoardVO;
import kimjoohe.myboard.domain.userVO;
import kimjoohe.myboard.mapper.BoardMapper;
import kimjoohe.myboard.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RestController
@Controller
public class BoardListController {
    @Autowired
    private BoardMapper boardMapper; //게시물 관련 메퍼인터페이스
    @Autowired
    private UserMapper userMapper;

    //@RequestMapping("/board") //게시물 리스트 가져오기 글목록
    //public List<BoardVO> board() throws Exception{
       // List<BoardVO> board = boardMapper.boardList(); //게시물 메퍼에 boardList 메소드 실행후 결과 담기
       // return board;
    //}

    @GetMapping("/board")  //게시물 리스트 가져오기 get방식
    public String list(HttpServletRequest request, Model model, @RequestParam("kind") String kind, @RequestParam("realm") String realm) throws Exception{
        String pageNumber = "1";
        int off = 0;
        int target2 = 0; //새로
        if(request.getParameter("pageNumber") != null){
            pageNumber = request.getParameter("pageNumber");
        }
        if(Integer.parseInt(pageNumber) == 1){
            off = 0;
        }
        else{
            off = (Integer.parseInt(pageNumber) - 1) * 20;
        }
        List<BoardVO> board = boardMapper.boardList(off, kind, realm);

        int target =(Integer.parseInt(pageNumber) - 1) * 10;
        int target1 = boardMapper.targetPage(target);
        target1 = target1 / 10;
        int startPage = (Integer.parseInt(pageNumber) / 10) * 10 + 1;
        if(Integer.parseInt(pageNumber) % 10 == 0) startPage -= 10;
        int page = Integer.parseInt(pageNumber);
        int count = boardMapper.boardCount(kind,realm);
        if((count - (page * 20)) % 10 != 0 && count - (page * 20) > 0 ){ //새로 매앞에꺼만 20고침
            target2 = ((count - (page * 20)) / 20) + 1; //고침
        } else if((count - (page * 20)) % 10 == 0 && count - (page * 20) > 0) { //새로
            if((count -(page * 20)) % 20 > 1){
                target2 = ((count - (page * 20)) / 20) + 1;
            }
            else {
                target2 = ((count - (page * 20)) / 20); //고침
            }
        } else {
            target2 = 0;
        }
        int resultCount = count - (page * 20); //현재페이지 이후부터의 남은 게시물 갯수
        model.addAttribute("board", board); //게시물 메퍼에 boardList 로 얻은 결과를 리스트로 담아서 boardList.html로 전달
        model.addAttribute("target", target1);
        model.addAttribute("startPage", startPage);
        model.addAttribute("page", page);
        model.addAttribute("count", resultCount);
        model.addAttribute("target2", target2);
        model.addAttribute("kind",kind); //새로
        model.addAttribute("realm",realm);//새로
        return "boardList";
    }

    @PostMapping("board")   //게시물 리스트 가져오기 post방식 글쓰기 완료후 폼 액션
    public String WriteAction(WriteForm form, RedirectAttributes redirect, HttpSession session, Model model) throws Exception{
        BoardVO board = new BoardVO();
        board.setWriter(form.getWriter());  //BoardVO형 도메인 모델에 폼 데이터 담기
        board.setSubject(form.getSubject());
        board.setContent(form.getContent());
        String notice = form.getNotice();
        String userID = (String) session.getAttribute("id");
        userVO user = userMapper.userLogin(userID);
        if(notice == null){
            board.setNotice(0);
        } else{
            if(user.getManager()==1){
                board.setNotice(Integer.parseInt(notice));
            }
            else{
              model.addAttribute("msg","관리자 권한이 없습니다.");
              model.addAttribute("url","board?kind="+form.getKind()+"&realm="+form.getRealm());
              return "alert";
            }
        }
        board.setKind(form.getKind());
        board.setRealm(form.getRealm());

        boardMapper.boardInsert(board); // 저장된 board 객체를 파라미터로 담아 게시물 메퍼에 boardInsert 실행
        redirect.addAttribute("kind", form.getKind());
        redirect.addAttribute("realm", form.getRealm());
        return "redirect:/board";
    }
    @GetMapping("/write")                  //글쓰기
    public String write(@RequestParam("kind") String kind, @RequestParam("realm") String realm, Model model, HttpSession session) throws Exception{
        String id = (String) session.getAttribute("id");
        if(id == null){
            model.addAttribute("msg","로그인이 되어있지 않습니다.");
            model.addAttribute("url","login"); //메세지와 url을 모델로 담아 알림창을 띄울 alert.html로 전달
            return "alert";
        }
        userVO user = userMapper.userLogin(id);
        model.addAttribute("user",user);
        model.addAttribute("kind", kind);
        model.addAttribute("realm",realm);
        return "write";
    }

    @GetMapping("/view")    //글 상세보기
    public String BoardView(@RequestParam("bno") int bno, @RequestParam("kind") String kind, @RequestParam("realm") String realm, Model model) throws Exception{
        boardMapper.boardHit(bno); //게시물 메퍼의 해당 게시물 조회수 증가 메소드 실행
        BoardVO board = boardMapper.boardView(bno,kind,realm); //파라미터로 받은 bno를 담아 게시물 메퍼의 boardView 메소드 실행된 결과를 board에 저장
        model.addAttribute("board", board); //저장된 board 객체를 모델로 담아 view.html로 전달
        return "view";

    }

    @PostMapping("/update")    //글 수정
    public String Update(UpdateForm form, Model model, HttpSession session) throws Exception{
        String userID = (String)session.getAttribute("id");
        if(!userID.equals(form.getWriter())){
            model.addAttribute("msg","접근할 수 없습니다.");
            model.addAttribute("url","board?kind="+form.getKind()+"&realm="+form.getRealm());
            return "alert";
        }
        BoardVO board = new BoardVO();
        board.setBno(form.getBno()); //BoardVO형 도메인 모델에 폼 데이터 담기
        board.setSubject(form.getSubject());
        board.setContent(form.getContent());
        board.setKind(form.getKind());
        board.setRealm(form.getRealm());
        model.addAttribute("board",board); //저장된 board 객체를 모델로 담아 update.html로 전달
        return "update";
    }

    @PostMapping("/UpdateAction")    //글 수정 폼 액션
    public String UpdateAction(UpdateForm form, RedirectAttributes redirect) throws Exception{
        BoardVO board = new BoardVO();
        board.setBno(form.getBno()); //BoardVO형 도메인 모델에 폼 데이터 담기
        board.setSubject(form.getSubject());
        board.setContent(form.getContent());
        board.setKind(form.getKind());
        board.setRealm(form.getRealm());
        boardMapper.boardUpdate(board); //저장된 board 객체를 파라미터로 담아 게시물 메퍼에 boardUpdate 메소드 실행
        redirect.addAttribute("kind", form.getKind());
        redirect.addAttribute("realm", form.getRealm());
        return "redirect:/board"; //("/board")로 매핑된 get메소드 실행
    }

    @GetMapping("/delete")  //글 삭제
    public String BoardDelete(@RequestParam("bno") int bno, @RequestParam("kind") String kind, @RequestParam("realm") String realm, @RequestParam("writer") String writer, Model model, HttpSession session) throws Exception{
        String userID = (String)session.getAttribute("id");
        if(!userID.equals(writer)){
            model.addAttribute("msg","접근할 수 없습니다.");
            model.addAttribute("url","board?kind="+kind+"&realm="+realm);
            return "alert";
        }
        boardMapper.boardDelete(bno,kind,realm,writer); //파라미터로 받은 bno를 담아 게시물 메퍼의 boardDelete메소드 실행
        model.addAttribute("msg","글을 삭제하였습니다.");
        model.addAttribute("url","board?kind="+kind+"&realm="+realm); //메세지와 url을 모델로 담아 알림창을 띄울 alert.html로 전달
        return "alert";

    }

    @PostMapping("/search_list")  //글 검색
    public String BoardSearch(HttpServletRequest request, SearchForm form, Model model) throws Exception{
        String search_option = form.getSearch_option(); //String형 변수에 폼 데이터 저장
        String keyword = form.getKeyword();
        String kind = form.getKind();
        String realm = form.getRealm();
        String pageNumber = "1";
        int off = 0;
        int target2 = 0; //새로
        if(request.getParameter("pageNumber") != null){
            pageNumber = request.getParameter("pageNumber");
        }
        if(Integer.parseInt(pageNumber) == 1){
            off = 0;
        }
        else{
            off = (Integer.parseInt(pageNumber) - 1) * 20;
        }
        int startPage = (Integer.parseInt(pageNumber) / 10) * 10 + 1;
        if(Integer.parseInt(pageNumber) % 10 == 0) startPage -= 10;
        int page = Integer.parseInt(pageNumber);
        int count = boardMapper.searchCount(search_option,keyword,kind,realm);
        if((count - (page * 20)) % 10 != 0 && count - (page * 20) > 0 ){ //새로 매앞에꺼만 20고침
            target2 = ((count - (page * 20)) / 20) + 1; //고침
        } else if((count - (page * 20)) % 10 == 0 && count - (page * 20) > 0) { //새로
            if((count -(page * 20)) % 20 > 1){
                target2 = ((count - (page * 20)) / 20) + 1;
            }
            else {
                target2 = ((count - (page * 20)) / 20); //고침
            }
        } else {
            target2 = 0;
        }
        int resultCount = count - (page * 20); //현재페이지 이후부터의 남은 게시물 갯수
        List<BoardVO> board = boardMapper.boardSearch(search_option, keyword,kind,realm,off); //저장된 변수들을 파라미터로 담아 boardSearch 실행후 결과를 board에 저장
        model.addAttribute("board",board); //반환된 결과인 board를 모델로 담아 search.html로 전달
        model.addAttribute("keyword",keyword); //키워드 전달
        model.addAttribute("search_option",search_option); //옵션 전달
        model.addAttribute("kind",kind);
        model.addAttribute("realm",realm);
        model.addAttribute("startPage",startPage);
        model.addAttribute("page",page);
        model.addAttribute("count",resultCount);
        model.addAttribute("target2",target2);
        return "search";
    }

    @GetMapping("/main")
    public String MainPage() throws Exception{
        return "mainpage";
    }

    @GetMapping("/search_list")  //글 검색
    public String BoardSearch2(@RequestParam("search_option") String search_option, @RequestParam("keyword") String keyword, @RequestParam("kind") String kind, @RequestParam("realm") String realm, HttpServletRequest request, SearchForm form, Model model) throws Exception{
        String pageNumber = "1";
        int off = 0;
        int target2 = 0; //새로
        if(request.getParameter("pageNumber") != null){
            pageNumber = request.getParameter("pageNumber");
        }
        if(Integer.parseInt(pageNumber) == 1){
            off = 0;
        }
        else{
            off = (Integer.parseInt(pageNumber) - 1) * 20;
        }
        int startPage = (Integer.parseInt(pageNumber) / 10) * 10 + 1;
        if(Integer.parseInt(pageNumber) % 10 == 0) startPage -= 10;
        int page = Integer.parseInt(pageNumber);
        int count = boardMapper.searchCount(search_option,keyword,kind,realm);
        if((count - (page * 20)) % 10 != 0 && count - (page * 20) > 0 ){ //새로 매앞에꺼만 20고침
            target2 = ((count - (page * 20)) / 20) + 1; //고침
        } else if((count - (page * 20)) % 10 == 0 && count - (page * 20) > 0) { //새로
            if((count -(page * 20)) % 20 > 1){
                target2 = ((count - (page * 20)) / 20) + 1;
            }
            else {
                target2 = ((count - (page * 20)) / 20); //고침
            }
        } else {
            target2 = 0;
        }
        int resultCount = count - (page * 20); //현재페이지 이후부터의 남은 게시물 갯수
        List<BoardVO> board = boardMapper.boardSearch(search_option, keyword,kind,realm,off); //저장된 변수들을 파라미터로 담아 boardSearch 실행후 결과를 board에 저장
        model.addAttribute("board",board); //반환된 결과인 board를 모델로 담아 search.html로 전달
        model.addAttribute("keyword",keyword); //키워드 전달
        model.addAttribute("search_option",search_option); //옵션 전달
        model.addAttribute("kind",kind);
        model.addAttribute("realm",realm);
        model.addAttribute("startPage",startPage);
        model.addAttribute("page",page);
        model.addAttribute("count",resultCount);
        model.addAttribute("target2",target2);
        return "search";
    }






}
