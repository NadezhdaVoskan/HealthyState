package com.example.healthystate.Pages

import android.app.Dialog
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.AdaptersLists.EatingList
import com.example.healthystate.AdaptersLists.TrainingList
import com.example.healthystate.AdaptersLists.WaterList
import com.example.healthystate.AddInfo.AddFoodDialog
import com.example.healthystate.AddInfo.AddWaterDialog
import com.example.healthystate.AddInfo.DelWaterDialog
import com.example.healthystate.AutoAndReg.LoginDialog
import com.example.healthystate.AutoAndReg.RegDialog
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendar
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class WaterBalance : AppCompatActivity() {


    var dialogAdd: Dialog? = null
    var dialogDel: Dialog? = null
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var currentDay = 0
    var tvDate: TextView? = null
    var tvDay: TextView? = null
    var singleRowCalendar: SingleRowCalendar? = null
    var progress: ProgressBar? = null
    var tvProgress: TextView? = null
    lateinit var tvNormal: TextView
    var normalWater: Int = 0
    var datemonth: Int = 0
    internal var status = 0
    private val handler = Handler()
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_water_balance)

        dialogAdd = Dialog(this@WaterBalance)
        dialogDel= Dialog(this@WaterBalance)

        val addButton = findViewById<Button>(R.id.btnAddWater)
        addButton.setOnClickListener{
            showAddDialog()
        }

        val delButton = findViewById<Button>(R.id.btnDelWater)
        delButton.setOnClickListener{
            showDelDialog()
        }

        tvNormal = findViewById(R.id.tvNormal)

        val paperDbClass = PaperDbClass()
        val progressBar: ProgressBar = findViewById(R.id.progress_bar)

        normalWater = paperDbClass.getUser()!!.weight!!*30
        tvNormal.text = "Ваша норма воды: " + (normalWater.toString())

        val retrofit = APIClient.buildService(UserService::class.java)
        val getWater: Call<List<WaterList>> = retrofit.getWaters()
        getWater.enqueue(object : Callback<List<WaterList>> {
            override fun onResponse(call: Call<List<WaterList>>, response: Response<List<WaterList>>) {
                if (response.isSuccessful) {
                    val allWater: List<WaterList>? = response.body()
                    val filteredWaterList = allWater?.filter { waterList ->
                        waterList.logPassId == paperDbClass.getUser()!!.idLogPass
                    }
                    val amountwater = filteredWaterList?.firstOrNull()?.amount ?: 0

                    val resources = resources
                    val drawable = resources.getDrawable(R.drawable.circle)
                    progressBar.max = normalWater
                    progressBar.progressDrawable = drawable
                    textView = findViewById(R.id.text_view_progress)
                    if (amountwater != 0) {
                        progressBar.progress = amountwater
                        textView.text = amountwater.toString()
                    } else {
                        progressBar.progress = 1
                        textView.text = "0"
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Недостоверные данные!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<WaterList>>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Ошибка со стороны клиента",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        tvDate = findViewById(R.id.tvDate)
        tvDay = findViewById(R.id.tvDay)

        singleRowCalendar = findViewById(R.id.main_single_row_calendar)

        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]
        currentDay = calendar[Calendar.DAY_OF_MONTH]

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        val myCalendarViewManager = object :
            CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {
                val cal = Calendar.getInstance()
                cal.time = date

                return if (isSelected)
                    when (cal[Calendar.DAY_OF_WEEK]) {
                        else -> R.layout.selected_calendar_item
                    }
                else
                // here we return items which are not selected
                    when (cal[Calendar.DAY_OF_WEEK]) {
                        else -> R.layout.calendar_item
                    }
            }


            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {

                holder.itemView.findViewById<TextView>(R.id.tv_day_calendar_item).text = DateUtils.getDay3LettersName(date)
                holder.itemView.findViewById<TextView>(R.id.tv_date_calendar_item).text = DateUtils.getDayNumber(date)
            }
        }

        // using calendar changes observer we can track changes in calendar
        val myCalendarChangesObserver = object :
            CalendarChangesObserver {
            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {

                val retrofit = APIClient.buildService(UserService::class.java)
                val getWater: Call<List<WaterList>> = retrofit.getWaters()
                getWater.enqueue(object : Callback<List<WaterList>> {
                    override fun onResponse(call: Call<List<WaterList>>, response: Response<List<WaterList>>) {
                        if (response.isSuccessful) {
                            val allWater: List<WaterList>? = response.body()
                            val filteredWaterList = allWater?.filter { waterList ->
                                val dayWater = waterList.dayWater.substring(0, 10)
                                dayWater == DateUtils.getYear(date) + "-" + DateUtils.getMonthNumber(date) + "-" + DateUtils.getDayNumber(date) && waterList.logPassId == paperDbClass.getUser()!!.idLogPass
                            }
                            val amountwater = filteredWaterList?.firstOrNull()?.amount ?: 0

                            val resources = resources
                            val drawable = resources.getDrawable(R.drawable.circle)
                            progressBar.max = normalWater
                            progressBar.progressDrawable = drawable
                            textView = findViewById(R.id.text_view_progress)
                            if (amountwater != 0) {
                                progressBar.progress = amountwater
                                textView.text = amountwater.toString()
                            } else {
                                progressBar.progress = 1
                                textView.text = "0"
                            }
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Недостоверные данные!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<List<WaterList>>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "Ошибка со стороны клиента",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
                tvDate?.text = "${DateUtils.getDayNumber(date)} ${DateUtils.getMonthName(date)}, "
                tvDay?.text = DateUtils.getDayName(date)
                super.whenSelectionChanged(isSelected, position, date)
            }


        }

        // selection manager is responsible for managing selection
        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                // set date to calendar according to position
                val cal = Calendar.getInstance()
                cal.time = date
                return when (cal[Calendar.DAY_OF_WEEK]) {
                    else -> true
                }
            }
        }

        // here we init our calendar, also you can set more properties if you haven't specified in XML layout
        val singleRowCalendar = singleRowCalendar?.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            initialPositionIndex = currentDay - 1
            setDates(getFutureDatesOfCurrentMonth())
            init()
            select(currentDay-1)
        }

        val btnRight = findViewById<Button>(R.id.btnRight)
        btnRight.setOnClickListener {
            singleRowCalendar?.setDates(getDatesOfNextMonth())
        }

        val btnLeft = findViewById<Button>(R.id.btnLeft)
        btnLeft.setOnClickListener {
            singleRowCalendar?.setDates(getDatesOfPreviousMonth())
        }
    }

    private fun getDatesOfNextMonth(): List<Date> {
        currentMonth++ // + because we want next month
        if (currentMonth == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 // 0 == january
        }
        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        currentMonth-- // - because we want previous month
        if (currentMonth == -1) {
            // we will switch to december of previous year, when we reach first month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            currentMonth = 11 // 11 == december
        }
        return getDates(mutableListOf())
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }


    private fun getDates(list: MutableList<Date>): List<Date> {
        // load dates of whole month
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }

    private fun showAddDialog() {
        AddWaterDialog().show(supportFragmentManager, "AddWaterDialog")

    }

    private fun showDelDialog() {
        DelWaterDialog().show(supportFragmentManager, "DelWaterDialog")
    }
}