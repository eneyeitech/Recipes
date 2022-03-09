package recipes.helper;

public class IDGenerator {

    private static long id = 0;

    public static long getId() {
            return ++id;
        }
}
