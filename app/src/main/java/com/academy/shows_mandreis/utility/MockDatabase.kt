package com.academy.shows_mandreis.utility

import android.util.Log
import com.academy.shows_mandreis.R
import com.academy.shows_mandreis.model.Show

object MockDatabase {

    private var shows: List<Show> = listOf(
        Show("1", "How I Met Your Mother", "A man tells his kids about all the women he banged before meeting their mother.", R.drawable.how_i_met_your_mother),
        Show("2", "Game of Thrones", "Random people in the winter fight for a chair.", R.drawable.game_of_thrones),
        Show("3", "Grey's Anatomy", "For every patient saved, two doctors will die.", R.drawable.greys_anatomy),
        Show("4", "Mad Men", "This guy named Dick changes his name to Don and gets rich selling cigarettes and Utz potato chips during the 1960s.", R.drawable.mad_men),
        Show("5", "Seinfeld", "It's a show about nothing.", R.drawable.seinfeld),
        Show("6", "Breaking Bad", "Unable to stop himself going bald, a man decides to change his career path.", R.drawable.breaking_bad),
        Show("7", "Doctor Who", "A lonely man picks up young women on his travels around the world.", R.drawable.doctor_who),
        Show("8", "The Walking Dead", "A man leads a group of people to rebel against the new diet trend.", R.drawable.walking_dead)
    )

    fun getShows(): List<Show> {
        return shows
    }

    fun getShowById(ID: String): Show? {
        for (show in shows) {
            if (show.ID == ID) return show
        }
        return null
    }

}