package BDDparse.scenarios.steps;

import BDDparse.scenarios.steps.keywords.Keyword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ScenarioStep {
    public static final Pattern stepBeginPattern = Pattern.compile("^(?:given)|(?:when)|(?:then)|(?:and).+", Pattern.CASE_INSENSITIVE);
    public static final Pattern andStepPattern = Pattern.compile("^And", Pattern.CASE_INSENSITIVE);

    String scenarioLine;
    List<Keyword> keywords;
    Map<String, Boolean> keywordsFound;

    public ScenarioStep(String scenarioLine){
        this.scenarioLine = scenarioLine;
        keywords = new ArrayList<>();
        keywordsFound = new HashMap<>();
        parseLine();
    }

    public void parseLine() {
        for (String pattern : Keyword.keywordPatterns.values()){
            Pattern keywordPattern = Pattern.compile(pattern);
            Matcher matcher = keywordPattern.matcher(scenarioLine);
            while (matcher.find()){
                if (!keywordsFound.containsKey(matcher.group(1))){
                    keywords.add(new Keyword(matcher.group(1), Keyword.keywordPatterns.entrySet().stream().filter(entry -> pattern.equals(entry.getValue())).map(Map.Entry::getKey).findFirst().get()));
                    keywordsFound.put(matcher.group(1), true);
                }
            }
        }
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }
}
