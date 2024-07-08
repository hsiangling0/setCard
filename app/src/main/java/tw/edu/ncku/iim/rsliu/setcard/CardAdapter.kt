package tw.edu.ncku.iim.rsliu.setcard

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min

class CardAdapter(data:List<CardData>,var listener:RecyclerViewClickListener): RecyclerView.Adapter<CardAdapter.ViewHolder>()
{
    var cards = listOf<CardData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    interface RecyclerViewClickListener {
        fun onItemClick(position: Int)
    }
    class ViewHolder(val view: View):
        RecyclerView.ViewHolder(view){
        val cardView = view.findViewById<SetCardView>(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardWidth=parent.width/3
        val cardHeight=parent.height/2
        val cardSideLength=min(cardHeight,cardWidth)
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.new_card_item,parent,false)
        val layoutParams=view.findViewById<SetCardView>(R.id.cardView).layoutParams
        layoutParams.width=cardSideLength
        layoutParams.height=cardSideLength
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardView.color=cards[position].color
        holder.cardView.number=cards[position].number
        holder.cardView.shape=cards[position].shape
        holder.cardView.shading=cards[position].shading
        holder.cardView.cardselected=cards[position].cardselected
        holder.view.setOnClickListener{
            listener.onItemClick(position)
        }
    }
}

