package com.psplog.whereisraspberry.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.psplog.whereisraspberry.R
import com.psplog.whereisraspberry.adapter.DeviceListAdapter
import com.psplog.whereisraspberry.adapter.NoiseListAdapter
import com.psplog.whereisraspberry.dto.device.DeviceDTO
import com.psplog.whereisraspberry.dto.device.NoiseDTO
import com.psplog.whereisraspberry.network.ApplicationController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceholderFragment : androidx.fragment.app.Fragment() {

    private lateinit var pageViewModel: PageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        when {
            arguments!!.getInt(ARG_SECTION_NUMBER) == 1 -> requestDeviceList(root, inflater.context)
            arguments!!.getInt(ARG_SECTION_NUMBER) == 2 -> requestNoiseList(root, inflater.context)
        }
        return root
    }

    private fun requestNoiseList(inflater: View, context: Context) {
        val getHomeProductResponse: Call<NoiseDTO> =
            ApplicationController.instance.networkService.getNoiseListResponse("application/json")

        getHomeProductResponse.enqueue(object : Callback<NoiseDTO> {
            override fun onFailure(call: Call<NoiseDTO>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<NoiseDTO>,
                response: Response<NoiseDTO>
            ) {
                if (response.isSuccessful) {
                    setNoiseList(context, inflater, response.body()!!.data.noises)
                }
            }
        })
    }

    private fun requestDeviceList(inflater: View, context: Context) {
        val getHomeProductResponse: Call<DeviceDTO> =
            ApplicationController.instance.networkService.getDeviceListResponse("application/json")

        getHomeProductResponse.enqueue(object : Callback<DeviceDTO> {
            override fun onFailure(call: Call<DeviceDTO>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<DeviceDTO>,
                response: Response<DeviceDTO>
            ) {
                if (response.isSuccessful) {
                    setDeviceList(context, inflater, response.body()!!.devices)
                }
            }
        })
    }


    private fun setDeviceList(context: Context, inflater: View, list: List<DeviceDTO.Device>) {
        var adapter = DeviceListAdapter(context, list)
        inflater.findViewById<RecyclerView>(R.id.rv_device_list).layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        inflater.findViewById<RecyclerView>(R.id.rv_device_list).adapter = adapter
    }

    private fun setNoiseList(
        context: Context,
        inflater: View,
        noises: List<NoiseDTO.Data.Noise>
    ) {
        var adapter = NoiseListAdapter(context, noises)
        inflater.findViewById<RecyclerView>(R.id.rv_device_list).layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        inflater.findViewById<RecyclerView>(R.id.rv_device_list).adapter = adapter
    }


    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}