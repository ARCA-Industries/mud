package mud.arca.io.mud.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.core.content.FileProvider;
import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.User;
import mud.arca.io.mud.DataStructures.Variable;

public class DatabaseExporter {

    public interface GetCSVCallback {
        void onCSVGenerated(String csv);
    }

    public static void getCSV(GetCSVCallback callback) {
        User.getCurrentUser().updateUserData(user -> callback.onCSVGenerated(getCSV(user)));
    }

    public static String getCSV(User user) {
        return getHeader(user) + "\n" + getRows(user);
    }

    @SuppressLint("NewApi")
    private static String getRows(User user) {
        return user.getDayData().stream().map(
                day -> getRow(user, day)
        ).collect(Collectors.joining("\n"));
    }

    @SuppressLint("NewApi")
    private static String getRow(User user, Day day) {
        List<String> cells = new ArrayList<>();

        cells.add(SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(day.getDate()));
        cells.add(day.getMoodString(""));
        for (Variable var : user.getVarData()) {
            cells.add(day.getVarString(var.getName(), ""));
        }

        return String.join(",", cells);
    }

    @SuppressLint("NewApi")
    static String getHeader(User user) {
        return "Date,Mood," +
                user.getVarData().stream().map(Variable::getName).collect(Collectors.joining(","));
    }

    public interface ShareIntentCallback {
        void onIntentReady(Intent intent);

        void onFailed(Exception e);
    }

    public static void getShareIntentAsync(Context context, ShareIntentCallback callback) {
        DatabaseExporter.getCSV(csv -> {
            try {
                callback.onIntentReady(
                        getShareIntent(context, csv)
                );
            } catch (IOException e) {
                e.printStackTrace();
                callback.onFailed(e);
            }
        });
    }

    public static Intent getShareIntent(Context context, String fileContents) throws IOException {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, DatabaseExporter.getLocalURI(fileContents, context));
        intent.setType("text/csv");

        return Intent.createChooser(intent, null);
    }

    public static Uri getLocalURI(String fileContents, Context context) throws IOException {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "mud_export_" + System.currentTimeMillis() + ".csv");
        PrintWriter out = new PrintWriter(file);
        out.write(fileContents);
        out.close();
        return FileProvider.getUriForFile(context, "io.arca.mud.fileprovider", file);
    }
}
