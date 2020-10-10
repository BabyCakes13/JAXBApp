import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Unmarshall {
    public static void main(String[] args) {

     try {
        File file = new File("question.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(Question.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Question question = (Question) jaxbUnmarshaller.unmarshal(file);

        System.out.println(question.getId() + " " + question.getQuestion());

        List<Answer> list = question.getAnswers();
        for(Answer answer : list)
          System.out.println(answer.getId() + " " + answer.getAnswer() + " (" + answer.getOwner() + ")");
      } catch (JAXBException e) {
        e.printStackTrace();
      }
    }
}
