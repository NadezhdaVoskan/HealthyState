package com.example.healthystate.Pages

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.AdaptersLists.PillAdapter
import com.example.healthystate.AdaptersLists.PillList
import com.example.healthystate.AdaptersLists.TrainingAdapter
import com.example.healthystate.AdaptersLists.TrainingList
import com.example.healthystate.AddInfo.AddPillDialog
import com.example.healthystate.AddInfo.AddTraningDialog
import com.example.healthystate.Navigations.NavigationForThemes
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
import java.text.SimpleDateFormat
import java.util.*

class Pill : AppCompatActivity() {

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var currentDay = 0
    var tvDate: TextView? = null
    var tvDay: TextView? = null
    var singleRowCalendar: SingleRowCalendar? = null
    var recyclerView: RecyclerView? = null
    var datemonth: Int = 0
    var pilllist: List<PillList>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_pill)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        val addButton = findViewById<Button>(R.id.btnAddPill)
        addButton.setOnClickListener{
            val intent: Intent = Intent(this, AddPillDialog::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recycler_view_pill)
        val gifView: LinearLayout? = findViewById(R.id.gifPill)


        val retrofit = APIClient.buildService(UserService::class.java)
        val getPill = retrofit.getPills()

        val paperDbClass = PaperDbClass()

        getPill.enqueue(object : Callback<MutableList<PillList>> {
            override fun onResponse(call: Call<MutableList<PillList>>, response: Response<MutableList<PillList>>) {
                val listpill: MutableList<PillList> = response.body()!!
                if (response.isSuccessful) {

                    recyclerView?.apply {
                        layoutManager = LinearLayoutManager(this@Pill)
                        pilllist =  listpill.filter { it -> it.logPassId == paperDbClass.getUser()!!.idLogPass}
                        adapter = PillAdapter(pilllist as MutableList<PillList>)
                    }

                    recyclerView?.setHasFixedSize(true)
                }
                if (pilllist?.isNotEmpty() == true) {
                    gifView?.visibility = View.INVISIBLE
                } else {
                    gifView?.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<MutableList<PillList>>, t: Throwable) {
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
                val getPill = retrofit.getPills()

                getPill.enqueue(object : Callback<MutableList<PillList>> {
                    override fun onResponse(call: Call<MutableList<PillList>>, response: Response<MutableList<PillList>>) {
                        if (response.isSuccessful) {
                            val listpill: MutableList<PillList> = response.body()!!
                            recyclerView?.apply {
                                layoutManager = LinearLayoutManager(this@Pill)

                                val calendarStart = Calendar.getInstance()
                                calendarStart.time = date
                                calendarStart.set(Calendar.HOUR_OF_DAY, 0)
                                calendarStart.set(Calendar.MINUTE, 0)
                                calendarStart.set(Calendar.SECOND, 0)
                                calendarStart.set(Calendar.MILLISECOND, 0)

                                val calendarEnd = Calendar.getInstance()
                                calendarEnd.time = date
                                calendarEnd.set(Calendar.HOUR_OF_DAY, 23)
                                calendarEnd.set(Calendar.MINUTE, 59)
                                calendarEnd.set(Calendar.SECOND, 59)
                                calendarEnd.set(Calendar.MILLISECOND, 999)

                                pilllist =  listpill.filter { it ->
                                    val dayPill = it.dayPill?.substringBeforeLast(".") ?: ""
                                    val dayPillEnd = it.dayPillEnd?.substringBeforeLast(".") ?: ""
                                    val calendarDayPill = Calendar.getInstance()
                                    calendarDayPill.timeInMillis = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).parse(dayPill).time
                                    val calendarDayPillEnd = Calendar.getInstance()
                                    calendarDayPillEnd.timeInMillis = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).parse(dayPillEnd).time
                                    (calendarDayPill <= calendarEnd && calendarDayPillEnd >= calendarStart && it.logPassId == paperDbClass.getUser()!!.idLogPass)
                                }
                                adapter = PillAdapter(pilllist as MutableList<PillList>)
                            }
                            recyclerView?.setHasFixedSize(true)
                            if (pilllist?.isNotEmpty() == true) {
                                gifView?.visibility = View.INVISIBLE
                            } else {
                                gifView?.visibility = View.VISIBLE
                            }
                        }
                    }

                    override fun onFailure(call: Call<MutableList<PillList>>, t: Throwable) {
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

}