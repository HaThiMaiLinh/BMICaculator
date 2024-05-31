package com.example.bmicalculator.model.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "bmi")
data class Bmi (
    @ColumnInfo(name = "time")
    var time: String,
    @ColumnInfo(name = "gender")
    var gender: Int,
    @ColumnInfo(name = "age")
    var age:Int,
    @ColumnInfo(name = "weight")
    var weight: String,
    @ColumnInfo(name = "height")
    var height:String,
    @ColumnInfo(name = "bmi")
    var bmi:Double
):Serializable{
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}