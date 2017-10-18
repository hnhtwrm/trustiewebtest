package bean;

public class UrlAndAnswer {

    private String url;
    private String answer;

    public UrlAndAnswer() {}

    public UrlAndAnswer(String url, String answer) {
        this.url = url;
        this.answer = answer;
    }

    public String getUrl() {
        return url;
    }

    public String getAnswer() {
        return answer;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "UrlAndAnswer{" +
                "url='" + url + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
