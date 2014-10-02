package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.pipes.AbstractPipe;

/**
 * IdPipe emits the id of the element.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdPipe extends AbstractPipe<Element, Object> implements TransformPipe<Element, Object> {

    protected Object processNextStart() {
        return this.starts.next().getId();
    }
}
