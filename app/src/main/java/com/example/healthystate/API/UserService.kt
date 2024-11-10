package com.example.healthystate.API

import com.example.healthystate.AdaptersLists.*
import com.example.healthystate.Models.*
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @POST("LogPasses")
    fun createUser(@Body requestModel: RequestModel):Call<DefaultResponse>

    @POST("PersonalDiaries")
    fun createPersonalDiaries(@Body diaryModel: DiaryModel):Call<DefaultResponse>

    @POST("Foods")
    fun createFood(@Body foodModel: FoodModel):Call<DefaultResponse>

    @POST("TrainingNames")
    fun createTrainingName(@Body trainingNameModel: TrainingNameModel):Call<DefaultResponse>

    @POST("ToDoLists")
    fun createToDoLists(@Body toDoListsModel: ToDoListsModel):Call<DefaultResponse>

    @POST("Workouts")
    fun createWorkout(@Body trainingModel: TrainingModel):Call<DefaultResponse>

    @POST("Pills")
    fun createPill(@Body pillModel: PillModel):Call<DefaultResponse>

    @GET("LogPasses")
    fun getUsers() : Call<List<User>>

    @GET("Moods")
    fun getMood() : Call<MutableList<Mood>>

    @GET("Workouts")
    fun getWorkouts() : Call<MutableList<TrainingList>>

    @GET("TrainingNames")
    fun getWorkoutNames() : Call<List<WorkoutNameList>>

    @GET("DoctorTests")
    fun getDoctorTests() : Call<MutableList<CheckUpList>>

    @GET("Pills")
    fun getPills() : Call<MutableList<PillList>>

    @GET("Eatings")
    fun getEatings() : Call<List<EatingList>>

    @GET("Foods")
    fun getFood() : Call<ArrayList<FoodList>>

    @GET("PersonalDiaries")
    fun getPersonalDiaries() : Call<ArrayList<DiaryList>>

    @GET("ToDoLists")
    fun getToDoLists() : Call<ArrayList<ToDoListList>>

    @PUT("Workouts/{id}")
    fun changeWorkout(@Path("id") id:Int, @Body changeTrainingModel: ChangeTrainingModel):Call<DefaultResponse>

    @DELETE("Workouts/{id}")
    fun deleteWorkout(@Path("id") id:Int):Call<DefaultResponse>

    @PUT("Pills/{id}")
    fun changePills(@Path("id") id:Int, @Body changePillModel: ChangePillModel):Call<DefaultResponse>

    @DELETE("Pills/{id}")
    fun deletePills(@Path("id") id:Int):Call<DefaultResponse>

    @PUT("Foods/{id}")
    fun changeFoods(@Path("id") id:Int, @Body changeFoodModel: ChangeFoodModel):Call<DefaultResponse>

    @DELETE("Foods/{id}")
    fun deleteFoods(@Path("id") id:Int):Call<DefaultResponse>

    @DELETE("ToDoLists/{id}")
    fun deleteToDoLists(@Path("id") id:Int):Call<DefaultResponse>

    @PUT("PersonalDiaries/{id}")
    fun changePersonalDiaries(@Path("id") id:Int, @Body changeDiaryModel: ChangeDiaryModel):Call<DefaultResponse>

    @DELETE("PersonalDiaries/{id}")
    fun deletePersonalDiaries(@Path("id") id:Int):Call<DefaultResponse>

    @PUT("LogPasses/{id}")
    fun changeUser(@Path("id") id:Int, @Body userModel: UserModel):Call<DefaultResponse>

    @DELETE("LogPasses/{id}")
    fun deleteUser(@Path("id") id:Int):Call<DefaultResponse>

    @DELETE("TrainingNames/{id}")
    fun deleteTrainingName(@Path("id") id:Int):Call<DefaultResponse>

    @GET("Waters")
    fun getWaters() : Call<List<WaterList>>

    @POST("Waters")
    fun createWaters(@Body waterModel: WaterModel):Call<DefaultResponse>

    @PUT("Waters/{id}")
    fun changeWaters(@Path("id") id:Int, @Body changeWaterModel: ChangeWaterModel):Call<DefaultResponse>


    @GET("DoctorTestViews")
    fun getCheckUpView() : Call<List<CheckUpViewList>>

    @PUT("DoctorTestViews/{id}")
    fun updateDoctorTestView(@Path("id") id:Int, @Body changeCheckUpModel: ChangeCheckUpModel):Call<DefaultResponse>

    @POST("DoctorTestViews")
    fun createDoctorTestViews(@Body checkUpViewModel: CheckUpViewModel):Call<DefaultResponse>

    @GET("Pills")
    fun getPillsByDateRange(@Query("startDate") startDate: String, @Query("endDate") endDate: String): Call<List<PillList>>

    @PUT("ToDoLists/{id}")
    fun updateToDoLists(@Path("id") id:Int, @Body changeToDoListModel: ChangeToDoListModel):Call<DefaultResponse>
}