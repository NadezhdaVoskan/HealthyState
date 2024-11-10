package com.example.healthystate.ChangeInfo

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.AdaptersLists.MoodAdapter
import com.example.healthystate.Models.ChangeDiaryModel
import com.example.healthystate.Models.DefaultResponse
import com.example.healthystate.Models.Mood
import com.example.healthystate.Pages.PersonalDiary
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangeDiaryTextDialog : AppCompatActivity() {
    var cardview: CardView? = null
    lateinit var namenote: EditText
    lateinit var txtDate: TextView
    lateinit var note: EditText
    var recyclerView: RecyclerView? = null
    var paperDbClass = PaperDbClass()

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_diary_text)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
        recyclerView = findViewById(R.id.recyclerviewmood)
        namenote = findViewById(R.id.group_name)
        note = findViewById(R.id.notes)
        txtDate = findViewById(R.id.group_date)

        val intent = getIntent()
        val idPersonalDiary = intent.getIntExtra("idPersonalDiary", 0)
        val nameNotes = intent.getStringExtra("nameNotes")
        val notes = intent.getStringExtra("notes")
        val dayPersonalDiary = intent.getStringExtra("dayPersonalDiary")
        val moodId= intent.getIntExtra("moodId", 0)
        val logPassId = intent.getIntExtra("logPassId", 0)

        namenote.setText(nameNotes)
        note.setText(notes)
        txtDate.setText(dayPersonalDiary?.dropLast(9))




        val retrofit = APIClient.buildService(UserService::class.java)

        val getMood = retrofit.getMood()

        getMood.enqueue(object : Callback<MutableList<Mood>> {
            override fun onResponse(call: Call<MutableList<Mood>>, response: Response<MutableList<Mood>>) {
                if (response.isSuccessful) {
                    val horizontalLayoutManagaer =
                        LinearLayoutManager(this@ChangeDiaryTextDialog, LinearLayoutManager.HORIZONTAL, false)
                    recyclerView?.apply {
                        layoutManager = LinearLayoutManager(this@ChangeDiaryTextDialog)
                        adapter = MoodAdapter(response.body()!!)
                    }

                    var adapter = MoodAdapter(response.body()!!)
                    val recyclerView: RecyclerView = findViewById(R.id.recyclerviewmood)
                    recyclerView.adapter = adapter
                    adapter.setSelectedItemPosition(moodId-1)
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

        val delItem = findViewById<Button>(R.id.DelButDiary)
        delItem.setOnClickListener{
            retrofit.deletePersonalDiaries(idPersonalDiary)
                .enqueue(object: Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Mistake. Try again!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        val intent: Intent =
                            Intent(applicationContext, PersonalDiary::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    }

                })
        }


        val updateItem = findViewById<Button>(R.id.ChangeButDiary)
        updateItem.setOnClickListener{
            val obj = ChangeDiaryModel(idPersonalDiary, namenote.text.toString().trim(), note.text.toString().trim(), txtDate.text.toString().trim(), paperDbClass.getID(), paperDbClass.getUser()!!.idLogPass)

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
            retrofit.changePersonalDiaries(idPersonalDiary, obj)
                .enqueue(object: Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Mistake. Try again!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        val intent: Intent =
                            Intent(applicationContext, PersonalDiary::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    }

                })
        }}

    }
}