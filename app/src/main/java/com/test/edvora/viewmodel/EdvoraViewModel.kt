package com.test.edvora.viewmodel

import androidx.lifecycle.*
import com.test.edvora.model.Ride
import com.test.edvora.model.RideDate
import com.test.edvora.model.User
import com.test.edvora.service.EdvoraService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class EdvoraViewModel @Inject constructor(private val service: EdvoraService) : ViewModel() {


    private val _allRides: MutableLiveData<List<Ride>> = MutableLiveData()
    val allRides: LiveData<List<Ride>> = _allRides
    private val _rides: MutableLiveData<List<Ride>> = MutableLiveData()
    val rides: LiveData<List<Ride>> = _rides

    private val _pastRides: MutableLiveData<List<Ride>> = MutableLiveData()
    val pastRides: LiveData<List<Ride>> = _pastRides

    private val _upcomingRides: MutableLiveData<List<Ride>> = MutableLiveData()
    val upcomingRides: LiveData<List<Ride>> = _upcomingRides

    private val _nearestRides: MutableLiveData<List<Ride>> = MutableLiveData()
    val nearestRides: LiveData<List<Ride>> = _nearestRides

    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> = _user

    private val _tabPosition: MutableLiveData<Int> = MutableLiveData(0)
    val tabPosition: LiveData<Int> = _tabPosition
    private val _state: MutableLiveData<List<Ride>> = MutableLiveData()
    val state: LiveData<List<Ride>> = _state
    private val _cities: MutableLiveData<List<String>> = MutableLiveData()
    val cities: LiveData<List<String>> = _cities

    fun publishUser() {
        viewModelScope.launch {
            _user.postValue(service.getUsers())
        }
    }

    fun publishTabPosition(position: Int) {
        _tabPosition.postValue(position)
    }

    fun publishState(position: Int,lifecycleOwner: LifecycleOwner) {

        rides.observe(lifecycleOwner) { ride ->
            _state.postValue(ride.distinctBy { it.state })
        }
    }

    fun filterRideByCities(city: String, lifecycleOwner: LifecycleOwner) {
        rides.observe(lifecycleOwner) { ride ->
            _rides.postValue(ride.filter { it.city == city })
        }
    }

    fun filterRideByState(state: String, lifecycleOwner: LifecycleOwner) {
        rides.observe(lifecycleOwner) { ride ->
            _rides.postValue(ride.filter { it.state == state })
        }
    }

    fun publishCities(currentState: String, lifecycleOwner: LifecycleOwner) {
        rides.observe(lifecycleOwner) { result ->
            _cities.postValue(result.filter { it.state == currentState }.distinctBy { it.city }
                .map { it.city })
        }
    }

    fun publishAllRides(owner: LifecycleOwner) {
        _user.observe(owner){
         if(it!=null){
             viewModelScope.launch {
                 val rideResponse = service.getRides()
                 if (rideResponse.isSuccessful) {
                     val rideResult = rideResponse.body()!!

                     _allRides.postValue(rideResult)
                     _rides.postValue(rideResult)
                     publishUpcomingRides(rideResult)
                     publishPastRides(rideResult)
                     publishNearestRides(owner, rideResult)


                 } else {
                     _allRides.postValue(listOf())
                 }
             }
         }
        }

    }

    fun publishRides(position: Int, owner: LifecycleOwner) {

        when (position) {
            0 -> {
                nearestRides.observe(owner) {
                    _rides.postValue(it)
                }
            }
            1 -> {
                upcomingRides.observe(owner) {
                    _rides.postValue(it)
                }
            }
            2 -> {
                pastRides.observe(owner) {
                    _rides.postValue(it)
                }
            }
        }

    }

    fun publishUpcomingRides(rideResult: List<Ride>) {

        viewModelScope.launch {
            _upcomingRides.postValue(rideResult.filter {
                val currentDate = getDate(System.currentTimeMillis())
                val rideDate = RideDate(it.date.split(" ")[0])
                rideDate.after(currentDate)
            })
        }

    }


    fun publishNearestRides(owner: LifecycleOwner, rideResult: List<Ride>) {

        user.observe(owner) { user ->

            viewModelScope.launch {
                _nearestRides.postValue(rideResult.sortedBy {
                    it.getDistance(user.station_code)

                })
            }
        }


    }


    fun publishPastRides(rideResult: List<Ride>) {
        viewModelScope.launch {
            _pastRides.postValue(rideResult.filter {
                val currentDate = getDate(System.currentTimeMillis())
                val rideDate = RideDate(it.date.split(" ")[0])
                rideDate.before(currentDate)
            })
        }
    }


    fun getDate(timeStamp: Long): String {
        val stamp = Timestamp(timeStamp)
        val date = Date(stamp.time)
        val f: DateFormat = SimpleDateFormat("MM/dd/yyyy")

        return f.format(date)
    }

}