package analyser;

import BDDparse.scenarios.Scenario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogAnalyser {

    Map<Scenario, Set<String>> scenarioRegexes;
    Map<String, Set<Scenario>> analysedLog;
    public LogAnalyser(Map<Scenario, Set<String>> scenarioRegexes){
        this.scenarioRegexes = scenarioRegexes;
    }

    public void analyseLog(File logFile){
        analysedLog = new LinkedHashMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(logFile))){
            String line;
            while ((line = reader.readLine()) != null){
                for (Map.Entry<Scenario, Set<String>> entry : scenarioRegexes.entrySet()){
                    for (String regexRaw : entry.getValue()){
                        if (Pattern.compile(regexRaw).matcher(line).find()){
                            if (!analysedLog.containsKey(line))
                                analysedLog.put(line, new HashSet<>());
                            analysedLog.get(line).add(entry.getKey());
                            break;
                        }
                    }
                }
            }
        } catch (IOException err){
            err.printStackTrace();
        }
    }
}
