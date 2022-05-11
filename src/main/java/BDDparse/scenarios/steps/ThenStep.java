package BDDparse.scenarios.steps;

import java.util.regex.Pattern;

public class ThenStep extends ScenarioStep{
    public static final Pattern thenStepPattern = Pattern.compile("^Then", Pattern.CASE_INSENSITIVE);

    public ThenStep(String scenarioLine){
        super(scenarioLine);
    }
}
