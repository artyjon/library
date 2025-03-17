function filterUsers() {
    let filter = document.getElementById('userFilter').value.toLowerCase();
    let rows = document.querySelectorAll('#userTableBody .user-row');

    rows.forEach(row => {
        let userName = row.getAttribute('data-username').toLowerCase();
        row.style.display = userName.includes(filter) ? '' : 'none';
    });
}

function filterBooks() {
    let filter = document.getElementById('bookFilter').value.toLowerCase();
    let rows = document.querySelectorAll('#bookTableBody .book-row');

    rows.forEach(row => {
        let title = row.getAttribute('data-title')?.toLowerCase() || '';
        row.style.display = title.includes(filter) ? '' : 'none';
    });
}

