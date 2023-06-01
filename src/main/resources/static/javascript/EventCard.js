
document.addEventListener('DOMContentLoaded', () => {
    fetch('/cartesEvenements')
        .then(response => {
            if (!response.ok) {
                throw new Error('Erreur lors de la récupération des données.');
            }
            return response.json();
        })
        .then(data => {
            var eventsHtml = "";
            var currentDate = new Date();

            data.forEach(event => {
                var startDate = new Date(event.start_date);
                if (startDate > currentDate) {
                    eventsHtml += `
                    <div class="card">
                        <img src="${event.photo}" alt="${event.title}">
                        <div class="card-content">
                            <p class="card-date">${event.start_date} - ${event.end_date}</p>
                            <h5 class="card-type">${event.type}</h5>
                            <h3 class="card-title">${event.title}</h3>                                   
                         
                        </div>
                    </div>
                `;
                }
            });
            document.getElementById("listings").innerHTML = eventsHtml;
        })

        .catch(error => console.error(error));
});