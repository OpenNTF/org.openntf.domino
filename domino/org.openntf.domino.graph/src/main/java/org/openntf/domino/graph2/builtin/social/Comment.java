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
import org.openntf.domino.graph2.annotations.TypedProperty;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Comment")
public interface Comment extends Likeable, Commentable {
	@TypedProperty("Body")
	public String getBody();

	@TypedProperty("Body")
	public void setBody(String body);

	@AdjacencyUnique(label = CommentsOn.LABEL, direction = Direction.IN)
	public Commenter getCommenter();

	@AdjacencyUnique(label = CommentsOn.LABEL, direction = Direction.IN)
	public CommentsOn addCommenter(Commenter commenter);

	@AdjacencyUnique(label = CommentsOn.LABEL, direction = Direction.IN)
	public void removeCommenter(Commenter commenter);

	@IncidenceUnique(label = CommentsOn.LABEL, direction = Direction.IN)
	public CommentsOn getCommentsOn();

	@IncidenceUnique(label = CommentsOn.LABEL, direction = Direction.IN)
	public int countCommentsOn();

	@IncidenceUnique(label = CommentsOn.LABEL, direction = Direction.IN)
	public void removeCommentsOn(CommentsOn commentsOn);

	@AdjacencyUnique(label = CommentsAbout.LABEL)
	public List<Commentable> getCommentables();

	@AdjacencyUnique(label = CommentsAbout.LABEL)
	public CommentsAbout addCommentable(Commentable commentable);

	@AdjacencyUnique(label = CommentsAbout.LABEL)
	public void removeCommentable(Commentable commentable);

	@Override
	@IncidenceUnique(label = CommentsAbout.LABEL)
	public List<CommentsAbout> getCommentsAbouts();

	@Override
	@IncidenceUnique(label = CommentsAbout.LABEL)
	public int countCommentsAbouts();

	@Override
	@IncidenceUnique(label = CommentsAbout.LABEL)
	public void removeCommentsAbout(CommentsAbout commentsAbout);

	@Override
	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public List<Socializer> getSocializers();

	@Override
	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public Mentions addSocializer(Socializer socializer);

	@Override
	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public void removeSocializer(Socializer socializer);

	@Override
	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public List<Mentions> getMentions();

	@Override
	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public int countMentions();

	@Override
	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public void removeMentions(Mentions mentions);

}
