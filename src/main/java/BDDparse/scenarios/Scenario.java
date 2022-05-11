package BDDparse.scenarios;

import BDDparse.scenarios.steps.GivenStep;
import BDDparse.scenarios.steps.ScenarioStep;
import BDDparse.scenarios.steps.ThenStep;
import BDDparse.scenarios.steps.WhenStep;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Scenario {
    public final static Pattern scenarioPattern = Pattern.compile("Scenario:(?:\\s+)?(.+)");
    final String scenarioName;
    List<ScenarioStep> scenarioSteps;

    public Scenario(String scenarioName) {
        this.scenarioName = scenarioName;
        scenarioSteps = new ArrayList<>();
    }

    public void addScenarioStep(String scenarioStep) {
        ScenarioStep step = createStepFromString(scenarioStep.trim());
        if (step != null)
            scenarioSteps.add(step);
    }

    public List<ScenarioStep> getScenarioSteps() {
        return scenarioSteps;
    }

    private ScenarioStep createStepFromString(String scenarioStepString) {
        ScenarioStep scenarioStep;
        if (GivenStep.givenStepPattern.matcher(scenarioStepString.trim()).find()){
            scenarioStep = new GivenStep(scenarioStepString);
        }
        else if (ThenStep.thenStepPattern.matcher(scenarioStepString.trim()).find()){
            scenarioStep = new ThenStep(scenarioStepString);
        }
        else if (WhenStep.whenStepPattern.matcher(scenarioStepString.trim()).find()){
            scenarioStep = new WhenStep(scenarioStepString);
        }
        else {
            try {
                scenarioStep = scenarioSteps.get(scenarioSteps.size() - 1).getClass().getConstructor(new Class[]{String.class}).newInstance(scenarioStepString);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                scenarioStep = null;
                e.printStackTrace();
            }
        }
        return scenarioStep;
    }
}
