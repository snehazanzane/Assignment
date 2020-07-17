package com.assignment.CountryDetails.data.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.assignment.CountryDetails.data.models.CountryDetailsRow;

@Database(entities = {CountryDetailsRow.class}, version = 1, exportSchema = false)
public abstract class CountryDatabase  extends RoomDatabase {
    private static CountryDatabase INSTANCE;

    /**
     * Gets app database.
     *
     * @param context the context
     * @return the app database
     */
    public static CountryDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), CountryDatabase.class, "country-database")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    /**
     * Destroy instance.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Country model dao
     *
     * @return the Country model dao
     */
    public abstract CountryDao countryDao();

}
