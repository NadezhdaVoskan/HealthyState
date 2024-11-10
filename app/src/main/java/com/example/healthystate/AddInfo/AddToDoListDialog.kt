package com.example.healthystate.AddInfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.Models.DefaultResponse
import com.example.healthystate.Models.ToDoListsModel
import com.example.healthystate.Models.TrainingModel
import com.example.healthystate.Pages.ToDoList
import com.example.healthystate.Pages.Training
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddToDoListDialog : DialogFragment() {

    lateinit var txtToDo: EditText
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.roundcornerdialog);
        val view: View = inflater!!.inflate(R.layout.addtodolist_window, container, false)

        txtToDo = view.findViewById(R.id.Name_ToDo)

        val retrofit = APIClient.buildService(UserService::class.java)

        val paperDbClass = PaperDbClass()

        val addBtn = view.findViewById<Button>(R.id.AddButWorkout)
        addBtn.setOnClickListener{

            val obj = ToDoListsModel(txtToDo.text.toString().trim(), paperDbClass.getUser()!!.idLogPass, false)

            if (txtToDo.text.toString().equals(""))
            {
                txtToDo?.requestFocus()
                Toast.makeText(activity?.applicationContext, "Введите задачу!", Toast.LENGTH_SHORT).show()
            }
            else {
            retrofit.createToDoLists(obj)
                .enqueue(object: Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(activity?.applicationContext, "Mistake. Try again!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        Toast.makeText(activity?.applicationContext, "Задача добавлена!", Toast.LENGTH_SHORT).show()
                        val intent: Intent =
                            Intent(activity?.applicationContext, ToDoList::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        activity?.startActivity(intent)
                        activity?.overridePendingTransition(0, 0)
                        activity?.finish()
                    }

                })
        }}


        return view;
    }
    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}