document.addEventListener('DOMContentLoaded', () => {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const watchlistButton = document.querySelector('#watchlistButton');
    watchlistButton.addEventListener('click', () => {
        const listingId = watchlistButton.dataset.lid;
        const action = watchlistButton.dataset.action;
        fetch(`/api/watchlist/${action}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({ id: listingId })
        }).then(response => {
            if (response.ok) {
                if (action === 'add') {
                    watchlistButton.classList.remove('btn-primary');
                    watchlistButton.classList.add('btn-secondary');
                    watchlistButton.textContent = 'Remove from watchlist';
                    watchlistButton.dataset.action = 'remove';
                } else {
                    watchlistButton.classList.remove('btn-secondary');
                    watchlistButton.classList.add('btn-primary');
                    watchlistButton.textContent = 'Add to watchlist';
                    watchlistButton.dataset.action = 'add';
                }

            }
            return response.json();
        }).then(data => console.log(data));
    });

    const commentForm = document.getElementById("commentForm");
    commentForm.addEventListener('submit', (ev) => {
        ev.preventDefault();
        const form = ev.target;
        const formData = new FormData(form);
        const jsonForm = {};
        formData.forEach((value, key) => {
           jsonForm[key] = value;
        });
        fetch('/listing/comment', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify(jsonForm)
        }).then(response => {
            if (response.ok) {
                commentForm.reset();
                return response.json();
            }
            else {
                alert('Some error occured. Try again');
            }
        }).then(data => {
            console.log(data);
            if (data) {
                const commentDiv = document.getElementById('commentDiv');
                const newComment = document.createElement('div');
                newComment.setAttribute("class", "card mb-2 highlight");
                const innerDiv = document.createElement('div');
                innerDiv.setAttribute("class", "card-body");

                const username = document.createElement('h5');
                username.setAttribute("class", "card-title");
                username.textContent = data.username;

                const date = document.createElement('h6');
                date.setAttribute("class", "card-subtitle mb-2 text-muted");
                date.textContent = new Date(data.publishedAt).toLocaleString();

                const content = document.createElement('p');
                content.setAttribute("class", "card-text");
                content.textContent = data.content;

                innerDiv.appendChild(username);
                innerDiv.appendChild(date);
                innerDiv.appendChild(content);
                newComment.appendChild(innerDiv);
                commentDiv.appendChild(newComment);

                newComment.scrollIntoView({ behavior: 'smooth' });
                setTimeout(() => {
                    newComment.classList.remove('highlight');
                }, 2000);

            }
        }).catch(error => alert(error));
    });

});