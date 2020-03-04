package mud.arca.io.mud.Database;

import android.annotation.SuppressLint;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import mud.arca.io.mud.DataStructures.Day;
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

}