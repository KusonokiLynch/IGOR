function filtrarTabla() {
    let input = document.getElementById("buscador");
    let filter = input.value.toLowerCase();
    let tabla = document.querySelector("table tbody");
    let filas = tabla.getElementsByTagName("tr");

    for (let i = 0; i < filas.length; i++) {
        let celdas = filas[i].getElementsByTagName("td");
        let textoFila = "";
        for(let j=0; j < celdas.length; j++) {
            textoFila += celdas[j].textContent.toLowerCase() + " ";
        }
        if (textoFila.indexOf(filter) > -1) {
            filas[i].style.display = "";
        } else {
            filas[i].style.display = "none";
        }
    }
}

