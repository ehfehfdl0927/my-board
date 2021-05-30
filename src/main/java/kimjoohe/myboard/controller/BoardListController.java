package kimjoohe.myboard.controller;

import kimjoohe.myboard.domain.BoardVO;
import kimjoohe.myboard.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RestController
@Controller
public class BoardListController {
    @Autowired
    private BoardMapper boardMapper;

    @RequestMapping("/board")
    public List<BoardVO> board() throws Exception{
        List<BoardVO> board = boardMapper.boardList();
        return board;
    }

    //@RequestMapping("/board")
    //public ModelAndView board() throws  Exception{
        //List<BoardVO> board = boardMapper.boardList();
       // return new ModelAndView("boardList","list", board);
    //}

    @GetMapping("/board")
    public String list(Model model) throws Exception{
        List<BoardVO> board = boardMapper.boardList();
        model.addAttribute("board", board);
        return "boardList";
    }
    @GetMapping("/bb")
    public String bb(){
        return "kk";
    }

    @PostMapping("board")
    public String WriteAction(WriteForm form) throws Exception{
        BoardVO board = new BoardVO();
        board.setWriter(form.getWriter());
        board.setSubject(form.getSubject());
        board.setContent(form.getContent());
        boardMapper.boardInsert(board);
        return "redirect:/board";
    }
    @GetMapping("/write")
    public String write(){
        return "write";
    }

    @GetMapping("/view")
    public String BoardView(@RequestParam("bno") int bno, Model model) throws Exception{
        BoardVO board = boardMapper.boardView(bno);
        boardMapper.boardHit(bno);
        model.addAttribute("board", board);
        return "view";

    }

    @PostMapping("/update")
    public String Update(UpdateForm form, Model model) {
        BoardVO board = new BoardVO();
        board.setBno(form.getBno());
        board.setSubject(form.getSubject());
        board.setContent(form.getContent());
        model.addAttribute("board",board);
        return "update";
    }

    @PostMapping("/UpdateAction")
    public String UpdateAction(UpdateForm form) throws Exception{
        BoardVO board = new BoardVO();
        board.setBno(form.getBno());
        board.setSubject(form.getSubject());
        board.setContent(form.getContent());
        boardMapper.boardUpdate(board);
        return "redirect:/board";
    }

    @GetMapping("/delete")
    public String BoardDelete(@RequestParam("bno") int bno, Model model) throws Exception{
        boardMapper.boardDelete(bno);
        model.addAttribute("msg","글을 삭제하였습니다.");
        model.addAttribute("url","board");
        return "alert";

    }

    @PostMapping("/search_list")
    public String BoardSearch(SearchForm form, Model model) throws Exception{
        String search_option = form.getSearch_option();
        String keyword = form.getKeyword();
        List<BoardVO> board = boardMapper.boardSearch(search_option, keyword);
        model.addAttribute("board",board);
        return "search";
    }





}
