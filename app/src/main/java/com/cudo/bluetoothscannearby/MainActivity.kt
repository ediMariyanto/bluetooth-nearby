package com.cudo.bluetoothscannearby

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.annotation.BinderThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cudo.bluetoothscannearby.adapter.DevicesAdapter
import com.cudo.bluetoothscannearby.utils.GlobalFun
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val REQUEST_ENABLE_BT = 1
    val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    var listDevices = mutableListOf<DevicesItems>()
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initAdapter()
        initComponent()

    }

    fun initAdapter() {
    }



    private fun setupTaskRecyclerView(listData: List<DevicesItems>) {
        rcv_list_device.layoutManager = LinearLayoutManager(this)
        rcv_list_device.setHasFixedSize(true)

//        tasklist.sortBy { it.prio }
        val heroesAdapter = DevicesAdapter(listData)
        rcv_list_device.adapter = heroesAdapter

    }

    fun initComponent() {

        progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("please wait...")

        var sortName = false
        var sortSignal = false
        var sortDistance = false

        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Log.d("Support", "tidak support")
        } else {
            Log.d("Cetak support : ", bluetoothAdapter.toString())
        }

        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)


//        listDevices.add(
//            DevicesItems(
//                dvcName = "est 5",
//                dvcAddress = "est 3",
//                dvcDistance = "20m",
//                dvcSignal = "est 7"
//            )
//        )
//        listDevices.add(
//            DevicesItems(
//                dvcName = "stest 2",
//                dvcAddress = "sTest 1",
//                dvcDistance = "30m",
//                dvcSignal = "stest 9"
//            )
//        )
//        listDevices.add(
//            DevicesItems(
//                dvcName = "test 1",
//                dvcAddress = "Test 6",
//                dvcDistance = "40m",
//                dvcSignal = "test 4"
//            )
//        )
//        listDevices.add(
//            DevicesItems(
//                dvcName = "etest 3",
//                dvcAddress = "eTest 7",
//                dvcDistance = "50m",
//                dvcSignal = "etest 1"
//            )
//        )
//
//        setupTaskRecyclerView(listDevices)

        btn_scan_main_menu.setOnClickListener {
            progressDialog.show()
            listDevices.clear()
            mBluetoothAdapter.startDiscovery()
        }

        ll_device_name.setOnClickListener {
            sortName = if (sortName){
                val sortedList = listDevices.sortedByDescending { it.dvcName }.toMutableList()
                setupTaskRecyclerView(sortedList)
                false
            } else{
                val sortedList = listDevices.sortedBy { it.dvcName }.toMutableList()
                setupTaskRecyclerView(sortedList)
                true
            }
        }

        ll_signal_strength.setOnClickListener {
            sortSignal = if (sortSignal){
                val sortedList = listDevices.sortedByDescending { it.dvcSignal }.toMutableList()
                setupTaskRecyclerView(sortedList)
                false
            } else{
                val sortedList = listDevices.sortedBy { it.dvcSignal }.toMutableList()
                setupTaskRecyclerView(sortedList)
                true
            }
        }

        ll_distance.setOnClickListener {
            sortDistance = if (sortDistance){
                val sortedList = listDevices.sortedByDescending { it.dvcDistance }.toMutableList()
                setupTaskRecyclerView(sortedList)
                false
            } else{
                val sortedList = listDevices.sortedBy { it.dvcDistance }.toMutableList()
                setupTaskRecyclerView(sortedList)
                true
            }
        }
    }


    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            Log.d("Cetak action : ", intent.action.toString())
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    progressDialog.dismiss()
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceNam = device?.name
                    val deviceHardwareAddress = device?.address // MAC address
                    val signalStrength = intent.getShortExtra(
                        BluetoothDevice.EXTRA_RSSI,
                        Short.MIN_VALUE
                    )
                        .toString()
                    val devicesDistance = GlobalFun.calculateAccuracy(
                        intent.getShortExtra(
                            BluetoothDevice.EXTRA_RSSI,
                            Short.MIN_VALUE
                        ).toDouble()
                    )
                    val distanceFormatter = String.format("%.2f", devicesDistance)

//                    Log.d(
//                        "Cetak action : ",
//                        deviceNam.toString() + " : " + deviceHardwareAddress.toString()
//                    )

                    listDevices.add(
                        DevicesItems(
                            dvcName = deviceNam,
                            dvcAddress = deviceHardwareAddress,
                            dvcDistance = distanceFormatter,
                            dvcSignal = signalStrength
                        )
                    )

                    val heroesAdapter = DevicesAdapter(listDevices)

                    rcv_list_device.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = heroesAdapter
                    }

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

}

data class DevicesItems(
    val dvcName: String?,
    val dvcAddress: String?,
    val dvcSignal: String,
    val dvcDistance: String
)