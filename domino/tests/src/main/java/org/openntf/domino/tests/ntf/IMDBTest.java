package org.openntf.domino.tests.ntf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class IMDBTest implements Runnable {
	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new IMDBTest(), "IMDB Test");
		thread.start();
	}

	public IMDBTest() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@SuppressWarnings("unused")
	private void importMovies(final Database db) {
		if (db == null) {
			System.out.println("db is null. Cannot proceed.");
			return;
		}
		BufferedReader br = getFileReader("c:/data/imdb/movies.list");
		String curLine;
		try {
			boolean atMovies = false;
			int movieCount = 0;
			while ((curLine = br.readLine()) != null) {
				if (!atMovies) {
					if (curLine.startsWith("MOVIES LIST")) {
						curLine = br.readLine();
						if (curLine.startsWith("===")) {
							atMovies = true;
						}
					}
				} else {
					if (curLine.length() > 2) {
						try {
							movieCount++;
							if (movieCount % 1000 == 0) {
								System.out.println("Processing line " + movieCount + ": " + curLine);
							}
							int tabPos = curLine.indexOf('\t');
							String rawMovieTitle = curLine.substring(0, tabPos).trim();
							String rawReleaseDate = curLine.substring(tabPos + 1).trim();
							String entryType = null;
							String expectedTitle = null;
							String episodeTitle = null;
							String episodeNumber = null;
							Integer seasonNumber = null;
							Integer showNumber = null;
							String expectedFilmDate = null;
							String remainingTitle = null;
							String entryKey = null;
							Integer releaseYear = null;
							char firstChar = rawMovieTitle.charAt(0);
							if (firstChar == '"') {

								//This is a TV series...
								/*
								"17 Kids and Counting" (2008)				2008-????
								"17 Kids and Counting" (2008) {20 Years, 20 Duggars (#3.15)}	2009
								"17 Kids and Counting" (2008) {38 Kids & Counting! (#8.7)}	2011

								 */
								entryKey = rawMovieTitle;
								int endQuote = rawMovieTitle.indexOf('"', 2);
								expectedTitle = rawMovieTitle.substring(1, endQuote);
								remainingTitle = rawMovieTitle.substring(endQuote);
								boolean isEpisode = false;
								if (remainingTitle.indexOf(") {") > -1) {
									isEpisode = true;
								}
								int startParens = remainingTitle.indexOf('(');
								int endParens = remainingTitle.indexOf(')');
								expectedFilmDate = remainingTitle.substring(startParens + 1, endParens);
								if (isEpisode) {
									entryType = "EPISODE";
									remainingTitle = remainingTitle.substring(endParens).trim();
									int startBrace = remainingTitle.indexOf('{');
									int endBrace = remainingTitle.lastIndexOf('}');
									remainingTitle = remainingTitle.substring(startBrace + 1, endBrace);
									startParens = remainingTitle.lastIndexOf('(');
									endParens = remainingTitle.lastIndexOf(')');
									if (startParens > -1 && endParens > 1) {
										if (startParens > 0) {
											episodeTitle = remainingTitle.substring(0, startParens - 1).trim();
										}
										episodeNumber = remainingTitle.substring(startParens + 1, endParens);
										int period = episodeNumber.indexOf('.');
										if (period > -1) {
											try {
												if (episodeNumber.startsWith("#")) {
													seasonNumber = Integer.valueOf(episodeNumber.substring(1, period));
												} else {
													seasonNumber = Integer.valueOf(episodeNumber.substring(0, period));
												}
												showNumber = Integer.valueOf(episodeNumber.substring(period + 1));
											} catch (Exception e) {
												// move along...
											}
										}
									} else {
										episodeTitle = remainingTitle.trim();
									}
								} else {
									entryType = "SERIES";

								}

							} else {
								//This is a movie (feature release, made for TV, straight to video)
								//$21 a Day - (Once a Month) (1941)			1941
								//'7th on Sixth': Inside Fashion Week (1999) (TV)		1999
								entryKey = rawMovieTitle;
								if (rawMovieTitle.endsWith("(TV)")) {
									entryType = "MOVIE_TV";
									remainingTitle = rawMovieTitle.substring(0, rawMovieTitle.lastIndexOf('(')).trim();
								} else if (rawMovieTitle.endsWith("(V)")) {
									entryType = "MOVIE_VIDEO";
									remainingTitle = rawMovieTitle.substring(0, rawMovieTitle.lastIndexOf('(')).trim();
								} else if (rawMovieTitle.endsWith("(VG)")) {
									entryType = "VIDEOGAME";
									remainingTitle = rawMovieTitle.substring(0, rawMovieTitle.lastIndexOf('(')).trim();
								} else {
									entryType = "MOVIE";
									remainingTitle = rawMovieTitle.trim();
								}
								int startParens = remainingTitle.lastIndexOf('(');
								int endParens = remainingTitle.lastIndexOf(')');
								if (startParens > -1 && endParens > 1) {
									expectedFilmDate = remainingTitle.substring(startParens + 1, endParens);
									expectedTitle = remainingTitle.substring(0, startParens);
								} else {
									expectedTitle = remainingTitle;
								}

							}
							if (rawReleaseDate.length() > 3 && !rawReleaseDate.startsWith("?")) {
								releaseYear = Integer.valueOf(rawReleaseDate.substring(0, 4).trim());
							}
							Document doc = db.getDocumentWithKey(entryKey, true);
							doc.replaceItemValue("StartDate", expectedFilmDate);
							doc.replaceItemValue("Title", expectedTitle);
							doc.replaceItemValue("Type", entryType);
							doc.replaceItemValue("EpisodeTitle", episodeTitle);
							doc.replaceItemValue("EpisodeNumber", episodeNumber);
							doc.replaceItemValue("SeasonNumber", seasonNumber);
							doc.replaceItemValue("ShowNumber", showNumber);
							doc.replaceItemValue("ReleaseYear", releaseYear);
							doc.save();
						} catch (Throwable t) {
							System.out.println("Exception processing line: " + curLine);
							t.printStackTrace();
						}
					}
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		/*
		MOVIES LIST
		===========

		"17 Kids and Counting" (2008)				2008-????
		"17 Kids and Counting" (2008) {20 Years, 20 Duggars (#3.15)}	2009
		"17 Kids and Counting" (2008) {38 Kids & Counting! (#8.7)}	2011
		"17 Kids and Counting" (2008) {40 Kids, Oh My!}		2012
		"17 Kids and Counting" (2008) {A Big Idea}		2013

		 */

	}

	private void importGenres(final Database db) {
		/*
		 8: THE GENRES LIST
		==================

		"17 - o poveste despre destin" (2008)			Drama
		"17 Kids and Counting" (2008)				Documentary
		"17 Meter - Wie weit kannst du gehn?" (2011)		Game-Show

		 */
		if (db == null) {
			System.out.println("db is null. Cannot proceed.");
			return;
		}
		BufferedReader br = getFileReader("c:/data/imdb/genres.list");
		int exceptionCount = 0;
		String curLine;
		try {
			boolean atMovies = false;
			int movieCount = 0;
			int lineCount = 0;
			String lastTitle = "";
			List<String> genres = new ArrayList<String>();
			while ((curLine = br.readLine()) != null) {
				if (exceptionCount > 500) {
					System.out.println("EXCEPTION COUNT EXCEEDED. TERMINATING.");
					return;
				}
				lineCount++;
				if (!atMovies) {
					if (curLine.startsWith("8: THE GENRES LIST")) {
						curLine = br.readLine();
						if (curLine.startsWith("===")) {
							atMovies = true;
						}
					}
				} else {
					if (curLine.length() > 2) {
						try {

							int tabPos = curLine.indexOf('\t');
							String rawMovieTitle = curLine.substring(0, tabPos).trim();
							String curGenre = curLine.substring(tabPos).trim();
							genres.add(curGenre);
							if (movieCount % 1000 == 0) {
								System.out.println("Processing plot for " + movieCount + ": " + rawMovieTitle + " (" + lineCount
										+ " lines)");
							}
							if (!rawMovieTitle.equalsIgnoreCase(lastTitle)) {
								movieCount++;
								Document doc = db.getDocumentWithKey(rawMovieTitle, false);
								if (doc != null) {
									doc.replaceItemValue("Genres", genres);
									doc.save();
								} else {
									exceptionCount++;
								}
								lastTitle = rawMovieTitle;
								genres = new ArrayList<String>();
							}
						} catch (Throwable t) {
							exceptionCount++;
							t.printStackTrace();
						}
					}
				}
			}
		} catch (Throwable t) {
			exceptionCount++;
			t.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void importPlots(final Database db) {
		/*
		 PLOT SUMMARIES LIST
		===================

		 -------------------------------------------------------------------------------
		MV: "17 Kids and Counting" (2008) {Coffee & Caricatures}

		PL: Jessa and Jinger volunteer at a local coffee shop and have no idea what's
		PL: in store. With Jinger being a big coffee fan, will she drink more than she
		PL: sells? And Jackson and Johannah are drawing caricatures to collect funds
		PL: for Josh and Anna's baby gift.

		BY: Anonymous

		-------------------------------------------------------------------------------
		MV: "17 Kids and Counting" (2008) {Designing Duggars (#3.27)}

		PL: The Duggars are back with the Bates and Wilsons! Watch as the families put
		PL: the finishing touches on the Bates' home renovation project. But with over
		PL: 40 kids running around, will the families be able to get everything done in
		PL: their one-week deadline?

		BY: Anonymous

		-------------------------------------------------------------------------------
		MV: "17 Kids and Counting" (2008) {Digesting Duggars (#5.2)}

		PL: Doctors make a breakthrough with Josie when they determine the cause of her
		PL: digestive problems. With the discovery comes relief when Michelle is
		PL: finally able to bring Josie back home. Meanwhile, Jana and John David take
		PL: off on their trip to Asia.

		BY: Anonymous

		 */

		if (db == null) {
			System.out.println("db is null. Cannot proceed.");
			return;
		}
		BufferedReader br = getFileReader("c:/data/imdb/plot.list");
		int exceptionCount = 0;
		String curLine;
		try {
			boolean atMovies = false;
			int movieCount = 0;
			int lineCount = 0;
			String expectedTitle = null;
			Map<String, String> plots = new HashMap<String, String>();
			StringBuilder curPlot = new StringBuilder();

			while ((curLine = br.readLine()) != null) {
				if (exceptionCount > 500) {
					System.out.println("EXCEPTION COUNT EXCEEDED. TERMINATING.");
					return;
				}
				lineCount++;
				if (!atMovies) {
					if (curLine.startsWith("PLOT SUMMARIES LIST")) {
						curLine = br.readLine();
						if (curLine.startsWith("===")) {
							curLine = br.readLine();
							atMovies = true;
						}
					}
				} else {
					if (curLine.length() > 2) {
						try {
							if (curLine.startsWith("MV:")) {
								expectedTitle = curLine.substring(3).trim();
								//"#LawstinWoods" (2013)
								movieCount++;
								if (movieCount % 1000 == 0) {
									System.out.println("Processing plot for " + movieCount + ": " + expectedTitle + " (" + lineCount
											+ " lines)");
								}
							} else if (curLine.startsWith("PL:")) {
								String plotLine = curLine.substring(3).trim();
								curPlot.append(plotLine);
								curPlot.append(" ");
							} else if (curLine.startsWith("BY:")) {
								String byLine = curLine.substring(3).trim();
								plots.put(byLine, curPlot.toString());
							} else if (curLine.startsWith("------")) {
								Document doc = db.getDocumentWithKey(expectedTitle, false);
								if (doc != null) {
									int plotNumber = 0;
									for (String author : plots.keySet()) {
										plotNumber++;
										doc.replaceItemValue("plot_" + plotNumber, plots.get(author));
										doc.replaceItemValue("author_" + plotNumber, author);
									}
									doc.save();
								} else {
									//									System.out.println("Unable to find movie doc with key: " + expectedTitle);
									exceptionCount++;
								}
								expectedTitle = null;
								plots = new HashMap<String, String>();
								curPlot = new StringBuilder();
							}

						} catch (Throwable t) {
							exceptionCount++;
							t.printStackTrace();
						}
					}
				}
			}
		} catch (Throwable t) {
			exceptionCount++;
			t.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void importDirectors(final Database db) {
		/*
		 THE DIRECTORS LIST
		==================

		Name			Titles
		----			------

		Enlow, Scott (I)	"17 Kids and Counting" (2008) {Big Family Meets Big Apple (#1.1)}
			"17 Kids and Counting" (2008) {Duggars Do New York (#1.2)}
			"17 Kids and Counting" (2008) {Duggars Movin' Out (#4.4)}
			"17 Kids and Counting" (2008) {Josh Gets Engaged (#1.3)}
			"Jon & Kate Plus 8" (2007) {Leah and Joel (#3.18)}


		Ennett, Jimmy		Action Movie (2011)  (co-director)
			Billy Cart (2013)
			Define by Ability (2013)
			Invasion (2011/I)
			Man of Action (2013)
			My Heart, a Drum (2010)
			Winning Streaks (2012)

		 */
	}

	@SuppressWarnings("unused")
	private void importActors(final DbDirectory dir) {
		if (dir == null) {
			System.out.println("dir is null. Cannot proceed.");
			return;
		}
		BufferedReader br = getFileReader("c:/data/imdb/actresses1.list");
		int exceptionCount = 0;
		String curLine;
		try {
			boolean atMovies = false;
			int actorCount = 0;
			int lineCount = 0;
			String curActor = "";
			String lastActor = "";
			List<String> genres = new ArrayList<String>();
			while ((curLine = br.readLine()) != null) {
				if (exceptionCount > 500) {
					System.out.println("EXCEPTION COUNT EXCEEDED. TERMINATING.");
					return;
				}
				lineCount++;
				if (!atMovies) {
					if (curLine.startsWith("8: THE GENRES LIST")) {
						curLine = br.readLine();
						if (curLine.startsWith("===")) {
							atMovies = true;
						}
					}
				} else {
					if (curLine.trim().length() > 2) {
						try {
							char firstChar = curLine.charAt(0);
							if (firstChar == '\t') {
								//continuation of prior actor
							} else {
								//new actor
								actorCount++;
								int tabPos = curLine.indexOf('\t');
								curActor = curLine.substring(0, tabPos).trim();
								if (actorCount % 1000 == 0) {
									System.out.println("Processing actor " + actorCount + ": " + curActor + " (" + lineCount + " lines)");
								}
								Database db = null; //TODO obviously not...
								if(db != null) {
									Document doc = db.getDocumentWithKey(curActor, false);
									if (doc != null) {
										doc.replaceItemValue("Genres", genres);
										doc.save();
									} else {
										exceptionCount++;
									}
								}
								lastActor = curActor;
								genres = new ArrayList<String>();
							}

						} catch (Throwable t) {
							exceptionCount++;
							t.printStackTrace();
						}
					}
				}
			}
		} catch (Throwable t) {
			exceptionCount++;
			t.printStackTrace();
		}
	}

	@Override
	public void run() {
		Session session = Factory.getSession(SessionType.CURRENT);
		session.setFixEnable(Fixes.REPLACE_ITEM_NULL, true);
		session.setFixEnable(Fixes.REMOVE_ITEM, true);
		session.setFixEnable(Fixes.CREATE_DB, true);
		DbDirectory dir = session.getDbDirectory("");
		Database moviesDb = dir.createDatabase("imdb/movies.nsf", true);
		//		importMovies(moviesDb);
		//		importPlots(moviesDb);
		importGenres(moviesDb);
		//		Database actorsDb = dir.createDatabase("imdb/actors.nsf", true);
		//		Database directorsDb = dir.createDatabase("imdb/directors.nsf", true);

	}

	public BufferedReader getFileReader(final String filepath) {
		File file = new File(filepath);
		if (file.exists()) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
				return br;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
