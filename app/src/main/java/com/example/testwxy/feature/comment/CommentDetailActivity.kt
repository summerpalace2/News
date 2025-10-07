package com.example.testwxy.feature.comment

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testwxy.R
import com.example.testwxy.feature.adapter.CommentsAdapter
import com.example.testwxy.data.model.Resource
import com.example.testwxy.databinding.ActivityCommentBinding
import com.example.testwxy.feature.news.MainViewModel

/**
 * description ： TODO:评论显示页
 * author : summer_palace2
 * date : 2025/5/3 17:31
 */

class CommentDetailActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        MainViewModel(application)
    }

    private lateinit var commentsadapter: CommentsAdapter

    private var id: Int = 0
    private val binding: ActivityCommentBinding by lazy {
        ActivityCommentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        id = intent.getIntExtra("id", 0)

        initRecyclerView()
        initRefreshLogic()
        observe()

        // 首次进入页面，手动开启转圈并加载数据
        binding.swipeRefreshLayout.post {
            binding.swipeRefreshLayout.isRefreshing = true
        }
        viewModel.getComments(id)
    }

    private fun initRecyclerView() {
        commentsadapter = CommentsAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@CommentDetailActivity)
            adapter = commentsadapter
        }

        // 返回按钮点击事件
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initRefreshLogic() {
        // 设置下拉刷新监听器
        binding.swipeRefreshLayout.setOnRefreshListener {
            // 重新调用获取评论列表的接口
            viewModel.getComments(id)
        }

        // 使用项目自定义的淡蓝色
        binding.swipeRefreshLayout.setColorSchemeResources(
            R.color.littlebule,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light
        )
    }

    /**
     * 核心观察逻辑：处理 Resource 状态切换
     */
    private fun observe() {
        // 观察评论列表数据（对应 ViewModel 中的 _commentListResult）
        viewModel.commentListResult.observe(this) { resource ->
            // 只要有结果返回，不管是 Loading 结束还是 Success/Error，都应考虑关闭刷新动画
            if (resource !is Resource.Loading) {
                binding.swipeRefreshLayout.isRefreshing = false
            }

            when (resource) {
                is Resource.Success -> {
                    // ListAdapter 差分刷新
                    commentsadapter.submitList(resource.data.comments)
                }

                is Resource.Error -> {
                    // 打印错误栈并提示
                    resource.exception.printStackTrace()
                    Toast.makeText(this, "评论加载失败: ${resource.exception.message}", Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {}
            }
        }
    }
}