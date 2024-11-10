package com.example.healthystate.Pages

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.AdaptersLists.CheckUpAdapter
import com.example.healthystate.AdaptersLists.CheckUpList
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckupDoctor : AppCompatActivity() {
    var recyclerViewOnceLife: RecyclerView? = null
    var recyclerViewTwoYear: RecyclerView? = null
    var recyclerViewYear: RecyclerView? = null
    var recyclerViewPolYear: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_checkup)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
        recyclerViewOnceLife = findViewById(R.id.recycler_view_OneLife)
        recyclerViewTwoYear = findViewById(R.id.recycler_view_TwoYear)
        recyclerViewYear = findViewById(R.id.recycler_view_Year)
        recyclerViewPolYear = findViewById(R.id.recycler_view_polYear)

        val paperDbClass = PaperDbClass()

        val retrofit = APIClient.buildService(UserService::class.java)
        val getDoctorTests = retrofit.getDoctorTests()

        getDoctorTests.enqueue(object : Callback<MutableList<CheckUpList>> {
            override fun onResponse(
                call: Call<MutableList<CheckUpList>>,
                response: Response<MutableList<CheckUpList>>
            ) {
                if (response.isSuccessful) {
                    val checkListDoc: MutableList<CheckUpList> = response.body()!!

                    recyclerViewOnceLife?.apply {
                        layoutManager = LinearLayoutManager(this@CheckupDoctor)
                        val checklist: List<CheckUpList> = checkListDoc.asSequence()
                            .filter { it.dayDoctorTestId == 1 }
                            .toList()
                        adapter = CheckUpAdapter(checklist as MutableList<CheckUpList>)
                        setLayoutManager(LinearLayoutManager(this@CheckupDoctor))
                        setHasFixedSize(true)
                        addItemDecoration(
                            DividerItemDecoration(
                                this@CheckupDoctor,
                                DividerItemDecoration.VERTICAL
                            )
                        )
                    }

                    recyclerViewTwoYear?.apply {
                        layoutManager = LinearLayoutManager(this@CheckupDoctor)
                        val checklist: List<CheckUpList> = checkListDoc.asSequence()
                            .filter { it.dayDoctorTestId == 2 }
                            .toList()
                        adapter = CheckUpAdapter(checklist as MutableList<CheckUpList>)
                        setLayoutManager(LinearLayoutManager(this@CheckupDoctor))
                        setHasFixedSize(true)
                        addItemDecoration(
                            DividerItemDecoration(
                                this@CheckupDoctor,
                                DividerItemDecoration.VERTICAL
                            )
                        )
                    }

                    recyclerViewYear?.apply {
                        layoutManager = LinearLayoutManager(this@CheckupDoctor)
                        val checklist: List<CheckUpList> = checkListDoc.asSequence()
                            .filter { it.dayDoctorTestId == 3 }
                            .toList()
                        adapter = CheckUpAdapter(checklist as MutableList<CheckUpList>)
                        setLayoutManager(LinearLayoutManager(this@CheckupDoctor))
                        setHasFixedSize(true)
                        addItemDecoration(
                            DividerItemDecoration(
                                this@CheckupDoctor,
                                DividerItemDecoration.VERTICAL
                            )
                        )
                    }

                    recyclerViewPolYear?.apply {
                        layoutManager = LinearLayoutManager(this@CheckupDoctor)
                        val checklist: List<CheckUpList> = checkListDoc.asSequence()
                            .filter { it.dayDoctorTestId == 4 }
                            .toList()
                        adapter = CheckUpAdapter(checklist as MutableList<CheckUpList>)
                        setLayoutManager(LinearLayoutManager(this@CheckupDoctor))
                        setHasFixedSize(true)
                        addItemDecoration(
                            DividerItemDecoration(
                                this@CheckupDoctor,
                                DividerItemDecoration.VERTICAL
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<CheckUpList>>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Ошибка со стороны клиента",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

}