package com.assignment.CountryDetails.data.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.assignment.CountryDetails.data.models.CountryDetailsRow;
import java.util.List;

@Dao
public interface CountryDao {

    @Query("SELECT * FROM CountryDetailsRow ORDER BY id DESC")
    List<CountryDetailsRow> getAllRec();

    @Query("SELECT * FROM CountryDetailsRow ORDER BY id ASC")
    LiveData<List<CountryDetailsRow>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CountryDetailsRow obj);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<CountryDetailsRow> array);

    @Query("DELETE FROM CountryDetailsRow")
    int deleteAllRows();
}
