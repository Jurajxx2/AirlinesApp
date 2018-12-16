package com.trasimus.airlines.Objects

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Airlines")
class Airline(
    @ColumnInfo(name = "logo") var logoUrl: String, @ColumnInfo(name = "name") var name: String, @ColumnInfo(
        name = "isFavourite"
    ) var isFavourite: Boolean
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}