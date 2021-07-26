package kimjoohe.myboard.domain;

public class FileVO {
    private int f_no;
    private int bno;
    private String filename;
    private String fileoriginname;
    private String fileurl;

    public int getF_no() {
        return f_no;
    }

    public void setF_no(int f_no) {
        this.f_no = f_no;
    }

    public int getBno() {
        return bno;
    }

    public void setBno(int b_no) {
        this.bno = b_no;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileoriginname() {
        return fileoriginname;
    }

    public void setFileoriginname(String fileoriginname) {
        this.fileoriginname = fileoriginname;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }
}
