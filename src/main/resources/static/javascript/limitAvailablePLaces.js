function toggleTotalPlaces() {
    var selectElement = document.getElementById("limit_places_select");
    var totalPlacesSection = document.getElementById("total_places_section");
    var totalPlacesSectionp = document.getElementById("total_places_section-p");
    var availablePlacesSection = document.getElementById("available_places_section");
    var availablePlacesSectionp = document.getElementById("available_places_section-p");


    if (selectElement.value === "1") {
        totalPlacesSection.style.display = "block";
        totalPlacesSectionp.style.display = "block";
        availablePlacesSection.style.display = "block";
        availablePlacesSectionp.style.display = "block";
    } else {
        totalPlacesSection.style.display = "none";
        totalPlacesSectionp.style.display = "none";
        availablePlacesSection.style.display = "none";
        availablePlacesSectionp.style.display = "none";
        totalPlacesSection.value = 0; // Mettre à 0
        availablePlacesSection.value = 0; // Mettre à 0
    }
    var dispo = parseInt(availablePlacesSection.value);
    var totalPlaces = parseInt(totalPlacesSection.value);
    var nonOption = selectElement.querySelector("option[value='0']");

    if (dispo !== 0 && totalPlaces !==0 && dispo !== totalPlaces) {
        nonOption.disabled = true; // Désactiver l'option "NON"
    } else {
        nonOption.disabled = false; // Activer l'option "NON"
    }
}
toggleTotalPlaces();