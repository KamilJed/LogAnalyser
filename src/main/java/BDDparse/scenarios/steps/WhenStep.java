package BDDparse.scenarios.steps;

import java.util.regex.Pattern;

public class WhenStep extends ScenarioStep{
    public static final Pattern whenStepPattern = Pattern.compile("^When", Pattern.CASE_INSENSITIVE);

    public WhenStep(String scenarioLine){
        super(scenarioLine);
    }
}
