//noinspection SuspiciousImport
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistmobile.R
import com.example.todolistmobile.Task
import com.example.todolistmobile.databinding.TaskItemBinding

class TaskAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    interface OnItemClickListener {
        fun onButtonClick(task: Task)
    }

    private var taskList = arrayListOf<Task>()

    class TaskHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TaskItemBinding.bind(itemView)

        fun bind(task:Task, listener: OnItemClickListener){
            binding.buttonTextToDo.text = task.description
            if(task.is_completed){
                binding.buttonTextToDo.setBackgroundResource(R.drawable.button_background_todo_accept)
            }
            else{
                binding.buttonTextToDo.setBackgroundResource(R.drawable.button_background_todo)
            }
            binding.buttonTextToDo.setOnClickListener {
                listener.onButtonClick(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskHolder(view)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(taskList[position], listener)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun addTask(text:String, is_complated:Boolean = false){
        taskList.add(Task(text,taskList.size, is_complated))
        notifyDataSetChanged()
    }
    fun loadNewTasks(tasks:ArrayList<Task>){
        taskList.clear()
        for(task in tasks){
            addTask(task.description,task.is_completed)
        }

        notifyDataSetChanged()
    }
    fun deleteTask(num:Int?){
        if (num != null) {
            taskList.removeAt(num)
            for(i in 0..taskList.size-1){
                taskList[i].numInList = i
            }
        }
        notifyDataSetChanged()
    }

    fun editTask(num:Int?, text:String, status:Boolean){
        if (num != null) {
            taskList.set(num,Task(text,num,status))
        }
        notifyDataSetChanged()
    }

    fun getTaskList(): ArrayList<Task> {
        return taskList
    }
    fun setTaskList(list:List<Task>) {
        taskList = ArrayList(list)
        notifyDataSetChanged()
    }

}