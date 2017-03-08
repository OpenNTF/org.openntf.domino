package com.github.javafaker;

import static org.apache.commons.lang.StringUtils.capitalize;
import static org.apache.commons.lang.StringUtils.join;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

public class Lorem {

	private final FakeValuesService fakeValuesService;
	private final RandomService randomService;

	public Lorem(final FakeValuesService fakeValuesService, final RandomService randomService) {
		this.fakeValuesService = fakeValuesService;
		this.randomService = randomService;
	}

	@SuppressWarnings({ "unchecked" })
	public String[] words(final int num) {
		List<String> words = (List<String>) fakeValuesService.fetchObject("lorem.words");
		String[] returnArr = new String[num];
		for (int i = 0; i < num; i++) {
			returnArr[i] = words.get(randomService.nextInt(words.size()));
		}
		return returnArr;
	}

	public String[] words() {
		return words(3);
	}

	public String sentence(final int wordCount) {
		return capitalize(join(words(wordCount + randomService.nextInt(6)), " ") + ".");
	}

	public String sentence() {
		return sentence(3);
	}

	public String[] sentences(final int sentenceCount) {
		String[] sentences = new String[sentenceCount];
		for (int i = 0; i < sentenceCount; i++) {
			sentences[i] = sentence();
		}
		return sentences;
	}

	public String paragraph(final int sentenceCount) {
		return join(sentences(sentenceCount + randomService.nextInt(3)), " ");
	}

	public String paragraph() {
		return paragraph(3);
	}

	public List<String> paragraphs(final int paragraphCount) {
		List<String> paragraphs = new ArrayList<String>(paragraphCount);
		for (int i = 0; i < paragraphCount; i++) {
			paragraphs.add(paragraph());
		}
		return paragraphs;
	}

	/**
	 * Create a string with a fixed size. Can be useful for testing validator based on length string for example
	 * 
	 * @param numberOfLetters
	 *            size of the expected String
	 * @return a string with a fixed size
	 */
	public String fixedString(final int numberOfLetters) {
		StringBuilder builder = new StringBuilder();
		while (builder.length() < numberOfLetters) {
			builder.append(sentence());
		}
		return StringUtils.substring(builder.toString(), 0, numberOfLetters);
	}
}
