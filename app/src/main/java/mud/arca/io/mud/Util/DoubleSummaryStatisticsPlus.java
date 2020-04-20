package mud.arca.io.mud.Util;

import android.os.Build;

import java.util.DoubleSummaryStatistics;
import java.util.stream.Collector;

import androidx.annotation.RequiresApi;

/**
 * Extends DoubleSummaryStatistics to include standard deviation
 * Adapted from https://stackoverflow.com/questions/36263352/java-streams-standard-deviation/36264148#36264148
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class DoubleSummaryStatisticsPlus extends DoubleSummaryStatistics {
    private double sumOfSquare = 0.0d;
    private double sumOfSquareCompensation; // Low order bits of sum
    private double simpleSumOfSquare; // Used to compute right sum for non-finite inputs

    @Override
    public void accept(double value) {
        super.accept(value);
        double squareValue = value * value;
        simpleSumOfSquare += squareValue;
        sumOfSquareWithCompensation(squareValue);
    }

    public DoubleSummaryStatisticsPlus combine(DoubleSummaryStatisticsPlus other) {
        super.combine(other);
        simpleSumOfSquare += other.simpleSumOfSquare;
        sumOfSquareWithCompensation(other.sumOfSquare);
        sumOfSquareWithCompensation(other.sumOfSquareCompensation);
        return this;
    }

    private void sumOfSquareWithCompensation(double value) {
        double tmp = value - sumOfSquareCompensation;
        double velvel = sumOfSquare + tmp; // Little wolf of rounding error
        sumOfSquareCompensation = (velvel - sumOfSquare) - tmp;
        sumOfSquare = velvel;
    }

    public double getSumOfSquare() {
        double tmp = sumOfSquare + sumOfSquareCompensation;
        if (Double.isNaN(tmp) && Double.isInfinite(simpleSumOfSquare)) {
            return simpleSumOfSquare;
        }
        return tmp;
    }

    public final double getStandardDeviation() {
        return getCount() > 0 ? Math.sqrt((getSumOfSquare() / getCount()) - Math.pow(getAverage(), 2)) : 0.0d;
    }

    public static Collector<Double, ?, DoubleSummaryStatisticsPlus> collector() {
        return Collector.of(DoubleSummaryStatisticsPlus::new, DoubleSummaryStatisticsPlus::accept, DoubleSummaryStatisticsPlus::combine);
    }
}
