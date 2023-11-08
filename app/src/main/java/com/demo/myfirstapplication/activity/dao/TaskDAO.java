//package com.demo.myfirstapplication.activity.dao;
//
//import androidx.room.Dao;
//import androidx.room.Insert;
//import androidx.room.Query;
//
//import com.demo.myfirstapplication.activity.enums.TaskState;
//import com.demo.myfirstapplication.activity.models.Task;
//
//import java.util.List;
//
//@Dao
//public interface TaskDAO {
//    @Insert
//    void insertTask(Task task);
//    @Query("Select * From Task where id = :id" )
//    Task findById(long id);
//    @Query("Select * From Task where title = :title" )
//    Task findByTaskTitle(String title);
//    @Query("Select * From Task where state = :taskState" )
//   List<Task> findTaskByState(TaskState taskState);
//    @Query("Select * From Task" )
//    List<Task> findAll();
//    @Query("Update Task set state = :taskState Where id=:taskId")
//    void updateTaskState(TaskState taskState,long taskId);
//    @Query("Delete From Task where id = :id" )
//    void deleteById(long id);
//}
