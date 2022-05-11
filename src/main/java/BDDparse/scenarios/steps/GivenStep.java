package BDDparse.scenarios.steps;

import java.util.regex.Pattern;

public class GivenStep extends ScenarioStep{
    public static final Pattern givenStepPattern = Pattern.compile("^Given", Pattern.CASE_INSENSITIVE);

    public GivenStep(String scenarioLine){
        super(scenarioLine);
    }
}
