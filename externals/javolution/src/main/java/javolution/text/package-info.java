/**
<p> Text handling package.</p>
<h2><a name="FAQ">FAQ:</a></h2>
<ol>
    <a name="FAQ-1"></a>
    <li><b> Is parsing/formatting of floating-points numbers (e.g. <code>double</code>)
            equivalent to standard String/Double methods?</b>
    <p> With Javolution 4.1, <code>double</code> formatting/parsing is lossless 
        and functionally the same as with the standard library. Parsing a character 
        sequence will always result in the same number whether it is performed with
        {@link javolution.text.TypeFormat TypeFormat} or using <code>Double.parseDouble(String))</code>.
        When formatting a <code>double</code> number, the number of digits output 
        is adjustable. The default (if the number of digits is unspecified) is <code>17</code> 
        or <code>16</code> when the the 16 digits representation  can be parsed back to
        the same <code>double</code> (mimic the standard library formatting).</p>
    <p> Javolution parsing/formatting do not generate garbage and has no adverse
        effect on GC. Better, it does not force the user to create intermediate <code>String</code>
        objects, any <code>CharSequence/Appendable</code> can be used! Serial parsing is also supported 
        (cursor parameter).</p>
    <p></p>
    <a name="FAQ-2"></a>
    <li><b> I'm accumulating a large string, and all I want to do is 
append to the end of the current string, which is the better class to use, 
Text or TextBuilder? Bearing in mind that speed is important, but I also want 
to conserve memory.</b>
    <p> It all depends of the size of the text to append (the actual size of the
         document being appended has almost no impact in both cases).</p>
    <p> If the text being appended is large (or arbitrarily large) then using 
        {@link javolution.text.Text Text} is preferable.
[code]
class FastCollection<T> {
     public final Text toText() {
         // We don't know the length of the text representation for
         // the collection's elements, we use Text concatenation 
         // to avoid copying what could be quite large.
         Text text = Text.valueOf("{");
         boolean isFirst = true;      
         for (T e : this) {
              if (!isFirst) { text = text.plus(", "); isFirst = false; }      
              text = text.plus(e);
         }
         return text.plus("}");
     }
}[/code]</p>
    <p></p>
    <a name="FAQ-3"></a>
    <li><b> In our project's use of strings, there are a lot of
           instances of directory path names, such as
<code>"/proj/lodecase/src/com/lodecase/util/foo.java"</code>, and
<code>"/proj/lodecase/src/com/lodecase/util/bar.java"</code>.
Can the 'Text' class save us memory when strings
have common prefixes?</b>
    <p> It depends how you build your text. For example in following code:
[code]
Text directoryName = Text.valueOf("/proj/lodecase/src/com/lodecase/util/");
Text fooFileName = directoryName.plus("foo.java");
Text barFileName = directoryName.plus("bar.java");[/code]
        The prefix (directoryName)is shared between <code>fooFileName</code> and <code>barFileName</code>.</p>
    <p> Text is a binary tree of blocks of characters. In the example,
        above, <code>fooFileName</code> is a node with <code>directoryName</code> for 
        head and "foo.java" for tail. The tree is maintained balanced automatically 
        through <a href="http://en.wikipedia.org/wiki/Tree_rotation">tree rotations</a>.</p>
    <p></p>
</ol>
 */
package javolution.text;

