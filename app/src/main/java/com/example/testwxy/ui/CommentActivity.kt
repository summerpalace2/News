package com.example.testwxy.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testwxy.R
import com.example.testwxy.adapter.CommentsAdapter
import com.example.testwxy.databinding.ActivityCommentBinding
import com.example.testwxy.repository.CommentsResult
import com.example.testwxy.viewmodel.MainViewModel

class CommentActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by lazy{
        MainViewModel(application)
    }
    private lateinit var commentsadapter: CommentsAdapter
    private var id:Int=0
    private val binding: ActivityCommentBinding by lazy {
        ActivityCommentBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        id=intent.getIntExtra("id",0)
        initView()
        observe()
    }

    private fun initView() {
        viewModel.getComment(id)
    }
    private fun observe() {
        viewModel.commentResult.observe(this) {
            result ->

                when (result) {
                    is CommentsResult.Success -> {
                        val comment = result.data
                        commentsadapter = CommentsAdapter(comment.comments)
                        binding.recyclerView.layoutManager = LinearLayoutManager(this)
                        binding.recyclerView.adapter = commentsadapter

                    }

                    is CommentsResult.Error -> {
                        result.exception.printStackTrace()
                    }

                }

            }


    }
}