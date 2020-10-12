import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class VideogameDOM {
   public static void main(String[] args) {

      try {
         File inputFile = new File("generated.xml");

         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();

         System.out.println("Root element:" + doc.getDocumentElement().getNodeName());

         NodeList nList = doc.getElementsByTagName("videogame");

         for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent element:" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;

               System.out.println("Name: "
                  + eElement.getAttribute("name"));

               System.out.println("Type: "
                  + eElement.getElementsByTagName("type").item(0).getTextContent());

               System.out.println("Year: "
                  + eElement.getElementsByTagName("year").item(0).getTextContent());

               System.out.println("Mode: "
                  + eElement.getElementsByTagName("mode").item(0).getTextContent());
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
       }}}
