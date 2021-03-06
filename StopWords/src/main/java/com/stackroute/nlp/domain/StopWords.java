package com.stackroute.nlp.domain;

import java.util.Arrays;
import java.util.List;

public class StopWords {

	//private List<String> StopWords = Arrays.asList("about");
	
	private List<String> StopWords = Arrays.asList("a", "about", "above", "after", "again", "all", "am", "an", "and",
			"any", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both",
			"but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing",
			"don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't",
			"have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself",
			"him", "himself", "his", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into",
			"isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself", "no",
			"nor", "not", "off", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out",
			"over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so", "some",
			"such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there",
			"there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to",
			"too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're", "we've", "were",
			"weren't", "what's", "when", "when's", "where's", "which", "while", "who", "who's", "whom",
			 "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your",
			"yours", "yourself", "yourselves");

	public List<String> getStopWords() {
		return StopWords;
	}

	public void setStopWords(List<String> stopWords) {
		StopWords = stopWords;
	}

}
