package Game;

public class Question {
    private String category;
    private String question;
    private String answer;
    private String hint;
    public Question(String category, String question, String answer, String hint) {
        this.category = category;
        this.question = question;
        this.answer = answer;
        this.hint = hint;
    }
    public String getHint(){
        return hint;
    }
    public void setHint(String hint){
        this.hint = hint;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "category='" + category + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
