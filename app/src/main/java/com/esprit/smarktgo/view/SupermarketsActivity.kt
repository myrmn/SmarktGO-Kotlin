package com.esprit.smarktgo.view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.esprit.smarktgo.R
import com.esprit.smarktgo.databinding.ActivityCartGroupBinding
import com.esprit.smarktgo.databinding.ActivitySupermarketsBinding
import com.esprit.smarktgo.model.Location
import com.esprit.smarktgo.utils.RetrofitInstance.BASE_URL
import com.esprit.smarktgo.viewmodel.SupermarketsViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.plugin.locationcomponent.location

var mapView: MapView? = null

private lateinit var supermarkets: SupermarketsViewModel
private lateinit var pointAnnotationManager: PointAnnotationManager
private lateinit var pointAnnotation: PointAnnotation
lateinit var builder: AlertDialog
lateinit var view:LayoutInflater
class SupermarketsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySupermarketsBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supermarkets)
        binding = ActivitySupermarketsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener { finish() }

        mapView = findViewById(R.id.mapView)
        mapView?.getMapboxMap()?.loadStyleUri(
            Style.MAPBOX_STREETS,
            object : Style.OnStyleLoaded {
                override fun onStyleLoaded(style: Style) {

                    supermarkets = SupermarketsViewModel()
                    supermarkets.observeSupermarketsLiveData()
                        .observe(this@SupermarketsActivity, Observer {

                                lista ->
                            for (i in lista.indices) {
                                val point = lista[i].location
                                prepareAnnotationMarker(point.coordinates[0], point.coordinates[1])
                                pointAnnotationManager.apply {
                                    addClickListener(
                                        OnPointAnnotationClickListener {
                                            marketDetails(lista[i].id,lista[i].name,lista[i].description,lista[i].address,lista[i].image,lista[i].location)
                                            false
                                        }
                                    )
                                }
                            }
                        })
                }

            }

        )

    }

    private fun marketDetails(id:String,name:String,description:String,address:String,i: String,location: Location) {
        val builder = AlertDialog.Builder(this).create()
        val view = layoutInflater.inflate(R.layout.map_dialog, null)
        val image = view.findViewById<ImageView>(R.id.mapImage)
        val close = view.findViewById<ImageView>(R.id.close)
        Glide.with(applicationContext).load(BASE_URL+"img/" + i).into(image)

        builder.setView(view)
        builder.show()
        close.setOnClickListener {
            view.visibility = View.GONE
            builder.cancel()
        }
        val seeMore = view.findViewById<TextView>(R.id.seeMore)
        seeMore.setOnClickListener {
            navigateToSupermarketActivity(id,name,description,address,i,location)
        }

    }


    fun displayCurrentLocation() {
        mapView!!.location.updateSettings {
            enabled = true
            pulsingEnabled = true
        }
    }

    private fun prepareAnnotationMarker(long: Double, lat: Double) {
        bitmapFromDrawableRes(this@SupermarketsActivity, R.drawable.red_marker)?.let {
            val annotationPlugin = mapView?.annotations
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(long, lat))
                .withIconImage(it)
                .withIconAnchor(IconAnchor.BOTTOM)
            pointAnnotationManager = annotationPlugin!!.createPointAnnotationManager()
            pointAnnotation = pointAnnotationManager.create(pointAnnotationOptions)
        }
    }


    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }
    fun navigateToSupermarketActivity(id: String, name: String, description: String?, address: String?, image: String?,location: Location) {
        val intent = Intent(this, SupermarketActivity::class.java).apply {
            putExtra("supermarketId", id)
            putExtra("name", name)
            putExtra("description", description)
            putExtra("address", address)
            putExtra("image", image)
            putExtra("latitude", location.coordinates[0])
            putExtra("longitude", location.coordinates[1])
        }
        startActivity(intent)
    }


}