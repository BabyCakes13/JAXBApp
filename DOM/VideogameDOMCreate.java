import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;

public class VideogameDOMCreate {

   public static void main(String argv[]) {

      try {
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document document = dBuilder.newDocument();

         Element rootElement = document.createElement("class");
         document.appendChild(rootElement);

         Element videogame = document.createElement("videogame");
         rootElement.appendChild(videogame);

         Attr attr = document.createAttribute("name");
         attr.setValue("World of Warcraft");
         videogame.setAttributeNode(attr);

         Element type = document.createElement("type");
         type.appendChild(document.createTextNode("mmorpg"));
         videogame.appendChild(type);

         Element year = document.createElement("year");
         year.appendChild(document.createTextNode("2004"));
         videogame.appendChild(year);

         Element mode = document.createElement("mode");
         mode.appendChild(document.createTextNode("multi-player"));
         videogame.appendChild(mode);

         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         transformerFactory.setAttribute("indent-number", new Integer(2));

         Transformer transformer = transformerFactory.newTransformer();
         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "5");
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");

         DOMSource source = new DOMSource(document);
         StreamResult result = new StreamResult(new File("generated_videogames.xml"));
         transformer.transform(source, result);

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
