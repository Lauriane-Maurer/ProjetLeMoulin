/**
 * Creates a Leaflet map centered on Bretagne, France and adds a tile layer from OpenStreetMap.
 * @type {L.Map} The Leaflet map object.
 */
let map = L.map('mapid').setView([48.2020471, -2.9326435], 8);

// ajouter une couche de tuiles OpenStreetMap à la carte
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: 'Map data © <a href="https://openstreetmap.org">OpenStreetMap</a> contributors',
    maxZoom: 14
}).addTo(map);

/**
 * Fetches organization data from the API and adds markers for each organization to the map.
 */
fetch("/api/partners")
    .then(response => response.json())
    .then(data => {
        // Browse the retrieved data and create a marker for each organizations.
        data.forEach(partner => {
            let marker = L.marker([partner.latitude, partner.longitude]).addTo(map);
            let url = partner.url;
            if (!/^http(s)?:\/\//i.test(url)) {
                url = "http://" + url;
            }
            let link = "<a href='" + url + "'>" + partner.url + "</a>";
            marker.bindPopup("<b>" + partner.name+ "</b><br>" + partner.adress + "<br>" + partner.zip_code+ "<br>" + partner.town + "<br>" + link).openPopup();


            // Add a 'click' event listener to the marker
            marker.on('click', function() {
                // Change the zoom level of the map
                map.setView([partner.latitude, partner.longitude], 14);
            });


            // Add a 'click' event listener to the organisme name in the table
            var partnerNames = document.querySelectorAll("td.cellule:first-child");
            partnerNames.forEach(partnerName => {
                if (partnerName.textContent === partner.name) {
                    partnerName.addEventListener('click', function() {
                        // Change the zoom level of the map
                        map.setView([partner.latitude, partner.longitude], 14);
                    });
                }
            });
        });
    })
    .catch(error => console.error(error));
