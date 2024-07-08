package tw.edu.ncku.iim.rsliu.setcard

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(),CardAdapter.RecyclerViewClickListener{
    override fun onItemClick(position: Int) {
        val result=randomfun.chooseCard(position)
        adapter.notifyDataSetChanged()
        if(result==1)Toast.makeText(this, "Matching Fail", Toast.LENGTH_LONG).show()
        else if(result==2)Toast.makeText(this, "You win the game!", Toast.LENGTH_LONG).show()
        else if(result==3)Toast.makeText(this, "You lose the game!", Toast.LENGTH_LONG).show()
    }
    val adapter by lazy{
        CardAdapter(listOf<CardData>(),this)
    }

    lateinit var randomfun:RandomCard
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        load_data()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.game_refresh->{
                load_data()
            }
            R.id.game_history->{
                create_history()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun load_data(){
        val recycleView=findViewById<RecyclerView>(R.id.recycleview)
        recycleView.adapter=adapter
        recycleView.layoutManager=GridLayoutManager(this,2)
        randomfun=RandomCard()
        GlobalScope.launch(Dispatchers.Main) {
            var cards=listOf<CardData>()
            withContext(Dispatchers.IO) {
                cards=randomfun.createCardOrder()
            }
            adapter.cards=cards
        }
    }
    fun create_history(){
        val intent = Intent(this, HistoryPreview::class.java)
        val historyData= Gson().toJson(randomfun.getHistory())
        intent.putExtra("history",historyData)
        startActivity(intent)
    }



}
