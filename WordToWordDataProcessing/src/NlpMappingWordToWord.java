import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.*;
import java.util.*;

public class NlpMappingWordToWord {

  public static void main(String[] args) throws Exception {
    //Loading Parts of speech-maxent model
    InputStream inputStream = new FileInputStream("en-pos-maxent.bin");
    POSModel model = new POSModel(inputStream);

    //Instantiating POSTaggerME class
    POSTaggerME tagger = new POSTaggerME(model);

    //Tokenizing the sentence using WhitespaceTokenizer class
    WhitespaceTokenizer whitespaceTokenizer= WhitespaceTokenizer.INSTANCE;

    //Main map: news -> noun -> noun -> frequency
    Map<String, Map<String, Map<String, Integer>>> newsToNounToNoun = new HashMap<>();

    //Main map: news -> noun -> noun -> frequency
    Map<String, Map<String, Map<String, Integer>>> newsToNounToVerb = new HashMap<>();

    Scanner inputFile = new Scanner(new File("data.csv"));

    //Scans each line
    while (inputFile.hasNextLine()) {

      //Splits by ,
      String[] arr = inputFile.nextLine().toLowerCase().split(",");

      if (!newsToNounToNoun.containsKey(arr[1])) {
        newsToNounToNoun.put(arr[1], new HashMap<>());
      }
      if (!newsToNounToVerb.containsKey(arr[1])) {
        newsToNounToVerb.put(arr[1], new HashMap<>());
      }

      //Split by ' '
      String[] tokens = whitespaceTokenizer.tokenize(arr[4]);
      //Tags each word
      String[] tags = tagger.tag(tokens);

      Set<String> nounInEachLine = new HashSet<>();
      Set<String> verbInEachLine = new HashSet<>();

      for (int i = 0; i < tags.length; i++) {
        if (tags[i].charAt(0) == 'N') {
          nounInEachLine.add(tokens[i]);
        } else if (tags[i].charAt(0) == 'V') {
          verbInEachLine.add(tokens[i]);
        }
      }

      Map<String, Map<String, Integer>> nounToNoun = newsToNounToNoun.get(arr[1]);
      Map<String, Map<String, Integer>> nounToVerb = newsToNounToVerb.get(arr[1]);
      for (String word1 : nounInEachLine) {
        if (!nounToNoun.containsKey(word1)) {
          nounToNoun.put(word1, new HashMap<>());
        }
        if (!nounToVerb.containsKey(word1)) {
          nounToVerb.put(word1, new HashMap<>());
        }
        Map<String, Integer> nouns = nounToNoun.get(word1);
        for (String word2 : nounInEachLine) {
          if (!nouns.containsKey(word2)) {
            nouns.put(word2, 1);
          } else {
            nouns.put(word2, nouns.get(word2) + 1);
          }
        }
        Map<String, Integer> verbs = nounToVerb.get(word1);
        for (String word2 : verbInEachLine) {
          if (!verbs.containsKey(word2)) {
            verbs.put(word2, 1);
          } else {
            verbs.put(word2, verbs.get(word2) + 1);
          }
        }
      }
    }

    for (String news : newsToNounToNoun.keySet()) {
      PrintStream output = new PrintStream(new FileOutputStream(news+"_N_to_N.txt"));
      Map<String, Map<String, Integer>> nounToNoun = newsToNounToNoun.get(news);
      for (String word1 : nounToNoun.keySet()) {
        if (word1.length() == 1)
          continue;
        output.print(word1);
        Map<String, Integer> nouns = nounToNoun.get(word1);
        for (String word2 : nouns.keySet()) {
          if (word2.length() == 1)
            continue;
          output.print(" " + word2 + " " + nouns.get(word2));
        }
        output.println();
      }
    }
    for (String news : newsToNounToVerb.keySet()) {
      PrintStream output = new PrintStream(new FileOutputStream(news+"_N_to_V.txt"));
      Map<String, Map<String, Integer>> nounToVerb = newsToNounToVerb.get(news);
      for (String word1 : nounToVerb.keySet()) {
        if (word1.length() == 1)
          continue;
        output.print(word1);
        Map<String, Integer> verbs = nounToVerb.get(word1);
        for (String word2 : verbs.keySet()) {
          if (word2.length() == 1)
            continue;
          output.print(" " + word2 + " " + verbs.get(word2));
        }
        output.println();
      }
    }
  }
}
