package BDDparse;

import BDDparse.scenarios.Scenario;
import BDDparse.scenarios.steps.ScenarioStep;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Feature {
    final Pattern featureNamePattern = Pattern.compile("^Feature:(?:\\s+)?(.+)$");

    final File featureFile;
    List<Scenario> scenarios;
    String featureName;

    public Feature(File featureFile) {
        this.featureFile = featureFile;
        parseFeatureFile();
    }

    private void parseFeatureFile() {
        boolean featureNameFound = false;
        boolean scenariosPart = false;
        scenarios = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(featureFile))) {
            String line;
            while((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                if (!featureNameFound) {
                    Matcher m = featureNamePattern.matcher(line.trim());
                    if (m.find()){
                        featureName = m.group(1);
                        featureNameFound = true;
                        System.out.println(featureName);
                    }
                }
                else {
                    Matcher m = ScenarioStep.stepBeginPattern.matcher(line.trim());
                    if (scenariosPart && m.find()) {
                        scenarios.get(scenarios.size() - 1).addScenarioStep(line);
                    }
                    else {
                        m = Scenario.scenarioPattern.matcher(line.trim());
                        if (m.find()){
                            scenarios.add(new Scenario(m.group(1)));
                            scenariosPart = true;
                        }
                    }
                }
            }
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }
}
