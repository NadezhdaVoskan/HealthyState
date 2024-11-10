package com.example.healthystate.Pages

import android.app.Dialog
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.AdaptersLists.ToDoListAdapter
import com.example.healthystate.AdaptersLists.ToDoListList
import com.example.healthystate.AdaptersLists.TrainingAdapter
import com.example.healthystate.AdaptersLists.TrainingList
import com.example.healthystate.AddInfo.AddToDoListDialog
import com.example.healthystate.AddInfo.AddTraningDialog
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ToDoList : AppCompatActivity() {

    var dialogAdd: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_to_do_list)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        var recyclerView: RecyclerView? = null
        recyclerView = findViewById(R.id.recycler_view_todo)


        val paperDbClass = PaperDbClass()

        val retrofit = APIClient.buildService(UserService::class.java)
        val getToDo = retrofit.getToDoLists()

        getToDo.enqueue(object : Callback<ArrayList<ToDoListList>> {
            override fun onResponse(call: Call<ArrayList<ToDoListList>>, response: Response<ArrayList<ToDoListList>>) {
                if (response.isSuccessful) {
                    val listtodo: ArrayList<ToDoListList> = response.body()!!
                    recyclerView?.apply {
                        layoutManager = LinearLayoutManager(this@ToDoList)
                        val todolist: List<ToDoListList> = listtodo.filter { it -> it.logPassId == paperDbClass.getUser()!!.idLogPass }
                        adapter = ToDoListAdapter(todolist as ArrayList<ToDoListList>)
                        recyclerView?.setLayoutManager(LinearLayoutManager(this@ToDoList))
                        recyclerView?.setHasFixedSize(true)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<ToDoListList>>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Ошибка со стороны клиента",
                    Toast.LENGTH_LONG
                ).show()
            }
        })



        dialogAdd = Dialog(this@ToDoList)

        val addButton = findViewById<Button>(R.id.addButToDoList)
        addButton.setOnClickListener{
            showAddDialog();
        }
    }

    private fun showAddDialog() {
        AddToDoListDialog().show(supportFragmentManager, "AddToDoListDialog")

    }
}