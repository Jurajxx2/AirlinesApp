package com.trasimus.airlines.Controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.trasimus.airlines.R
import android.content.Intent
import android.net.Uri
import android.widget.Button
import com.squareup.picasso.Picasso
import org.jetbrains.anko.sdk27.coroutines.onClick


class AirlineDetail : AppCompatActivity() {

    private lateinit var logo: ImageView
    private lateinit var name: TextView
    private lateinit var phone: TextView
    private lateinit var web: TextView
    private lateinit var webButton: Button

    private lateinit var airlineName: String
    private lateinit var airlinePhone: String
    private lateinit var airlineUrl: String
    private lateinit var logoUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airline_detail)

        val intent = intent
        airlineName = intent.getStringExtra("name")
        airlinePhone = intent.getStringExtra("phone")
        airlineUrl = intent.getStringExtra("web")
        logoUrl = intent.getStringExtra("logoUrl")

        logo = findViewById(R.id.logoAirline)
        name = findViewById(R.id.airline)
        phone = findViewById(R.id.number)
        web = findViewById(R.id.web)
        webButton = findViewById(R.id.webButton)

        phone.text = airlinePhone
        name.text = airlineName
        web.text = airlineUrl

        Picasso.with(this)
            .load("https://www.kayak.com$logoUrl")
            .error(R.drawable.warning)
            .into(logo)

        webButton.onClick {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://$airlineUrl"))
            startActivity(browserIntent)
        }
    }
}
