package Model;

public class FloatThreshold {
    public static boolean isEqualAtThreshold(float first, float second) {
        final float THRESHOLD = 0.0001f;

        boolean isEqual = false;
        if (Math.abs(first - second) < THRESHOLD) {
            isEqual = true;
        }

        return isEqual;
    }
}
