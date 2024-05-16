package com.exam.guidomia.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.exam.core.data.Car
import com.exam.guidomia.R
import com.exam.guidomia.databinding.CardCarBinding
import com.exam.guidomia.util.DEFAULT_FILTER_MAKE
import com.exam.guidomia.util.DEFAULT_FILTER_MODEL
import com.exam.guidomia.util.NO_CONS_ADDED
import com.exam.guidomia.util.NO_PROS_ADDED
import com.exam.guidomia.util.shortenPrice


class CarListAdapter(private var cars: ArrayList<Car>, val listUpdate: ListUpdate) :
    RecyclerView.Adapter<CarListAdapter.CarViewHolder>() {

    // keep a copy of the cars data to be used in filter
    var carsCopy = arrayListOf<Car>()

    inner class CarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

        @RequiresApi(Build.VERSION_CODES.R)
        @SuppressLint("SetTextI18n", "DiscouragedApi")
        fun bind(car: Car, position: Int, expand: (position: Int) -> Unit) {
            val context = binding.root.context
            val uri = "@drawable/image${car.id}"
            val imageResource: Int = context.resources.getIdentifier(uri, null, context.packageName)
            val carImage: Drawable? = ContextCompat.getDrawable(context, imageResource)
            image.setImageDrawable(carImage)

            name.text = "${car.make} ${car.model}"

            if (car.expanded) {
                collapsibleView.visibility = View.VISIBLE
            } else {
                collapsibleView.visibility = View.GONE
            }

            price.text = "Price: ${shortenPrice(car.marketPrice.toLong())}"
            rating.rating = car.rating.toFloat()

            val allPros = arrayListOf<String>()
            val allCons = arrayListOf<String>()

            allPros.addAll(car.prosList)
            allCons.addAll(car.consList)

            if (car.prosList.none { it.isNotEmpty() }) {
                allPros.addAll(listOf(NO_PROS_ADDED))
            }
            if (car.consList.none { it.isNotEmpty() }) {
                allCons.addAll(listOf(NO_CONS_ADDED))
            }

            layoutPros.removeAllViews()
            allPros.filter { it.isNotEmpty() }.forEach {
                val inflater: LayoutInflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val itemLayout = if(it.contentEquals(NO_PROS_ADDED)) {
                    R.layout.layout_empty_bullet_point
                } else {
                    R.layout.layout_bullet_point_text
                }
                val layoutBullet = inflater.inflate(itemLayout, null)
                val textView = layoutBullet.findViewById<TextView>(R.id.textBullet)
                textView.text = it
                layoutPros.addView(layoutBullet)
            }

            layoutCons.removeAllViews()
            allCons.filter { it.isNotEmpty() }.forEach {
                val inflater: LayoutInflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val itemLayout = if(it.contentEquals(NO_CONS_ADDED)) {
                    R.layout.layout_empty_bullet_point
                } else {
                    R.layout.layout_bullet_point_text
                }
                val layoutBullet =  inflater.inflate(itemLayout, null)
                val textView = layoutBullet.findViewById<TextView>(R.id.textBullet)
                textView.text = it
                layoutCons.addView(layoutBullet)
            }

            card.setOnClickListener {
                expand(position)
                if (collapsibleView.visibility == View.VISIBLE) {
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
            val query = constraint.toString().trim().lowercase()
            val filters = query.split("-")
            var filterMake = filters[0]
            var filterModel = filters[1]

            if (filterMake.lowercase().contains(DEFAULT_FILTER_MAKE.lowercase())) {
                filterMake = ""
            }

            if (filterModel.lowercase().contains(DEFAULT_FILTER_MODEL.lowercase())) {
                filterModel = ""
            }

            if (constraint.isNullOrEmpty()) {
                cars.clear()
                cars.addAll(carsCopy) // copy original list
                filteredList.addAll(cars)
            } else {
                if (filterMake.isEmpty() && filterModel.isEmpty()) {
                    filteredList.addAll(carsCopy)
                } else {
                    carsCopy.forEach { car ->
                        if (filterMake.isEmpty() && filterModel.isNotEmpty()) {
                            if (car.model.lowercase().contains(filterModel.lowercase())) {
                                filteredList.add(car)
                            }
                        } else if (filterModel.isEmpty() && filterMake.isNotEmpty()) {
                            if (car.make.lowercase().contains(filterMake.lowercase())) {
                                filteredList.add(car)
                            }
                        } else {
                            if (car.model.lowercase().contains(filterModel.lowercase()) && car.make.lowercase()
                                    .contains(filterMake.lowercase())
                            ) {
                                filteredList.add(car)
                            }
                        }
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results != null && (results.values as ArrayList<Car>).size > 0) {
                cars.clear()
                cars.addAll(results.values as MutableList<Car>)
                listUpdate.onUpdateList(false)
            } else {
                cars.clear()
                listUpdate.onUpdateList(true)
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
        listUpdate.onUpdateList(false)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder =
        CarViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_car, parent, false)
        )

    override fun getItemCount(): Int = cars.size

    @RequiresApi(Build.VERSION_CODES.R)
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