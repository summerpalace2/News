## 「知晤晨笺」

### 一个仿写知乎日报的APP



#### 简约的登录方式，来自App的轻语。使用room数据库实现本地登陆注册，并且使用sharepreferences 和 checkbox实现保存数据，勾选记住密码后，用sharepreferences保存这一状态，在下次登录时自动填写密码![Screenshot_2025-05-05-18-25-48-32_6bd9dd3c8e06a94]https://github.com/summerpalace2/News/blob/main/app/src/images/Screenshot_2025-05-05-18-25-48-32_6bd9dd3c8e06a94.jpg

### 注册界面同样简约!https://github.com/summerpalace2/News/blob/main/app/src/images/Screenshot_2025-05-05-18-36-48-50_76d041c8b40ba84.jpg

https://github.com/summerpalace2/News/blob/main/app/src/images/mmexport1746441551204.gif

#### 来到主界面，映入眼帘的就是观赏性好的轮播图，使用viewpager2+监听器的方式实现，在创建的同时实现了过渡自然的动画，点击轮播图后，触发点击事件，进入一个全新的activity，传入url，并使用webview，实现网页详情。在下面的新闻列表中，点击进入，可以看到不止有新闻详情，还有评论点赞收藏和转发，其中点击评论，就可以查看评论了，如果你觉得这篇文章很好看，还可以点击转发按钮，转发给其他人看，一起享受这段美好

https://github.com/summerpalace2/News/blob/main/app/src/images/mmexport1746441551204.gif

### 如果你想要沉浸式阅读，可以随便点击一个新闻，看完就右滑，这通过viewpager2的创建，外加动态添加的结果，当某一天的新闻滑动底时，会触发监听事件，进而更新内容。

https://github.com/summerpalace2/News/blob/main/app/src/images/mmexport1746442340840.gif

#### 持续滑动新闻，似乎发现一个规律，那就是页面通过日期+新闻详情+进度条（暂时的）,的形式循环，并且三者都是动态刷新，这利用了recycleview的分页加载功能，先创建一个密封类包含三种类型的数据，在adapter的创建中传入（实际上要先传入一个空列表给adapter，因为我们拿到的数据是申请网络请求之后的，等到返回数据后在调用update方法传入，防止adapter报错），在里面创建多个viewholder，在使用getItemViewType方法根据当前位置的 `newsList` 数据类型，告诉 RecyclerView 应该使用哪种类型的 ViewHolder 来显示该项。在这次循环到下次循环开始前，删除页脚，即可实现加载条的动态刷新。不断给recycleview增添数据，给你带来更丰富的体验，同时设置1秒的响应时间，以防止重复刷新https://github.com/summerpalace2/News/blob/main/app/src/images/Screenshot_2025-05-05-19-41-42-73_76d041c8b40ba84.jpg

### 观察右上角，可以看到你的头像https://github.com/summerpalace2/News/blob/main/app/src/images/mmexport1746442340840.gif

### 点击按钮即可实现更换头像，并且同步更新到主页，这使用了LiveData来快速响应，并且使用sharepreferences实现本地保存。

从2月中后期到4月份初，其实我对课程的学习并不是很上心，很长一段时间就是看了课件就不管了，缺少练习，并且对知识点掌握不深，让我对这次考核充满了担忧，并且我只是看了课件，没有写自己的笔记，所以在考核前的几周里，我认真的看了之前的课件并且做笔记，看第一行代码，并且通过ai辅助理解和生成有用的知识点，做适当的练习，以加强理解，但就算这样，在期中考核来临前，我仍然没有学完课件，这给我很大压力，所以我下定决心，全身心全投入的面对考核在这紧张的5天中，我花了一天学习compose结果发现知识量庞大，几天不可能速通，于是后面的3天半都非常赶，在这个过程中，我逐渐属性MVVM架构，也学习了recycleview的动态加载，并且让我更有信心，同时也认识到自己的基础不行，需要在后面的日子了多补补基础。总之，这次考核锻炼了我的心态，也认识到了自己的不足，所以我想在下半学期更加努力，争取赶上进度。
