package com.trasimus.airlines.Controller

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.trasimus.airlines.Controller.Database.AppDatabase
import com.trasimus.airlines.Objects.Airline
import com.trasimus.airlines.R
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.uiThread
import java.net.URL

class AirlinesAdapter(private var airlines:List<Airline>, private val context: Context): RecyclerView.Adapter<AirlinesAdapter.AirlineViewHolder>() {

    private lateinit var appDatabase: AppDatabase

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AirlineViewHolder {
        appDatabase = AppDatabase.getInstance(context)
        val itemView = LayoutInflater.from(p0.context)
            .inflate(R.layout.airline_item, p0, false)

        return AirlineViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return airlines.size
    }

    override fun onBindViewHolder(p0: AirlineViewHolder, p1: Int) {
        val context = p0.logo.context
        p0.name.text = airlines[p1].name

        p0.favouriteToggleButton.isChecked = airlines[p1].isFavourite

        Picasso.with(context)
            .load("https://www.kayak.com" + airlines[p1].logoUrl)
            .error(R.drawable.warning)
            .into(p0.logo)                        //Your image view object.
    }

    inner class AirlineViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var logo: ImageView = view.findViewById(R.id.logo)
        var name: TextView = view.findViewById(R.id.name)
        var favouriteToggleButton: ToggleButton = view.findViewById(R.id.favouriteToggle)

        init {
            favouriteToggleButton.onClick {
                (context as AirlinesActivity).userItemClick(adapterPosition)
            }
            logo.onClick {
                (context as AirlinesActivity).airlineClick(adapterPosition)
            }
            name.onClick {
                (context as AirlinesActivity).airlineClick(adapterPosition)
            }
        }
     }
}