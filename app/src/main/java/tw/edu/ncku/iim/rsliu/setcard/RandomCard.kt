package tw.edu.ncku.iim.rsliu.setcard

import android.graphics.Color
import android.util.Log
import android.widget.Toast

class RandomCard {
    var initialCard=mutableListOf<CardData>()
    val cards=mutableListOf<CardData>()
    var index=0
    val sumNum=81
    val colorOption= listOf(Color.RED,Color.GREEN,Color.rgb(154, 18, 179))
    val numOption= listOf(1,2,3)
    var indexOfSelect=mutableListOf<Int>()
    var matchHistory=ArrayList<CardData>()

    fun createCardOrder():List<CardData>{
       for(shading in SetCardView.Shading.values()){
           for (shape in SetCardView.Shape.values()){
                for(num in numOption){
                    for(color in colorOption){
                        val temp=CardData(color,num,shape, shading,false)
                        initialCard.add(temp)
                    }
                }
           }
       }
        initialCard.shuffle()
        while(index<12){
            cards.add(initialCard[index])
            index+=1
        }
        checkifAdd()
        return cards
    }
    fun chooseCard(position:Int):Int{
        var result=0
        if(cards[position].cardselected){
            cards[position].cardselected=false
            indexOfSelect.remove(position)
            return 0
        }
        else{
            cards[position].cardselected=true
            indexOfSelect.add(position)
        }
        if(indexOfSelect.size==3){
            indexOfSelect.add(position)
            val matchSet=mutableListOf<CardData>()
            val matchingResult=checkMatch(cards[indexOfSelect[0]],cards[indexOfSelect[1]],cards[indexOfSelect[2]])
            for(i in 0..2){
                cards[indexOfSelect[i]].cardselected=false
                matchSet.add(cards[indexOfSelect[i]])
            }
            if(matchingResult){
                for(i in 0..2){
                    if(index<sumNum){
                        cards[indexOfSelect[i]]=initialCard[index]
                        index+=1
                    }
                    else cards.remove(matchSet[i])
                    matchHistory.add(matchSet[i])
                }
                if(cards.size==0){
                    result=2
                }
                if(!checkifAdd())result=3
            }
            else{
                result=1
            }
            indexOfSelect.clear()
        }
        return result
    }
    fun checkMatch(card1:CardData,card2:CardData,card3:CardData):Boolean{
        var colorMatch=false
        var numMatch=false
        var shapeMatch=false
        var shadingMatch=false
        if(card1.color==card2.color&& card2.color==card3.color)colorMatch=true
        else if(card1.color!=card2.color&&card2.color!=card3.color&&card1.color!=card3.color)colorMatch=true
        if(card1.number==card2.number&& card2.number==card3.number)numMatch=true
        else if(card1.number!=card2.number&&card2.number!=card3.number&&card1.number!=card3.number)numMatch=true
        if(card1.shape==card2.shape&& card2.shape==card3.shape)shapeMatch=true
        else if(card1.shape!=card2.shape&&card2.shape!=card3.shape&&card1.shape!=card3.shape)shapeMatch=true
        if(card1.shading==card2.shading&& card2.shading==card3.shading)shadingMatch=true
        else if(card1.shading!=card2.shading&&card2.shading!=card3.shading&&card1.shading!=card3.shading)shadingMatch=true
        if(colorMatch&&numMatch&&shapeMatch&&shadingMatch)return true
        else return false
    }
    fun getHistory():ArrayList<CardData>{
        return matchHistory
    }
    fun checkifAdd():Boolean{
        do {
            for(i in 0..(cards.size-3)){
                for(j in (i+1)..(cards.size-2))
                    for(k in (j+1)..(cards.size-1)){
                        if(checkMatch(cards[i],cards[j],cards[k])){
                            Log.i("testIndex",i.toString())
                            Log.i("testIndex",j.toString())
                            Log.i("testIndex",k.toString())
                            Log.i("testIndex","end")
                            return true
                        }

                    }
            }
            addThreeCard()
        }while((index+3)<=sumNum)
        return false
    }
    fun addThreeCard(){
        for(i in 1..3){
            if(index<sumNum){
                cards.add(initialCard[index])
                index+=1
            }
            else return
        }
    }
}