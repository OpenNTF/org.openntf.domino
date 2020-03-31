/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.tests.paul;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.Incidence;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.frames.modules.typedgraph.TypeField;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

public class Graph2DataModel {

	private static final String directedBy = "DirectedBy";
	private static final String appearsIn = "AppearsIn";
	private static final String portrays = "Portrays";
	private static final String starring = "Starring";
	private static final String spawns = "Spawns";
	private static final String kills = "Kills";

	@TypeField("form")
	public interface DVertexFrame extends VertexFrame {

	}

	@TypeField("form")
	public interface DEdgeFrame extends EdgeFrame {

	}

	@TypeValue("movie")
	public interface Movie extends DVertexFrame {
		@Property("title")
		public String getTitle();

		@Property("title")
		public void setTitle(String title);

		@Incidence(label = directedBy)
		public DirectedBy getDirectedBy();

		@Incidence(label = directedBy)
		public DirectedBy addDirectedBy(Crew crew);

		@Incidence(label = directedBy)
		public void removeDirectedBy(Crew crew);

		@Adjacency(label = directedBy)
		public Crew getDirectedByCrew();

		@Adjacency(label = directedBy)
		public Crew addDirectedByCrew(Crew crew);

		@Adjacency(label = directedBy)
		public Crew removeDirectedByCrew(Crew crew);

		@Incidence(label = starring)
		public Iterable<Starring> getStarring();

		@Incidence(label = starring)
		public Starring addStarring(Crew crew);

		@Incidence(label = starring)
		public void removeStarring(Crew crew);

		@Adjacency(label = starring)
		public Iterable<Crew> getStarringCrew();

		@Adjacency(label = starring)
		public Crew addStarringCrew(Crew crew);

		@Adjacency(label = starring)
		public Crew removeStarringCrew(Crew crew);

	}

	@TypeValue(directedBy)
	public interface DirectedBy extends DEdgeFrame {
		@Property("rating")
		public Integer getRating();

		@Property("rating")
		public void setRating(Integer rating);

		@OutVertex
		Crew getDirector();

		@InVertex
		Movie getMovie();

	}

	@TypeValue(starring)
	public interface Starring extends DEdgeFrame {
		@OutVertex
		Movie getMovie();

		@InVertex
		Crew getStar();
	}

	@TypeValue(portrays)
	public interface Portrays extends DEdgeFrame {
		@OutVertex
		Crew getStar();

		@InVertex
		Character getCharacter();
	}

	@TypeValue(appearsIn)
	public interface AppearsIn extends DEdgeFrame {
		@OutVertex
		Character getCharacter();

		@InVertex
		Movie getMovie();
	}

	@TypeValue(spawns)
	public interface Spawns extends DEdgeFrame {
		@OutVertex
		Character getParent();

		@InVertex
		Character getCreated();
	}

	@TypeValue(kills)
	public interface Kills extends DEdgeFrame {
		@Property("film")
		public String getFilm();

		@Property("film")
		public void setFilm(String film);

		@OutVertex
		Character getKiller();

		@InVertex
		Character getVictim();
	}

	@TypeValue("character")
	public interface Character extends DVertexFrame {
		@Property("name")
		public String getName();

		@Property("name")
		public void setName(String name);

		@Adjacency(label = appearsIn)
		public Iterable<Movie> getAppearsInMovies();

		@Incidence(label = appearsIn)
		public Iterable<AppearsIn> getAppearsIn();

		@Incidence(label = appearsIn)
		public AppearsIn addAppearsIn(Movie movie);

		@Incidence(label = appearsIn)
		public void removeAppearsIn(AppearsIn appearance);

		@Adjacency(label = portrays, direction = Direction.IN)
		public Iterable<Crew> getPortrayedByCrew();

		@Incidence(label = portrays, direction = Direction.IN)
		public Iterable<Portrays> getPortrayedBy();

		@Incidence(label = portrays, direction = Direction.IN)
		public Portrays addPortrayedBy(Crew crew);

		@Incidence(label = portrays, direction = Direction.IN)
		public void removePortrayedBy(Portrays portrayal);

		@Adjacency(label = kills, direction = Direction.IN)
		public Iterable<Character> getKilledByCharacters();

		@Incidence(label = kills, direction = Direction.IN)
		public Iterable<Kills> getKilledBy();

		@Incidence(label = kills, direction = Direction.IN)
		public Kills addKilledBy(Character character);

		@Incidence(label = kills, direction = Direction.IN)
		public void removeKilledBy(Kills kill);

		@Adjacency(label = kills, direction = Direction.OUT)
		public Iterable<Character> getKillsCharacters();

		@Incidence(label = kills, direction = Direction.OUT)
		public Iterable<Kills> getKills();

		@Incidence(label = kills, direction = Direction.OUT)
		public Kills addKills(Character character);

		@Incidence(label = kills, direction = Direction.OUT)
		public void removeKills(Kills kill);

		@Adjacency(label = spawns, direction = Direction.IN)
		public Iterable<Character> getSpawnedByCharacters();

		@Incidence(label = spawns, direction = Direction.IN)
		public Iterable<Spawns> getSpawnedBy();

		@Incidence(label = spawns, direction = Direction.IN)
		public Spawns addSpawnedBy(Character character);

		@Incidence(label = spawns, direction = Direction.IN)
		public void removeSpawnedBy(Spawns spawn);

		@Adjacency(label = spawns, direction = Direction.OUT)
		public Iterable<Character> getSpawnsCharacters();

		@Incidence(label = spawns, direction = Direction.OUT)
		public Iterable<Spawns> getSpawns();

		@Incidence(label = spawns, direction = Direction.OUT)
		public Spawns addSpawns(Character character);

		@Adjacency(label = spawns, direction = Direction.OUT)
		public void removeSpawns(Character spawn);
	}

	@TypeValue("crew")
	@JavaHandlerClass(CrewImpl.class)
	public interface Crew extends DVertexFrame {
		@Property("firstName")
		public String getFirstName();

		@Property("lastName")
		public String getLastName();

		@Property("firstName")
		public void setFirstName(String firstName);

		@Property("lastName")
		public void setLastName(String lastName);

		@JavaHandler
		public void setFullName(String fullName);

		@Adjacency(label = portrays)
		public Iterable<Character> getPortraysCharacters();

		@Incidence(label = portrays)
		public Iterable<Portrays> getPortrayals();

		@Incidence(label = portrays)
		public Portrays addPortrayals(Character character);

		@Incidence(label = portrays)
		public void removePortrayals(Portrays portrayal);

		@Adjacency(label = starring)
		public Iterable<Movie> getStarsInMovies();

		@Incidence(label = starring)
		public Iterable<Starring> getStarsIn();

		@Incidence(label = starring)
		public Starring addStarsInMovie(Movie movie);

		@Adjacency(label = starring)
		public void removeStarsInMovie(Movie movie);
	}

	public static abstract class CrewImpl implements JavaHandlerContext<Vertex>, Crew {
		@Override
		public void setFullName(final String fullName) {
			int pos = fullName.lastIndexOf(' ');
			setFirstName(fullName.substring(0, pos));
			setLastName(fullName.substring(pos + 1));
		}
	}

	public Graph2DataModel() {
	}

}
