package mud.arca.io.mud.Util;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import mud.arca.io.mud.DataStructures.Day;

@SuppressLint("NewApi")
public class VariableImportanceUtil {

    public static Map<String, Double> calculateCovariances(Collection<Day> days, Collection<String> varNames) {
        return varNames.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        s -> calcCovOfVar(days, s)
                ));
    }

    public static double calcCovOfVar(Collection<Day> days, String varName) {
        // It only makes sense to consume days with both a mood and variable recording here.
        // That limits our data quite a bit. But comparing yesterday's mood to tomorrow's sleep doesn't make sense.

        List<Day> filteredDays = days.stream()
                .filter(day -> day.getMoodRecording() != null)
                .filter(day -> day.getMeasurements() != null)
                .filter(day -> day.getMeasurements().stream().anyMatch(m -> m.getVariable().getName().equals(varName)))
                .collect(Collectors.toList());

        // Calculate mean mood
        DoubleSummaryStatisticsPlus moodSummaryPlus = filteredDays.stream()
                .map(value -> (double) value.getMoodRecording().getValue())
                .collect(DoubleSummaryStatisticsPlus.collector());

        // Calculate mean var
        DoubleSummaryStatisticsPlus variableSummaryPlus = filteredDays.stream()
                .map(day -> (double) day.getMeasurements().stream().filter(m -> m.getVariable().getName().equals(varName)).findAny().get().getValue())
                .collect(DoubleSummaryStatisticsPlus.collector());


        // Shift and scale mood values to Z-score style scale
        // Makes mean 0 so that moodValues is also the deviation from the mean values
        double[] moodValues = filteredDays.stream()
                .mapToDouble(value -> value.getMoodRecording().getValue())
                .map(val -> (val - moodSummaryPlus.getAverage()) / moodSummaryPlus.getStandardDeviation()).toArray();

        double[] variableValues = filteredDays.stream()
                .mapToDouble(day -> day.getMeasurements().stream().filter(m -> m.getVariable().getName().equals(varName)).findAny().get().getValue())
                .map(val -> (val - variableSummaryPlus.getAverage()) / variableSummaryPlus.getStandardDeviation()).toArray();

        double coSum = 0;
        for (int i = 0; i < moodValues.length; i++) {
            coSum += moodValues[i] * variableValues[i];
        }

        return (coSum) / filteredDays.size();
    }


    public interface CalculationFinishListener {
        void onCalculationFinish(Map<String, Double> result);
    }

    public static class CalculationParams {
        Collection<Day> days;
        Collection<String> varNames;

        public CalculationParams(Collection<Day> days, Collection<String> varNames) {
            this.days = days;
            this.varNames = varNames;
        }
    }

    public static class CalculateTask extends AsyncTask<CalculationParams, Void, Map<String, Double>> {
        private CalculationFinishListener listener;

        public CalculateTask(CalculationFinishListener listener) {
            this.listener = listener;
        }

        public void execute(Collection<Day> days, Collection<String> varnames) {
            execute(new CalculationParams(days, varnames));
        }

        @Override
        protected Map<String, Double> doInBackground(CalculationParams... params) {
            return VariableImportanceUtil.calculateCovariances(params[0].days, params[0].varNames);
        }

        @Override
        protected void onPostExecute(Map<String, Double> result) {
            // This is called on the UI thread
            super.onPostExecute(result);
            listener.onCalculationFinish(result);
        }
    }

}
