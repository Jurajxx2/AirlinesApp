package com.trasimus.airlines.Controller

import android.content.SharedPreferences
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trasimus.airlines.Controller.Database.AppDatabase
import com.trasimus.airlines.Objects.Airline
import com.trasimus.airlines.Objects.AirlineAPIObject
import com.trasimus.airlines.R
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.net.URL
import android.widget.Toast



class AirlinesActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private var firstBoot: Boolean = false
    private lateinit var airlines: List<Airline>
    private lateinit var airlinesList: ArrayList<Airline>
    private lateinit var appDatabase: AppDatabase

    private lateinit var airlinesData: List<AirlineAPIObject>
    private lateinit var airlineEnums: Collection<AirlineAPIObject>
    private lateinit var airlineJson: String
    private var isFavourite: Boolean = false

    private lateinit var loadingBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airlines)

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        firstBoot = sharedPref.getBoolean("firstBoot", false)
        appDatabase = AppDatabase.getInstance(this)
        loadingBar = findViewById(R.id.loading)

        airlinesList = arrayListOf()

        doAsync {
            downloadData()
            uiThread {
                convertData()
            }
        }
    }

    private fun downloadData(){
        appDatabase = AppDatabase.getInstance(this@AirlinesActivity)
        airlineJson = URL("https://www.kayak.com/h/mobileapis/directory/airlines").readText()
        val airlineCollection = object : TypeToken<Collection<AirlineAPIObject>>(){} .type
        airlineEnums = Gson().fromJson(airlineJson, airlineCollection)
        airlinesData = airlineEnums.toList()
    }

    private fun sync(){
        loadingBar.visibility = View.VISIBLE
        doAsync {
            appDatabase.airlineModel().deleteAllAirlines()
            downloadData()
            convertData()
        }
    }

    private fun showData(){
        val recyclerView = findViewById<RecyclerView>(R.id.airlinesList)
        val mAdapter = AirlinesAdapter(airlines, this@AirlinesActivity)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = mAdapter
        loadingBar.visibility = View.GONE
    }

    private fun convertData(){
        var airlineObject: Airline
        airlinesData.forEach {
            airlineObject = Airline(it.logoURL, it.name, false)
            airlinesList.add(airlineObject)
        }
        doAsync {
            handleDatabase()
            uiThread { showData() }
        }
    }

    private fun handleDatabase(){
        airlines = appDatabase.airlineModel().allAirlines
        if (airlines.isEmpty()) {
            airlinesList.forEach{
                appDatabase.airlineModel().insertAirline(it)
            }
        }
        airlines = appDatabase.airlineModel().allAirlines
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.favourite, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.favourite -> {
                if (isFavourite){
                    isFavourite = false
                    item.setIcon(R.drawable.heartoff)
                    doAsync {
                        airlines = appDatabase.airlineModel().allAirlines
                        uiThread {
                            showData()
                        }
                    }
                } else {
                    isFavourite = true
                    item.setIcon(R.drawable.hearton)
                    doAsync {
                        airlines = appDatabase.airlineModel().getFavouriteAirlines()
                        uiThread {
                            showData()
                        }
                    }
                }
                true
            }
            R.id.sync -> {
                sync()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun userItemClick(pos: Int) {
        doAsync {
            airlines[pos].isFavourite = !airlines[pos].isFavourite
            appDatabase.airlineModel().updateAirline(airlines[pos])
            uiThread {
                if (airlines[pos].isFavourite){
                    toast("Added to favourites!")
                } else {
                    toast("Removed from favourites!")
                }
            }
        }
    }
}
