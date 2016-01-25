package com.bttf.buzzquotebingo.graph;

import org.openntf.domino.graph2.impl.DGraph;

import com.tinkerpop.frames.FramedTransactionalGraph;

public class BTTF_Initializer {

	public enum FilmCharEnum {
		MARTY("Marty McFly"), DOC("Doc Brown"), CLOCK("Clocktower Lady"), JENNIFER("Jennifer Parker"), GEORGE("George McFly"),
		SAM("Sam Baines"), STELLA("Stella Baines"), STRICKLAND("Mr Strickland"), BIFF("Biff Tannen"), BUFORD("Buford \"Mad Dog\" Tannen"),
		BAR("Bartender"), LOU("Lou"), MARSHAL("Marshal Strickland"), SON("Marshal Strickland's Son"), DEPUTY("Marshal's Deputy"),
		DANCERS("Dancers"), MATCH("Match"), IKO("Iko \"Jitz\" Fujitsu"), ENGINEER("Engineer");
		private String value;

		private FilmCharEnum(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public static void createSections(final FramedTransactionalGraph<DGraph> framedGraph) {
		for (int i = 0; i < SectionName.values().length; i++) {
			Section section = framedGraph.addVertex(SectionName.values()[i].name(), Section.class);
			section.setName(SectionName.values()[i].getValue());
		}
	}

	public static void createQuotes(final FramedTransactionalGraph<DGraph> framedGraph) {
		// BACK TO THE FUTURE
		Quote flux = addQuote(framedGraph, "Flux", Film.ONE, FilmCharEnum.MARTY, null, SectionName.INTRODUCTION);
		flux.setQuote("Okay. Time circuits on, flux capacitor...fluxing. Engine running. All right.");
		Quote model = addQuote(framedGraph, "model", Film.ONE, FilmCharEnum.DOC, FilmCharEnum.MARTY, SectionName.INTRODUCTION);
		model.setQuote("Please excuse the crudity of the model, I didn't have time to build it to scale.");
		Quote clock = addQuote(framedGraph, "Clock", Film.ONE, FilmCharEnum.CLOCK, FilmCharEnum.MARTY, SectionName.INTRODUCTION);
		clock.setQuote("Save the clock tower");
		Quote libyans = addQuote(framedGraph, "Libyans", Film.ONE, FilmCharEnum.DOC, FilmCharEnum.MARTY, SectionName.NONE);
		libyans.setQuote("From a group of Libyan nationalists");
		Quote accom = addQuote(framedGraph, "Accomplish", Film.ONE, FilmCharEnum.MARTY, FilmCharEnum.GEORGE, SectionName.SUMMARY);
		accom.setQuote("If you put your mind to it, you can accomplish anything");
		Quote works = addQuote(framedGraph, "Works", Film.ONE, FilmCharEnum.DOC, FilmCharEnum.MARTY, SectionName.NONE);
		works.setQuote("It works! It works! I finally invent something that works.");
		Quote idiot = addQuote(framedGraph, "Idiot", Film.ONE, FilmCharEnum.SAM, FilmCharEnum.STELLA, SectionName.PHOTO);
		idiot.setQuote("He's an idiot. Comes from upbringing. His parents are probably idiots, too.");
		Quote kid = addQuote(framedGraph, "Kid", Film.ONE, FilmCharEnum.DOC, null, SectionName.NONE);
		kid.setQuote("Damn where is that kid. Damn! Damn damn!");
		Quote gig = addQuote(framedGraph, "Gigawatts", Film.ONE, FilmCharEnum.DOC, FilmCharEnum.MARTY, SectionName.NONE);
		gig.setQuote("1.21 gigawatts!");
		Quote scott = addQuote(framedGraph, "Scott", Film.ONE, FilmCharEnum.DOC, FilmCharEnum.MARTY, SectionName.NONE);
		scott.setQuote("Great scott!");
		Quote weather = addQuote(framedGraph, "Weathermen", Film.ONE, FilmCharEnum.MARTY, FilmCharEnum.DOC, SectionName.NONE);
		weather.setQuote("Since when can weathermen predict the weather, let alone the future");
		Quote time = addQuote(framedGraph, "Time", Film.ONE, FilmCharEnum.MARTY, null, SectionName.POWER_OF_LOVE);
		time.setQuote("If only I had more time.");
		Quote notWork = addQuote(framedGraph, "Doesnt Work", Film.ONE, FilmCharEnum.DOC, FilmCharEnum.MARTY, SectionName.NONE);
		notWork.setQuote("My god, do you know what this means? It means this damn thing doesn't work at all.");
		Quote brain = addQuote(framedGraph, "Brain Melting", Film.ONE, FilmCharEnum.MARTY, FilmCharEnum.GEORGE, SectionName.POWER_OF_LOVE);
		brain.setQuote("Let's keep this brain-melting stuff to ourselves okay?");
		Quote reject = addQuote(framedGraph, "Rejection", Film.ONE, FilmCharEnum.MARTY, FilmCharEnum.JENNIFER, SectionName.POWER_OF_LOVE);
		reject.setQuote("I mean, I just don't think I can take that kind of rejection.");
		Quote hill = addQuote(framedGraph, "Hill Valley", Film.ONE, FilmCharEnum.STRICKLAND, FilmCharEnum.MARTY, SectionName.NONE);
		hill.setQuote("No McFly ever amounted to anything in the history of Hill Valley.");
		Quote shit = addQuote(framedGraph, "Shit", Film.ONE, FilmCharEnum.DOC, FilmCharEnum.MARTY, SectionName.NONE);
		shit.setQuote("When this baby hits 88 miles per hour, you're gonna see some serious shit.");
		Quote milk = addQuote(framedGraph, "Chocolate", Film.ONE, FilmCharEnum.GEORGE, FilmCharEnum.LOU, SectionName.NONE);
		milk.setQuote("Give me a milk! Chocolate!");
		Quote good = addQuote(framedGraph, "Good Stuff", Film.ONE, FilmCharEnum.GEORGE, FilmCharEnum.MARTY, SectionName.NONE);
		good.setQuote("I'm writing this down. This is good stuff.");
		Quote love = addQuote(framedGraph, "Love It", Film.ONE, FilmCharEnum.MARTY, FilmCharEnum.DANCERS, SectionName.NONE);
		love.setQuote("You're kids are gonna love it.");

		// BTTF 2
		Quote sched = addQuote(framedGraph, "Schedule", Film.TWO, FilmCharEnum.DOC, FilmCharEnum.MARTY, SectionName.PHOTO);
		sched.setQuote("Precisely on schedule.");
		Quote two = addQuote(framedGraph, "Hell", Film.TWO, FilmCharEnum.BIFF, null, SectionName.PINES);
		two.setQuote("What the hell...two of 'em?");
		Quote paradox = addQuote(framedGraph, "Paradox", Film.TWO, FilmCharEnum.BIFF, null, SectionName.PINES);
		paradox.setQuote(
				"Or 2, the encounter could create a time paradox the results of which could cause a chain reaction that would unravel the very fabric of the space time continuum and destroy the entire universe. Granted, that's a worst case scenario.");
		Quote easy = addQuote(framedGraph, "Easy", Film.TWO, FilmCharEnum.MATCH, FilmCharEnum.MARTY, SectionName.POWER_OF_LOVE);
		easy.setQuote("We can do this the easy way or the hard way.");
		Quote women = addQuote(framedGraph, "Women", Film.TWO, FilmCharEnum.DOC, FilmCharEnum.MARTY, SectionName.NONE);
		women.setQuote("Better that I devote myself to study the other great mystery of the universe: women!");
		Quote familiar = addQuote(framedGraph, "Familiar", Film.TWO, FilmCharEnum.BIFF, null, SectionName.MARTY_TANNENS);
		familiar.setQuote("There's something very familiar about all this.");
		Quote delorean = addQuote(framedGraph, "DeLorean", Film.TWO, FilmCharEnum.BIFF, null, SectionName.NONE);
		delorean.setQuote("A flying DeLorean.");
		Quote fax = addQuote(framedGraph, "Fax", Film.TWO, FilmCharEnum.IKO, FilmCharEnum.MARTY, SectionName.NONE);
		fax.setQuote("Read my fax!");

		// BTTF 3
		Quote future = addQuote(framedGraph, "Future", Film.THREE, FilmCharEnum.DOC, FilmCharEnum.JENNIFER, SectionName.SUMMARY);
		future.setQuote("our future hasn't been written yet. No one's has. Your future is whatever you make it. So make it a good one.");
		Quote wreck = addQuote(framedGraph, "Wreck", Film.THREE, FilmCharEnum.DOC, FilmCharEnum.MARTY, SectionName.ALTERNATE);
		wreck.setQuote("It'll be a spectacular wreck.");
		Quote disc = addQuote(framedGraph, "Discipline", Film.THREE, FilmCharEnum.MARSHAL, FilmCharEnum.SON, SectionName.ERASED);
		disc.setQuote("Maintain discipline at all times. Remember that word - 'discipline.'");
		Quote ten = addQuote(framedGraph, "Ten Minutes", Film.THREE, FilmCharEnum.MARTY, FilmCharEnum.BAR, SectionName.INDIANS);
		ten.setQuote("Ten minutes? Why do we have to cut these things so damn close?");
		Quote destroyed = addQuote(framedGraph, "Destroyed", Film.THREE, FilmCharEnum.MARTY, null, SectionName.NONE);
		destroyed.setQuote("Well, Doc, it's destroyed. Just like you wanted.");
		Quote science = addQuote(framedGraph, "Science", Film.THREE, FilmCharEnum.DOC, FilmCharEnum.ENGINEER, SectionName.NONE);
		science.setQuote("It's a science experiment!");
		Quote manure = addQuote(framedGraph, "Manure", Film.THREE, FilmCharEnum.BUFORD, FilmCharEnum.DEPUTY, SectionName.NONE);
		manure.setQuote("I hate manure.");
		framedGraph.commit();
	}

	public static Quote addQuote(final FramedTransactionalGraph<DGraph> framedGraph, final String key, final Film film,
			final FilmCharEnum from, final FilmCharEnum to, final SectionName sectionName) {
		Quote quote = framedGraph.addVertex(key, Quote.class);
		quote.setSummary(key);
		quote.setFilm(film.getValue());
		FilmCharacter fromChar = framedGraph.addVertex(from.getValue(), FilmCharacter.class);
		fromChar.setName(from.getValue());
		quote.addQuotedBy(fromChar);
		if (null == to) {
			quote.addSaidTo(fromChar);
		} else {
			FilmCharacter toChar = framedGraph.addVertex(to.getValue(), FilmCharacter.class);
			toChar.setName(to.getValue());
			quote.addSaidTo(toChar);
		}
		Section section = framedGraph.addVertex(sectionName.getValue(), Section.class);
		section.setName(sectionName.getValue());
		quote.addUsedIn(section);
		return quote;
	}

}
