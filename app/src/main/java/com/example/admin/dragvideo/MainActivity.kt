package com.example.admin.dragvideo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {

    private val mAdapter by lazy {
        RvAdapter(this)
    }
    private val mRv by lazy {
        findViewById<RecyclerView>(R.id.rv)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        Eyes.translucentStatusBar(this, true)
        mRv.layoutManager=LinearLayoutManager(this)
        mRv.setHasFixedSize(true)
        mRv.adapter=mAdapter
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: ScrollTopEvent) {
        if (event.isScroll) {
            moveToPosition(mRv.layoutManager as LinearLayoutManager,mAdapter.mClickPosition )
        }
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun moveToPosition(layoutManager: LinearLayoutManager, selectedPosition: Int) {
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        val top = mRv.getChildAt(selectedPosition - firstVisiblePosition).top
        mRv.scrollBy(0, top)
    }
}
