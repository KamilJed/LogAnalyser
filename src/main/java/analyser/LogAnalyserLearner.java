package analyser;

import BDDparse.Feature;
import BDDparse.scenarios.Scenario;
import BDDparse.scenarios.steps.ScenarioStep;
import BDDparse.scenarios.steps.keywords.Keyword;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LogAnalyserLearner {
    private final Pattern JSON_CHECK_PATTERN = Pattern.compile("\\{(?:.+)\\}");
    private final Pattern JSON_KEY_VALUE_PATTERN = Pattern.compile("([^:,]+:[^:,]+)+");
    private final String JSON_VALUE_REPLACE = "(.+)";
    // assume the log is in format:
    // [TIMESTAMP] [LOG LEVEL] LOG ENTRY
    private final Pattern RAW_LOG_PATTERN = Pattern.compile("(?:\\[[^\\]]+\\] \\[[^\\]]+\\] )?(.+)");

    Map<Feature, File> features;
    Map<Scenario, Set<String>> scenarioRegexes;


    public LogAnalyserLearner(List<Feature> features, List<File> featureLogFiles) {
        if (features.size() != featureLogFiles.size())
            throw new RuntimeException("Every feature must have corresponding log file");
        this.features = new HashMap<>();
        for (int i = 0; i < features.size(); i++){
            this.features.put(features.get(i), featureLogFiles.get(i));
        }
        createAnalyzerRegexes();
    }

    private void createAnalyzerRegexes() {
        scenarioRegexes = new HashMap<>();
        for (Feature feature: features.keySet()){
            File featureLogFile = features.get(feature);
            try(BufferedReader reader = new BufferedReader(new FileReader(featureLogFile))){
                String line;
                while ((line = reader.readLine()) != null){
                    Matcher rawMatch = RAW_LOG_PATTERN.matcher(line);
                    if (rawMatch.find()){
                        line = rawMatch.group(1);
                    }
                    for (Scenario scenario : feature.getScenarios()){
                        createRegexFromLogLine(line, scenario);
                    }
                }
            } catch (IOException err){
                err.printStackTrace();
            }
        }
    }

    private void createRegexFromLogLine(String logLine, Scenario scenario) {
        boolean isJsonLine = JSON_CHECK_PATTERN.matcher(logLine).find();
        for (ScenarioStep step : scenario.getScenarioSteps()){
            String regex = logLine;
            boolean validRegex = false;
            for (Keyword keyword : step.getKeywords()){
                if (keyword.isDate()){
                    checkVariousDateFormats(logLine, keyword);
                }
                if (logLine.contains(keyword.getKeyword())){
                    validRegex = true;
                    regex = regex.replace(keyword.getKeyword(), Keyword.keywordPatterns.get(keyword.getDescription()));
                }
            }
            if (validRegex){
                if (!scenarioRegexes.containsKey(scenario)){
                    scenarioRegexes.put(scenario, new HashSet<>());
                }
                if (isJsonLine)
                    regex = generalizeJSON(regex);
                scenarioRegexes.get(scenario).add(regex);
            }
        }
    }

    private String generalizeJSON(String jsonString){
        Matcher jsonMatcher = JSON_KEY_VALUE_PATTERN.matcher(jsonString);
        while(jsonMatcher.find()){
            String jsonField = jsonMatcher.group(1);
            String jsonVal = jsonField.split(":")[1];
            if (!Keyword.keywordPatterns.containsValue(jsonVal.replace("\"", "")))
                jsonString = jsonString.replace(jsonVal, JSON_VALUE_REPLACE);
        }
        return jsonString;
    }

    private void checkVariousDateFormats(String logLine, Keyword keyword){
        String dateString = keyword.getKeyword();
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
            SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM d yyyy HH:mm:ss");
            String dateFormatted = dateFormat.format(date).concat(" GMT+0100 (Central European Standard Time)");
            if (logLine.contains(dateFormatted)){
                keyword.setKeyword(dateFormatted);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
