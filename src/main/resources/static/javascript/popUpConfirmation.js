function confirmDeletion(id) {
    if (confirm("Êtes-vous sûr de vouloir supprimer cette inscription ?")) {
        window.location.href = '/annulationParticipation/' + id;
    }
}


    function confirmDeletionEvent(id) {
        if (confirm("Êtes-vous sûr de vouloir supprimer cet évènement ?")) {
            window.location.href = '/supprimerEvenement/' + id;
        }
}

function confirmDeletionPartner(id) {
    if (confirm("Êtes-vous sûr de vouloir supprimer ce partenaire ?")) {
        window.location.href = '/supprimerPartenaire/' + id;
    }
}