public class Answer {
  private int id;
  private String answer;
  private String owner;

  public Answer() {}

  public Answer(int id, String answer, String owner) {
    super();
    this.id = id;
    this.answer = answer;
    this.owner = owner;
  }
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getAnswer() {
    return answer;
  }
  public void setAnswer(String answer) {
    this.answer = answer;
  }
  public String getOwner() {
    return owner;
  }
  public void setOwner(String owner) {
    this.owner = owner;
  }

}
