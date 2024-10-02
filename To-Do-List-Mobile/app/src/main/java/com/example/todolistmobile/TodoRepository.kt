import android.content.Context
import android.util.Log
import com.example.todolistmobile.ApiRequests
import com.example.todolistmobile.Task
import com.google.gson.Gson
import java.io.File
import java.io.FileWriter
import java.io.IOException

class TodoRepository(private val context: Context) {

    private val gson = Gson()






    // Сохранение списка дел в файл с именем, которое вводит пользователь
    fun saveTodoList(todoList: List<Task>, fileName: String) {
        val jsonString = gson.toJson(todoList)
        try {
            val file = File(context.filesDir, "$fileName.json")
            val writer = FileWriter(file)
            writer.write(jsonString)
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }



    // Загрузка списка дел из выбранного файла
    fun loadTodoList(fileName: String): ArrayList<Task> {
        val file = File(context.filesDir, "$fileName.json")
        return if (file.exists()) {
            val jsonString = file.readText()
            val todoArray = gson.fromJson(jsonString, Array<Task>::class.java)
            ArrayList(todoArray.toList())
        } else {
            ArrayList()
        }
    }

    // Получение списка доступных файлов в директории
    fun getAvailableFiles(): List<String> {
        val files = context.filesDir.listFiles()
        return files?.filter { it.isFile && it.extension == "json" }?.map { it.nameWithoutExtension } ?: emptyList()
    }
}