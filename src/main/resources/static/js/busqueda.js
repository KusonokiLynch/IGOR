document.getElementById('searchInput').addEventListener('keyup', function() {
            const searchTerm = this.value.toLowerCase();
            const rows = document.querySelectorAll('#docsTable tbody tr:not(.hidden)');
            
            rows.forEach(row => {
                if (!row.id.startsWith('formulario-')) {
                    const text = row.textContent.toLowerCase();
                    row.style.display = text.includes(searchTerm) ? '' : 'none';
                }
            });
        });