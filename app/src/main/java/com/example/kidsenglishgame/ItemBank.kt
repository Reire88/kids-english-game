package com.example.kidsenglishgame

import android.content.Context

data class GameItem(val word: String, val drawableName: String) {
    fun drawableResId(context: Context): Int {
        return context.resources.getIdentifier(drawableName, "drawable", context.packageName)
    }
}

object ItemBank {

    val items: List<GameItem> = listOf(
        GameItem("apple", "apple"),
        GameItem("banana", "banana"),
        GameItem("bear", "bear"),
        GameItem("bee", "bee"),
        GameItem("bread", "bread"),
        GameItem("butterfly", "butterfly"),
        GameItem("cat", "cat"),
        GameItem("chicken", "chicken"),
        GameItem("cow", "cow"),
        GameItem("dog", "dog"),

        GameItem("elephant", "elephant"),
        GameItem("fish", "fish"),
        GameItem("home", "home"),
        GameItem("honey", "honey"),
        GameItem("horse", "horse"),
        GameItem("lion", "lion"),
        GameItem("milk", "milk"),
        GameItem("monkey", "monkey"),
        GameItem("shark", "shark"),
        GameItem("squirrel", "squirrel"),

        GameItem("tomato", "tomato"),
        GameItem("watermelon", "watermelon"),
        GameItem("car", "car"),
        GameItem("table", "table"),
        GameItem("chair", "chair"),
        GameItem("clock", "clock"),
        GameItem("boat", "boat"),
        GameItem("zebra", "zebra"),
        GameItem("tree", "tree"),
        GameItem("book", "book"),

        GameItem("candy", "candy"),
        GameItem("red", "red"),
        GameItem("blue", "blue"),
        GameItem("green", "green"),
        GameItem("yellow", "yellow"),
        GameItem("purple", "purple"),
        GameItem("black", "black"),
        GameItem("white", "white"),
        GameItem("pink", "pink"),
        GameItem("orange", "orange"),

        GameItem("pig", "pig"),
        GameItem("bus", "bus"),
        GameItem("bike", "bike"),
        GameItem("moon", "moon"),
        GameItem("sun", "sun"),
        GameItem("star", "star"),
        GameItem("cloud", "cloud"),
        GameItem("truck", "truck"),
        GameItem("grapes", "grapes"),
        GameItem("rocket", "rocket")
    )

    val prizes: Map<Int, GameItem> = mapOf(
        0 to GameItem("prize", "prize_0_walk"),
        1 to GameItem("prize", "prize_1_scooter"),
        2 to GameItem("prize", "prize_2_bicycle"),
        3 to GameItem("prize", "prize_3_skateboard"),
        4 to GameItem("prize", "prize_4_motorcycle"),
        5 to GameItem("prize", "prize_5_car"),
        6 to GameItem("prize", "prize_6_truck"),
        7 to GameItem("prize", "prize_7_bus"),
        8 to GameItem("prize", "prize_8_train"),
        9 to GameItem("prize", "prize_9_ship"),
        10 to GameItem("prize", "prize_10_airplane")
    )
}
