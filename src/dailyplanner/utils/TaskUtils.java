package dailyplanner.utils;

public class TaskUtils {

    public static <T extends Enum<T>> String[] getAllEnumNamesForDialog(Class<T> aEnum)
    {
        T[] values = aEnum.getEnumConstants();
        String[] result = new String[values.length];
        for (T value : values) {
            int index = value.ordinal();
            result[index] = String.format("\t(%d) %s", index + 1, value);
        }
        return result;
    }

    public static <T extends Enum<T>> T getEditActionById(Class<T> aEnum, int idAction)
    {
        return aEnum.getEnumConstants()[idAction - 1];
    }

}
