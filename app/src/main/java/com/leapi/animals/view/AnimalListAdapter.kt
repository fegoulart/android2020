package com.leapi.animals.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.leapi.animals.R
import com.leapi.animals.databinding.ItemAnimalBinding
import com.leapi.animals.model.Animal
import com.leapi.animals.util.getProgressDrawable
import com.leapi.animals.util.loadImage
import kotlinx.android.synthetic.main.item_animal.view.*

//Adapter transform raw data to data to the screen
class AnimalListAdapter(private val animalList: ArrayList<Animal>) :
    RecyclerView.Adapter<AnimalListAdapter.AnimalViewHolder>(), AnimalClickListener {

    fun updateAnimalList(newAnimalList: List<Animal>) {
        animalList.clear()
        animalList.addAll(newAnimalList)
        notifyDataSetChanged() // informs system to reload all
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.item_animal, parent, false)
        val view = DataBindingUtil.inflate<ItemAnimalBinding>(
            inflater,
            R.layout.item_animal,
            parent,
            false
        )
        return AnimalViewHolder(view)
    }


    override fun getItemCount() = animalList.size

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        // holder.view.animalName.text = animalList[position].name
        //holder.view.animalImage.loadImage(animalList[position].imageUrl, getProgressDrawable(holder.view.context))
        holder.view.animal = animalList[position]
//        holder.view.animalLayout.setOnClickListener {
//            val action = ListFragmentDirections.actionDetail(animalList[position])
//            Navigation.findNavController(holder.view).navigate(action)
//        }
        holder.view.listener = this
    }

    //class AnimalViewHolder(var view: View): RecyclerView.ViewHolder(view)
    class AnimalViewHolder(var view: ItemAnimalBinding) : RecyclerView.ViewHolder(view.root)

    override fun onClick(v: View) {
        for (animal in animalList) {
            if (v.tag == animal.name) {
                val action = ListFragmentDirections.actionDetail(animal)
                Navigation.findNavController(v).navigate(action)
            }
        }
    }
}