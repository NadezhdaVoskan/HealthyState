package com.example.healthystate.Pages

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.AdaptersLists.*
import com.example.healthystate.AddInfo.AddFoodDialog
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

class Food : AppCompatActivity() {

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var currentDay = 0
    var tvDate: TextView? = null
    var tvDay: TextView? = null
    var singleRowCalendar: SingleRowCalendar? = null
    private lateinit var arrow: ImageButton
    private lateinit var hiddenView: LinearLayout
    private lateinit var cardView: CardView
    private lateinit var arrow2: ImageButton
    private lateinit var hiddenView2: LinearLayout
    private lateinit var cardView2: CardView
    private lateinit var arrow3: ImageButton
    private lateinit var hiddenView3: LinearLayout
    private lateinit var cardView3: CardView
    private lateinit var arrow4: ImageButton
    private lateinit var hiddenView4: LinearLayout
    private lateinit var cardView4: CardView
    var datemonth: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_food)

        cardView = findViewById(R.id.base_cardview)
        arrow = findViewById(R.id.arrow_button)
        hiddenView = findViewById(R.id.hidden_view)

        arrow.setOnClickListener {
            if (hiddenView.visibility == View.VISIBLE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                }
                hiddenView.visibility = View.GONE
                arrow.setImageResource(R.drawable.baseline_expand_more_24)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                }
                hiddenView.visibility = View.VISIBLE
                arrow.setImageResource(R.drawable.baseline_expand_less_24)
            }
        }

        val paperDbClass = PaperDbClass()
        cardView2 = findViewById(R.id.base_cardview2)
        arrow2 = findViewById(R.id.arrow_button2)
        hiddenView2 = findViewById(R.id.hidden_view2)

        arrow2.setOnClickListener {
            if (hiddenView2.visibility == View.VISIBLE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(cardView2, AutoTransition())
                }
                hiddenView2.visibility = View.GONE
                arrow2.setImageResource(R.drawable.baseline_expand_more_24)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(cardView2, AutoTransition())
                }
                hiddenView2.visibility = View.VISIBLE
                arrow2.setImageResource(R.drawable.baseline_expand_less_24)
            }
        }

        cardView3 = findViewById(R.id.base_cardview3)
        arrow3 = findViewById(R.id.arrow_button3)
        hiddenView3 = findViewById(R.id.hidden_view3)

        arrow3.setOnClickListener {
            if (hiddenView3.visibility == View.VISIBLE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(cardView3, AutoTransition())
                }
                hiddenView3.visibility = View.GONE
                arrow3.setImageResource(R.drawable.baseline_expand_more_24)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(cardView3, AutoTransition())
                }
                hiddenView3.visibility = View.VISIBLE
                arrow3.setImageResource(R.drawable.baseline_expand_less_24)
            }
        }

        cardView4 = findViewById(R.id.base_cardview4)
        arrow4 = findViewById(R.id.arrow_button4)
        hiddenView4 = findViewById(R.id.hidden_view4)

        arrow4.setOnClickListener {
            if (hiddenView4.visibility == View.VISIBLE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(cardView4, AutoTransition())
                }
                hiddenView4.visibility = View.GONE
                arrow4.setImageResource(R.drawable.baseline_expand_more_24)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(cardView4, AutoTransition())
                }
                hiddenView4.visibility = View.VISIBLE
                arrow4.setImageResource(R.drawable.baseline_expand_less_24)
            }
        }

        var recyclerView: RecyclerView? = null
        var recyclerView2: RecyclerView? = null
        var recyclerView3: RecyclerView? = null
        var recyclerView4: RecyclerView? = null
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView2 = findViewById(R.id.recycler_view2)
        recyclerView3 = findViewById(R.id.recycler_view3)
        recyclerView4 = findViewById(R.id.recycler_view4)

        val retrofit = APIClient.buildService(UserService::class.java)
        val getFood = retrofit.getFood()

        getFood.enqueue(object : Callback<ArrayList<FoodList>> {
            override fun onResponse(call: Call<ArrayList<FoodList>>, response: Response<ArrayList<FoodList>>) {
                if (response.isSuccessful) {
                    val foodList: ArrayList<FoodList> = response.body()!!
                    recyclerView?.apply {
                        layoutManager = LinearLayoutManager(this@Food)
                        val checklist: List<FoodList> = foodList.filter { it -> it.eatingId == 1 && it.logPassId == paperDbClass.getUser()!!.idLogPass }
                        adapter = FoodAdapter(checklist as ArrayList<FoodList>)
                        recyclerView?.setLayoutManager(LinearLayoutManager(this@Food))
                        recyclerView?.setHasFixedSize(true)
                        recyclerView?.addItemDecoration(
                            DividerItemDecoration(this@Food,
                                DividerItemDecoration.VERTICAL)
                        )
                    }
                    recyclerView2?.apply {
                        layoutManager = LinearLayoutManager(this@Food)
                        val checklist: List<FoodList> = foodList.filter { it -> it.eatingId == 2 && it.logPassId == paperDbClass.getUser()!!.idLogPass}
                        adapter = FoodAdapter(checklist as ArrayList<FoodList>)
                        recyclerView2?.setLayoutManager(LinearLayoutManager(this@Food))
                        recyclerView2?.setHasFixedSize(true)
                        recyclerView2?.addItemDecoration(
                            DividerItemDecoration(this@Food,
                                DividerItemDecoration.VERTICAL)
                        )
                    }
                    recyclerView3?.apply {
                        layoutManager = LinearLayoutManager(this@Food)
                        val checklist: List<FoodList> = foodList.filter { it -> it.eatingId == 3 && it.logPassId == paperDbClass.getUser()!!.idLogPass}
                        adapter = FoodAdapter(checklist as ArrayList<FoodList>)
                        recyclerView3?.setLayoutManager(LinearLayoutManager(this@Food))
                        recyclerView3?.setHasFixedSize(true)
                        recyclerView3?.addItemDecoration(
                            DividerItemDecoration(this@Food,
                                DividerItemDecoration.VERTICAL)
                        )
                    }
                    recyclerView4?.apply {
                        layoutManager = LinearLayoutManager(this@Food)
                        val checklist: List<FoodList> = foodList.filter { it -> it.eatingId == 4 && it.logPassId == paperDbClass.getUser()!!.idLogPass}
                        adapter = FoodAdapter(checklist as ArrayList<FoodList>)
                        recyclerView4?.setLayoutManager(LinearLayoutManager(this@Food))
                        recyclerView4?.setHasFixedSize(true)
                        recyclerView4?.addItemDecoration(
                            DividerItemDecoration(this@Food,
                                DividerItemDecoration.VERTICAL)
                        )
                    }

                }
            }

            override fun onFailure(call: Call<ArrayList<FoodList>>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Ошибка со стороны клиента",
                    Toast.LENGTH_LONG
                ).show()
            }
        })



        val addButton = findViewById<Button>(R.id.btnAddFood)
        addButton.setOnClickListener{
            val intent: Intent = Intent(this, AddFoodDialog::class.java)
            startActivity(intent)
        }

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

        val myCalendarChangesObserver = object :
            CalendarChangesObserver {
            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {


                val retrofit = APIClient.buildService(UserService::class.java)
                val getFood = retrofit.getFood()

                getFood.enqueue(object : Callback<ArrayList<FoodList>> {
                    override fun onResponse(call: Call<ArrayList<FoodList>>, response: Response<ArrayList<FoodList>>) {
                        datemonth = DateUtils.getMonthNumber(date).toInt();
                        if (response.isSuccessful) {
                            val foodList: ArrayList<FoodList> = response.body()!!
                            recyclerView?.apply {
                                layoutManager = LinearLayoutManager(this@Food)
                                val checklist: List<FoodList> = foodList.filter { it -> it.eatingId == 1 &&
                                    it.dayFood.dropLast(9) == DateUtils.getYear(date)+"-"+DateUtils.getMonthNumber(date)+"-"+DateUtils.getDayNumber(date)  && it.logPassId == paperDbClass.getUser()!!.idLogPass}
                                adapter = FoodAdapter(checklist as ArrayList<FoodList>)
                                recyclerView?.setLayoutManager(LinearLayoutManager(this@Food))
                                recyclerView?.setHasFixedSize(true)
                                recyclerView?.addItemDecoration(
                                    DividerItemDecoration(this@Food,
                                        DividerItemDecoration.VERTICAL)
                                )
                            }
                            recyclerView2?.apply {
                                layoutManager = LinearLayoutManager(this@Food)
                                val checklist: List<FoodList> = foodList.filter { it -> it.eatingId == 2 &&
                                    it.dayFood.dropLast(9) == DateUtils.getYear(date)+"-"+DateUtils.getMonthNumber(date)+"-"+DateUtils.getDayNumber(date) && it.logPassId == paperDbClass.getUser()!!.idLogPass}
                                adapter = FoodAdapter(checklist as ArrayList<FoodList>)
                                recyclerView2?.setLayoutManager(LinearLayoutManager(this@Food))
                                recyclerView2?.setHasFixedSize(true)
                                recyclerView2?.addItemDecoration(
                                    DividerItemDecoration(this@Food,
                                        DividerItemDecoration.VERTICAL)
                                )
                            }
                            recyclerView3?.apply {
                                layoutManager = LinearLayoutManager(this@Food)
                                val checklist: List<FoodList> = foodList.filter { it -> it.eatingId == 3 &&
                                    it.dayFood.dropLast(9) == DateUtils.getYear(date)+"-"+DateUtils.getMonthNumber(date)+"-"+DateUtils.getDayNumber(date) && it.logPassId == paperDbClass.getUser()!!.idLogPass}
                                adapter = FoodAdapter(checklist as ArrayList<FoodList>)
                                recyclerView3?.setLayoutManager(LinearLayoutManager(this@Food))
                                recyclerView3?.setHasFixedSize(true)
                                recyclerView3?.addItemDecoration(
                                    DividerItemDecoration(this@Food,
                                        DividerItemDecoration.VERTICAL)
                                )
                            }
                            recyclerView4?.apply {
                                layoutManager = LinearLayoutManager(this@Food)
                                val checklist: List<FoodList> = foodList.filter { it -> it.eatingId == 4 &&
                                    it.dayFood.dropLast(9) == DateUtils.getYear(date)+"-"+DateUtils.getMonthNumber(date)+"-"+DateUtils.getDayNumber(date) && it.logPassId == paperDbClass.getUser()!!.idLogPass}
                                adapter = FoodAdapter(checklist as ArrayList<FoodList>)
                                recyclerView4?.setLayoutManager(LinearLayoutManager(this@Food))
                                recyclerView4?.setHasFixedSize(true)
                                recyclerView4?.addItemDecoration(
                                    DividerItemDecoration(this@Food,
                                        DividerItemDecoration.VERTICAL)
                                )
                            }

                        }
                    }

                    override fun onFailure(call: Call<ArrayList<FoodList>>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "Ошибка со стороны клиента",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
                super.whenSelectionChanged(isSelected, position, date)
                tvDate?.text = "${DateUtils.getDayNumber(date)} ${DateUtils.getMonthName(date)}, "
                tvDay?.text = DateUtils.getDayName(date)
            }


        }

        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                val cal = Calendar.getInstance()
                cal.time = date
                return when (cal[Calendar.DAY_OF_WEEK]) {
                    else -> true
                }
            }
        }

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