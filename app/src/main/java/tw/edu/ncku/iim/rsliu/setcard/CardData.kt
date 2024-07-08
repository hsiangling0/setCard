package tw.edu.ncku.iim.rsliu.setcard

import android.graphics.Color


class CardData(val color: Int=Color.RED, val number:Int=1, val shape:SetCardView.Shape=SetCardView.Shape.OVAL, val shading:SetCardView.Shading=SetCardView.Shading.EMPTY,var cardselected:Boolean=false) {
}