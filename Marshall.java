import java.io.FileOutputStream;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;


public class Marshall {
  public static void main(String[] args) throws Exception {
      JAXBContext contextObj = JAXBContext.newInstance(Question.class);

      Marshaller marshallerObj = contextObj.createMarshaller();
      marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      Answer answer1 = new Answer(1 , "The Human.", "Jaina Proudmore");
      Answer answer2 = new Answer(2 , "The King.", "Anduin Wrynn");
      Answer answer3 = new Answer(3 , "The Dwarf.", "Magni Bronzebeard");

      ArrayList<Answer> answersList=new ArrayList<Answer>();
      answersList.add(answer1);
      answersList.add(answer2);
      answersList.add(answer3);

      Question question=new Question(1, "Who are you?", answersList);
      marshallerObj.marshal(question, new FileOutputStream("question.xml"));
  }
}
