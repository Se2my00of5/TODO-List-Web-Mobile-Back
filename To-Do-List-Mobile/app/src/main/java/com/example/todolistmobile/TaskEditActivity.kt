package com.example.todolistmobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.todolistmobile.databinding.ActivityTaskEditBinding


class TaskEditActivity : AppCompatActivity() {

    lateinit var binding: ActivityTaskEditBinding

    lateinit var task:Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskEditBinding.inflate(layoutInflater)

        setContentView(binding.root)

        task = Task(
            intent.getStringExtra("text").toString(),
            intent.getStringExtra("num")!!.toInt(),
            intent.getStringExtra("status").toBoolean()
        )

        binding.input.setText(task.description)

        editStatus(task.is_completed)

        binding.apply {
            accept.setOnClickListener{
                if(task.is_completed){
                    editStatus(false)
                }
                else{
                    editStatus(true)
                }
            }

            trash.setOnClickListener{
                val intent = Intent(this@TaskEditActivity, MainActivity::class.java)

                intent.putExtra("num",task.numInList.toString())
                intent.putExtra("key", "delete")

                setResult(RESULT_OK,intent)
                finish()
            }

            cancel.setOnClickListener{
                val intent = Intent(this@TaskEditActivity, MainActivity::class.java)

                setResult(RESULT_CANCELED,intent)
                finish()
            }

            save.setOnClickListener{
                if(input.text.toString().strip()!=""){
                    val intent = Intent(this@TaskEditActivity, MainActivity::class.java)

                    intent.putExtra("num",task.numInList.toString())
                    intent.putExtra("key", "update")
                    intent.putExtra("text",input.text.toString().strip())
                    Log.d("aqqq",task.is_completed.toString())
                    intent.putExtra("status", task.is_completed.toString())

                    setResult(RESULT_OK,intent)
                    finish()
                }
            }
        }
    }



    fun editStatus(status: Boolean){
        if(status){
            task.is_completed = true
            binding.status.setText("Complete")
        }
        else{
            task.is_completed = false
            binding.status.setText("Uncomplete")
        }
    }
}