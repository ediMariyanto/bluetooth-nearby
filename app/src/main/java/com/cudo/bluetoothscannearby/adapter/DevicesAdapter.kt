package com.cudo.bluetoothscannearby.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cudo.bluetoothscannearby.DevicesItems
import com.cudo.bluetoothscannearby.R
import kotlinx.android.synthetic.main.layout_device_item.view.*

class DevicesAdapter(private val data: List<DevicesItems>) : RecyclerView.Adapter<DevicesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevicesHolder {
        return DevicesHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_device_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DevicesHolder, position: Int) {
        holder.bindDevices(data[position])
    }

    override fun getItemCount(): Int =
        data.size
}

class DevicesHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvNameBluetooth = view.tv_name_bluetooth
    private val tvSignal = view.tv_signal_strength_bluetooth
    private val tvDistance = view.tv_distance_bluetooth
    private val tvAddress = view.tv_mac_bluetooth

    fun bindDevices(devicesItem: DevicesItems) {
        tvNameBluetooth.text = devicesItem.dvcName
        tvSignal.text = devicesItem.dvcSignal +"dbm"
        tvDistance.text = devicesItem.dvcDistance+"m"
        tvAddress.text = devicesItem.dvcAddress

    }
}