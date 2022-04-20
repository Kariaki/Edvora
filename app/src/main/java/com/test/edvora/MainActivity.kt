package com.test.edvora

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TableLayout
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.data.recyclerview_helper.GeneralAdapter
import com.data.recyclerview_helper.MainViewHolder
import com.data.recyclerview_helper.SuperClickListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.kcoding.recyclerview_helper.SuperEntity
import com.test.edvora.databinding.ActivityMainBinding
import com.test.edvora.databinding.RideComponenetBinding
import com.test.edvora.model.Ride
import com.test.edvora.model.User
import com.test.edvora.ui.FilterModal
import com.test.edvora.viewmodel.EdvoraViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.userAgent

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SuperClickListener, GeneralAdapter.ViewHolderPlug,
    TabLayout.OnTabSelectedListener, FilterModal.OnFilterSelectedListener {


    lateinit var binding: ActivityMainBinding
    val generalAdapter = GeneralAdapter()


    val viewmodel: EdvoraViewModel by viewModels()

    var rideList: List<Ride> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewmodel.user.observe(this) {

            if (it != null) {
                Glide.with(this).load(it.url).into(binding.userProfileImage)
                binding.userName.text = it.name
            }else{
                Snackbar.make(binding.root,"No user found",Snackbar.LENGTH_SHORT).show()
            }

        }

        viewmodel.publishUser()
        viewmodel.publishAllRides(this)

        viewmodel.tabPosition.observe(this) {

            viewmodel.publishRides(it, this)
        }


        binding.rideTabs.addOnTabSelectedListener(this)

        generalAdapter.apply {

            viewHolderPlug = this@MainActivity
            superClickListener = this@MainActivity
        }

        binding.filter.setOnClickListener {
            val modal = FilterModal(binding.rideTabs.selectedTabPosition)
            modal.filterSelectedListener = this
            modal.show(supportFragmentManager, null)

        }
        binding.rideList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = generalAdapter
        }

        viewmodel.rides.observe(this) {
            rideList = it
            generalAdapter.items = rideList
            generalAdapter.notifyDataSetChanged()
        }

    }

    override fun setPlug(group: ViewGroup, viewType: Int): MainViewHolder {
        val contentView = LayoutInflater.from(this).inflate(R.layout.ride_componenet, group, false)

        return viewHolder(contentView)
    }

    val viewHolder: (itemView: View) -> MainViewHolder = { itemView ->
        object : MainViewHolder(itemView) {
            val binding = RideComponenetBinding.bind(itemView)

            override fun bindPostType(
                types: SuperEntity,
                context: Context,
                clickListener: SuperClickListener
            ) {

                types as Ride

                Glide.with(context).load(types.map_url).into(binding.mapUrl)
                binding.dateValue.text = types.date
                binding.cityName.text = types.city
                binding.stateName.text = types.state
                binding.rideId.text = types.id.toString()
                binding.originStationValue.text = types.origin_station_code.toString()
                binding.stationPathArray.text = types.station_path.toList().toString()

                viewmodel.user.observe(this@MainActivity) {

                    binding.distanceValue.text = types.getDistance(it.station_code).toString()
                }

            }

        }
    }

    override fun onClickItem(position: Int) {
        /**
        click functionality is not implemented

         */
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {

        viewmodel.publishTabPosition(tab?.position!!)

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

        viewmodel.publishTabPosition(tab?.position!!)
    }

    override fun selectState(selectedState: String) {

        generalAdapter.items = rideList.filter { it.state == selectedState }
        generalAdapter.notifyDataSetChanged()

    }

    override fun selectCity(selectedCity: String) {

        generalAdapter.items = rideList.filter { it.city == selectedCity }
        generalAdapter.notifyDataSetChanged()
    }

}