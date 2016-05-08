

public class AppManager {


    private static AppManager appManager = new AppManager();

    private AppManager() {}

    public static AppManager getInstance() { return appManager; }
}
