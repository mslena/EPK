document.addEventListener('DOMContentLoaded', function () {
    const links = document.querySelectorAll('.navbar .nav-link');
    const currentUrl = window.location.pathname;

    links.forEach(link => {
        if (link.getAttribute('href') === currentUrl) {
            link.classList.add('active');
        }

        link.addEventListener('click', function () {
            links.forEach(l => l.classList.remove('active'));
            this.classList.add('active');
        });
    });
});