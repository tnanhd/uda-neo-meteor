package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("statusIcon")
fun ImageView.bindAsteroidStatusImage(isHazardous: Boolean) {
    if (isHazardous) {
        setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("absoluteMagnitudeText")
fun TextView.bindTextViewToMagnitude(number: Double) {
    val context = context
    text = String.format(context.getString(R.string.astronomical_unit_format_number), number)
}

@BindingAdapter("astronomicalUnitText")
fun TextView.bindTextViewToAstronomicalUnit(number: String) {
    val context = context
    text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun TextView.bindTextViewToKmUnit(number: Double) {
    val context = context
    text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun TextView.bindTextViewToDisplayVelocity(number: String) {
    val context = context
    text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("pictureOfDay")
fun ImageView.bindPictureOfDay(pictureOfDay: PictureOfDay?) {
    pictureOfDay?.let {
        if (it.mediaType == "image") {
            Picasso.get()
                .load(it.url)
                .into(this)
        }
    }
}