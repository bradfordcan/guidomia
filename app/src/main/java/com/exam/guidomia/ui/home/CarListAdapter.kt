package com.exam.guidomia.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.exam.core.data.Car
import com.exam.guidomia.R
import com.exam.guidomia.databinding.CardCarBinding


class CarListAdapter(private var cars: ArrayList<Car>): RecyclerView.Adapter<CarListAdapter.CarViewHolder>() {

    var carsCopy = arrayListOf<Car>()

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
        private val layoutPros = binding.layoutPros
        private val layoutCons = binding.layoutCons

        @SuppressLint("SetTextI18n", "DiscouragedApi")
        fun bind(car: Car, position: Int, expand: (position: Int) -> Unit) {
            val context = binding.root.context
            val uri = "@drawable/image${car.id}"
            val imageResource: Int = context.resources.getIdentifier(uri, null, context.packageName)
            val carImage: Drawable? = ContextCompat.getDrawable(context, imageResource)
            image.setImageDrawable(carImage)

            name.text = "${car.make} ${car.model}"

            if(car.expanded) {
                collapsibleView.visibility = View.VISIBLE
            } else {
                collapsibleView.visibility = View.GONE
            }

            val format: NumberFormat = NumberFormat.getCurrencyInstance()
            format.setMaximumFractionDigits(0)
            format.currency = Currency.getInstance("USD")
            price.text = format.format(car.marketPrice)
            rating.rating = car.rating.toFloat()

            layoutPros.removeAllViews()
            car.prosList.forEach {
                if(it.isNotEmpty()) {
                    val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val layoutBullet = inflater.inflate(R.layout.layout_bullet_point_text,null)
                    val textView = layoutBullet.findViewById<TextView>(R.id.textBullet)
                    textView.text = it
                    layoutPros.addView(layoutBullet)
                }
            }

            layoutCons.removeAllViews()
            car.consList.forEach {
                if(it.isNotEmpty()) {
                    val inflater: LayoutInflater =
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val layoutBullet = inflater.inflate(R.layout.layout_bullet_point_text, null)
                    val textView = layoutBullet.findViewById<TextView>(R.id.textBullet)
                    textView.text = it
                    layoutCons.addView(layoutBullet)
                }
            }

            /*car.consList.forEach {
                textView.text = it
                layoutCons.addView(layoutBullet)
            }*/

            card.setOnClickListener {
                expand(position)
                if(collapsibleView.visibility == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(card, AutoTransition())
                    collapsibleView.visibility = View.GONE
                } else {
                    TransitionManager.beginDelayedTransition(card, AutoTransition())
                    collapsibleView.visibility = View.VISIBLE
                }
            }
        }
    }

    private val carsFilter = object : Filter() {
        val filteredList: ArrayList<Car> = ArrayList()
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            filteredList.clear()
            if(constraint.isNullOrEmpty()) {
                cars.clear()
                cars.addAll(carsCopy) // copy original list
                filteredList.addAll(cars)
            } else {
                val query = constraint.toString().trim().lowercase()
                val filters = query.split("-")
                val filterMake = filters[0]
                val filterModel = filters[1]

                cars.forEach { car ->
                    if(car.make.lowercase().contains(filterMake) && car.model.lowercase().contains(filterModel)) {
                        filteredList.add(car)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results != null && (results.values as ArrayList<Car>).size > 0) {
                cars.clear()
                cars.addAll(results.values as MutableList<Car>)
            }
            notifyDataSetChanged()
        }
    }

    fun getFilter(): Filter {
        return carsFilter
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCars(newCars: List<Car>, id: Long? = -1) {
        cars.clear()
        newCars[0].expanded = true
        cars.addAll(newCars)

        // make a copy for filtering
        carsCopy.clear()
        carsCopy.addAll(newCars)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder = CarViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_car, parent, false)
    )

    override fun getItemCount(): Int = cars.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(cars[position], position) { positionClicked ->
            // reset
            cars.forEach {
                it.expanded = false
            }
            notifyDataSetChanged()
            cars[positionClicked].expanded = true
            notifyItemChanged(positionClicked)
        }
    }

}