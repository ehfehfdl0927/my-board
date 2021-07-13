package kimjoohe.myboard.controller;

public class SearchForm {
    private String search_option;
    private String keyword;
    private String kind;
    private String realm;

    public String getSearch_option() {
        return search_option;
    }

    public void setSearch_option(String search_option) {
        this.search_option = search_option;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }
}
