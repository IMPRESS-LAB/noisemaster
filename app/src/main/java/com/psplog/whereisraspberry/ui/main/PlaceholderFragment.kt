package com.psplog.whereisraspberry.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection
import com.psplog.whereisraspberry.R
import com.psplog.whereisraspberry.adapter.DeviceListAdapter
import com.psplog.whereisraspberry.adapter.NoiseListAdapter
import com.psplog.whereisraspberry.dto.device.DeviceDTO
import com.psplog.whereisraspberry.dto.device.NoiseDTO
import com.psplog.whereisraspberry.network.ApplicationController
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceholderFragment : androidx.fragment.app.Fragment() {
    private var currentPage = 0;
    private lateinit var pageViewModel: PageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        root.swipyrefreshlayout.setOnRefreshListener {
            if(it==SwipyRefreshLayoutDirection.TOP)
                currentPage=0
            else if (it == SwipyRefreshLayoutDirection.BOTTOM)
                currentPage++
            setView(root, inflater)
        }
        setView(root, inflater)
        return root
    }

    private fun setView(root: View, inflater: LayoutInflater) {
        when {
            arguments!!.getInt(ARG_SECTION_NUMBER) == 1 -> requestDeviceList(root, inflater.context)
            arguments!!.getInt(ARG_SECTION_NUMBER) == 2 -> requestNoiseList(root, inflater.context)
        }
    }

    private fun requestNoiseList(inflater: View, context: Context) {
        val getHomeProductResponse: Call<NoiseDTO> =
            ApplicationController.instance.networkService.getNoiseListResponse(
                "application/json",
                page = currentPage
            )

        getHomeProductResponse.enqueue(object : Callback<NoiseDTO> {
            override fun onFailure(call: Call<NoiseDTO>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<NoiseDTO>,
                response: Response<NoiseDTO>
            ) {
                if (response.isSuccessful) {
                    swipyrefreshlayout.isRefreshing = false
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
                    swipyrefreshlayout.isRefreshing = false
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