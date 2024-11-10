package com.example.healthystate.Pages

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.AdaptersLists.DiaryAdapter
import com.example.healthystate.AdaptersLists.DiaryList
import com.example.healthystate.AdaptersLists.TrainingAdapter
import com.example.healthystate.AdaptersLists.TrainingList
import com.example.healthystate.AddInfo.AddDiaryTextDialog
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PersonalDiary : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_personal_diary)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        val dateNow = Calendar.getInstance().time
        val sdf = SimpleDateFormat("yyyy.MM.dd")
        val formatedDate = sdf.format(dateNow)


        var recyclerView: RecyclerView? = null
        recyclerView = findViewById(R.id.recycler_view_diary)

        val paperDbClass = PaperDbClass()

        val retrofit = APIClient.buildService(UserService::class.java)
        val getDiary = retrofit.getPersonalDiaries()

        getDiary.enqueue(object : Callback<ArrayList<DiaryList>> {
            override fun onResponse(call: Call<ArrayList<DiaryList>>, response: Response<ArrayList<DiaryList>>) {
                if (response.isSuccessful) {
                    val listdiary: ArrayList<DiaryList> = response.body()!!
                    recyclerView?.apply {
                        layoutManager = LinearLayoutManager(this@PersonalDiary)
                        val diarylist: List<DiaryList> = listdiary.filter { it -> it.logPassId == paperDbClass.getUser()!!.idLogPass }
                        adapter = DiaryAdapter(diarylist as ArrayList<DiaryList>)
                        recyclerView?.setLayoutManager(LinearLayoutManager(this@PersonalDiary))
                        recyclerView?.setHasFixedSize(true)
                        recyclerView?.addItemDecoration(
                            DividerItemDecoration(this@PersonalDiary,
                                DividerItemDecoration.VERTICAL)
                        )
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<DiaryList>>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Ошибка со стороны клиента",
                    Toast.LENGTH_LONG
                ).show()
            }
        })




        val addButton = findViewById<Button>(R.id.addButDiary)
        addButton.setOnClickListener{
            val intent: Intent = Intent(this, AddDiaryTextDialog::class.java)
            startActivity(intent)
        }
    }
}