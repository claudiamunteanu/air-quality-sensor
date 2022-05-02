package app.companionapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import app.companionapp.data.AirQualityData
import app.companionapp.databinding.ActivityMainBinding
import app.companionapp.utils.Constants
import app.companionapp.utils.SerializerUtil
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttMessage
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleMqtt()
        initChart()
    }

    private fun handleMqtt() {
        val mqtt = MQTT(this)
        mqtt.connectMQTT(
            connectionLost = {
                Log.e(TAG, "connection lost", it)
            },
            messageArrived = { topic: String, message: MqttMessage ->
                val messageString = String(message.payload)
                handleCurrentResponse(messageString, topic)
                Log.i(TAG, "topic: $topic, msg: $messageString")
            },
            deliveryComplete = {
                Log.i(TAG, "msg delivered")
            },
            onSuccess = {
                Log.i(TAG, "connect succeed")
                mqtt.subscribeTopic(topic = Constants.TOPIC_CURRENT_VALUES)
                mqtt.subscribeTopic(topic = Constants.TOPIC_NOTIFICATIONS)
            },
            onFailure = { _: IMqttToken, exception: Throwable ->
                Log.e(TAG, "connect failed", exception)
            }
        )
    }

    private fun handleCurrentResponse(payload: String, topic: String) {
        when (topic) {
            Constants.TOPIC_CURRENT_VALUES -> {
                val data = SerializerUtil.transformJsonToAirQualityData(payload)
                refreshCurrentValuesInUI(data)
                updateChart(data.temperature?.toDouble()?: 0.0, data.timestamp?: System.currentTimeMillis())
            }
            Constants.TOPIC_NOTIFICATIONS -> {
                createNotification(getString(R.string.value_hit_template, payload.replaceFirstChar { it.uppercase() }))
            }
            else -> {
                // default topic handle
            }
        }
    }

    private fun refreshCurrentValuesInUI(data: AirQualityData) {
        binding.humidityValue.text = getString(R.string.humidity_template, data.humidity.toString())
        binding.pressureValue.text = getString(R.string.pressure_template, data.pressure.toString())
        binding.temperatureValue.text = getString(R.string.temperature_template, data.temperature.toString())
    }

    private fun createNotification(notificationMessage: String) {
        val channelId = "i.apps.notifications"
        val name = "MyApp"
        val descriptionText = "No description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val existingChannel = notificationManager.getNotificationChannel(channelId)
        if (existingChannel == null) {
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = descriptionText
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Threshold reached")
            .setContentText(notificationMessage)
            .setSmallIcon(R.drawable.ic_cloud)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(1234, builder.build())
    }

    private fun initChart() {
        binding.chart.xAxis?.enableGridDashedLine(10f, 10f, 0f)
        binding.chart.setTouchEnabled(true)
        binding.chart.isDragEnabled = true
        binding.chart.setScaleEnabled(true)
        binding.chart.axisRight?.isEnabled = false


        val yAxis = binding.chart.axisLeft
        yAxis?.enableGridDashedLine(10f, 10f, 0f)
        yAxis?.axisMinimum = 0f

        binding.chart.xAxis?.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return ""
            }
        }
    }
    private fun updateChart(value: Double, timestamp: Long) {
        val timestampSeconds = timestamp / 1000L - 1651169416L

            if (binding.chart.data != null && binding.chart.data!!.dataSetCount > 0) {
                val set1 = binding.chart.data?.getDataSetByIndex(0) as? LineDataSet
                val values = set1?.values


                values?.add(Entry(timestampSeconds.toFloat(), value.toFloat()))

                set1?.values = values
                set1?.notifyDataSetChanged()
                binding.chart.data.notifyDataChanged()
                binding.chart.notifyDataSetChanged()
            } else {
                val values = ArrayList<Entry>()
                values.add(Entry(timestampSeconds.toFloat(), value.toFloat()))

                val set1 = LineDataSet(values, "Temperature in time")
                val dataSets = ArrayList<ILineDataSet>()
                dataSets.add(set1)

                binding.chart.data = LineData(dataSets)
            }
        }




}