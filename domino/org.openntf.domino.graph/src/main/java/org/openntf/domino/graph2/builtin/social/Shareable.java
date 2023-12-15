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

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Shareable")
public interface Shareable extends DVertexFrame {
	@AdjacencyUnique(label = ShareAbout.LABEL, direction = Direction.IN)
	public List<Share> getShares();

	@AdjacencyUnique(label = ShareAbout.LABEL, direction = Direction.IN)
	public ShareAbout addShare(Share share);

	@AdjacencyUnique(label = ShareAbout.LABEL, direction = Direction.IN)
	public void removeShare(Share share);

	@IncidenceUnique(label = ShareAbout.LABEL, direction = Direction.IN)
	public List<ShareAbout> getShareAbouts();

	@IncidenceUnique(label = ShareAbout.LABEL, direction = Direction.IN)
	public int countShareAbouts();

	@IncidenceUnique(label = ShareAbout.LABEL, direction = Direction.IN)
	public void removeShareAbout(ShareAbout shareAbout);

	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public List<Socializer> getSocializers();

	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public Mentions addSocializer(Socializer socializer);

	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public void removeSocializer(Socializer socializer);

	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public List<Mentions> getMentions();

	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public int countMentions();

	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public void removeMentions(Mentions mentions);

}
