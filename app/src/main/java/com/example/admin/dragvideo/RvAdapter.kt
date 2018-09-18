package com.example.admin.dragvideo

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

/**
 * Created by zjw on 2018/9/10.
 */ class RvAdapter(val activity: Activity): Adapter<RecyclerView.ViewHolder>() {


    var mClickPosition =-1

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        return RvHolder(LayoutInflater.from(activity).inflate(R.layout.item_rv,parent,false))
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RvHolder).setData(position)
    }

    inner class RvHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mIv = itemView.findViewById<ImageView>(R.id.iv)
        fun setData(position: Int) {
            mIv.setOnClickListener {
                mClickPosition=position
                val globalRect = Rect()
                val intent = Intent(activity, VideoDetailActivity::class.java)
                itemView.getGlobalVisibleRect(globalRect)

               /* val ivLeft= mIv.left
                val ivTop= mIv.top
                val ivRight= mIv.right
                val ivBottom= mIv.bottom
                Log.d("eee=iv","left=$ivLeft top=$ivTop right=$ivRight bottom=$ivBottom")*/
                val ivRect = Rect()
                mIv.getGlobalVisibleRect(ivRect)
                Log.d("eee=iv height ","${mIv.height}")
                Log.d("eee=iv minus height ",""+(ivRect.bottom-ivRect.top))
                intent.apply {
//                    putExtra("global_rect", intArrayOf(mIv.left, itemView.top+mIv.top,mIv.right,
//                            itemView.bottom-(itemView.height-mIv.bottom), mIv.height))
                    putExtra("global_rect", intArrayOf(ivRect.left, ivRect.top,ivRect.right,
                            ivRect.bottom, mIv.height))
                    putExtra("video_index", position)
                    //判定点击的是不是最后一行的item
                    putExtra("is_last_row", position >= itemCount - 2)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivity(intent)
                    activity.overridePendingTransition(0,0)
                }
            }
        }
    }

}