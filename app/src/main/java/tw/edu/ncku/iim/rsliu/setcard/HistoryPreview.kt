package tw.edu.ncku.iim.rsliu.setcard

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class HistoryPreview : AppCompatActivity(),HistoryAdapter.RecyclerViewClickListener {
    var historyData=listOf<CardData>()
    val adapter by lazy{
        HistoryAdapter(listOf<CardData>(),this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        load_history()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        if(item.itemId==android.R.id.home){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    fun load_history(){
        val recycleView=findViewById<RecyclerView>(R.id.historyView)
        recycleView.adapter=adapter
        recycleView.layoutManager=GridLayoutManager(this,3)
        recycleView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//        adapter.cards=historyData
        val type = object : TypeToken<List<CardData?>?>() {}.type
        historyData = Gson().fromJson(intent.getStringExtra("history"), type)
        supportActionBar?.title="History"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        adapter.cards=historyData
//        Log.i("historyData", historyData[0].color.toString())
    }

    override fun onItemClick(position: Int) {
        Log.i("historyPosition",position.toString())
    }
}