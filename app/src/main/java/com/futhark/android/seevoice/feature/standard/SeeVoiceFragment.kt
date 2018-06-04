package com.futhark.android.seevoice.feature.standard

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
import com.futhark.android.seevoice.view.DisplayVoiceView
import java.util.*


/**
 * exercising fragment
 * Created by liuhr on 07/04/2017.
 */
const val empty: Short = 0
class SeeVoiceFragment : BaseFragment() {
  private var recordDataSize = 0

  private var initData: ShortArray? = null
  private var pressingWhenTalking: ImageView? = null
  private var displayVoiceView: DisplayVoiceView? = null

  private var audioRecord: AudioRecord? = null
  private var paint: Paint? = null
  private var isRecording = false

  private val onTouchListener = View.OnTouchListener { v, event ->
    when (event.action) {
      MotionEvent.ACTION_DOWN -> {
        displayVoiceController.clear()
        audioRecord!!.startRecording()
        isRecording = true
        Log.d(BaseFragment.TAG, "start to record")
      }
      MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
        if (audioRecord != null) audioRecord!!.stop()
        isRecording = false
        v.performClick()
        Log.d(BaseFragment.TAG, "stop recording")
      }
    }
    true
  }

  private val displayVoiceController = object : SeeVoiceController {
    var lastVoiceRecord: ShortArray? = null
    private var recordDisplayData: ShortArray? = null
    private var displayData: FloatArray? = null
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
        endY = lastVoiceRecord!![index].toFloat() * dotHeight * zoom + verticalCenter
        displayIndex = index * 4
        displayData!![displayIndex] = startX
        displayData!![displayIndex + 1] = startY
        displayData!![displayIndex + 2] = endX
        displayData!![displayIndex + 3] = endY
        startX = endX
        startY = endY
      }
      canvas.drawLines(displayData!!, paint!!)
    }

    override fun update() {
      if (!isRecording) return
      val read = audioRecord!!.read(recordDisplayData!!, 0, recordDataSize)
      when (read) {
        AudioRecord.ERROR -> Log.d(BaseFragment.TAG, " 不知道什么东西错了")
        AudioRecord.ERROR_BAD_VALUE -> Log.d(BaseFragment.TAG, "数据格式有问题")
        AudioRecord.ERROR_INVALID_OPERATION -> Log.d(BaseFragment.TAG, "操作不正确")
        else -> if (read > 0) {
          System.arraycopy(recordDisplayData!!, 0, lastVoiceRecord!!, 0, read)
          currentPosition = lastVoiceRecord!!.size
        }
      }
    }

    override fun clear() {
      Arrays.fill(lastVoiceRecord!!, empty)
      Arrays.fill(recordDisplayData!!, empty)
      Arrays.fill(displayData!!, empty.toFloat())
      currentPosition = 0
    }

    override fun onCreate() {
      lastVoiceRecord = ShortArray(recordDataSize)
      recordDisplayData = ShortArray(recordDataSize)
      displayData = FloatArray(recordDataSize * 4)
    }

    override fun setVoiceRecord(voiceData: ShortArray) {
      this.lastVoiceRecord = voiceData
      currentPosition = lastVoiceRecord!!.size
    }
  }

  val lastRecordData: ShortArray?
    get() = displayVoiceController.lastVoiceRecord

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val arguments = arguments
    paint = Paint()
    paint!!.color = Color.WHITE
    if (arguments == null) return
    if (arguments.containsKey(FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL)) {
      this.initData = arguments.getSerializable(FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL) as ShortArray
    }

  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_see_voice, container, false)
    pressingWhenTalking = view.findViewById(R.id.touch_pressing_when_talking)
    displayVoiceView = view.findViewById(R.id.display_exercising_voice)
    pressingWhenTalking!!.setOnTouchListener(this.onTouchListener)
    recordDataSize = AudioRecord.getMinBufferSize(32000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT) * 2
    this.displayVoiceController.onCreate()
    displayVoiceView!!.displayVoiceController = this.displayVoiceController
    if (this.initData != null) {
      this.displayVoiceController.setVoiceRecord(initData!!)
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

  internal interface SeeVoiceController : DisplayVoiceView.DisplayVoiceController {
    fun setVoiceRecord(voiceData: ShortArray)
  }

  companion object {
    const val FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL = "fragment_exercising_argument_item_model"
    fun newInstance(initData: ShortArray?): SeeVoiceFragment {
      val fragment = SeeVoiceFragment()
      if (initData != null) {
        val arguments = Bundle()
        arguments.putSerializable(FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL, initData)
        fragment.arguments = arguments
      }
      return fragment
    }
  }
}