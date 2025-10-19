package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.databinding.ActivityAddCourseBinding
import com.dicoding.courseschedule.util.TimePickerFragment
import java.text.SimpleDateFormat
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private lateinit var binding: ActivityAddCourseBinding
    private lateinit var addCourseViewModel: AddCourseViewModel
    private var startTime: String = ""
    private var endTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = AddCourseViewModelFactory.createFactory(this)
        addCourseViewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_insert -> {
                val courseName = binding.addEdCourseName.text.toString().trim()
                val day = binding.addDaySpinner.selectedItemPosition
                val lecturer = binding.addEdLecturer.text.toString().trim()
                val note = binding.addEdNote.text.toString().trim()

                addCourseViewModel.insertCourse(
                    courseName,
                    day,
                    startTime,
                    endTime,
                    lecturer,
                    note
                )

                addCourseViewModel.saved.observe(this) { event ->
                    event.getContentIfNotHandled()?.let { isSaved ->
                        if (isSaved) {
                            finish()
                        } else {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.insert_course_failed),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showTimePicker(view: View){
        val timePickerFragment = TimePickerFragment()
        when (view.id) {
            R.id.add_ib_start_time -> timePickerFragment.show(
                supportFragmentManager,
                "startTimePicker"
            )
            R.id.add_ib_end_time -> timePickerFragment.show(
                supportFragmentManager,
                "endTimePicker"
            )
        }
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(
            java.util.Calendar.getInstance().apply {
                set(java.util.Calendar.HOUR_OF_DAY, hour)
                set(java.util.Calendar.MINUTE, minute)
            }.time
        )
        when (tag) {
            "startTimePicker" -> {
                startTime = time
                binding.addTvStartTime.text = startTime
            }
            "endTimePicker" -> {
                endTime = time
                binding.addTvEndTime.text = endTime
            }
        }
    }


}