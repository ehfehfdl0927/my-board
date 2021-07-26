package kimjoohe.myboard.controller;

public class CommentForm {
    private int board_bno;
    private String comment_writer;
    private String commentContent;
    private int c_sequence;
    private String board_kind;
    private String board_realm;

    public int getBoard_bno() {
        return board_bno;
    }

    public void setBoard_bno(int board_bno) {
        this.board_bno = board_bno;
    }

    public String getComment_writer() {
        return comment_writer;
    }

    public void setComment_writer(String comment_writer) {
        this.comment_writer = comment_writer;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public int getC_sequence() {
        return c_sequence;
    }

    public void setC_sequence(int c_sequence) {
        this.c_sequence = c_sequence;
    }

    public String getBoard_kind() {
        return board_kind;
    }

    public void setBoard_kind(String board_kind) {
        this.board_kind = board_kind;
    }

    public String getBoard_realm() {
        return board_realm;
    }

    public void setBoard_realm(String board_realm) {
        this.board_realm = board_realm;
    }
}
