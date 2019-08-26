package com.my_project.deliveringgoods.ui.map

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer


class OrderClusterRenderer (
    context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<OrderMarker>
) : DefaultClusterRenderer<OrderMarker>(context, map, clusterManager) {

    init {
        minClusterSize = 2
    }

    override fun onBeforeClusterItemRendered(item: OrderMarker, markerOptions: MarkerOptions) {
        markerOptions.icon(item.getIcon())
    }
}