package com.futhark.android.seevoice.controller.fragment

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.futhark.android.seevoice.R
import com.futhark.android.seevoice.base.BaseFragment
import com.futhark.android.seevoice.model.database.TableVoiceSpecification
import com.futhark.android.seevoice.view.DisplayVoiceView

import java.util.Arrays

import butterknife.BindView
import butterknife.ButterKnife

/**
 * exercising fragment
 * Created by liuhr on 07/04/2017.
 */

class ExercisingFragment : BaseFragment() {
    private var recordDataSize = 0

    private var itemModel: TableVoiceSpecification.VoiceSpecificationEntry? = null
    @BindView(R.id.touch_pressing_when_talking)
    internal var pressingWhenTalking: ImageView? = null
    @BindView(R.id.display_exercising_voice)
    internal var displayVoiceView: DisplayVoiceView? = null
    @BindView(R.id.display_voice_specification)
    internal var specificationVoiceView: DisplayVoiceView? = null

    private var audioRecord: AudioRecord? = null
    private var paint: Paint? = null
    private var isRecording = false

    private val onTouchListener = View.OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                displayVoiceController.clear()
                audioRecord!!.startRecording()
                isRecording = true
                Log.d(BaseFragment.Companion.TAG, "start to record")
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (audioRecord != null) audioRecord!!.stop()
                isRecording = false
                Log.d(BaseFragment.Companion.TAG, "stop recording")
            }
        }
        true
    }
    private val specificationController = object : DisplayVoiceView.DisplayVoiceController {
        private var recordData: ShortArray? = null
        private var displayData: FloatArray? = null
        override fun draw(canvas: Canvas) {
            if (itemModel == null) {
                Log.d(BaseFragment.Companion.TAG, "no data")
                return
            }
            if (displayData == null) {
                displayData = FloatArray(itemModel!!.data!!.size * 4)
                recordData = itemModel!!.data
            }
            //            int startAt = currentPosition - recordDataSize < 0 ? 0 : currentPosition - recordDataSize;
            val startAt = 0
            //            int dataLength = recordDataSize;
            val dataLength = recordData!!.size
            val canvasRect = canvas.clipBounds
            val paddingTop = canvasRect.height() / 20
            val wavePeak = canvasRect.height() / 2 - paddingTop

            paint!!.color = Color.WHITE
            val verticalCenter = canvasRect.height() / 2
            val max = 32767f
            val zoom = 4f
            val dotWidth = canvasRect.width() * 1.0f / (dataLength + 1)
            val dotHeight = wavePeak * 1f / max
            var startX = 0f
            var startY = (canvasRect.height() / 2).toFloat()
            var endX: Float
            var endY: Float
            var displayIndex: Int
            for (index in startAt until dataLength) {
                endX = index * dotWidth
                endY = recordData!![index].toFloat() * dotHeight * zoom + verticalCenter
                displayIndex = index * 4
                displayData.set(displayIndex, startX)
                displayData.set(displayIndex+1, startY)
                displayData.set(displayIndex+2, endX)
                displayData.set(displayIndex+3, endY)
                startX = endX
                startY = endY
            }
            canvas.drawLines(displayData!!, paint!!)

        }

        override fun update() {

        }

        override fun clear() {

        }

        override fun onCreate() {

        }
    }
    private val displayVoiceController = object : DisplayVoiceView.DisplayVoiceController {
        private var recordData: ShortArray? = null
        private var recordDisplayData: ShortArray? = null
        private var displayData: FloatArray? = null
        private val empty: Short = 0
        private var currentPosition = 0

        override fun draw(canvas: Canvas) {
            //            int startAt = currentPosition - recordDataSize < 0 ? 0 : currentPosition - recordDataSize;
            val startAt = 0
            //            int dataLength = recordDataSize;
            val dataLength = currentPosition
            val canvasRect = canvas.clipBounds
            val paddingTop = canvasRect.height() / 20
            val wavePeak = canvasRect.height() / 2 - paddingTop

            paint!!.color = Color.WHITE
            val verticalCenter = canvasRect.height() / 2
            val max = 32767f
            val zoom = 4f
            val dotWidth = canvasRect.width() * 1.0f / (dataLength + 1)
            val dotHeight = wavePeak * 1f / max
            var startX = 0f
            var startY = (canvasRect.height() / 2).toFloat()
            var endX: Float
            var endY: Float
            var displayIndex: Int
            for (index in startAt until dataLength) {
                endX = index * dotWidth
                endY = recordData!![index].toFloat() * dotHeight * zoom + verticalCenter
                displayIndex = index * 4
                displayData[displayIndex] = startX
                displayData[displayIndex + 1] = startY
                displayData[displayIndex + 2] = endX
                displayData[displayIndex + 3] = endY
                startX = endX
                startY = endY
            }
            canvas.drawLines(displayData!!, paint!!)
        }

        override fun update() {
            if (!isRecording) return
            val read = audioRecord!!.read(recordDisplayData!!, 0, recordDataSize)
            when (read) {
                AudioRecord.ERROR -> Log.d(BaseFragment.Companion.TAG, " 不知道什么东西错了")
                AudioRecord.ERROR_BAD_VALUE -> Log.d(BaseFragment.Companion.TAG, "数据格式有问题")
                AudioRecord.ERROR_INVALID_OPERATION -> Log.d(BaseFragment.Companion.TAG, "操作不正确")
                else -> if (read > 0) {
                    System.arraycopy(recordDisplayData!!, 0, recordData!!, 0, read)
                    currentPosition = recordData!!.size
                }
            }
        }

        override fun clear() {
            Log.d(BaseFragment.Companion.TAG, "audio record is clear")
            Arrays.fill(recordData!!, empty)
            Arrays.fill(recordDisplayData!!, empty)
            Arrays.fill(displayData!!, empty.toFloat())
            currentPosition = 0
        }

        override fun onCreate() {
            recordData = ShortArray(recordDataSize)
            recordDisplayData = ShortArray(recordDataSize)
            displayData = FloatArray(recordDataSize * 4)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        paint = Paint()
        paint!!.color = Color.WHITE
        if (arguments == null) return
        if (arguments.containsKey(FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL)) {
            this.itemModel = arguments.getSerializable(FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL) as TableVoiceSpecification.VoiceSpecificationEntry
        }
        activity.title = activity.title.toString() + " " + itemModel!!.title
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {
        val view = inflater.inflate(R.layout.fragment_exercising, container, false)
        ButterKnife.bind(this, view)
        pressingWhenTalking!!.setOnTouchListener(this.onTouchListener)
        recordDataSize = AudioRecord.getMinBufferSize(32000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT) * 2
        this.displayVoiceController.onCreate()
        displayVoiceView!!.displayVoiceController = this.displayVoiceController
        if (itemModel == null) {
            specificationVoiceView!!.visibility = View.GONE
        } else {
            specificationVoiceView!!.displayVoiceController = this.specificationController
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        if (audioRecord == null) {
            audioRecord = AudioRecord(MediaRecorder.AudioSource.DEFAULT, 32000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, 2 * recordDataSize)
        }
    }

    override fun onStop() {
        super.onStop()
        if (audioRecord != null) {
            audioRecord!!.stop()
            audioRecord!!.release()
            audioRecord = null
        }
    }

    companion object {
        val FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL = "fragment_exercising_argument_item_model"
        fun newInstance(itemModel: TableVoiceSpecification.VoiceSpecificationEntry): ExercisingFragment {
            val fragment = ExercisingFragment()
            val arguments = Bundle()
            arguments.putSerializable(FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL, itemModel)
            fragment.arguments = arguments
            return fragment
        }
    }
}
