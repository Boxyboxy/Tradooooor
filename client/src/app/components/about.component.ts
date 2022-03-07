import { Component, OnInit, ViewChild } from '@angular/core';
import { MapInfoWindow, MapMarker } from '@angular/google-maps';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css'],
})
export class AboutComponent implements OnInit {
  zoom = 15;
  center: google.maps.LatLngLiteral = {
    lat: 1.2921916035340624,
    lng: 103.77660858532768,
  };
  options: google.maps.MapOptions = {
    mapTypeId: 'hybrid',
    zoomControl: false,
    scrollwheel: true,
    disableDoubleClickZoom: true,
    maxZoom: 25,
    minZoom: 8,
  };

  markerOptions: google.maps.MarkerOptions = { draggable: false };
  markerPositions: google.maps.LatLngLiteral[] = [
    {
      lat: 1.2921916035340624,
      lng: 103.77660858532768,
    },
  ];

  constructor() {}

  ngOnInit(): void {}

  // openInfoWindow(marker: MapMarker) {
  //   this.infoWindow.open(marker);
  // }
  // addMarker(event: google.maps.MapMouseEvent) {
  //   this.markerPositions.push(event.latLng.toJSON());
  // }
  // zoomIn() {
  //   if (this.zoom < this.options.maxZoom) this.zoom++;
  // }

  // zoomOut() {
  //   if (this.zoom > this.options.minZoom) this.zoom--;
  // }
}
