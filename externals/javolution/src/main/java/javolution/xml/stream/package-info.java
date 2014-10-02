/**
<p> StAX-like XML readers/writers which do not require object
    creation (such as String) and are consequently faster than standard StAX.</p> 
<p> The main difference with "javax.xml.stream.*" classes is the integration with
    OSGi to retrieve {@code XMLInputFactory/XMLOutputFactory} instances and the use 
    of <code>CharSequence</code> instead of <code>String</code>. Since 
    <code>String</code> is a <code>CharSequence</code> (JDK 1.4+), most 
    existing StAX code requires very little modification to be used with these 
    new classes.</p>
<p> For more information about the usage of this package please read the 
    documentation for the {@link javolution.xml.stream.XMLStreamReader} and 
    {@link javolution.xml.stream.XMLStreamWriter} interfaces.</p>
<p> For more information about StAX (Streaming API for XML) in general see 
    <a href="http://en.wikipedia.org/wiki/StAX">Wikipedia: StAX</a></p>
 */
package javolution.xml.stream;

