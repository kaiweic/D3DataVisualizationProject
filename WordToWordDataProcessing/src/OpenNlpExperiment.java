import opennlp.tools.parser.Parse;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.FileInputStream;
import java.io.InputStream;

public class OpenNlpExperiment {

  public static void main(String[] args) throws Exception {
    //Loading Parts of speech-maxent model
    InputStream inputStream = new
            FileInputStream("en-pos-maxent.bin");
    POSModel model = new POSModel(inputStream);

    //Instantiating POSTaggerME class
    POSTaggerME tagger = new POSTaggerME(model);

    String sentence = "He is a good person";

    //Tokenizing the sentence using WhitespaceTokenizer class
    WhitespaceTokenizer whitespaceTokenizer= WhitespaceTokenizer.INSTANCE;
    String[] tokens = whitespaceTokenizer.tokenize(sentence);

    //Generating tags
    String[] tags = tagger.tag(tokens);

    for (int i = 0; i < tags.length; i++) {
      System.out.print(tags[i] + " ");
    }
    System.out.println();

    //Instantiating the POSSample class
    POSSample sample = new POSSample(tokens, tags);
    System.out.println(sample.toString());
  }
}
