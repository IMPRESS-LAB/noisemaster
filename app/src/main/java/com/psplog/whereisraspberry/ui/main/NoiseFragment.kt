package com.psplog.whereisraspberry.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

class NoiseFragment : androidx.fragment.app.Fragment() {
    private val searchListener = object : Dialog.Search {
        override fun onClickSearch(tag: String, decibel: Int) {
            if (arguments!!.getInt(ARG_SECTION_NUMBER) == 2) {
                requestNoiseList(tag, decibel)
            }
        }

    }
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

        activity?.findViewById<FloatingActionButton>(R.id.fab)?.setOnClickListener {
            showDialog()
        }

        root.swipyrefreshlayout.setOnRefreshListener {
            if (it == SwipyRefreshLayoutDirection.TOP)
                currentPage = 0
            else if (it == SwipyRefreshLayoutDirection.BOTTOM)
                currentPage++
            setView()
        }
        setView()
        return root
    }


    private fun showDialog() {
        val dialog = Dialog(searchListener)
        dialog.show(fragmentManager, "Dialog")
    }


    private fun setView() {
        when {
            arguments!!.getInt(ARG_SECTION_NUMBER) == 1 -> requestDeviceList()
            arguments!!.getInt(ARG_SECTION_NUMBER) == 2 -> requestNoiseList()
        }
    }

    private fun requestNoiseList(
        tag: String? = null,
        decibel: Int? = null
    ) {
        val getHomeProductResponse: Call<NoiseDTO> =
            ApplicationController.instance.networkService.getNoiseListResponse(
                "application/json",
                page = currentPage,
                tag = tag,
                decibel = decibel
            )

        getHomeProductResponse.enqueue(object : Callback<NoiseDTO> {
            override fun onFailure(call: Call<NoiseDTO>, t: Throwable) {}
            override fun onResponse(
                call: Call<NoiseDTO>,
                response: Response<NoiseDTO>
            ) {
                if (response.isSuccessful) {
                    swipyrefreshlayout.isRefreshing = false
                    setNoiseList(response.body()!!.data.noises)
                }
            }
        })
    }

    private fun requestDeviceList() {
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
                    setDeviceList(response.body()!!.devices)
                }
            }
        })
    }


    private fun setDeviceList(list: List<DeviceDTO.Device>) {
        var adapter = DeviceListAdapter(list)
        rv_device_list.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv_device_list.adapter = adapter
    }

    private fun setNoiseList(
        noises: List<NoiseDTO.Data.Noise>
    ) {
        var adapter = NoiseListAdapter(noises)
        rv_device_list.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv_device_list.adapter = adapter
    }


    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): NoiseFragment {
            return NoiseFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}