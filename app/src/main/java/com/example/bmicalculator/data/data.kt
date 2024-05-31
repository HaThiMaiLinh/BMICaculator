package com.example.bmicalculator.data

import com.example.bmicalculator.R
import com.example.bmicalculator.model.ItemSettingModel
import com.example.bmicalculator.model.LanguageModel
import com.example.bmicalculator.model.MothModel
import com.example.bmicalculator.model.TutorialModel
import com.example.bmicalculator.model.YearModel
import java.time.LocalDate

class data {

    companion object {
        private val currentYear = LocalDate.now().year
        val languagelist = arrayListOf<LanguageModel>(
            LanguageModel(R.drawable.english,"English","en",false),
            LanguageModel(R.drawable.spanish, "Spanish","es",false),
            LanguageModel(R.drawable.french,"French","fr",false),
            LanguageModel(R.drawable.hindi,"Hindi","hi",false),
            LanguageModel(R.drawable.portugeese,"Portugeese","pt",false),
            LanguageModel(R.drawable.german,"German","de",false),
            LanguageModel(R.drawable.indo,"Indonesian","io",false)
        )
        val tutoriallist = arrayListOf<TutorialModel>(
            TutorialModel(R.drawable.tutorial_1,R.string.app_name,R.string.des_tutorial1),
            TutorialModel(R.drawable.tutorial_2,R.string.title_tutorial2,R.string.des_tutorial2),
            TutorialModel(R.drawable.tutorial_3,R.string.title_tutorial3,R.string.des_tutorial3),
        )
        val itemsettinglist = arrayListOf<ItemSettingModel>(
            ItemSettingModel(R.drawable.ic_lang,R.string.tv_lang),
            ItemSettingModel(R.drawable.ic_share,R.string.tv_share),
            ItemSettingModel(R.drawable.ic_rate,R.string.tv_rate),
            ItemSettingModel(R.drawable.ic_version,R.string.tv_version),
            ItemSettingModel(R.drawable.ic_policy,R.string.tv_policy)
        )
        val filterlist = arrayListOf(
            MothModel(R.string.January,false),
            MothModel(R.string.February,false),
            MothModel(R.string.March,false),
            MothModel(R.string.April,false),
            MothModel(R.string.May,false),
            MothModel(R.string.June,false),
            MothModel(R.string.July,false),
            MothModel(R.string.August,false),
            MothModel(R.string.September,false),
            MothModel(R.string.October,false),
            MothModel(R.string.November,false),
            MothModel(R.string.December,false)
            )
        val filterYear = arrayListOf(
            YearModel((currentYear-3),false),
            YearModel((currentYear-2),false),
            YearModel((currentYear-1),false),
            YearModel((currentYear),false),
            YearModel((currentYear+1),false),
            YearModel((currentYear+2),false),
            YearModel((currentYear+3),false)
        )

    }
}