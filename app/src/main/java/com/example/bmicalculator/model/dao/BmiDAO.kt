package com.example.bmicalculator.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bmicalculator.model.dto.Bmi
import kotlinx.coroutines.flow.Flow


@Dao
interface BmiDAO  {
    @Query("SELECT * FROM bmi")
    fun getAll(): Flow<List<Bmi>>

    @Query("select * from bmi where id = :id")
    fun getBmi(id: Int): Bmi

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bmi: Bmi)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(bmi: Bmi)

    @Delete
    fun delete(bmi: Bmi)
}