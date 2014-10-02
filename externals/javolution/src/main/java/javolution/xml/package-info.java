/**
<p>Support for the encoding of objects, and the objects reachable from them,
   into <code>XML</code>; and the complementary reconstruction of the
   object graph from <code>XML</code>. 
</p>
<p><i> This page has been translated into 
<a href="http://www.webhostinghub.com/support/es/misc/paquete-javolutionxml">Spanish</a>
language by Maria Ramos  from <a href="http://www.webhostinghub.com/support/edu">
Webhostinghub.com/support/edu</a>.</i></p>

<h2><a name="OVERVIEW">XML marshalling/unmarshalling facility:</a></h2>


<IMG alt="XML Data Binding" src="doc-files/xmlDataBinding.png">

<p> Key Advantages:<ul>
    <li> Real-time characteristics with no adverse effect on memory footprint or 
 garbage collection (e.g. it can be used for time critical communications).
 {@link javolution.xml.XMLFormat XMLFormat} is basically a "smart" 
 wrapper around our real-time StAX-like
 {@link javolution.xml.stream.XMLStreamReader XMLStreamReader} and 
 {@link javolution.xml.stream.XMLStreamWriter XMLStreamWriter}.</li>
    <li> Works directly with your existing Java classes, no need to create new classes
 or customize your implementation in any way.</li>
    <li> The XML representation can be high level and impervious to obfuscation
 or changes to your implementation.</li>
    <li> Performance on a par or better than default Java<sup>TM</sup> Serialization/Deserialization 
 (See <a href="https://bindmark.dev.java.net/">bindmark</a> for performance comparison).</li>
    <li> Small footprint, runs on any platform including Android.</li>
    <li> The XML mapping can be defined for a top class (or interface) and is automatically 
 inherited by all sub-classes (or all implementing classes).</li>
    <li> Supports object references (to avoid expanding objects already serialized).</li>
    </ul>
</p>

<p> The default XML format for a class is typically defined using 
    the {@link javolution.xml.DefaultXMLFormat} annotation tag. 
[code]
@DefaultXMLFormat(Graphic.XML.class)     
public abstract class Graphic implements XMLSerializable {
    private boolean isVisible;
    private Paint paint; // null if none.
    private Stroke stroke; // null if none.
    private Transform transform; // null if none.
   
    // Default XML format with name associations (members identified by an unique name).
    public static class XML extends XMLFormat {
        public void write(Graphic g, OutputElement xml) throws XMLStreamException {
            xml.setAttribute("isVisible", g.isVisible); 
            xml.add(g.paint, "Paint");
            xml.add(g.stroke, "Stroke");
            xml.add(g.transform, "Transform");
        }
        public void read(InputElement xml, Graphic g) throws XMLStreamException {
            g.isVisible = xml.getAttribute("isVisible", true);
            g.paint = xml.get("Paint");
            g.stroke = xml.get("Stroke");
            g.transform = xml.get("Transform");
        }
    };
}[/code]
    Sub-classes may override the inherited XML format:
[code]
@DefaultXMLFormat(Area.XML.class)     
public class Area extends Graphic {
    private Shape geometry;  

    // Adds geometry to format.
    public static class XML extends XMLFormat<Area> {
        XMLFormat graphicXML = new Graphic.XML();
        public void write(Area area, OutputElement xml) throws XMLStreamException {
            graphicXML.write(area, xml); // Calls parent write.
            xml.add(area.geometry, "Geometry");
        }
        public void read(InputElement xml, Area area) throws XMLStreamException {
            graphicXML.read(xml, area); // Calls parent read.
            area.geometry = xml.get("Geometry");
        }
    };
}[/code]
    
The following writes a graphic area to a file, then reads it:

[code]    
// Creates some useful aliases for class names.
XMLBinding binding = new XMLBinding();
binding.setAlias(Color.class, "Color");
binding.setAlias(Polygon.class, "Polygon");
binding.setClassAttribute("type"); // Use "type" instead of "class" for class attribute.
    
// Writes the area to a file.
XMLObjectWriter writer = XMLObjectWriter.newInstance(new FileOutputStream("C:/area.xml"));
writer.setBinding(binding); // Optional.
writer.setIndentation("\t"); // Optional (use tabulation for indentation).
writer.write(area, "Area", Area.class);
writer.close(); 

// Reads the area back
XMLObjectReader reader = XMLObjectReader.newInstance(new FileInputStream("C:/area.xml"));
reader.setBinding(binding);
Area a = reader.read("Area", Area.class);
reader.close();
[/code]
 
     Here is an example of valid XML representation for an area:

[code]
<Area isVisible="true">
    <Paint type="Color" rgb="#F3EBC6" />
    <Geometry type="Polygon">
        <Vertex x="123" y="-34" />
        <Vertex x="-43" y="-34" />
        <Vertex x="-12" y="123" />
    </Geometry>
</Area>[/code]
   
</p>
<p> The following table illustrates the variety of XML representations supported 
    (Foo class with a single String member named text):
    <center>
      <table cellpadding="2" cellspacing="2" border="1" style="text-align: left"><tbody>
      <tr>
        <td style="vertical-align: top; text-align: center;"><span style="font-weight: bold;">XML FORMAT</span></td>
        <td style="vertical-align: top; text-align: center;"><span style="font-weight: bold;">XML DATA</span></td>
      </tr>
      <tr>
<td>[code]
XMLFormat<Foo> XML = new XMLFormat<Foo>() {
    public void write(Foo foo, OutputElement xml) throws XMLStreamException {
         xml.setAttribute("text", foo.text); 
    }
    public void read(InputElement xml, Foo foo) throws XMLStreamException {
         foo.text = xml.getAttribute("text", "");
    }
};[/code]</td>
<td><pre>
 <b>&lt;!-- Member as attribute --&gt;</b>
 &lt;Foo text="This is a text"/&gt;
</pre></td>
      </tr>
      
      <tr>
<td>
[code]
XMLFormat<Foo> XML = new XMLFormat<Foo>() {
    public void write(Foo foo, OutputElement xml) throws XMLStreamException {
        xml.add(foo.text); 
    }
    public void read(InputElement xml, Foo foo) throws XMLStreamException {
        foo.text = xml.getNext();
    }
};[/code]</td>
<td><pre>
 <b>&lt;!-- Member as anonymous nested element --&gt;</b>
 &lt;Foo&gt;
     &lt;java.lang.String value="This is a text"/&gt;
 &lt;/Foo&gt;
</pre></td>
      </tr>

      <tr>
<td>
[code]
XMLFormat<Foo> XML = new XMLFormat<Foo>(Foo.class) {
    public void write(Foo foo, OutputElement xml) throws XMLStreamException {
        xml.addText(foo.text); // or xml.getStreamWriter().writeCDATA(foo.text) to use CDATA block. 
    }
    public void read(InputElement xml, Foo foo) throws XMLStreamException {
        foo.text = xml.getText().toString(); // Content of a text-only element.
    }
};[/code]</td>
<td><pre>
 <b>&lt;!-- Member as Character Data --&gt;</b>
 &lt;Foo&gt;This is a text&lt;/Foo&gt;
</pre></td>
      </tr>

      <tr>
<td>
[code]
XMLFormat<Foo> XML = new XMLFormat<Foo>(Foo.class) {
    public void write(Foo foo, OutputElement xml) throws XMLStreamException {
        xml.add(foo.text, "Text"); 
    }
    public void read(InputElement xml, Foo foo) throws XMLStreamException {
        foo.text = xml.get("Text");
    }
};[/code]</td>
<td><pre>
 <b>&lt;!-- Member as named element of unknown type  --&gt;</b>
 &lt;Foo&gt;
     &lt;Text class="java.lang.String" value="This is a text"/&gt;
 &lt;/Foo&gt;
</pre></td>
      </tr>

      <tr>
<td><pre>
[code]
XMLFormat<Foo> XML = new XMLFormat<Foo>(Foo.class) {
    public void write(Foo foo, OutputElement xml) throws XMLStreamException {
        xml.add(foo.text, "Text", String.class); 
    }
    public void read(InputElement xml, Foo foo) throws XMLStreamException {
        foo.text = xml.get("Text", String.class);
    }
};[/code]</td>
<td><pre>
 <b>&lt;!-- Member as named element of actual type known --&gt;</b>
 &lt;Foo&gt;
     &lt;Text value="This is a text"/&gt;
 &lt;/Foo&gt;
</pre></td>
      </tr>
      </tbody></table>
    </center>
</p>

<p> XML format do not have to use the classes
    public no-arg constructors, instances can be created using factory methods, 
    private constructors (with constructor parameters set from the XML element)
    or even retrieved from a collection 
    (if the object is shared or unique). For example:
[code]
@DefaultXMLFormat(Point.XML.class)     
public final class Point implements XMLSerializable { 
    private int x;
    private int y;
    private Point() {}; // No-arg constructor not visible.
    public static Point valueOf(int x, int y) { ... }
    public static class XML = new XMLFormat<Point>() {
        public boolean isReferencable() {
            return false; // Always manipulated by value.
        }
        public Point newInstance(Class<Point> cls, InputElement xml) throws XMLStreamException {
             return Point.valueOf(xml.getAttribute("x", 0), xml.getAttribute("y", 0)); 
        }
        public void write(Point point, OutputElement xml) throws XMLStreamException {
             xml.setAttribute("x", point.x);
             xml.setAttribute("y", point.y);
        }
        public void read(InputElement xml, Point point) throws XMLStreamException {
            // Do nothing immutable.
        }
    };
}[/code]
</p>

<p> Document cross-references are supported, including circular references. 
    Let's take for example:
[code]
@DefaultXMLFormat(xml=Polygon.XML.class)     
public class Polygon implements Shape, XMLSerializable { 
    private Point[] vertices;
    public static class XML extends XMLFormat<Polygon> {
         public void write(Polygon polygon, OutputElement xml) throws XMLStreamException {
             xml.setAttibutes("count", vertices.length);
             for (Point p : vertices) {
                 xml.add(p, "Vertex", Point.class);
             }
         }
         public void read(InputElement xml, Polygon polygon) throws XMLStreamException {
             int count = xml.getAttributes("count", 0);
             polygon.vertices = new Point[count];
             for (int i=0; i < count; i++) {
                 vertices[i] = xml.get("Vertex", Point.class);
             }
        }
    };
}
Polygon[] polygons = new Polygon[] {p1, p2, p1};
...
TextBuilder xml = TextBuilder.newInstance();
AppendableWriter out = new AppendableWriter().setOutput(xml)
XMLObjectWriter writer = XMLObjectWriter.newInstance(out);
writer.setXMLReferenceResolver(new XMLReferenceResolver()); // Enables cross-references.
writer.write(polygons, "Polygons", Polygon[].class); 
writer.close();
System.out.println(xml);
[/code]
    Prints the following (noticed that the first polygon and last one are being shared).
[code]
<Polygons length="3">
   <Polygon id="0" count="3">
      <Vertex x="123" y="-34" />  
      <Vertex x="-43" y="-34" />
      <Vertex x="-12" y="123" />
   </Polygon>
   <Polygon id="1" count="3">
      <Vertex x="-43" y="-34" />
      <Vertex x="123" y="-34" />
      <Vertex x="-12" y="123" />
   </Polygon>
   <Polygon ref="0"/>
      </Polygons>[/code] 
</p>

<B>ALGORITHMS:</B>

<p> Our {@link javolution.xml.XMLObjectReader XMLObjectReader}/{@link javolution.xml.XMLObjectWriter XMLObjectWriter} 
    are in fact simple wrappers around our <b>J</b>avolution high-performance StAX-like
    {@link javolution.xml.stream.XMLStreamReader XMLStreamReader} and 
    {@link javolution.xml.stream.XMLStreamWriter XMLStreamWriter} classes. 
    The logic of these wrappers is described below:
<pre>

OutputElement.add(object, name, uri, class):

1. if (object == null) return

2. getStreamWriter().writeStartElement(uri, name)

3. isReference = referenceResolver.writeReference(object, this)
   
4. if (!isReference) binding.getFormat(class).write(object, this)

5. getStreamWriter().writeEndElement()

6. end


InputElement.get(name, uri, class):

1. if (!getStreamReader().getLocalName().equals(name) ||
!getStreamReader().getNamespaceURI().equals(uri)) return null

2. object = referenceResolver.readReference(inputElement)
   
3. if (object != null) Goto 8 // Found reference

4. format = binding.getFormat(class)

5. object = format.newInstance(class, inputElement)

6. referenceResolver.createReference(object, inputElement) // Done before parsing to support circular references.

7. format.read(inputElement, object)

8. getStreamReader().nextTag()

9. end
</pre></p>
 */
package javolution.xml;

