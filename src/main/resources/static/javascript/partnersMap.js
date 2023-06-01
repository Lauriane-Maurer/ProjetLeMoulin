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
 * Fetches partners data from the API and adds markers for each organization to the map.
 */
fetch("/geolocalisationPartenaires")
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


//-----------------------------------------------------------------------------------------------------//

/**
 * Adds the ability to sort partners by name when the "Partner name" column header is clicked.
 */
const nomHeader = document.querySelector(".table-head:nth-child(1)");

// Add a click event listener to the header of the "Organization name" column.
nomHeader.addEventListener("click", function () {
    const table = document.querySelector(".grid");
    const tbody = table.querySelector("tbody");
    const rows = Array.from(tbody.querySelectorAll("tr"));

    // Sorting of the data according to the name of the festival.
    rows.sort(function (a, b) {
        const aNom = a.querySelector(".cell:nth-child(1)").innerText;
        const bNom = b.querySelector(".cell:nth-child(1)").innerText;

        return aNom.localeCompare(bNom);
    });

    // Reverse the order if the user has already sorted in ascending order.
    if (nomHeader.dataset.sort === "asc") {
        rows.reverse();
        nomHeader.dataset.sort = "desc";
    } else {
        nomHeader.dataset.sort = "asc";
    }

    // Update the table with the sorted data.
    rows.forEach(row => tbody.appendChild(row));
});


//-----------------------------------------------------------------------------------------------------//


/**
 * Adds the ability to sort partners by activity when the "Partner Activity" column header is clicked.
 */
const activiteHeader = document.querySelector(".table-head:nth-child(2)");

// Add a click event listener to the header of the "Organization name" column.
activiteHeader.addEventListener("click", function () {
    const table = document.querySelector(".grid");
    const tbody = table.querySelector("tbody");
    const rows = Array.from(tbody.querySelectorAll("tr"));

    // Sorting of the data according to the name of the festival.
    rows.sort(function (a, b) {
        const aActivite = a.querySelector(".cell:nth-child(2)").innerText;
        const bActivite = b.querySelector(".cell:nth-child(2)").innerText;

        return aActivite.localeCompare(bActivite);
    });

    // Reverse the order if the user has already sorted in ascending order.
    if (activiteHeader.dataset.sort === "asc") {
        rows.reverse();
        activiteHeader.dataset.sort = "desc";
    } else {
        activiteHeader.dataset.sort = "asc";
    }

    // Update the table with the sorted data.
    rows.forEach(row => tbody.appendChild(row));
});


//-----------------------------------------------------------------------------------------------------//


/**
 * Adds the ability to sort partners by town when the "Partner town" column header is clicked.
 */
const villeHeader = document.querySelector(".table-head:nth-child(4)");

// Add a click event listener to the header of the "Organization name" column.
villeHeader.addEventListener("click", function () {
    const table = document.querySelector(".grid");
    const tbody = table.querySelector("tbody");
    const rows = Array.from(tbody.querySelectorAll("tr"));

    // Sorting of the data according to the name of the festival.
    rows.sort(function (a, b) {
        const aVille = a.querySelector(".cell:nth-child(4)").innerText;
        const bVille = b.querySelector(".cell:nth-child(4)").innerText;

        return aVille.localeCompare(bVille);
    });

    // Reverse the order if the user has already sorted in ascending order.
    if (villeHeader.dataset.sort === "asc") {
        rows.reverse();
        villeHeader.dataset.sort = "desc";
    } else {
        villeHeader.dataset.sort = "asc";
    }

    // Update the table with the sorted data.
    rows.forEach(row => tbody.appendChild(row));
});


//-----------------------------------------------------------------------------------------------------//

/**
 * Search a partner by name, town, or activity.
 */
function filterPartners() {

    let input = document.getElementById('search-input');
    let filter = input.value.toUpperCase();
    let table = document.querySelector('table');
    let rows = table.querySelectorAll('tr');


    for (let i = 0; i < rows.length; i++) {
        let nameCol = rows[i].querySelector('td:nth-child(1)');
        let activityCol = rows[i].querySelector('td:nth-child(3)');
        let cityCol = rows[i].querySelector('td:nth-child(5)');
        if (nameCol || activityCol || cityCol) {
            let name = nameCol.textContent.toUpperCase();
            let activity = activityCol.textContent.toUpperCase();
            let city = cityCol.textContent.toUpperCase();
            if (name.includes(filter) || city.includes(filter) || activity.includes(filter))  {
                rows[i].style.display = '';
            } else {
                rows[i].style.display = 'none';
            }
        }
    }
}

input.addEventListener('keyup', function () {
    filterTable();
});