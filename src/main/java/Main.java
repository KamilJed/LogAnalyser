import BDDparse.Feature;
import BDDparse.scenarios.Scenario;
import analyser.LogAnalyserLearner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Feature feature = new Feature(new File("/home/kamil/Documents/offwork/mgr/acmeair_bdd/src/test/java/resources/features/FlightsPage.feature"));
        File featureFile = new File("/home/kamil/Documents/offwork/mgr/acmeair-nodejs/log.txt");
        List<Feature> featureList = new ArrayList<>();
        List<File> fileList = new ArrayList<>();
        featureList.add(feature);
        fileList.add(featureFile);
        LogAnalyserLearner learner = new LogAnalyserLearner(featureList, fileList);
        System.out.println("Done");
    }
}
