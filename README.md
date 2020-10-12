# JAXB vs DOM

In this example, we try to explain what JAXB and DOM are, what are the main differences between the two libraries, as well as exeplifying through some applications how each of this API's work.

<p>&nbsp;</p>

## Table of contents
### What is JAXB?
### JAXB Annotations
### JAXB Application
### What is DOM?
### DOM Interfaces and Methods
### DOM Application
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

![Figure 1: Application class diagram.](/home/babycakes/Documents/Master/XML/JAXBvsDOM/JAXB/util/question_answer.png  "Figure 1: Application class diagram.")

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


### What is DOM?

**DOM (Document Object Model)** defines an **interface** that enables programs to access and update the style, structure, and contents of **XML documents**. DOM is an official recommendation of the World Wide Web Consortium (W3C). DOM is used when:

- we want to know more about the structure of an XML document;
- we need to move some parts of an XML document around;
- we need to use the information provided by an XML document more than once, hence, store it;

When an XML document is parsed with DOM, a **tree structure** that contains all of the elements of the document is generated. There are a variety of function provided by DOM which can be used to manipulate the harvested data.

### DOM Interfaces

The DOM defines several **Java interfaces**, such as:
- **Node**
- **Element**
- **Attr**
- **Text**
- **Document**

#### Node

```java
public interface Node
```

The Node interface is the primary datatype for the entire Document Object Model. It represents a single node in the document tree [[1]](#1).

#### Element
```java
public interface Element
extends Node
```

The Element interface represents an element in an HTML or XML document. Elements may have attributes associated with them; since the Element interface inherits from Node, the generic Node interface attribute attributes may be used to retrieve the set of all attributes for an element [[2]](#2).

#### Attr 
```java
public interface Attr
extends Node
```

The Attr interface represents an attribute in an Element object. Typically the allowable values for the attribute are defined in a schema associated with the document [[3]](#3). 

#### Text
```java
public interface Text
extends CharacterData
```
The Text interface inherits from CharacterData and represents the textual content (termed character data in XML) of an Element or Attr [[4]](#4). 

#### Document
```java
public interface Document
extends Node
```

The Document interface represents the entire HTML or XML document. Conceptually, it is the root of the document tree, and provides the primary access to the document's data.

Since elements, text nodes, comments, processing instructions, etc. cannot exist outside the context of a Document, the Document interface also contains the factory methods needed to create these objects. The Node objects created have a ownerDocument attribute which associates them with the Document within whose context they were created  [[5]](#5). 

### DOM Methods

Besides the most common Java interfaces, here are some of the most commonly used methods:

- **Document.getDocumentElement()**
- **Node.getFirstChild()**
- **Node.getLastChild()**
- **Node.getNextSibling()**
- **Node.getPreviousSibling()**
- **Node.getAttribute(attrName)**

These and other useful methods can be found at the following [link](https://docs.oracle.com/javase/7/docs/api/index.html).

#### Document.getDocumentElement()

```java
Element getDocumentElement()
```

This is a convenience attribute that allows direct access to the child node that is the document element of the document.

#### Node.getFirstChild()

```java
Node getFirstChild()
```

The first child of this node. If there is no such node, this returns null.

#### Node.getLastChild()

```java
Node getLastChild()
```

The last child of this node. If there is no such node, this returns null.

#### Node.getNextSibling()

```java
Node getNextSibling()
```

The node immediately following this node. If there is no such node, this returns null.

#### Node.getPreviousSibling()

```java
Node getPreviousSibling()
```

The node immediately preceding this node. If there is no such node, this returns null. 

#### Node.getAttribute(String attrName)

```java
String getAttribute(String name)
```

Retrieves an attribute value by name.

Parameters:
    name - The name of the attribute to retrieve.
Returns:
    The Attr value as a string, or the empty string if that attribute does not have a specified or default value
    
The above mentioned methods and interfaces are the most commonly used ones, and we are going to use a application to exemplify their use in real-life scenarios.

## DOM Application



## References
<a id="1">[1]</a>  [https://docs.oracle.com/javase/7/docs/api/org/w3c/dom/Node.html](https://docs.oracle.com/javase/7/docs/api/org/w3c/dom/Node.html) 
<a id="2">[2]</a>  [https://docs.oracle.com/javase/7/docs/api/org/w3c/dom/Element.html](https://docs.oracle.com/javase/7/docs/api/org/w3c/dom/Element.html) 
<a id="3">[3]</a>  [https://docs.oracle.com/javase/7/docs/api/org/w3c/dom/Attr.html](https://docs.oracle.com/javase/7/docs/api/org/w3c/dom/Attr.html)
<a id="4">[4]</a>  [https://docs.oracle.com/javase/7/docs/api/org/w3c/dom/Text.html](https://docs.oracle.com/javase/7/docs/api/org/w3c/dom/Text.html) 
<a id="5">[5]</a>  [https://docs.oracle.com/javase/7/docs/api/org/w3c/dom/Document.html](https://docs.oracle.com/javase/7/docs/api/org/w3c/dom/Document.html)