function FormularioEdit(id) {
            const row = document.getElementById("formulario-" + id);
             if (row.style.display === "table-row") {
                 row.style.display = "none";
             } 
             else {
                row.style.display = "table-row";
              }
        }