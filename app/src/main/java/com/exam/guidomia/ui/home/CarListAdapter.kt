package com.exam.guidomia.ui.home

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.text.SpannableString
import android.text.style.BulletSpan
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.exam.core.data.Car
import com.exam.guidomia.R
import com.exam.guidomia.databinding.CardCarBinding


class CarListAdapter(private var cars: ArrayList<Car>): RecyclerView.Adapter<CarListAdapter.CarViewHolder>() {

    inner class CarViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = CardCarBinding.bind(view)

        // list item
        private val image = binding.carImage
        private val name = binding.name
        private val price = binding.price
        private val rating = binding.rating

        // card view
        private val card = binding.baseCardView
        private val collapsibleView = binding.hiddenView
        private val textPros = binding.textPros
        private val textCons = binding.textCons

        @SuppressLint("SetTextI18n", "DiscouragedApi")
        fun bind(car: Car) {
            val context = binding.root.context
            val uri = "@drawable/image${car.id}"
            val imageResource: Int = context.resources.getIdentifier(uri, null, context.packageName)
            val carImage: Drawable? = ContextCompat.getDrawable(context, imageResource)
            image.setImageDrawable(carImage)

            name.text = "${car.make} ${car.model}"

            val format: NumberFormat = NumberFormat.getCurrencyInstance()
            format.setMaximumFractionDigits(0)
            format.currency = Currency.getInstance("USD")
            price.text = format.format(car.marketPrice)
            rating.rating = car.rating.toFloat()

            textPros.text = car.prosList.toBulletedList()
            textCons.text = car.consList.toBulletedList()

            card.setOnClickListener {
                if(collapsibleView.visibility == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(card, AutoTransition())
                    collapsibleView.visibility = View.GONE
                } else {
                    TransitionManager.beginDelayedTransition(card, AutoTransition())
                    collapsibleView.visibility = View.VISIBLE
                }
            }
        }

        // solution to dynamically add bullet points using TextView
        // source: https://stackoverflow.com/questions/4992794/how-to-add-bulleted-list-to-android-application
        private fun List<String>.toBulletedList(): CharSequence {
            return SpannableString(this.joinToString("\n")).apply {
                this@toBulletedList.foldIndexed(0) { index, acc, span ->
                    val end = acc + span.length + if (index != this@toBulletedList.size - 1) 1 else 0
                    this.setSpan(BulletSpan(16), acc, end, 0)
                    end
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCars(newCars: List<Car>) {
        cars.clear()
        cars.addAll(newCars)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder = CarViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_car, parent, false)
    )

    override fun getItemCount(): Int = cars.size

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(cars[position])
    }


}