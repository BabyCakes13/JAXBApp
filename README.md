# JAXB vs DOM

In this example, we try to explain what JAXB and DOM are, what are the main differences between the two libraries, as well as exeplifying through some applications how each of this API's work.

<p>&nbsp;</p>

## Table of contents
### What is JAXB?
### JAXB features
### JAXB application
### What is DOM?
### DOM features
### DOM application
### JAXB vs DOM

 <p>&nbsp;</p>
 
## What is JAXB?
Jakarta XML Binding (JAXB; formerly Java Architecture for XML Binding) is a software framework which provides a fast and convenient way for marshalling (writing) Java objects into XML and un-marshalling (reading) XML into Java objcts. A biding framework for mapping XML elements and attributes to Java fields and properties is being used through Java annotations. 

#### How to install the plugin?

JAXB is supported only until **Java 8** (including), so in versions 9 and 10 it is deprecated, while in Java 11 JAXB is removed. So if you want to test it out, you can install Java 8 (both java and javac).

- **java**
```shell
% sudo update-alternatives --config java                 
[sudo] password for hostname: 

There are 2 programs which provide 'java'.

  Selection    Command
-----------------------------------------------
   1           java-11-openjdk.x86_64 (/usr/lib/jvm/java-11-openjdk-11.0.8.10-2.fc32.x86_64/bin/java)
*+ 2           java-1.8.0-openjdk.x86_64 (/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.265.b01-1.fc32.x86_64/jre/bin/java)

Enter to keep the current selection[+], or type selection number: 
```

- **javac**
```shell
[130] % sudo update-alternatives --config javac

There are 2 programs which provide 'javac'.

  Selection    Command
-----------------------------------------------
*+ 1           java-1.8.0-openjdk.x86_64 (/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.265.b01-1.fc32.x86_64/bin/javac)
   2           java-11-openjdk.x86_64 (/usr/lib/jvm/java-11-openjdk-11.0.8.10-2.fc32.x86_64/bin/javac)

Enter to keep the current selection[+], or type selection number: 
```

Also, since I use Java directly from command line (Linux distro Fedora 32), so I simply installed the Maven plugin which supports JAXB:

```shell
sudo dnf install jaxb2-maven-plugin
```
<p>&nbsp;</p>

## JAXB features

As we said before, JAXB annotations are paired with Java in order to support marshalling and un-marshalling of objects. The following example ilustrates how some annotations are used on Java objects in order to support this.

Some popular annotations are the following (for more interesting annotations, check the following [link](https://docs.oracle.com/javaee/7/api/javax/xml/bind/annotation/package-summary.html)):

- **@XmlRootElement**
- **@XmlElement**
- **@XmlAttribute**
- **@XmlType**
- **@XmlTransient**

#### **@XmlRootElement**
This represents the name of the root XML element which will be derived from the class name. It can be used at top level class or for an enum type. 

The following example illustrates its use and mashalling output:
```java
     @XmlRootElement
     class Point {
        int x;
        int y;
        Point(int _x,int _y) {x=_x;y=_y;}
     }

     marshal( new Point(3,5), System.out);
```
The generated XML document:
```xml
     <!-- Example: XML output -->
     <point>
       <x> 3 
       <y> 5 
     </point>
```

#### **@XmlType**

This annotation defines the order in which the fields of the Java object are writen in the XML file:

The following example illustrates its use and mashalling output:
```java
@XmlType(propOrder={"street", "city" , "state", "zip", "name" })
   public class USAddress {
     String getName() {..};
     void setName(String) {..};
 
     String getStreet() {..};
     void setStreet(String) {..};

     String getCity() {..}; 
     void setCity(String) {..};
 
     String getState() {..};
     void setState(String) {..};

     java.math.BigDecimal getZip() {..};
     void setZip(java.math.BigDecimal) {..};
   }
```
The generated XML document:
```xml
   <!-- XML Schema mapping for USAddress -->
   <xs:complexType name="USAddress">
     <xs:sequence>
       <xs:element name="street" type="xs:string"/>
       <xs:element name="city" type="xs:string"/>
       <xs:element name="state" type="xs:string"/>
       <xs:element name="zip" type="xs:decimal"/>
       <xs:element name="name" type="xs:string"/>
     </xs:all>
   </xs:complexType> 
```

#### **@XmlElement**
Maps a Java property to a XML element derived from the property name. A Java object property, when annotated with @XmlElement annotation is mapped to a local element in the XML Schema complex type to which the containing class is mapped.

The following example illustrates its use and mashalling output:
```java
public class USPrice {
         @XmlElement(name="itemprice")
         public java.math.BigDecimal price;
     }
```
The generated XML document:
```xml
     <!-- Example: Local XML Schema element -->
     <xs:complexType name="USPrice"/>
       <xs:sequence>
         <xs:element name="itemprice" type="xs:decimal" minOccurs="0"/>
       </sequence>
     </xs:complexType>
```

#### **@XmlAttribute**

A static final field is mapped to a XML fixed attribute.

The following example illustrates its use and mashalling output:
```java
     public class USPrice { 
         @XmlAttribute
         public java.math.BigDecimal getPrice() {...} ;
         public void setPrice(java.math.BigDecimal ) {...};
     }
```
The generated XML document:
```xml
     <!-- Example: XML Schema fragment -->
     <xs:complexType name="USPrice">
       <xs:sequence>
       </xs:sequence>
       <xs:attribute name="price" type="xs:decimal"/>
     </xs:complexType>
```

#### **@XmlTransient**

Prevents the mapping of a JavaBean property/type to XML representation.  This is useful for resolving name collisions between a JavaBean property name and a field name or preventing the mapping of a field/property.

The following example illustrates its use and mashalling output:
```java
public class USAddress {

       // The field name "name" collides with the property name 
       // obtained by bean decapitalization of getName() below
       @XmlTransient public String name;

       String getName() {..};
       String setName() {..};
   }
```
The generated XML document:
```xml
   <!-- Example: XML Schema fragment -->
   <xs:complexType name="USAddress">
     <xs:sequence>
       <xs:element name="name" type="xs:string"/>
     </xs:sequence>
   </xs:complexType>
```

<p>&nbsp;</p>

## JAXB Mini-application

For this, we have a simple application which creates a question and some answers for it. The generated XML document will contain the question along with the list of answers it got. 

![Figure 1: Application class diagram.](/home/babycakes/Documents/Master/XML/JAXB/util/question_answer.png  "Figure 1: Application class diagram.")

#### Question class

As we can see in the code below, we have set in place the XmlRootElement, which, as described above, sets the root of the XML Schema. Alongside the root, we also set as XML elements the question and list of answers to the question. Since we'd like to also have an attribute as an example, an ID is also set using XmlAttribute. Since we did not specify the XmlType, the attributes are coming inorder.
```java
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Question {
  private int id;
  private String question;
  private List<Answer> answers;

  public Question() {}

  public Question(int id, String question, List<Answer> answers) {
    super();
    this.id = id;
    this.question = question;
    this.answers = answers;
  }

  @XmlAttribute
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @XmlElement
  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  @XmlElement
  public List<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<Answer> answers) {
    this.answers = answers;
  }
}
```

#### Answer class

The class contains the answer, who has responded (owner) and again, an ID for the object. Also, note that we did not have to specify the fields of the answer in order to structure them into the resulting XML Schema.

```java
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

```

### Marshalling

Marshalling provides a application the ability to convert a JAXB derived Java object tree into XML data. By default, the Marshaller uses UTF-8 encoding when generating XML data.

The marshalling class will also create instances of a question and its answers in order to set the example for mashalling the Question and Answer object into an XML document.  

For this, we create a JAXB context object for what we want to write, in this case, Question.class. After this, we can setup the marshaller object which will be used to convert the Question object into XML format.

```java
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
```

After this, all we have to do is compile and run the application. The generated XML document in my case is named "question.xml" and after compiling and running, we get the following output: 

```shell
% javac Marshall.java && java Marshall && cat question.xml
```

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<question id="1">
    <answers>
        <answer>The Human.</answer>
        <id>1</id>
        <owner>Jaina Proudmore</owner>
    </answers>
    <answers>
        <answer>The King.</answer>
        <id>2</id>
        <owner>Anduin Wrynn</owner>
    </answers>
    <answers>
        <answer>The Dwarf.</answer>
        <id>3</id>
        <owner>Magni Bronzebeard</owner>
    </answers>
    <question>Who are you?</question>
</question>
```

### Unmarshalling

Unmarshalling converts a XML document into a Java object. For this, we need to specify the context. In this case, we want to convert the XML document into a Question object. We then specify the unmarshaller and write the object. Through this, we can now access all the attributes specified by the XML Schema.


```java
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
          System.out.println(answer.getId() + " " + answer.getAnswer() + "  " + answer.getOwner());  
      } catch (JAXBException e) {
        e.printStackTrace();
      }
    }
}
```

After compiling and running the unmarshalling class above, we get the following result printed to cmd:
```shell
% javac Unmarshall.java && java Unmarshall
```

```shell
1 Who are you?
1 The Human. (Jaina Proudmore)
2 The King. (Anduin Wrynn)
3 The Dwarf. (Magni Bronzebeard)
```


