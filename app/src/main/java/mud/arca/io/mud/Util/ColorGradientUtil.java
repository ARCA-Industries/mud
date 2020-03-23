package mud.arca.io.mud.Util;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;

public class ColorGradientUtil {

    private float[] points;
    private int[] colors;

    /**
     * Assumes points is sorted
     */
    public ColorGradientUtil(float[] points, int[] colors) {
        if (colors.length != points.length) {
            throw new IllegalArgumentException();
        }

        this.colors = colors;
        this.points = points;

    }

    public int getColorForPoint(float point) {
        int startIndex = 0;
        int endIndex = 1;

        // Find start and end index surrounding point
        while (points[endIndex] < point) {
            startIndex += 1;
            endIndex += 1;
        }

        float d = points[endIndex] - points[startIndex];
        float normalized = (point - points[startIndex]) / d;

        return Color.rgb(
                (int) (normalized * Color.red(colors[endIndex]) + (1 - normalized) * Color.red(colors[startIndex])),
                (int) (normalized * Color.green(colors[endIndex]) + (1 - normalized) * Color.green(colors[startIndex])),
                (int) (normalized * Color.blue(colors[endIndex]) + (1 - normalized) * Color.blue(colors[startIndex]))
        );
    }

    public static float[] getFloatArray(Resources resources, int id) {
        TypedArray array = resources.obtainTypedArray(id);
        try {
            if (array.length() == 0) {
                return null;
            }
            float[] values = new float[array.length()];
            for (int i = 0; i < values.length; i++) {
                values[i] = array.getFloat(i, Float.NaN);
                if (Float.isNaN(values[i])) {
                    return null;
                }
            }
            return values;

        } finally {
            array.recycle();
        }
    }


}
