package com.example.kidsenglishgame

import android.content.Context

data class GameItem(val word: String, val drawableName: String) {
    fun drawableResId(context: Context): Int {
        return context.resources.getIdentifier(drawableName, "drawable", context.packageName)
    }
}

object ItemBank {

    // 60 items: animals + colors + objects + food
    val items: List<GameItem> = listOf(
        // Animals (20)
        GameItem("cow","img_cow"),
        GameItem("dog","img_dog"),
        GameItem("cat","img_cat"),
        GameItem("horse","img_horse"),
        GameItem("lion","img_lion"),
        GameItem("tiger","img_tiger"),
        GameItem("bear","img_bear"),
        GameItem("rabbit","img_rabbit"),
        GameItem("elephant","img_elephant"),
        GameItem("giraffe","img_giraffe"),
        GameItem("monkey","img_monkey"),
        GameItem("zebra","img_zebra"),
        GameItem("penguin","img_penguin"),
        GameItem("fish","img_fish"),
        GameItem("frog","img_frog"),
        GameItem("bird","img_bird"),
        GameItem("duck","img_duck"),
        GameItem("sheep","img_sheep"),
        GameItem("goat","img_goat"),
        GameItem("chicken","img_chicken"),

        // Colors (10)
        GameItem("red","img_red"),
        GameItem("blue","img_blue"),
        GameItem("green","img_green"),
        GameItem("yellow","img_yellow"),
        GameItem("orange","img_orange"),
        GameItem("purple","img_purple"),
        GameItem("pink","img_pink"),
        GameItem("black","img_black"),
        GameItem("white","img_white"),
        GameItem("brown","img_brown"),

        // Objects (15)
        GameItem("house","img_house"),
        GameItem("tree","img_tree"),
        GameItem("ball","img_ball"),
        GameItem("book","img_book"),
        GameItem("chair","img_chair"),
        GameItem("table","img_table"),
        GameItem("bed","img_bed"),
        GameItem("door","img_door"),
        GameItem("window","img_window"),
        GameItem("car","img_car"),
        GameItem("bus","img_bus"),
        GameItem("phone","img_phone"),
        GameItem("clock","img_clock"),
        GameItem("star","img_star"),
        GameItem("sun","img_sun"),

        // Food (15)
        GameItem("apple","img_apple"),
        GameItem("banana","img_banana"),
        GameItem("orange","img_orange_fruit"),
        GameItem("grapes","img_grapes"),
        GameItem("bread","img_bread"),
        GameItem("milk","img_milk"),
        GameItem("water","img_water"),
        GameItem("egg","img_egg"),
        GameItem("cheese","img_cheese"),
        GameItem("pizza","img_pizza"),
        GameItem("rice","img_rice"),
        GameItem("cake","img_cake"),
        GameItem("ice cream","img_icecream"),
        GameItem("cookie","img_cookie"),
        GameItem("carrot","img_carrot")
    )

    // Prize per score (0..10)
    val prizes: Map<Int, GameItem> = mapOf(
        0 to GameItem("prize","prize_0_walk"),
        1 to GameItem("prize","prize_1_scooter"),
        2 to GameItem("prize","prize_2_bicycle"),
        3 to GameItem("prize","prize_3_skateboard"),
        4 to GameItem("prize","prize_4_motorcycle"),
        5 to GameItem("prize","prize_5_car"),
        6 to GameItem("prize","prize_6_truck"),
        7 to GameItem("prize","prize_7_bus"),
        8 to GameItem("prize","prize_8_train"),
        9 to GameItem("prize","prize_9_ship"),
        10 to GameItem("prize","prize_10_airplane")
    )
}
