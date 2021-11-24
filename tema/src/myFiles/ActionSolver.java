package myFiles;

import common.Constants;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ActionSolver {
    private Writer fileWriter;
    private JSONArray arrayResult;
    private List<ActionInputData> actions;

    public ActionSolver(Writer fileWriter, JSONArray arrayResult, List<ActionInputData> actions) {
        this.fileWriter = fileWriter;
        this.arrayResult = arrayResult;
        this.actions = actions;
    }

    private void finalResult(String result, ActionInputData action) throws IOException {
//       this.arrayResult.add(fileWriter.writeFile(action.getActionId(), "", result));
       this.arrayResult.add(arrayResult.size(), fileWriter.writeFile(action.getActionId(), "", result));
    }

    public void solveActions() throws IOException {
        for (ActionInputData action : actions) {
            String result = "";
            if (Objects.equals(action.getActionType(), Constants.COMMAND)) {
                result = CommandSolver.solve(action);
            }
            if (Objects.equals(action.getActionType(), Constants.QUERY)) {
                result = QuerrySolver.solve(action);
            }
            if (Objects.equals(action.getActionType(), Constants.RECOMMENDATION)) {
                result = RecommendationSolver.solve(action);
            }
            finalResult(result, action);
        }
    }
}
