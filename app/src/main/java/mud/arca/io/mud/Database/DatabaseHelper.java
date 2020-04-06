package mud.arca.io.mud.Database;

import android.annotation.SuppressLint;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import mud.arca.io.mud.DataStructures.Day;
import mud.arca.io.mud.R;
import mud.arca.io.mud.Util.App;
import mud.arca.io.mud.Util.DateUtil;
import mud.arca.io.mud.Util.Util;
import mud.arca.io.mud.DataStructures.Variable;

public class DatabaseHelper {


    public interface LoadSuccessListener<T> {
        void onLoaded(T data);
    }

    @SuppressLint("NewApi")
    // TODO: This will need to be desugared later. Android Studio 4.0 should be able to handle this automatically for us.
    public static void loadDayData(LoadSuccessListener<List<Day>> listener) {
        CollectionReference mItemsCollection;

        mItemsCollection = getDaysCollection();

        mItemsCollection.orderBy("date", Query.Direction.ASCENDING).get().addOnCompleteListener(task -> {
            List<Day> dayData = (task.getResult().getDocuments().stream().map(documentSnapshot -> documentSnapshot.toObject(Day.class)).collect(Collectors.toCollection(ArrayList::new)));
            Util.debug("User.loadDayDataFromFirestore completed");
            listener.onLoaded(dayData);
        });
    }


    @SuppressLint("NewApi")
    // TODO: This will need to be desugared later. Android Studio 4.0 should be able to handle this automatically for us.
    public static void loadVariableData(LoadSuccessListener<List<Variable>> listener) {
        CollectionReference mItemsCollection;

        mItemsCollection = getVariableCollection();

        mItemsCollection.orderBy("name", Query.Direction.ASCENDING).get().addOnCompleteListener(task -> {
            List<Variable> varData = (task.getResult().getDocuments().stream().map(documentSnapshot -> documentSnapshot.toObject(Variable.class)).collect(Collectors.toCollection(ArrayList::new)));
            Util.debug("User.loadVariableData completed");
            listener.onLoaded(varData);
        });
    }

    public static CollectionReference getVariableCollection() {
        return FirebaseFirestore.getInstance().collection("users/" + FirebaseAuth.getInstance().getUid() + "/variables");
    }

    public static CollectionReference getDaysCollection() {
        return FirebaseFirestore.getInstance().collection("users/" + FirebaseAuth.getInstance().getUid() + "/days");
    }


    /**
     * If there are zero variables in the database,
     * create some default ones and add them to the database
     */
    public static void ensureDefaultVariables() {
        getVariableCollection().get().addOnCompleteListener(runnable -> {
           if (runnable.getResult().isEmpty()) {
               List<Object> defaults = Arrays.asList(
                    new Variable("Sleep", "hr", Variable.VarType.FLOAT),
                    new Variable("Exercised", "bool", Variable.VarType.BOOL),
                    new Variable("Calories Eaten", "kcal", Variable.VarType.INT)
               );
               for (Object aDefault : defaults) {
                   getVariableCollection().add(aDefault);
               }
           }
        });
    }

    @SuppressLint("NewApi")
    // TODO: Android Gradle Plugin 4.0 (and Android Studio 4.0) will desugar this for us
    public static void ensureContinuousDays() {
        // Note: I'm on record saying I don't like adding blank entries to the database, but
        // the alternative is tricking the FirestoreRecyclerAdapter into displaying blank items
        // and keeping track of the insert/delete stuff. Maybe later.

        // TODO: If this instance hasn't polled the database, it'll add blank days again.

        CollectionReference daysCollection = DatabaseHelper.getDaysCollection();
        daysCollection.orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(runnable -> {
            List<DocumentSnapshot> documents = runnable.getResult().getDocuments();

            int numDays = 0;
            if (documents.isEmpty()) {
                // When a new user calls this method, populate with some blank days
                numDays = App.getContext().getResources().getInteger(R.integer.num_days_before_install);

                System.out.println("Adding " + numDays + " to the database.");
                for (int i = 0; i < numDays; i++) {
                    daysCollection.add(new Day(
                            DateUtil.dateFromLocalDate(LocalDate.now().minusDays(i))
                    ));
                }
            } else {
                // Since there's already stuff in the database, there might be gaps in days.
                // Fill those gaps in
                // Count the current date as the latest entry and the earliest entry in the database as the earliest.

                // Walk through the existing days from latest to earliest
                for (int i = 0; i < documents.size(); i++) {
                    LocalDate endDate;
                    if (i==0) {
                        endDate = LocalDate.now();
                    } else {
                        endDate = DateUtil.localDateFromDate(documents.get(i-1).toObject(Day.class).getDate()).minusDays(1);
                    }

                    // Note that we actually do care about time travellers here.
                    // More specifically, if a device's time is set behind, we don't want it to add any days at all.
                    // A negative numDays value will do just fine for that.
                    // Also, I'm going to just cast to int. If a user opens this app only once every 10^6 days, it's going to break for them. Oh well.
                    numDays = (int) ChronoUnit.DAYS.between(
                            DateUtil.localDateFromDate(documents.get(i).toObject(Day.class).getDate()),
                            endDate
                    );

                    // Add those days to the database
                    for (int j = 0; j < numDays; j++) {
                        daysCollection.add(new Day(
                                DateUtil.dateFromLocalDate(endDate.minusDays(j))
                        ));
                    }

                }
            }
        });
    }

}
