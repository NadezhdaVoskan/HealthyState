package com.example.healthystate.AddInfo

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.AdaptersLists.CheckUpList
import com.example.healthystate.AdaptersLists.MoodAdapter
import com.example.healthystate.Models.DefaultResponse
import com.example.healthystate.Models.DiaryModel
import com.example.healthystate.Models.Mood
import com.example.healthystate.Pages.PersonalDiary
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*


class AddDiaryTextDialog : AppCompatActivity() {

    var cardview: CardView? = null
    lateinit var namenote: EditText
    lateinit var txtDate: TextView
    lateinit var note: EditText
    var recyclerView: RecyclerView? = null
    var paperDbClass: PaperDbClass? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_text)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
        recyclerView = findViewById(R.id.recyclerviewmood)
        namenote = findViewById(R.id.group_name)
        note = findViewById(R.id.notes)
        txtDate = findViewById(R.id.group_date)


        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        txtDate.text = currentDate

        val retrofit = APIClient.buildService(UserService::class.java)
        var paperDbClass = PaperDbClass()
        val getMood = retrofit.getMood()

        getMood.enqueue(object : Callback<MutableList<Mood>> {
            override fun onResponse(call: Call<MutableList<Mood>>, response: Response<MutableList<Mood>>) {
                if (response.isSuccessful) {
                    val horizontalLayoutManagaer =
                        LinearLayoutManager(this@AddDiaryTextDialog, LinearLayoutManager.HORIZONTAL, false)
                    recyclerView?.apply {
                        layoutManager = LinearLayoutManager(this@AddDiaryTextDialog)
                        adapter = MoodAdapter(response.body()!!)

                    }
                    recyclerView?.setLayoutManager(horizontalLayoutManagaer)
                    recyclerView?.setHasFixedSize(true)
                }
            }

            override fun onFailure(call: Call<MutableList<Mood>>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Ошибка со стороны клиента",
                    Toast.LENGTH_LONG
                ).show()
            }
        })


        val addBtn = findViewById<Button>(R.id.btnAddDiary)
        addBtn.setOnClickListener{

            val obj = DiaryModel(namenote.text.toString().trim(), note.text.toString().trim(), currentDate, paperDbClass.getID(), paperDbClass.getUser()!!.idLogPass)

            if (namenote.text.toString().equals(""))
            {
                namenote?.requestFocus()
                Toast.makeText(applicationContext, "Введите заголовок!", Toast.LENGTH_SHORT).show()
            }
            else if (note.text.toString().equals(""))
            {
                note?.requestFocus()
                Toast.makeText(applicationContext, "Введите текст!", Toast.LENGTH_SHORT).show()
            }
            else {
            retrofit.createPersonalDiaries(obj)
                .enqueue(object: Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Mistake. Try again!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        Toast.makeText(applicationContext, "Запись добавлена!" + paperDbClass.getID(), Toast.LENGTH_SHORT).show()
                        val intent: Intent =
                            Intent(applicationContext, PersonalDiary::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        finish()
                    }

                })
        }}
    }


}