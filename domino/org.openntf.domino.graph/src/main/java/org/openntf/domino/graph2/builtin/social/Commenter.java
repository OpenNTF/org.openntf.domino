/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.graph2.builtin.social;

import java.util.List;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Commenter")
public interface Commenter extends DVertexFrame {
	@AdjacencyUnique(label = CommentsOn.LABEL)
	public List<Comment> getComments();

	@AdjacencyUnique(label = CommentsOn.LABEL)
	public CommentsOn addComment(Comment comment);

	@AdjacencyUnique(label = CommentsOn.LABEL)
	public void removeComment(Comment comment);

	@IncidenceUnique(label = CommentsOn.LABEL)
	public List<CommentsOn> getCommentsOns();

	@IncidenceUnique(label = CommentsOn.LABEL)
	public int countCommentsOns();

	@IncidenceUnique(label = CommentsOn.LABEL)
	public void removeCommentsOn(CommentsOn commentsOn);

}
