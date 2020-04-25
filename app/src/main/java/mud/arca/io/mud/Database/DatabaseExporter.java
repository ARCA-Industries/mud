package mud.arca.io.mud.Database;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import androidx.core.content.FileProvider;

public class DatabaseExporter {

    public static String getCSV(Context context) {
        // TODO: Implement
        return "a,b,c\n1,2,3\n4,5,6\n7,8,9";
    }

    public static Intent getShareIntent(Context context) throws IOException {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, DatabaseExporter.getLocalURI(DatabaseExporter.getCSV(context), context));
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
