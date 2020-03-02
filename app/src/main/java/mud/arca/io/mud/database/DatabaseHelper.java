package mud.arca.io.mud.database;

import android.annotation.SuppressLint;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.DataStructures.Util;

public class DatabaseHelper {


    public interface LoadSuccessListener<T> {
        void onLoaded(T data);
    }

    @SuppressLint("NewApi")
    // TODO: This will need to be desugared later. Android Studio 4.0 should be able to handle this automatically for us.
    public static void loadDayData(LoadSuccessListener<List<Day>> listener) {
        CollectionReference mItemsCollection;

        mItemsCollection = FirebaseFirestore.getInstance().collection("users/" + FirebaseAuth.getInstance().getUid() + "/days");

        mItemsCollection.orderBy("date", Query.Direction.ASCENDING).get().addOnCompleteListener(task -> {
            List<Day> dayData = (task.getResult().getDocuments().stream().map(documentSnapshot -> documentSnapshot.toObject(Day.class)).collect(Collectors.toCollection(ArrayList::new)));
            Util.debug("User.loadDayDataFromFirestore completed");
            listener.onLoaded(dayData);
        });
    }

}
