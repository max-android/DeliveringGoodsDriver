package com.my_project.deliveringgoods.ui.map

import CONST
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.model.DirectionsRoute
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.entities.Order
import com.my_project.deliveringgoods.data.provider.ResourceProvider
import com.my_project.deliveringgoods.ui.base.BaseFragment
import com.my_project.deliveringgoods.ui.custom_view.NavigationsStatusToast
import com.my_project.deliveringgoods.utilities.*
import com.my_project.deliveringgoods.viewmodels.map.DeliveryMapViewModel
import com.my_project.deliveringgoods.viewmodels.map.DeliveryMapViewState
import gone
import kotlinx.android.synthetic.main.fragment_delivery_map.*
import kotlinx.android.synthetic.main.layout_search_directions.*
import mainActivity
import org.jetbrains.anko.design.snackbar
import timber.log.Timber
import visible
import javax.inject.Inject


class DeliveryMapFragment : BaseFragment(), OnMapReadyCallback {

    companion object {
        private const val DELIVERY_MAP_KEY = "delivery_map_key"
        private const val PLACE_FROM_AUTOCOMPLETE_REQUEST_CODE = 1
        private const val PLACE_TO_AUTOCOMPLETE_REQUEST_CODE = 2
        @JvmStatic
        fun newInstance(): Fragment = DeliveryMapFragment()
    }

    @Inject
    lateinit var rProvider: ResourceProvider
    private lateinit var viewModel: DeliveryMapViewModel
    lateinit var mapLocationManager: MapLocationManager
    private var mMap: GoogleMap? = null
    private var checkPermission: Boolean = false
    private var requestingLocationUpdates: Boolean = false
    private var oldLocation: Location? = null
    private var targetBearing: Float = 0F
    private var myLatLng: LatLng? = null
    private var userLocationMarker: Marker? = null
    private var destinationMarker: Marker? = null
    private var routePolyline: Polyline? = null
    private var orderClusterManager: ClusterManager<OrderMarker>? = null
    private var bottomSheet: MapBottomSheet? = null
    private var searchFromLatLng: LatLng? = null
    private var searchToLatLng: LatLng? = null

    override fun getLayoutRes(): Int = R.layout.fragment_delivery_map

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this).get(DeliveryMapViewModel::class.java)
        mapLocationManager = MapLocationManager(context!!, locationCallback)
        askPermissions()
        setRequestLocationListener()
        init()
        observeData()
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        if (checkPermission && !requestingLocationUpdates) {
            mapLocationManager.startLocationUpdates()
        }
        requestingLocationUpdates = false
    }

    override fun onStop() {
        super.onStop()
        if (checkPermission) mapLocationManager.stopLocationUpdates()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PLACE_FROM_AUTOCOMPLETE_REQUEST_CODE -> {
                    val placeFrom = PlaceAutocomplete.getPlace(context, data)
                    placeFromTextView.text = placeFrom.name
                    searchFromLatLng = placeFrom.latLng
                }
                PLACE_TO_AUTOCOMPLETE_REQUEST_CODE -> {
                    val placeTo = PlaceAutocomplete.getPlace(context, data)
                    placeToTextView.text = placeTo.name
                    searchToLatLng = placeTo.latLng
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LocationPermission.LOCATION_PERMISSION_REQUEST_CODE)
            if (grantResults.size == 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                view!!.snackbar(R.string.permission_location_granted)
                actionIfLocPermission()
            } else {
                view!!.snackbar(R.string.permission_location_denied)
            }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        val uiSettings: UiSettings? = mMap?.uiSettings
        uiSettings?.apply {
            isCompassEnabled = false
        }
        mMap?.let {
            it.setOnMapLongClickListener { coord ->
                showRouteFromUserToDestination(coord)
            }
            it.setOnMapClickListener {
                searchCardView.gone()
                bottomSheet?.dismiss()
            }
            setOrderClusterManager(it)
            initState()
            ordersRequest()
        }
    }

    private fun init() {
        updateToolbar()
        setSearchListener()
        initMap()
        setMapButtonListener()
    }

    private fun observeData() = viewModel.dmLiveData.observe(this, Observer { processViewState(it) })

    private fun processViewState(viewState: DeliveryMapViewState?) {
        viewState?.let {
            when (it) {
                is DeliveryMapViewState.Loading -> showProgress()
                is DeliveryMapViewState.SuccessAddress -> showAddressDestination(it.address)
                is DeliveryMapViewState.SuccessRoute -> showRoute(it.directionsRoute)
                is DeliveryMapViewState.SuccessOrders -> showOrders(it.orders)
                is DeliveryMapViewState.Error -> showError(it.error)
            }
        }
    }

    private fun updateToolbar() {
        mainActivity?.toolbar?.gone()
    }

    private fun setSearchListener() {
        searchView.setOnClickListener { searchCardView.visible() }

        placeFromTextView.setOnClickListener { startPlaceAutocomplete(PLACE_FROM_AUTOCOMPLETE_REQUEST_CODE) }

        placeToTextView.setOnClickListener { startPlaceAutocomplete(PLACE_TO_AUTOCOMPLETE_REQUEST_CODE) }

        buildRouteButton.setOnClickListener {
            if (searchFromLatLng != null && searchToLatLng != null) {
                viewModel.routeRequest(
                    com.google.maps.model.LatLng(searchFromLatLng!!.latitude, searchFromLatLng!!.longitude),
                    com.google.maps.model.LatLng(searchToLatLng!!.latitude, searchToLatLng!!.longitude)
                )
                searchCardView.gone()
            }
        }
    }

    private fun startPlaceAutocomplete(requestCode: Int) {
        val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(activity)
        startActivityForResult(intent, requestCode)
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragmemt) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setMapButtonListener() {
        plusZoomButton.setOnClickListener { mMap?.animateCamera(CameraUpdateFactory.zoomIn()) }
        minusZoomButton.setOnClickListener { mMap?.animateCamera(CameraUpdateFactory.zoomOut()) }
        myLocationButton.setOnClickListener { myLatLng?.let { zoomUserPosition(it) } }
    }

    private fun askPermissions() {
        LocationPermission().requestPermission(context!!) {
            actionIfLocPermission()
        }
    }

    private fun actionIfLocPermission() {
        checkLocationServiceEnabled()
        mapLocationManager.startLocationUpdates()
        checkPermission = true
        requestingLocationUpdates = true
    }

    private fun checkLocationServiceEnabled() {
        context?.let { if (!it.checkLocationServiceEnabled()) it.showDialogLocation() }
    }

    private fun setRequestLocationListener() {
        if (checkPermission) mapLocationManager.task.addOnSuccessListener { locationSettingsResponse ->
            Timber.tag("--MAP").d("addOnSuccessListener%s", locationSettingsResponse.locationSettingsStates.isLocationUsable)
        }
        mapLocationManager.showPlaceUserLocation()
    }

    private fun initState() {
        if (viewModel.vmMyLatLng != null && viewModel.vmDestinationLatLng != null && viewModel.vmSelectRoute != null) {
            specifyPointUserLocation(viewModel.vmMyLatLng!!)
            specifyPointDestination(viewModel.vmDestinationLatLng!!)
            showRoute(viewModel.vmSelectRoute!!)
        }
    }

    private fun onLocationChanged(location: Location?) {
        location?.let {
            if (oldLocation != null) {
                targetBearing = oldLocation!!.bearingTo(location)
            }
            oldLocation = location
            val latLng = LatLng(location.latitude, location.longitude)
            specifyPointUserLocation(latLng)
        }
    }

    private fun specifyPointUserLocation(latLng: LatLng) {
        userLocationMarker?.remove()
        myLatLng = latLng
        viewModel.vmMyLatLng = latLng
        val option = MarkerOptions()
            .apply {
                title(rProvider.getString(R.string.my_location))
                position(latLng)
                rotation(targetBearing)
                draggable(true)
                flat(true)
                icon(BitmapDescriptorFactory.fromBitmap(context?.bitmapFromVectorDrawable(R.drawable.ic_my_position)))
            }
        userLocationMarker = mMap?.addMarker(option)
        userLocationMarker?.tag = rProvider.getString(R.string.user_tag)
        zoomUserPosition(latLng)
    }

    private fun zoomUserPosition(position: LatLng) {
        mMap?.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .apply {
                        target(position)
                        zoom(18F)
                        bearing(targetBearing)
                    }.build()
            )
        )
    }

    private fun showRouteFromUserToDestination(latLng: LatLng) {
        if (myLatLng != null) {
            specifyPointDestination(latLng)
            addressDestinationRequest(latLng)
            routeRequest(latLng)
        }
    }

    private fun specifyPointDestination(latLng: LatLng) {
        destinationMarker?.remove()
        viewModel.vmDestinationLatLng = latLng
        val option = MarkerOptions()
            .apply {
                title(rProvider.getString(R.string.my_target))
                position(latLng)
                icon(BitmapDescriptorFactory.fromResource(R.mipmap.my_destination))
            }
        destinationMarker = mMap?.addMarker(option)
        destinationMarker?.tag = rProvider.getString(R.string.destination_tag)
        mMap?.addCircle(CircleOptions().center(latLng).radius(CONST.RADIUS).strokeColor(Color.RED))
    }

    private fun addressDestinationRequest(latLng: LatLng) = viewModel.addressDestinationRequest(latLng)

    private fun ordersRequest() = viewModel.ordersRequest()

    private fun routeRequest(destinationLatLng: LatLng) = viewModel.routeRequest(
        com.google.maps.model.LatLng(myLatLng!!.latitude, myLatLng!!.longitude),
        com.google.maps.model.LatLng(destinationLatLng.latitude, destinationLatLng.longitude)
    )

    private fun showAddressDestination(address: Address) {
        //TODO SECURITY
    }

    private fun showRoute(route: DirectionsRoute) {
        viewModel.vmSelectRoute = route
        routePolyline?.remove()
        routePolyline = mMap?.addPolyline(PolylineOptions().addAll(PolyUtil.decode(route.overviewPolyline.encodedPath)))
        routePolyline?.apply {
            color = rProvider.getColor(R.color.colorPrimary)
            width = CONST.LINE_WIDTH
            isGeodesic = true
            jointType = JointType.ROUND
        }.also { animateTo(it) }
        removeProgress()
    }

    private fun animateTo(routePolyline: Polyline?) {
        routePolyline?.run {
            val bounds = LatLngBounds.Builder().apply { routePolyline.points.forEach { include(it) } }.build()
            mMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, context!!.dpToPx(48)))
        }
    }

    private fun showOrders(orders: List<Order>) {
        orderClusterManager?.run {
            clearItems()
            val markers = orders.map { OrderMarker(it) }
            addItems(markers)
            cluster()
        }
        removeProgress()
    }

    private fun setOrderClusterManager(map: GoogleMap) {
        orderClusterManager = ClusterManager<OrderMarker>(context, map).apply {
            renderer = OrderClusterRenderer(context!!, map, this)
        }
        map.setOnCameraIdleListener(orderClusterManager)
        map.setOnMarkerClickListener(orderClusterManager)
        orderClusterManager?.setOnClusterItemClickListener { stationMarker ->
            map.animateCamera(
                CameraUpdateFactory.newLatLng(
                    LatLng(
                        stationMarker.order.latitude,
                        stationMarker.order.longitude
                    )
                )
            )
            showOrdersInfoForSelect(stationMarker.order)
            true
        }
    }

    private fun showOrdersInfoForSelect(order: Order) {
        bottomSheet = MapBottomSheet(context!!)
        bottomSheet!!.show(order) { showRouteFromUserToDestination(LatLng(order.latitude, order.longitude)) }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(location: LocationResult?) {
            super.onLocationResult(location)
            onLocationChanged(location?.lastLocation)
            //TODO только для теста
            NavigationsStatusToast(context!!).makeText(
                location?.lastLocation?.latitude.toString()
                        + "---"
                        + location?.lastLocation?.longitude.toString(), Toast.LENGTH_LONG
            ).show()
        }

        override fun onLocationAvailability(p0: LocationAvailability?) {
            super.onLocationAvailability(p0)
        }
    }
}