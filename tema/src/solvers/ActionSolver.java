package solvers;

import common.Constants;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public final class ActionSolver {
    private Writer fileWriter;
    private JSONArray arrayResult;
    private List<ActionInputData> actions;

    public ActionSolver(final Writer fileWriter, final JSONArray arrayResult,
                        final List<ActionInputData> actions) {
        this.fileWriter = fileWriter;
        this.arrayResult = arrayResult;
        this.actions = actions;
    }

    private void finalResult(final String result,
                             final ActionInputData action) throws IOException {
       this.arrayResult.add(arrayResult.size(), fileWriter.writeFile(action.getActionId(),
               "", result));
    }

    /**
     * function that calls the method to solve
     * a certain action (command, query, recommendation)
     * @throws IOException
     */
    public void solveActions() throws IOException {
        for (ActionInputData action : actions) {
            String result = "";
            if (Objects.equals(action.getActionType(), Constants.COMMAND)) {
                result = CommandSolver.solve(action);
            }
            if (Objects.equals(action.getActionType(), Constants.QUERY)) {
                result = QuerySolver.solve(action);
            }
            if (Objects.equals(action.getActionType(), Constants.RECOMMENDATION)) {
                result = RecommendationSolver.solve(action);
            }
            finalResult(result, action);
        }
    }
}
