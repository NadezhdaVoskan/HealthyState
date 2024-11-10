package com.example.healthystate

import com.example.healthystate.Models.User
import io.paperdb.Paper

class PaperDbClass {

    fun getUser(): User? {
        return Paper.book("user").read("current_user") as? User
    }

    fun setID(ID: Int) {
        Paper.book("position").write("ID", ID)
    }

    fun getID(): Int {
        return Paper.book("position").read("ID")!!
    }


    fun saveUser(user: User) {
        Paper.book("user").write("current_user", user)
    }

    fun removeUser() {
        Paper.book("user").delete("current_user")
    }

}