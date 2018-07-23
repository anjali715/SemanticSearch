package com.stackroute.index.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
//import java.util.Collection;
//import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.stackroute.index.exception.NANException;
import com.stackroute.index.exception.TermNotFoundException;
import com.stackroute.index.messenger.Listener;
//import com.stackroute.index.model.Indicator;
import com.stackroute.index.model.Indicator;
import com.stackroute.index.model.Level;

/*
 * Service for Indexer
 */

@Service
public class ScoreService {

	List<String> intents = Arrays.asList("basics", "tutorials", "example", "complete reference", "trouble shooting",
			"getting started");

	// Create list of basic, intermediate and advanced words
	// List<String> basics = Arrays.asList("main", "fundamental", "essential",
	// "beginner", "basics", "model", "secondary",
	// "additional", "auxillary", "snippets");
	// List<String> tutorials = Arrays.asList("acedemic", "informational",
	// "instructive", "guidance", "instructional",
	// "tutorials", "training", "primary", "primitive", "foundation", "fundamental",
	// "beginner");
	// List<String> example = Arrays.asList("source", "code", "reference",
	// "relating", "snippets", "sample", "expression",
	// "model", "instance", "class", "function", "instructive", "guidance",
	// "informational", "simple", "primary",
	// "essential");
	// List<String> tr = Arrays.asList("note", "implementation", "relating",
	// "indicates", "meaning", "source code",
	// "advance", "refer to", "extract", "give us example", "sample", "expression",
	// "snippets", "guidance",
	// "source");
	// List<String> ts = Arrays.asList("rectify", "fix", "service", "improve",
	// "support", "repair");
	// List<String> gs = Arrays.asList("helloworld", "how to code");
	// // Create array to retrieve weightage of relationships
	// double[] basicsWeight = { 0.3, 0.8, 0.8, 0.9, 0.6, 0.4, 0.6, 0.3, 0.7, 0.3 };
	// double[] tutorialsWeight = { 0.7, 0.7, 0.8, 0.7, 0.8, 0.8, 0.7, 0.6, 0.5,
	// 0.7, 0.8, 0.7 };
	// double[] exampleWeight = { 0.8, 0.6, 0.6, 0.7, 0.8, 0.5, 0.8, 0.6, 0.7, 0.6,
	// -0.5, -0.8, -0.4, -0.6, -0.5, -0.4,
	// -0.3 };
	// double[] trWeight = { 0.7, 0.8, 0.6, 0.7, 0.6, 0.6, 0.6, 0.7, 0.6, 0.8, -0.6,
	// -0.7, -0.5, -0.7, -0.4 };
	// double[] tsWeight = { 0.7, 0.7, 0.6, 0.5, 0.6, 0.7 };
	// double[] gsWeight = { 0.7, 0.7 };

	@Autowired
	Listener listen;

	Logger log = Logger.getLogger(getClass());

	double basicsScore = 0.0, tutorialsScore = 0.0, exampleScore = 0.0, trScore = 0.0, tsScore = 0.0, gsScore = 0.0;

	// Give base intent weighted averages for each term
	public List<Double> searchTerm(Map<String, Integer> map, Collection<Indicator> termweight) throws NANException {

		log.info("Input terms: " + map);

		for (String key : map.keySet()) {

			List<Double> ResultList = null;

			try {

				if (map.get(key) != 0) {
					ResultList = findWeight(key.trim(), termweight);
				} else
					continue;
			} catch (TermNotFoundException e) {
				e.printStackTrace();
			}

			basicsScore += (double) map.get(key) * ResultList.get(0);
			tutorialsScore += (double) map.get(key) * ResultList.get(1);
			exampleScore += (double) map.get(key) * ResultList.get(2);
			trScore += (double) map.get(key) * ResultList.get(3);
			tsScore += (double) map.get(key) * ResultList.get(4);
			gsScore += (double) map.get(key) * ResultList.get(5);
		}

		double total = basicsScore + tutorialsScore + exampleScore + trScore + tsScore + gsScore;
		double basicsPercentage = 0;
		double tutorialsPercentage = 0;
		double examplePercentage = 0;
		double trPercentage = 0;
		double tsPercentage = 0;
		double gsPercentage = 0;

		// Calculate Confidence Score
		if (total == 0) {
			basicsPercentage = 0;
			tutorialsPercentage = 0;
			examplePercentage = 0;
			trPercentage = 0;
			tsPercentage = 0;
			gsPercentage = 0;
		}
		else
		{
			basicsPercentage = (basicsScore / total) * 10000;
			tutorialsPercentage = (tutorialsScore / total) * 10000;
			examplePercentage = (exampleScore / total) * 10000;
			trPercentage = (trScore / total) * 10000;
			tsPercentage = (tsScore / total) * 10000;
			gsPercentage = (gsScore / total) * 10000;
		}
		
		basicsPercentage = Math.round(basicsPercentage*100.0)/100.0;
		tutorialsPercentage = Math.round(tutorialsPercentage*100.0)/100.0;
		examplePercentage = Math.round(examplePercentage*100.0)/100.0;
		trPercentage = Math.round(trPercentage*100.0)/100.0;
		tsPercentage = Math.round(tsPercentage*100.0)/100.0;
		gsPercentage = Math.round(gsPercentage*100.0)/100.0;
		
		log.info("basicScore: " + basicsPercentage);
		log.info("tutorialsScore: " + tutorialsPercentage);
		log.info("exampleScore: " + examplePercentage);
		log.info("trScore: " + trPercentage);
		log.info("tsScore: " + tsPercentage);
		log.info("gsScore: " + gsPercentage);

		// List<Double> list =
		// Arrays.asList(basicPercentage,intermediatePercentage,advantagePercentage);

		return Arrays.asList(basicsPercentage, tutorialsPercentage, examplePercentage, trPercentage, tsPercentage,
				gsPercentage);

	}

	// Method to find term in list and store its weight
	public List<Double> findWeight(String term, Collection<Indicator> termweight) throws TermNotFoundException {

		Collection<Indicator> intentterms = termweight;
		List<Double> list = new ArrayList<>();

		double basicscore = 0.0, tutorialscore = 0.0, examplescore = 0.0, trscore = 0.0, tsscore = 0.0, gsscore = 0.0;

		// search are intent weight from intent graph
		Map<String, Double> map = searchIntent(intentterms, term);
		
		for (String key : map.keySet()) {
			switch (key) {
			case "basics":
				basicscore = Math.round(map.get(key)*100.0)/100.0;
				break;
			case "tutorials":
				tutorialscore = Math.round(map.get(key)*100.0)/100.0;
				break;
			case "example":
				examplescore = Math.round(map.get(key)*100.0)/100.0;
				break;
			case "complete reference":
				trscore = Math.round(map.get(key)*100.0)/100.0;
				break;
			case "trouble shooting":
				tsscore = Math.round(map.get(key)*100.0)/100.0;
				break;
			case "getting started":
				gsscore = Math.round(map.get(key)*100.0)/100.0;
				break;
			default:
				log.info("key is not in intents");
			}
		}

		// if (basics.contains(term)) {
		// log.info("term found : " + term + " score : " +
		// basicsWeight[basics.indexOf(term)]);
		// basicscore = basicsWeight[basics.indexOf(term)];
		// }
		// if (tutorials.contains(term)) {
		// log.info(
		// "term found in tutorials : " + term + " score : " +
		// tutorialsWeight[tutorials.indexOf(term)]);
		// tutorialscore = tutorialsWeight[tutorials.indexOf(term)];
		// }
		// if (example.contains(term)) {
		// System.out
		// .println("term found in tutorials : " + term + " score : " +
		// exampleWeight[example.indexOf(term)]);
		// examplescore = exampleWeight[example.indexOf(term)];
		// }
		// if (tr.contains(term)) {
		// trscore = trWeight[tr.indexOf(term)];
		// }
		// if (ts.contains(term)) {
		// tsscore = tsWeight[ts.indexOf(term)];
		// }
		// if (gs.contains(term)) {
		// gsscore = gsWeight[gs.indexOf(term)];
		// }

		// ReceiverService receiverService = new ReceiverService();
		// Map<String, HashMap<String, Double>> map = receiverService.getData();

		// HashMap<String,Double> iWeight = map.get(term);

		list.add(basicscore);
		list.add(tutorialscore);
		list.add(examplescore);
		list.add(trscore);
		list.add(tsscore);
		list.add(gsscore);

		log.info("list of terms: " + list);

		return list;
	}

	private Map<String, Double> searchIntent(Collection<Indicator> intentterms, String term) {

		Map<String, Double> map = new HashMap<>();

		String parent = null;

		for (Indicator it : intentterms) {
			// log.info("term : "+term);
			// log.info("indicator term : "+it.getTerms().getName());
			Gson gson = new Gson();

			if ((it.getTerms().getName()).equals(term)) {
				log.info("term : " + it.getTerms().getName() + " found");

				if (it.getLevel().getClass() == Integer.class) {

					log.info("parent id : " + it.getLevel() + " found");

					int instant = (Integer) it.getLevel();

					for (Indicator temp : intentterms) {

						if (temp.getLevel().getClass() == Integer.class) {
							continue;
						}

						String json = gson.toJson(temp.getLevel());

						Level level = gson.fromJson(json, Level.class);

						log.info("parent id : " + instant);
						log.info("temp id : " + level.getId());

						if (level.getId() == (long) instant) {
							parent = level.getName();
							log.info("parent : " + parent + " found as nested");
							break;
						}
					}
				}

				else {
					String json = gson.toJson(it.getLevel());

					Level level = gson.fromJson(json, Level.class);

					parent = level.getName();
					log.info("parent : " + parent + " found as starting");
				}

				if (intents.contains(parent)) {
					Map<String, Double> map1 = new HashMap<>();
					map1.put(parent, (double) it.getWeight());
					return map1;
				} else {
					map = searchIntent(intentterms, parent);
					break;
				}
			}
		}
		return map;
	}

}
