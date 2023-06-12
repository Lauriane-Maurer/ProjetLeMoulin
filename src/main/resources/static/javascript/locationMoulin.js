let map = L.map('mapLocationMoulin').setView([48.56790835686583, -4.6629741396325075], 8);

// Ajouter une couche de tuiles OpenStreetMap à la carte
L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 14,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

// Ajouter le marqueur
let marker = L.marker([48.56790835686583, -4.6629741396325075]).addTo(map);

// Ajouter un événement 'click' au marqueur
marker.on('click', function() {
    // Changer le niveau de zoom de la carte
    map.setView([48.56790835686583, -4.6629741396325075], 14);
});

