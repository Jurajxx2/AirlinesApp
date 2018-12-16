package com.trasimus.airlines.Controller.Database

import android.arch.persistence.room.*
import com.trasimus.airlines.Objects.Airline

@Dao
interface AirlineDao {

    @get:Query("SELECT * FROM Airlines")
    val allAirlines: List<Airline>

    @Query("SELECT * FROM Airlines where id = :id")
    fun getAirlineById(id: Int): Airline

    @Query("DELETE FROM Airlines")
    fun deleteAllAirlines()

    @Query("SELECT * FROM Airlines where isFavourite = 1")
    fun getFavouriteAirlines(): List<Airline>

    @Insert
    fun insertAirline(airline: Airline)

    @Delete
    fun deleteAirline(airline: Airline)

    @Update
    fun updateAirline(airline: Airline)
}