package mud.arca.io.mud;

import android.app.Application;
import android.content.Context;

/**
 * The purpose of this class is to allow you to get resource content (e.g. colors)
 * from a static context.
 * Usage: App.getContext().getColor(R.color.green)
 * https://stackoverflow.com/questions/4391720/how-can-i-get-a-resource-content-from-a-static-context
 */
public class App extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}